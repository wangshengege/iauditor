package org.mylibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import junit.framework.Assert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mylibrary.common.FileAccessor;

/**
 * 工具类
 */
public class Utils {
    public static final String TAG = "lvcong.ImageUtils";
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
    public static boolean inNativeAllocAccessError = false;
    /**
     * 当前SDK版本号
     */
    private static int mSdkint = -1;
    private static Context mContext;

    /**
     * 计算语音文件的时间长度
     *
     * @param file
     * @return
     */
    public static int calculateVoiceTime(String file) {
        File _file = new File(file);
        if (!_file.exists()) {
            return 0;
        }
        // 650个字节就是1s
        int duration = (int) Math.ceil(_file.length() / 650);

        if (duration > 60) {
            return 60;
        }

        if (duration < 1) {
            return 1;
        }
        return duration;
    }


    /**
     * SDK版本号
     *
     * @return
     */
    public static int getSdkint() {
        if (mSdkint < 0) {
            mSdkint = Build.VERSION.SDK_INT;
        }
        return mSdkint;
    }

    /**
     * Java文件操作 获取文件扩展名 Get the file extension, if no extension or file name
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 返回文件名
     *
     * @param pathName
     * @return
     */
    public static String getFilename(String pathName) {
        File file = new File(pathName);
        if (!file.exists()) {
            return "";
        }
        return file.getName();
    }
    static MediaPlayer mediaPlayer = null;

    /**
     * 播放声音
     */
    public static void playNotifycationMusic(Context context, String voicePath)
            throws IOException {
        // paly music ...
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(
                voicePath);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                fileDescriptor.getStartOffset(), fileDescriptor.getLength());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /**
     * save image from uri
     *
     * @param outPath
     * @param bitmap
     * @return imagePath
     */
    public static String saveBitmapToLocal(String outPath, Bitmap bitmap) {
        try {
            String imagePath = outPath
                    + "/" + Tools.getTimeStamp() + ".jpg";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    bufferedOutputStream);
            bufferedOutputStream.close();
            LogTools.showLogi(TAG, "photo image from data, path:" + imagePath);
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setInNativeAlloc(BitmapFactory.Options options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && !inNativeAllocAccessError) {
            try {
                BitmapFactory.Options.class.getField("inNativeAlloc")
                        .setBoolean(options, true);
                return;
            } catch (Exception e) {
                inNativeAllocAccessError = true;
            }
        }
    }

    public static boolean checkByteArray(byte[] b) {
        return b != null && b.length > 0;
    }

    public static Bitmap getSuitableBitmap(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            LogTools.showLogi(TAG, "filepath is null or nil");
            return null;
        }

        if (!new File(filePath).exists()) {
            LogTools.showLogi(TAG,
                    "getSuitableBmp fail, file does not exist, filePath = "
                            + filePath);
            return null;
        }
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap decodeFile = BitmapFactory.decodeFile(filePath, options);
            if (decodeFile != null) {
                decodeFile.recycle();
            }

            if ((options.outWidth <= 0) || (options.outHeight <= 0)) {
                LogTools.showLogi(TAG,
                        "get bitmap fail, file is not a image file = "
                                + filePath);
                return null;
            }

            int maxWidth = 960;
            int width = 0;
            int height = 0;
            if ((options.outWidth <= options.outHeight * 2.0D)
                    && options.outWidth > 480) {
                height = maxWidth;
                width = options.outWidth;
            }
            if ((options.outHeight <= options.outWidth * 2.0D)
                    || options.outHeight <= 480) {
                width = maxWidth;
                height = options.outHeight;
            }

