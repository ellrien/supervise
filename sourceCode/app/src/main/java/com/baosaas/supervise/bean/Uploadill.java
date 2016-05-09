package com.baosaas.supervise.bean;

/**
 * Created by Administrator on 2015/12/17.
 */
public class Uploadill {
    private String taskCode; //盘库单号
    //    private String createDate; //创建日期
    private String weights;//已盘总重量
    private int type; //盘库类型
    private String financialwarehouse; //仓库名称
    private int nums; //已盘件数
    private String taskImage;//Base64编码过的图片字符串

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(String taskImage) {
        this.taskImage = taskImage;
    }


//    public String getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(String createDate) {
//        this.createDate = createDate;
//    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFinancialwarehouse() {
        return financialwarehouse;
    }

    public void setFinancialwarehouse(String financialwarehouse) {
        this.financialwarehouse = financialwarehouse;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
}
