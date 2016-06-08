package org.mylibrary.biz;

public class MyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -453260313823712498L;
	public int mErrCode;
	public static final int ERR_NO_AVAILABLE_NETWORK = 1;
	public static final String NO_AVAILABLE_NETWORK="网络连接出错";
	
	public static final int ERR_CONNECT_TIMEOUT = 2;
	
	public static final int ERR_SOKET_TIMEOUT = 3;

	public MyException(Throwable e) {
		saveLog(e);
	}

	public MyException(Throwable e, int code) {
		saveLog(e);
		mErrCode = code;
	}

	public MyException() {

	}

	@Override
	public String toString() {
		switch (mErrCode) {
		case ERR_NO_AVAILABLE_NETWORK:
			return NO_AVAILABLE_NETWORK;
		}
		return super.toString();
	}
	
	@Override
	public String getMessage() {
		switch (mErrCode) {
		case ERR_NO_AVAILABLE_NETWORK:
			return NO_AVAILABLE_NETWORK;
		}
		return super.getMessage();
	}

	public void saveLog(Throwable e) {
		if (e != null) {
			e.printStackTrace();
		}
	}
	
	
}
