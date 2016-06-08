package org.mylibrary.utils;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by ws on 2015/12/31.
 * purpose of uuDoc
 */
public class CodeBtnTools {
    private static Handler handler;
    private static int codeS;
    public static  void resetCodeBtn(TextView btn){
        if(handler!=null){
            handler.removeCallbacks(onClick);
        }
        btn.setEnabled(true);
        btn.setText("获取验证码");
    }
    public static void clickCodeBtn(final TextView btn, int s) {
        codeS = s;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    btn.setEnabled(true);
                    btn.setText("获取验证码");
                }else{
                    btn.setText(codeS+"秒后可再次发送");
                }
            }
        };
        btn.setEnabled(false);
        handler.postDelayed(onClick, 1000);
    }

    private static Runnable onClick = new Runnable() {
        @Override
        public void run() {
            if (codeS > 0) {
                handler.postDelayed(this, 1000);
                handler.sendEmptyMessage(0);
                codeS--;
            }else {
                handler.sendEmptyMessage(1);
            }
        }
    };
}
