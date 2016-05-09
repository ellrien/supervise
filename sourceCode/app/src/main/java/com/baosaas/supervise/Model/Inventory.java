package com.baosaas.supervise.Model;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by lym
 */
@Table(name = "inventory")
public class Inventory {
    @Id
    private Integer id;
    private String taskCode; //盘库单号
    private String createDate; //创建日期
    private String sysId; //系统资源号
    private String type; //盘库类型
    private String warehouse; //仓库名称
    private String packNum; //捆包号
    private String weight; //重量
    private String barCode; //条形码
    private String position; //库位
    private String tag; //标签号
    private String standard; //规格
    private int status; //0为新增 1为已验证未上传  2为已上传
    private int checkResult;//盘库结果 成功=Config.RESULT_SUCCESS 失败=RESULT_FAILURE
//    private String checkErrorType;//异常类型
    private String checkErrorDesc;//异常描述
    private String checkDate;//盘库时间 yyyy-MM-dd hh:mi:dialog_enter
    private String packImage;//捆包照片
    private String financialwarehouse;//金融仓库

    public String getFinancialwarehouse() {
        return financialwarehouse;
    }

    public void setFinancialwarehouse(String financialwarehouse) {
        this.financialwarehouse = financialwarehouse;
    }

    public Inventory() {
    }

    public Inventory(Integer id, String taskCode, String createDate, String sysId, String type, String warehouse, String packNum, String weight, String barCode, String position, String tag, String standard, int status, int checkResult, String checkErrorType, String checkErrorDesc, String checkDate, String packImage, String financialwarehouse) {
        this.id = id;
        this.taskCode = taskCode;
        this.createDate = createDate;
        this.sysId = sysId;
        this.type = type;
        this.warehouse = warehouse;
        this.packNum = packNum;
        this.weight = weight;
        this.barCode = barCode;
        this.position = position;
        this.tag = tag;
        this.standard = standard;
        this.status = status;
        this.checkResult = checkResult;
//        this.checkErrorType = checkErrorType;
        this.checkErrorDesc = checkErrorDesc;
        this.checkDate = checkDate;
        this.packImage = packImage;
        this.financialwarehouse = financialwarehouse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getType() {
        type = "0";
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public int getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(int checkResult) {
        this.checkResult = checkResult;
    }

//    public int getCheckErrorType() {
//        return checkErrorType;
//    }
//
//    public void setCheckErrorType(int checkErrorType) {
//        this.checkErrorType = checkErrorType;
//    }


//    public String getCheckErrorType() {
//        return checkErrorType;
//    }
//
//    public void setCheckErrorType(String checkErrorType) {
//        this.checkErrorType = checkErrorType;
//    }

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

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getPackImage() {
        return packImage;
    }

    public void setPackImage(String packImage) {
        this.packImage = packImage;
    }
}
