package com.baosaas.supervise.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Ellrien on 2015/11/19.
 */
public class HandlerUtils {
    public static void sendMsg(Handler handler, int what, Object obj) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = obj;
        handler.sendMessage(message);
    }
}
