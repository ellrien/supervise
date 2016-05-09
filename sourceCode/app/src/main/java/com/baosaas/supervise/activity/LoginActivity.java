package com.baosaas.supervise.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baosaas.supervise.AppManager;
import com.baosaas.supervise.R;
import com.baosaas.supervise.api.SystemInterface;
import com.baosaas.supervise.base.BaseActivity;
import com.baosaas.supervise.bean.UserIn;
import com.baosaas.supervise.bean.UserOut;
import com.baosaas.supervise.util.HandlerUtils;
import com.baosaas.supervise.util.NetSupport;
import com.baosaas.supervise.util.RedirectUtil;
import com.baosaas.supervise.util.SharedUtil;
import com.baosaas.supervise.util.StringUtils;
import com.baosaas.supervise.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.et_user_code)
    EditText etUserCode;
    @ViewInject(R.id.tv_version)
    TextView tvVersion;
    @ViewInject(R.id.et_password)
    EditText etPassword;

    private static int SHOW_TOAST = 1;
    private static int NO_NETWORK = 2;

    String userCode, password;

    SharedUtil config;

    @Override
    public void handleMsg(Message msg) {
        if (msg != null) {
            if (msg.what == SHOW_TOAST) {
                hideProgress();
                showToast(msg.obj.toString());
            } else if (msg.what == RESULT_OK) {
                hideProgress();
                showToast("登录成功");
//                RedirectUtil.go2Menu(this);
//                RedirectUtil.go2Select(this);
            } else if (msg.what == NO_NETWORK) {
                exit();
            }
        }
    }

    private void exit() {
        NetSupport.exitSysByNetErr(this, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getInstance().addExitActivity(this);
        ViewUtils.inject(this);
        config = SharedUtil.getInstance();
        if (config != null) {
            tvVersion.setText("v" + config.version);
            etUserCode.setText(config.userCode);
        }
    }

    public void login(View view) {
        if (!Tools.isFastDoubleClick()) {
            userCode = etUserCode.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            validate();
        }
    }

    private void validate() {
        if (StringUtils.isEmpty(userCode)) {
            showToast("请输入用户名");
            return;
        }
        if (StringUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        showProgress(userCode+"登录中...",false);
        new ChildThread().start();
    }

    class ChildThread extends Thread {
        @Override
        public void run() {
            //1.检查网络
            if (NetSupport.checkNetWork(LoginActivity.this)) {
                SystemInterface systemI = new SystemInterface();
//                UserIn userIn = new UserIn(userCode, MD5Util.md5(password));
                UserIn userIn = new UserIn(userCode, password);
                UserOut out = systemI.login(userIn);
                if (out != null && out.getFlag() == 0) {
                    HandlerUtils.sendMsg(handler, SHOW_TOAST, out.getMsg());
                    return;
                } else if (out != null && out.getFlag() == 1) {
                    SharedUtil sp = SharedUtil.getInstance();
                    sp.accessToken = out.getToken();
                    sp.userCode = userCode;
                    sp.userName = out.getUserName();
                    sp.saveInstance();
                    HandlerUtils.sendMsg(handler, RESULT_OK, "OK");
                } else {
                    HandlerUtils.sendMsg(handler, SHOW_TOAST, "登录失败");
//                    HandlerUtils.sendMsg(handler, RESULT_OK, "OK");
                    return;
                }
            } else {
                HandlerUtils.sendMsg(handler, NO_NETWORK, "无网络");
            }
        }
    }
}
