package org.mylibrary.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.mylibrary.biz.MyApplication;
import org.mylibrary.utils.LogTools;

/**
 * IM文件操作工具类
 * 
 * @author ws
 */
public class FileAccessor {

	public static final String TAG = FileAccessor.class.getName();
	public static String EXTERNAL_STOREPATH = getExternalStorePath();
	public static final String APPS_ROOT_DIR = getExternalStorePath()
			+ "/safecoo";
	public static final String EXPORT_DIR = getExternalStorePath()
			+ "/safecoo/safecoo_IM";
	public static final String CAMERA_PATH = getExternalStorePath()
			+ "/DCIM/safecoo";
	public static final String TACK_PIC_PATH = getExternalStorePath()
			+ "/safecoo/.tempchat";
	public static final String IMESSAGE_VOICE = getExternalStorePath()
			+ "/safecoo/voice";
	public static final String IMESSAGE_IMAGE = getExternalStorePath()
			+ "/safecoo/image";
	public static final String IMESSAGE_CHATBG = getExternalStorePath()
			+ "/safecoo/other";
	public static final String IMESSAGE_FILE = getExternalStorePath()
			+ "/safecoo/file";
	public static final String LOCAL_PATH = APPS_ROOT_DIR + "/config.txt";

	/**
	 * 初始化应用文件夹目录
	 */
	public static void initFileAccess() {
		File rootDir = new File(APPS_ROOT_DIR);
		if (!rootDir.exists()) {
			rootDir.mkdir();
		}

		File imessageDir = new File(IMESSAGE_VOICE);
		if (!imessageDir.exists()) {
			imessageDir.mkdir();
		}

		File imageDir = new File(IMESSAGE_IMAGE);
		if (!imageDir.exists()) {
			imageDir.mkdir();
		}

		File fileDir = new File(IMESSAGE_FILE);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		File avatarDir = new File(IMESSAGE_CHATBG);
		if (!avatarDir.exists()) {
			avatarDir.mkdir();
		}
	}

