package com.baosaas.supervise.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置类
 * Created by Ellrien on 2015/11/16.
 */
public class Config {
    /**
     * 正式环境http://west.baosaas.com/YSCC_WebService/JGGLWebservice.asmx======http://t.cn/R4qABHG
     * 测试环境http://10.31.88.151/YSCC_WebService/JGGLWebService.asmx?wsdl=====http://t.cn/R4qArFi
     * http://10.31.88.151/YSCC_WebService/JGGLWebService.asmx
     */
    public final static String SERVICE_ROOT_URL = "http://west.baosaas.com/YSCC_WebService/JGGLWebservice.asmx";
    public final static String SERVER_NAMESPACE = "http://baosaas.org/";
    public final static String UMENG_API_KEY = "564c530ae0f55af56b0040b1";

    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "xUtils.db";
    public static final String SHARED_PREFERENCES = "sb";
    public  static  long EXIT_TIME = 0;
    /**
     * 强制更新标识
     */
    public static final int UPDATE_MANDATORY = 10;
    public static final int APP_TYPE = 10;//安卓PDA版本为10   安卓平板为20

    //    报错给线程提示
    public static final int FAILURE = 50;
    //    成功给线程提示
    public static final int SUCCESSFUL = 20;

    /**
     * 本地数据库里数据状态
     * 新数据为new_data
     * 已校验未上传un_upload
     * 已上传uploaded
     */
    public static final int NEW_DATA = 0;
    public static final int UN_UPLOAD = 1;
    public static final int UPLOADED = 2;

    public static final String RESULT_OK = "1";
    public static final String RESULT_ERROR = "0";

    /**
     * 盘库结果
     */
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILURE = 0;

    /**
     * 异常类型
     * 货物破损  10
     * 货物变形  20
     * 货物丢失  30
     * 堆放超高  40
     * 已出库    50
     * 其他     90
     * 无       0
     */
    public static final int check_Error_Type1 = 10;
    public static final int check_Error_Type2 = 20;
    public static final int check_Error_Type3 = 30;
    public static final int check_Error_Type4 = 40;
    public static final int check_Error_Type5 = 50;
    public static final int check_Error_Type6 = 90;
    public static final int check_Error_Type = 0;
    /**
     * APK文件名
     */
    public static final String FILE_NAME = "ouyeel_supervise.apk";

    public static final List<String> getCheckErrorTypes() {
        List<String> list = new ArrayList<>();
        list.add("货物破损");
        list.add("货物变形");
        list.add("货物丢失");
        list.add("堆放超高");
        list.add("已出库");
        list.add("其他");
        list.add("正常");
        return list;
    }

    public static final String getCheckErrorTypes(String errorDesc) {
        String result = null;
        if (errorDesc.equals("货物破损")) {
            result = "10";
        } else if (errorDesc.equals("货物变形")) {
            result = "20";
        } else if (errorDesc.equals("货物丢失")) {
            result = "30";
        } else if (errorDesc.equals("堆放超高")) {
            result = "40";
        } else if (errorDesc.equals("已出库")) {
            result = "50";
        } else if (errorDesc.equals("其他")) {
            result = "90";
        } else if (errorDesc.equals("正常")) {
            result = "0";
        }
        return result;
    }

    public static final String getCheckErrorDesc(int errorType) {
        String result = null;
        switch(errorType){
            case check_Error_Type1:
                result = getCheckErrorTypes().get(0);
                break;
            case check_Error_Type2:
                result = getCheckErrorTypes().get(1);
                break;
            case check_Error_Type3:
                result = getCheckErrorTypes().get(2);
                break;
            case check_Error_Type4:
                result = getCheckErrorTypes().get(3);
                break;
            case check_Error_Type5:
                result = getCheckErrorTypes().get(4);
                break;
            case check_Error_Type6:
                result = getCheckErrorTypes().get(5);
                break;
            case check_Error_Type:
                result = getCheckErrorTypes().get(6);
                break;
        }
        return result;
    }
}
