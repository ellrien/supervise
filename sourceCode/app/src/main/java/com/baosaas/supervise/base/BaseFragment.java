package com.baosaas.supervise.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baosaas.supervise.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Ellrien on 2015/12/8.
 */
public class BaseFragment extends FragmentActivity {
    private UserDefinedProgressDialog progressDlg;
    private Toast toast;
    public static String TAG;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleMsg(msg);
            }
        };
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void handleMsg(Message msg) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        TAG = this.getClass().getSimpleName();
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
    }

    /**
     * 显示loading
     *
     * @param msg 自定义消息
     */
    public void showProgress(String msg) {
        if (this != null) {
            progressDlg = new UserDefinedProgressDialog(this);
            progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDlg.setMessage(msg);
            progressDlg.setIndeterminate(true);
            progressDlg.show();
        }
    }

    /**
     * 显示loading
     *
     * @param msg 自定义消息
     */
    public void showProgress(String msg, boolean canCancle) {
        if (canCancle) {
            showProgress(msg);
            return;
        }
        if (this != null) {
            progressDlg = new UserDefinedProgressDialog(this);
            progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDlg.setMessage(msg);
            progressDlg.setCanceledOnTouchOutside(false);
            progressDlg.setIndeterminate(true);
            progressDlg.show();
        }
    }

    /**
     * 隐藏loading
     */
    public void hideProgress() {

        if (progressDlg != null) {
            progressDlg.dismiss();
        }
    }

    public void showToast(String msg) {
        showToast(msg, true);
    }

    public void showToast(String msg, int id, boolean status) {
        if (status) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout layout = (LinearLayout) toast.getView();
            ImageView image = new ImageView(getApplicationContext());
            image.setImageResource(id);
            layout.addView(image, 0);
            toast.show();
        } else {
            if (toast != null) {
                toast.cancel();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        showToast("", R.mipmap.ic_launcher, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
        showToast("", R.mipmap.ic_launcher, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDlg != null && progressDlg.isShowing()) {
            progressDlg.cancel();
        }

        showToast("", R.mipmap.ic_launcher, false);
    }

    //请在onpase ondestory时调用（关闭或暂停时）
    public void showToast(String msg, boolean status) {
        if (status == true) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        if (status == false) {
            toast.cancel();
        }
    }

    public void showLongToast(String msg, boolean status) {
        if (status == true) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        if (status == false) {
            toast.cancel();
        }
    }
}
