package com.baosaas.supervise.util;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

/**
 * @author hpj
 * @version 1.1
 */
public class WSObjectUtil {
    /**
     * @param o
     *            调用webservice返回的结果
     * @return 包含DataTable值的SoapObject
     */
    public static SoapObject getDataTableObject(SoapObject o) {
        return getDataTableObject(o, 0);
    }

    /**
     * @param o
     *            调用webservice返回的结果
     * @param i
     *            第i张表
     * @return 包含DataTable值的SoapObject
     */
    public static SoapObject getDataTableObject(SoapObject o, int i) {
        SoapObject rows = null;

        try {
            SoapObject diffgram = getDiffgramObject(o);
            rows = (SoapObject) diffgram.getProperty(i);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return rows;
    }

    /**
     * @param o
     *            从调用webservice返回的结果中取出的DataTable
     * @param index
     *            第index行
     * @return DataTable中一行的数据
     */
    public static SoapObject getDataRowObject(SoapObject o, int index) {
        SoapObject row = null;

        try {
            SoapObject diffgram = (SoapObject) o.getProperty(1);
            SoapObject rows = (SoapObject) diffgram.getProperty(0);
            row = (SoapObject) rows.getProperty(index);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return row;
    }

    /**
     * @param o
     *            调用webservice返回的结果
     * @return 包含DataSet对象的diffgram对象
     */
    public static SoapObject getDiffgramObject(SoapObject o) {
        return (SoapObject) o.getProperty("diffgram");
    }

    /**
     * @param o
     *            调用webservice返回的结果
     * @return DataSet
     */
    public static SoapObject getDataSetObject(SoapObject o) {
        SoapObject diffgr = getDiffgramObject(o);
        if (diffgr.toString().equals("anyType{}")) {
            return null;
        } else {
            return (SoapObject) diffgr.getProperty(0);
        }
    }

}

