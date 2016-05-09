package com.baosaas.supervise.base;



import com.baosaas.supervise.util.Config;
import com.baosaas.supervise.util.DebugUtil;
import com.baosaas.supervise.util.WSUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Ellrien
 * @Date 2015年7月9日上午10:59:17
 * @Version 1.0
 */
public class BaseWebService {
    private static String namespace = Config.SERVER_NAMESPACE;
    private static String wsdl = Config.SERVICE_ROOT_URL;

    public static String getDataBySoapObjectJson(String json, String method) {
        String result = null;
        try {
            //1.实例化SoapObject对象
            SoapObject request = new SoapObject(Config.SERVER_NAMESPACE, method);
            //2.如果方法需要参数，设置参数
            request.addProperty("json", json);
            // request.addProperty("strPassWordMd5", strPassWordMd5);
            //3.设置Soap的请求信息,参数部分为Soap协议的版本号

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            //4.构建传输对象
            HttpTransportSE transport = new HttpTransportSE(wsdl);
            transport.debug = true;
            //5.访问WebService,第一个参数为调用方法的Aciton,第二个参数为Envelope对象

            transport.call(namespace + method, envelope);

            //6.解析返回的数据
            result = envelope.getResponse().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDataByJSON(String json, String method) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jsonstr", json);


        DebugUtil.longInfo("********* Method Name :" + method + " Request is ---> : ", json);

        String result = null;
        try {
            SoapPrimitive sp = WSUtil.getSoapPrimitiveByCallingWS(namespace, method, map, wsdl);
            result = sp.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        DebugUtil.longInfo("======== Method Name :" + method + "  Response is ----> : ", result);
        return result;
    }

    public static String getObjectDataByJSON(String json, String method) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jsonstr", json);
        String result = null;
        try {
            Object sp = WSUtil.getObjectByCallingWS(namespace, method, map, wsdl);
            result = (String) sp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDataByMap(Map<String, Object> map, String method) {
        String result = null;
        try {
            SoapPrimitive sp = WSUtil.getSoapPrimitiveByCallingWS(namespace, method, map, wsdl);
            result = sp.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}