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
    private String sysId; //系统资源号
    private String inventoryType; //盘库类型
    private String warehouse; //仓库名称
    private String packNum; //捆包号
    private String weight; //重量
    private String barCode; //条形码
    private String position; //库位
    private String tag; //标签号
    private String standard; //规格
    private int status; //0为新增 1为已验证未上传  2为已上传
    private int checkResult;//盘库结果 成功=Config.RESULT_SUCCESS 失败=RESULT_FAILURE
    private String checkErrorDesc;//异常描述
//    private String checkDate;//盘库时间 yyyy-MM-dd hh:mi:dialog_enter
    private String packImage;//捆包照片


    public Inventory() {
    }

    public Inventory(Integer id, String taskCode, String sysId, String inventoryType, String warehouse, String packNum, String weight, String barCode, String position, String tag, String standard, int status, int checkResult, String checkErrorDesc, String packImage) {
        this.id = id;
        this.taskCode = taskCode;
        this.sysId = sysId;
        this.inventoryType = inventoryType;
        this.warehouse = warehouse;
        this.packNum = packNum;
        this.weight = weight;
        this.barCode = barCode;
        this.position = position;
        this.tag = tag;
        this.standard = standard;
        this.status = status;
        this.checkResult = checkResult;
        this.checkErrorDesc = checkErrorDesc;
//        this.checkDate = checkDate;
        this.packImage = packImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
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


    public String getCheckErrorDesc() {
        return checkErrorDesc;
    }

    public void setCheckErrorDesc(String checkErrorDesc) {
        this.checkErrorDesc = checkErrorDesc;
    }
//
//    public String getCheckDate() {
//        return checkDate;
//    }
//
//    public void setCheckDate(String checkDate) {
//        this.checkDate = checkDate;
//    }

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

    public String getInventoryType() {
        inventoryType = "0";
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }
}
