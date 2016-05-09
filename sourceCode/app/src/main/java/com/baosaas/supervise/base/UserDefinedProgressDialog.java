package com.baosaas.supervise.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

public class UserDefinedProgressDialog extends ProgressDialog {

	public UserDefinedProgressDialog(Context context) {
		super(context);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
        	//显示进度对话框时屏蔽返回键
        	return false;
        }
        return super.onKeyDown(keyCode, event);  
    }
}