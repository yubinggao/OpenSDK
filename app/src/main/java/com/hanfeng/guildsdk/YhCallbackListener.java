package com.hanfeng.guildsdk;


public interface YhCallbackListener<T> {
	
	public void callback(int code, T response);
}
