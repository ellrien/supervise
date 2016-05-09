package com.baosaas.supervise.api;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baosaas.supervise.base.BaseWebService;
import com.baosaas.supervise.bean.UserIn;
import com.baosaas.supervise.bean.UserOut;
import com.baosaas.supervise.bean.VersionIn;
import com.baosaas.supervise.bean.VersionOut;
import com.baosaas.supervise.util.StringUtils;

/**
 * Created by Ellrien on 2015/11/16.
 */
public class SystemInterface extends BaseWebService {

    /**
     * 获取版本号
     */
    public VersionOut getAppVersion(VersionIn versionIn) {
        String json = JSON.toJSONString(versionIn);
        String result = getDataBySoapObjectJson(json, "getAppVersion");
        VersionOut out = null;
        if (StringUtils.isNotEmpty(result)) {

            out = JSON.parseObject(result, VersionOut.class);
        }
        return out;
    }

    /**
     * 登录
     */
    public UserOut login(UserIn user) {
        String json = JSON.toJSONString(user);
//        Map<String, Object> map = new HashMap<>();
//        map.put("userID", user.getUserID());
//        map.put("passWord", user.getPassWord());
        String result = getDataBySoapObjectJson(json, "login");
        UserOut out = null;
        if (StringUtils.isNotEmpty(result)) {
            out = JSON.parseObject(result, UserOut.class);
            Log.e("login", out.toString());
        }
        return out;
    }

}
