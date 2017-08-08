package com.hanfeng.guildsdk;




import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.hanfeng.guildsdk.tool.AssetTool;
import com.reyun.sdk.ReYunTrack;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import android.app.Application;
import android.util.Log;
import android.util.Xml;
/**
 * 
 * */
public class HfsdkApplication extends Application{
//	TalkingDataAppCpa：申请下的：814645396DC24708B376ECCCFBAD9E86
//	ReYun:申请下的：7adbe9deb5b7bd03b05ed8c3c4bd6844
	private static final String TAG = "TDRYSDK";
	@Override
	public void onCreate() {
		super.onCreate();
//			TalkingDataAppCpa. setVerboseLogDisable();
			Map<String, String> sdkconfig = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
//			路径：hfsdk测试的："yhsdk/conf/sdkconf.xml"  
//			String xmlstr = AssetTool.getInstance().getAssetConfigs(getApplicationContext(), "yhsdk/conf/sdkconf.xml");
//			路径：打包正式的："hfnsdk/config/sdkconf.xml"  
			String xmlstr = AssetTool.getInstance().getAssetConfigs(getApplicationContext(), "hfnsdk/config/sdkconf.xml");
			try {
				parser.setInput(new StringReader(xmlstr));
				int eventType = parser.getEventType();
				while(eventType != XmlPullParser.END_DOCUMENT){
					if (eventType == XmlPullParser.START_TAG) {
						String tag = parser.getName();
						if ("configer".equals(tag) || parser.getAttributeCount() == 0) {
							eventType = parser.next();
							continue;
						}else{
							sdkconfig.put(tag, parser.getAttributeValue(0));
						}
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			String td = sdkconfig.get("td");
			String ry = sdkconfig.get("ry");
			String jointd = sdkconfig.get("hasjointd");
			String joinry = sdkconfig.get("hasjoinry");
			
			
			if ("false".equals(jointd)) {
				TalkingDataAppCpa.init(getApplicationContext(),td , Constants.TD_RY_CHANNEL) ;
				Log.i(TAG, "TD.Init");
				Log.i(TAG, "TD.Init");
				Log.i(TAG, "td:chanel"+Constants.TD_RY_CHANNEL);
			}else{
				Log.i(TAG, "TD.NoInit");
			}
			
			if ("false".equals(joinry)) {
				ReYunTrack.initWithKeyAndChannelId(getApplicationContext(),ry , Constants.TD_RY_CHANNEL);
				Log.i(TAG, "RY.Init");
				Log.i(TAG, "ry:chanel"+Constants.TD_RY_CHANNEL);
			}else{
				Log.i(TAG, "RY.NoInit");
			}
		
	}
}
