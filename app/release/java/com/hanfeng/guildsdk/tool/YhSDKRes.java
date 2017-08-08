package com.hanfeng.guildsdk.tool;

import android.content.Context;

public class YhSDKRes {
	private static YhSDKRes resource = null;
	
	private YhSDKRes(){
	}
	
	public int getLayoutId(Context context, String name) {
		return context.getResources().getIdentifier(name, "layout", context.getPackageName());
	}
	
	public int getRStringId(Context context, String name) {
		return context.getResources().getIdentifier(name, "string", context.getPackageName());
	}
	
	public int getDrawableId(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}
	
	public int getResId(Context context, String name) {
		return context.getResources().getIdentifier(name, "id", context.getPackageName());
	}
	
	public String getRString(Context context, String name) {
		return context.getApplicationContext().getString(getRStringId(context, name));
	}
	
	public static YhSDKRes getRes() {
		if (resource == null) {
			resource = new YhSDKRes();
		}
		return resource;
	}
}