            Bitmap bitmap = extractThumbNail(filePath, width, height, false);
            if (bitmap == null) {
                LogTools.showLogi(TAG,
                        "getSuitableBmp fail, temBmp is null, filePath = "
                                + filePath);
                return null;
            }
            int degree = readPictureDegree(filePath);
            if (degree != 0) {
                bitmap = degreeBitmap(bitmap, degree);
            }
            return bitmap;
        } catch (Exception e) {
            LogTools.showLogi(TAG, "decode bitmap err: " + e.getMessage());
            return null;
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * @param src
     * @param degree
     * @return
     */
    public static Bitmap degreeBitmap(Bitmap src, float degree) {
        if (degree == 0.0F) {
            return src;
        }
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree, src.getWidth() / 2, src.getHeight() / 2);
        Bitmap resultBitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
        boolean filter = true;
        if (resultBitmap == null) {
            LogTools.showLogi(TAG, "resultBmp is null: ");
            filter = true;
        } else {
            filter = false;
        }

        if (resultBitmap != src) {
            src.recycle();
        }
        LogTools.showLogi(TAG, "filter: " + filter + "  degree:" + degree);
        return resultBitmap;
    }

    /**
     * 得到指定路径图片的options
     *
     * @param srcPath
     * @return Options {@link BitmapFactory.Options}
     */
    public final static BitmapFactory.Options getBitmapOptions(String srcPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        return options;
    }

    /**
     * 压缩发送到服务器的图片
     *
     * @param origPath     原始图片路径
     * @param widthLimit   图片宽度限制
     * @param heightLimit  图片高度限制
     * @param format       图片格式
     * @param quality      图片压缩率
     * @param authorityDir 图片目录
     * @param outPath      图片详细目录
     * @return
     */
    public static boolean createThumbnailFromOrig(String origPath,
                                                  int widthLimit, int heightLimit, Bitmap.CompressFormat format,
                                                  int quality, String authorityDir, String outPath) {
        Bitmap bitmapThumbNail = extractThumbNail(origPath, widthLimit,
                heightLimit, false);
        if (bitmapThumbNail == null) {
            return false;
        }

        try {
            saveImageFile(bitmapThumbNail, quality, format, authorityDir,
                    outPath);
            return true;
        } catch (IOException e) {
            LogTools.showLogi(TAG, "create thumbnail from orig failed: "
                    + outPath);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap extractThumbNail(final String path, final int width,
                                          final int height, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0
                && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            LogTools.showLogi(TAG, "extractThumbNail: round=" + width + "x"
                    + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            LogTools.showLogi(TAG, "extractThumbNail: extract beX = " + beX
                    + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY)
                    : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                options.inMutable = true;
            }
            LogTools.showLogi(TAG, "bitmap required size=" + newWidth + "x"
                    + newHeight + ", orig=" + options.outWidth + "x"
                    + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            setInNativeAlloc(options);
            if (bm == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }

            LogTools.showLogi(TAG, "bitmap decoded size=" + bm.getWidth() + "x"
                    + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth,
                    newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm,
                        (bm.getWidth() - width) >> 1,
                        (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                LogTools.showLogi(TAG, "bitmap croped size=" + bm.getWidth()
                        + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            LogTools.showLogi(TAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }

    public static void saveImageFile(Bitmap bitmap, int quality,
                                     Bitmap.CompressFormat format, String authorityDir, String outPath)
            throws IOException {
        if (!TextUtils.isEmpty(authorityDir) && !TextUtils.isEmpty(outPath)) {
            LogTools.showLogi(TAG, "saving to " + authorityDir + outPath);
            File file = new File(authorityDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            File outfile = new File(file, outPath);
            outfile.createNewFile();

            try {
                FileOutputStream outputStream = new FileOutputStream(outfile);
                bitmap.compress(format, quality, outputStream);
                outputStream.flush();
            } catch (Exception e) {
                LogTools.showLogi(TAG, "saveImageFile fil=" + e.getMessage());
            }
        }
    }

    /**
     * 获取图片被旋转的角度
     *
     * @param filePath
     * @return
     */
    public static int getBitmapDegrees(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            LogTools.showLogi(TAG, "filePath is null or nil");
            return 0;
        }
        if (!new File(filePath).exists()) {
            LogTools.showLogi(TAG, "file not exist:" + filePath);
            return 0;
        }
        ExifInterface exifInterface = null;
        try {

            if (Integer.valueOf(Build.VERSION.SDK).intValue() >= 5) {
                exifInterface = new ExifInterface(filePath);
                int attributeInt = -1;
                if (exifInterface != null) {
                    attributeInt = exifInterface.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, -1);
                }

                if (attributeInt != -1) {
                    switch (attributeInt) {
                        case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                        case ExifInterface.ORIENTATION_TRANSPOSE:
                        case ExifInterface.ORIENTATION_TRANSVERSE:
                            return 0;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            return 180;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            return 90;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            return 270;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LogTools.showLogi(TAG, "cannot read exif :" + e.getMessage());
        } finally {
            exifInterface = null;
        }
        return 0;
    }

    /**
     * 旋转图片
     *
     * @param srcPath
     * @param degrees
     * @param format
     * @param root
     * @param fileName
     * @return
     */
    public static boolean rotateCreateBitmap(String srcPath, int degrees,
                                             Bitmap.CompressFormat format, String root, String fileName) {
        Bitmap decodeFile = BitmapFactory.decodeFile(srcPath);
        if (decodeFile == null) {
            LogTools.showLogi(TAG, "rotate: create bitmap fialed");
            return false;
        }
        int width = decodeFile.getWidth();
        int height = decodeFile.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, width / 2.0F, height / 2.0F);
        Bitmap createBitmap = Bitmap.createBitmap(decodeFile, 0, 0, width,
                height, matrix, true);
        decodeFile.recycle();
        try {
            saveImageFile(createBitmap, 60, format, root, fileName);
            return true;
        } catch (Exception e) {
            LogTools.showLogi(TAG, "create thumbnail from orig failed: "
                    + fileName);
        }
        return false;
    }

    /**
     * 生成一张缩略图
     *
     * @param bitmap
     * @param paramFloat
     * @return
     */
    public static Bitmap processBitmap(Bitmap bitmap, float paramFloat) {
        Assert.assertNotNull(bitmap);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        Rect localRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(localRect);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-4144960);
        canvas.drawRoundRect(rectF, paramFloat, paramFloat, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, localRect, localRect, paint);
        bitmap.recycle();
        return resultBitmap;
    }

    /**
     * @param stream
     * @param dip
     * @return
     */
    public static Bitmap decodeStream(InputStream stream, float dip) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (dip != 0.0F) {
            options.inDensity = (int) (160.0F * dip);
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        setInNativeAlloc(options);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
            return bitmap;
        } catch (OutOfMemoryError e) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            setInNativeAlloc(options);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(stream, null,
                        options);
                return bitmap;
            } catch (OutOfMemoryError e2) {
            }
        }
        return null;
    }

    public static String getLastText(String text) {
        if (text == null) {
            return null;
        }
        for (int i = text.length() - 1; i >= 0; --i) {
            int j = text.charAt(i);
            if ((j >= 19968) && (j <= 40869)) {
                return String.valueOf(j);
            }
        }
        return null;
    }

    /**
     * @return
     */
    public static Paint newPaint() {
        Paint paint = new Paint(1);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        return paint;
    }

    /*
     *
     */
    public static Bitmap newBitmap(int width, int height, Bitmap.Config config) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            return bitmap;
        } catch (Throwable localThrowable) {
            LogTools.showLogi(TAG, localThrowable.getMessage());
        }
        return null;
    }

    public static int compareVersion(String curVer, String serVer) {
        if (curVer.equals(serVer) || serVer == null) {
            return 0;
        }
        String[] version1Array = curVer.split("\\.");
        String[] version2Array = serVer.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen
                && (diff = Integer.parseInt(getStringNum(version1Array[index]))
                - Integer.parseInt(getStringNum(version2Array[index]))) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (i >= 4 || Integer.parseInt(version1Array[i]) > 0) {
                    // 没有新版本
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    // 有新版本更新
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    private static String getStringNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 内存的大小
     */
    public static long getAvailaleSize() {

        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (availableBlocks * blockSize) / 1024 / 1024;// MIB单位
    }

    /**
     * 通过得到的intent获取图片路径
     *
     * @param context
     * @param intent
     * @param appPath
     * @return
     */
    public static String resolvePhotoFromIntent(Context context, Intent intent,
                                                String appPath) {
        mContext = context;
        if (context == null || intent == null || appPath == null) {
            LogTools.showLogi("resolvePhotoFromIntent fail, invalid argument");
            return null;
        }
        Uri uri = Uri.parse(intent.toURI());
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, proj, null, null,
                    null);

            String pathFromUri = null;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int columnIndex = cursor
                        .getColumnIndex(MediaStore.MediaColumns.DATA);
                // if it is a picasa image on newer devices with OS 3.0 and up
                if (uri.toString().startsWith(
                        "content://com.google.android.gallery3d")) {
                    // Do this in a background thread, since we are fetching a
                    // large image from the web
                    pathFromUri = saveBitmapToLocal(appPath,
                            createChattingImageByUri(intent.getData()));
                } else {
                    // it is a regular local image file
                    pathFromUri = cursor.getString(columnIndex);
                }
                cursor.close();
                LogTools.showLogi(TAG, "photo from resolver, path: "
                        + pathFromUri);
                return pathFromUri;
            } else {

                if (intent.getData() != null) {
                    pathFromUri = intent.getData().getPath();
                    if (new File(pathFromUri).exists()) {
                        LogTools.showLogi(TAG, "photo from resolver, path: "
                                + pathFromUri);
                        return pathFromUri;
                    }
                }

                // some devices (OS versions return an URI of com.android
                // instead of com.google.android
                if ((intent.getAction() != null)
                        && (!(intent.getAction().equals("inline-data")))) {
                    // use the com.google provider, not the com.android
                    // provider.
                    // Uri.parse(intent.getData().toString().replace("com.android.gallery3d","com.google.android.gallery3d"));
                    pathFromUri = saveBitmapToLocal(appPath, (Bitmap) intent
                            .getExtras().get("data"));
                    LogTools.showLogi(TAG, "photo from resolver, path: "
                            + pathFromUri);
                    return pathFromUri;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        LogTools.showLogi(TAG, "resolve photo from intent failed ");
        return null;
    }

    /**
     * @param uri
     * @return
     */
    public static Bitmap createChattingImageByUri(Uri uri) {
        return createChattingImage(0, null, null, uri, 0.0F, 400, 800);
    }

    /**
     * @param resource
     * @param path
     * @param b
     * @param uri
     * @param dip
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createChattingImage(int resource, String path,
                                             byte[] b, Uri uri, float dip, int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        int outWidth = 0;
        int outHeight = 0;
        int sampleSize = 0;
        try {

            do {
                if (dip != 0.0F) {
                    options.inDensity = (int) (160.0F * dip);
                }
                options.inJustDecodeBounds = true;
                decodeMuilt(options, b, path, uri, resource);
                //
                outWidth = options.outWidth;
                outHeight = options.outHeight;

                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                if (outWidth <= width || outHeight <= height) {
                    sampleSize = 0;
                    setInNativeAlloc(options);
                    Bitmap decodeMuiltBitmap = decodeMuilt(options, b, path,
                            uri, resource);
                    return decodeMuiltBitmap;
                } else {
                    options.inSampleSize = (int) Math.max(outWidth / width,
                            outHeight / height);
                    sampleSize = options.inSampleSize;
                }
            } while (sampleSize != 0);

        } catch (IncompatibleClassChangeError e) {
            e.printStackTrace();
            throw ((IncompatibleClassChangeError) new IncompatibleClassChangeError(
                    "May cause dvmFindCatchBlock crash!").initCause(e));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            BitmapFactory.Options catchOptions = new BitmapFactory.Options();
            if (dip != 0.0F) {
                catchOptions.inDensity = (int) (160.0F * dip);
            }
            catchOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            if (sampleSize != 0) {
                catchOptions.inSampleSize = sampleSize;
            }
            setInNativeAlloc(catchOptions);
            try {
                return decodeMuilt(options, b, path, uri, resource);
            } catch (IncompatibleClassChangeError twoE) {
                twoE.printStackTrace();
                throw ((IncompatibleClassChangeError) new IncompatibleClassChangeError(
                        "May cause dvmFindCatchBlock crash!").initCause(twoE));
            } catch (Throwable twoThrowable) {
                twoThrowable.printStackTrace();
            }
        }

        return null;
    }

    /**
     * @param options
     * @param data
     * @param path
     * @param uri
     * @param resource
     * @return
     */
    public static Bitmap decodeMuilt(BitmapFactory.Options options,
                                     byte[] data, String path, Uri uri, int resource) {
        if (mContext != null) {
            try {

                if (!checkByteArray(data) && TextUtils.isEmpty(path)
                        && uri == null && resource <= 0) {
                    return null;
                }

                if (checkByteArray(data)) {
                    return BitmapFactory.decodeByteArray(data, 0, data.length,
                            options);
                }

                if (uri != null) {
                    InputStream inputStream = mContext.getContentResolver()
                            .openInputStream(uri);
                    Bitmap localBitmap = BitmapFactory.decodeStream(
                            inputStream, null, options);
                    inputStream.close();
                    return localBitmap;
                }

                if (resource > 0) {
                    return BitmapFactory.decodeResource(
                            mContext.getResources(), resource, options);
                }
                return BitmapFactory.decodeFile(path, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将上下文移除
     */
    public static void removeContext() {
        mContext = null;
    }

    /**
     * 保存图片
     */
    public static String savePicture(Bitmap bitmap) {
        String url = null;
        if (!FileAccessor.isExistExternalStore()) { // 检测sd是否可用
            return url;
        }
        FileOutputStream b = null;
        FileAccessor.initFileAccess();
        try {
            File file = new File(FileAccessor.getOtherImgName(),
                    String.valueOf(System.currentTimeMillis()) + ".jpg");
            b = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            url = file.getAbsolutePath();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理(比较耗时最好放在线程里)
     *
     * @param imgFilePath 图片地址
     */
    public static String GetImageStr(String imgFilePath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            File f = new File(imgFilePath);
            if (f.isFile()) {
                InputStream in = new FileInputStream(f);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null) {
            return Base64.encodeToString(data, Base64.DEFAULT);// 返回Base64编码过的字节数组字符串
        }
        return null;
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr      编码后的数据
     * @param imgFilePath 图片path
     */
    public static boolean GenerateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) // 图像数据为空
            return false;
        try {
            // Base64解码
            byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 拍照
     *
     * @param content     上下文
     * @param file        拍照后的照片文件
     * @param requestCode 请求码
     */
    public static void photo(Activity content, File file, int requestCode) {
        if (FileAccessor.isExistExternalStore()) {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 调用照相机
            Uri imageUri = Uri.fromFile(file);
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            content.startActivityForResult(openCameraIntent, requestCode);
        } else {
            Tools.showToast(content, "未检测sd卡，无法存储照片");
        }
    }

    /**
     * 相册
     *
     * @param content     上下文
     * @param requestCode 请求码
     */
    public static void album(Activity content, int requestCode) {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        content.startActivityForResult(intentFromGallery, requestCode);
    }
}
