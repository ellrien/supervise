package com.baosaas.supervise.util;

import android.util.Log;


/**
 * Created by Ocean on 8/27/15.
 */
public class DebugUtil {

    public static void longInfo(String tag, String str) {
        if(StringUtils.isEmpty(str)){
            return;
        }
        Log.e(tag, str);
    }
}
