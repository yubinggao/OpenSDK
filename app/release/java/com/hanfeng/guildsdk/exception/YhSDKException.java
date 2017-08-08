package com.hanfeng.guildsdk.exception;

public class YhSDKException extends Exception {
	private static final long serialVersionUID = 6718244309216359724L;


	public YhSDKException(){
		super();
	}
	
	
	public YhSDKException(String message) {
		super(message);
	}
	
	public YhSDKException(String message, Throwable cause){
		super(message, cause);
	}
	
}
