package com.hanfeng.guildsdk.tool;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;
public class LoginPictureTools {
	public static Drawable loadImageFromAsserts(final Context ctx, String fileName) {
        try {
            String name = "yhsdk/images/";
            InputStream is = ctx.getResources().getAssets().open(name+fileName);
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
