package com.baosaas.supervise.api;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baosaas.supervise.Model.Inventory;
import com.baosaas.supervise.base.BaseWebService;
import com.baosaas.supervise.bean.UserBillIn;
import com.baosaas.supervise.bean.InventoryBillOut;
import com.baosaas.supervise.bean.InventoryUploadIn;
import com.baosaas.supervise.bean.InventoryUploadOut;
import com.baosaas.supervise.util.SharedUtil;
import com.baosaas.supervise.util.StringUtils;

import java.util.List;

/**
 * Created by ThinkPad on 2015/12/15.
 * 盘库单下载以及盘库结果上传接口
 */
public class InventoryInterface extends BaseWebService {
    /**
     * 获取盘库数据
     * @param user
     * @return
     */
    public InventoryBillOut getBillList(UserBillIn user) {
        //test
//        user.setToken("49218165271a4be0b14667f51d8cba9c");
//        user.setUserId("10086");
        //end of test
        String json = JSON.toJSONString(user);
        String result = getDataBySoapObjectJson(json, "getBillList");
        //result = "{\"flag\":1,\"msg\":\"获取盘库信息成功！\",\"bills\":[{\"businessBillId\":\"PK20151016001\",\"createDate\":\"2015-10-16\",\"packNum\":\"ji080\",\"weight\":\"230.000\",\"sysId\":\"\",\"warehouse\":\"1号库\",\"type\":20,\"barCode\":\"\",\"position\":\"9\",\"tag\":\"ji080\",\"standard\":\"9*9\"},{\"businessBillId\":\"PK20151016002\",\"createDate\":\"2015-10-16\",\"packNum\":\"2015090930\",\"weight\":\"210.000\",\"sysId\":\"\",\"warehouse\":\"1号库\",\"type\":20,\"barCode\":\"\",\"position\":\"7\",\"tag\":\"2015090930\",\"standard\":\"7*7\"},{\"businessBillId\":\"PK20151016002\",\"createDate\":\"2015-10-16\",\"packNum\":\"jd002\",\"weight\":\"230.000\",\"sysId\":\"\",\"warehouse\":\"1号库\",\"type\":20,\"barCode\":\"\",\"position\":\"3\",\"tag\":\"jd002\",\"standard\":\"8*8\"},{\"businessBillId\":\"PK20151016002\",\"createDate\":\"2015-10-16\",\"packNum\":\"MU1006009881\",\"weight\":\"7.000\",\"sysId\":\"\",\"warehouse\":\"1号库\",\"type\":20,\"barCode\":\"\",\"position\":\"112\",\"tag\":\"MU1006009881\",\"standard\":\"220*12*C\"}],\"unbounds\":null}";
//        Log.e("getBillList", result);
        InventoryBillOut out = null;
        if (StringUtils.isNotEmpty(result)) {
            out = JSON.parseObject(result, InventoryBillOut.class);
            Log.e("getInventoryBills", out.toString());
        }
        return out;
    }

    /**
     * 上传盘库信息
     * @param list
     * @return
     */
    public InventoryUploadOut uploadBillList(List<Inventory> list) {
        InventoryUploadIn in = new InventoryUploadIn(SharedUtil.getInstance().userCode,SharedUtil.getInstance().accessToken,list);
        //test
//        in.setToken("49218165271a4be0b14667f51d8cba9c");
//        in.setUserId("10086");
        //end of test
        String json = JSON.toJSONString(in);
        String result = getDataBySoapObjectJson(json, "uploadBillList");
        InventoryUploadOut uploadOut = null;
        if (StringUtils.isNotEmpty(result)) {
            uploadOut = JSON.parseObject(result, InventoryUploadOut.class);
            Log.e("changeUserPwd", uploadOut.toString());
        }
        return uploadOut;
    }
}
