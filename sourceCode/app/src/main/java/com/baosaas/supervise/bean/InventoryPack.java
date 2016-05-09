package com.baosaas.supervise.bean;


import com.baosaas.supervise.util.Config;

import java.io.Serializable;

/**
 * Created by ZhangZhiCheng on 2015/12/9.
 */
public class InventoryPack implements Serializable {
    private String businessBillId; //盘库单号
    private String packNum; //捆包号
    private String weight; //重量
    private String barCode; //条形码
    private String position; //库位
    private String standard; //规格
    private Integer id;
    private int status; //0为新增 1为已验证未上传  2为已上传
    private int checkResult;//盘库结果 成功=Config.RESULT_SUCCESS 失败=RESULT_FAILURE
    private int checkErrorType;//异常类型
    private String checkErrorDesc;//异常描述
    private String checkDate;//盘库时间 yyyy-MM-dd hh:mi:ss
    private String tag;

    public InventoryPack() {
    }

    public String getCheckStatusString()
    {
        if (status == Config.NEW_DATA)
        {
            return "未盘";
        }
        if (checkErrorType != Config.check_Error_Type)
        {
            return "异常";
        }
        return "已盘";
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessBillId() {
        return businessBillId;
    }

    public void setBusinessBillId(String businessBillId) {
        this.businessBillId = businessBillId;
    }

    public String getPackNum() {
        return packNum;
    }

    public void setPackNum(String packNum) {
        this.packNum = packNum;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String spec) {
        this.standard = spec;
    }

    public int getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(int checkResult) {
        this.checkResult = checkResult;
    }

    public int getCheckErrorType() {
        return checkErrorType;
    }

    public void setCheckErrorType(int checkErrorType) {
        this.checkErrorType = checkErrorType;
    }

    public String getCheckErrorDesc() {
        return checkErrorDesc;
    }

    public void setCheckErrorDesc(String checkErrorDesc) {
        this.checkErrorDesc = checkErrorDesc;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