	public static String getAppKey() {
		if (isExistExternalStore()) {
			String content = readContentByFile(LOCAL_PATH);
			if (content != null) {
				try {
					String result = content.split(",")[0];
					if (result != null && result.contains("appkey=")) {
						return result.replace("appkey=", "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String readContentByFile(String path) {
		BufferedReader reader = null;
		String line = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				StringBuilder sb = new StringBuilder();
				reader = new BufferedReader(new FileReader(file));
				while ((line = reader.readLine()) != null) {
					sb.append(line.trim());
				}
				return sb.toString().trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 获取语音文件存储目录
	 * 
	 * @return
	 */
	public static File getVoicePathName() {
		if (!isExistExternalStore()) {
			LogTools.showLogi(TAG, "储存卡已拔出，语音功能将暂时不可用");
			return null;
		}

		File directory = new File(IMESSAGE_VOICE);
		if (!directory.exists() && !directory.mkdirs()) {
			LogTools.showLogi(TAG, "Path to file could not be created");
			return null;
		}

		return directory;
	}

	/**
	 * 其他的图片
	 * 
	 * @return
	 */
	public static File getOtherImgName() {
		if (!isExistExternalStore()) {
			LogTools.showLogi(TAG, "储存卡已拔出，语音功能将暂时不可用");
			return null;
		}

		File directory = new File(IMESSAGE_CHATBG);
		if (!directory.exists() && !directory.mkdirs()) {
			LogTools.showLogi(TAG, "Path to file could not be created");
			return null;
		}

		return directory;
	}

	/**
	 * 获取文件目录
	 * 
	 * @return
	 */
	public static File getFilePathName() {
		if (!isExistExternalStore()) {
			LogTools.showLogi(TAG, "储存卡已拔出");
			return null;
		}

		File directory = new File(IMESSAGE_FILE);
		if (!directory.exists() && !directory.mkdirs()) {
			LogTools.showLogi(TAG, "Path to file could not be created");
			return null;
		}

		return directory;
	}

	/**
	 * 返回图片存放目录
	 * 
	 * @return
	 */
	public static File getImagePathName() {
		if (!isExistExternalStore()) {
			LogTools.showLogi(TAG, "储存卡已拔出");
			return null;
		}

		File directory = new File(IMESSAGE_IMAGE);
		if (!directory.exists() && !directory.mkdirs()) {
			LogTools.showLogi(TAG, "Path to file could not be created");
			return null;
		}

		return directory;
	}

	/**
	 * 获取文件名后面有宽展名的
	 * 
	 * @param pathName
	 * @return
	 */
	public static String getFileName(String pathName) {

		int start = pathName.lastIndexOf("/");
		if (start != -1) {
			return pathName.substring(start + 1, pathName.length());
		}
		return pathName;

	}

	/**
	 * 外置存储卡的路径
	 * 
	 * @return
	 */
	public static String getExternalStorePath() {
		if (isExistExternalStore()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}
	/**
	 * 外置存储卡
	 */
	public static File getExternalStore(){
		if (isExistExternalStore()) {
			return Environment.getExternalStorageDirectory();
		}
		return null;
	}
	/**
	 * 是否有外存卡
	 * 
	 * @return
	 */
	public static boolean isExistExternalStore() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * /data/data/com.school51.bluetooth/files
	 *
	 * @return
	 */
	public static String getAppContextPath() {
		return MyApplication.getContext().getFilesDir().getAbsolutePath();
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	public static String getFileUrlByFileName(String fileName) {
		return FileAccessor.IMESSAGE_IMAGE + File.separator
				+ FileAccessor.getSecondLevelDirectory(fileName)
				+ File.separator + fileName;
	}

	/**
	 *
	 * @param filePaths删除的文件列表
	 */
	public static void delFiles(ArrayList<String> filePaths) {
		for (String url : filePaths) {
			if (!TextUtils.isEmpty(url))
				delFile(url);
		}
	}

	/** 删除文件 */
	public static boolean delFile(String filePath) {
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return true;
		}

		return file.delete();
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	public static String getSecondLevelDirectory(String fileName) {
		if (TextUtils.isEmpty(fileName) || fileName.length() < 4) {
			return null;
		}

		String sub1 = fileName.substring(0, 2);
		String sub2 = fileName.substring(2, 4);
		return sub1 + File.separator + sub2;
	}

	/**
	 *
	 * @param root
	 *            文件所在文件夹
	 * @param srcName
	 *            文件旧名字
	 * @param destName
	 *            文件新名字
	 */
	public static void renameTo(String root, String srcName, String destName) {
		if (TextUtils.isEmpty(root) || TextUtils.isEmpty(srcName)
				|| TextUtils.isEmpty(destName)) {
			return;
		}

		File srcFile = new File(root + srcName);
		File newPath = new File(root + destName);

		if (srcFile.exists()) {
			srcFile.renameTo(newPath);
		}
	}

	public static File getTackPicFilePath() {
		File localFile = new File(getExternalStorePath()
				+ "/mayi_school51/.tempchat", "temp.jpg");
		if ((!localFile.getParentFile().exists())
				&& (!localFile.getParentFile().mkdirs())) {
			LogTools.showLogi(TAG, "内存卡不存在");
			localFile = null;
		}
		return localFile;
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
	 * Java文件操作 获取文件扩展名 Get the file extension, if no extension or file name
	 *
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
	 * 文件大小
	 * */
	public static String formatFromSize(long size) {
		String suffix = null;

		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}

		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	/**
	 * 返回文件名
	 * 
	 * @param pathName
	 * @return
	 */
	public static String getFileAllname(String pathName) {
		File file = new File(pathName);
		if (!file.exists()) {
			return "";
		}
		return file.getName();
	}

	/**
	 * 打开文件
	 * 
	 * @param context
	 *            上下
	 * @param url
	 *            文件路径
	 * */
	public static void openFile(Context context, String url) {
		if (!TextUtils.isEmpty(url)) {
			File f = new File(url);
			if (f.isFile()) {
				openFile(context, f);
			} else {
				// LogTools.showToast(context, "文件没下载完成或者文件已删除");
			}
		}
	}

	/**
	 * 打开文件
	 * 
	 * @param context
	 *            上下
	 * @param f
	 *            文件
	 * */
	public static void openFile(Context context, File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}

	/**
	 * 得到文件类型
	 * 
	 * @param url
	 *            文件路径
	 * */
	public static String getMIMEType(String url) {
		String end = url.substring(url.lastIndexOf(".") + 1, url.length())
				.toLowerCase();
		String type = "";
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		return type;
	}

	/**
	 * 得到文件类型
	 * 
	 * @param f
	 *            文件
	 * */
	public static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (!end.equals("apk")) {
			type += "/*";
		}
		return type;
	}
}
