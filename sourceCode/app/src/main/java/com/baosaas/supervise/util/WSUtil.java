package com.baosaas.supervise.util;

import android.app.Activity;
import android.util.Log;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class WSUtil {
    public static Boolean getBooleanByCallingWS(String nameSpace,
                                                String methodName, Map<String, Object> params, String wsdl)
            throws IOException, XmlPullParserException {
        return (Boolean) getObjectByCallingWS(nameSpace, methodName, params,
                wsdl);
    }

    @SuppressWarnings("unchecked")
    public static Vector<SoapObject> getSoapObjectVectorByCallingWS(
            String nameSpace, String methodName, Map<String, Object> params,
            String wsdl) throws IOException, XmlPullParserException {
        return (Vector<SoapObject>) getObjectByCallingWS(nameSpace, methodName,
                params, wsdl);
    }

    public static SoapObject getSoapObjectByCallingWS(String nameSpace,
                                                      String methodName, Map<String, Object> params, String wsdl)
            throws IOException, XmlPullParserException {
        return (SoapObject) getObjectByCallingWS(nameSpace, methodName, params,
                wsdl);
    }

    public static SoapPrimitive getSoapPrimitiveByCallingWS(String methodName,
                                                            Map<String, Object> params) throws IOException, XmlPullParserException {
        return (SoapPrimitive)getObjectByCallingWS(Config.SERVER_NAMESPACE, methodName, params, Config.SERVICE_ROOT_URL);
    }

    public static SoapPrimitive getSoapPrimitiveByCallingWS(String nameSpace,
                                                            String methodName, Map<String, Object> params, String wsdl)
            throws IOException, XmlPullParserException {
        return (SoapPrimitive) getObjectByCallingWS(nameSpace, methodName,
                params, wsdl);
    }

    public static Object getObjectByCallingWS(String nameSpace,
                                              String methodName, Map<String, Object> params, String wsdl)
            throws IOException, XmlPullParserException {

        final String SOAP_ACTION = nameSpace + methodName;
        Object soapPrimitiveResult = null;

        SoapSerializationEnvelope envelope = constructRequestObject2(nameSpace,
                methodName, params);
        soapPrimitiveResult = callWebservice(SOAP_ACTION, wsdl, envelope);

        return soapPrimitiveResult;
    }

    private static SoapSerializationEnvelope constructRequestObject2(
            String nameSpace, String methodName, Map<String, Object> params) {
        SoapObject request = new SoapObject(nameSpace, methodName);// 第一步，实例化SoapObject对象，并指定命名空间和方法名
        if (params != null && !params.isEmpty()) {
            for (Iterator<Map.Entry<String, Object>> it = params.entrySet()
                    .iterator(); it.hasNext();) {
                Map.Entry<String, Object> e = it.next();
                if (e.getValue() instanceof byte[]) {
                    byte[] d = (byte[]) e.getValue();
                    String data = new String(Base64.encode(d));
                    // request.addProperty(e.getKey(), new
                    // SoapPrimitive(SoapEnvelope.ENC, "base64Binary", data));
                    request.addProperty(e.getKey(), data);// 第二步，设置调用参数方法，包括参数名称和参数值
                } else {
                    request.addProperty(e.getKey().toString(), e.getValue());
                }
            }
        }
        // 第三步，设置SOAP请求信息（参数部分为SOAP协议版本号，与你要调用到的WEBSERVICE中的版本号一致）
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);
        envelope.setOutputSoapObject(request);

        // envelope.bodyOut = request;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapEnvelope.ENC;
        return envelope;
    }

    public static SoapPrimitive getSoapPrimitiveByCallingWSWithFile(
            String nameSpace, String methodName, Map<String, Object> params,
            String wsdl) throws IOException, XmlPullParserException {

        final String SOAP_ACTION = nameSpace + methodName;
        SoapPrimitive soapPrimitiveResult = null;

        SoapObject request = constructRequestObjectWithFile(nameSpace,
                methodName, params);
        SoapSerializationEnvelope envelope = getEncEnvelope(request);
        soapPrimitiveResult = (SoapPrimitive) callWebservice(SOAP_ACTION, wsdl,
                envelope);

        return soapPrimitiveResult;
    }

    public static Object callWebservice(String SOAP_ACTION, String wsdl,
                                        SoapSerializationEnvelope envelope) throws IOException,
            XmlPullParserException {

        // registerObjects(envelope);
        Object resultObject = null;
        //默认超市事件改为 30S
        HttpTransportSE ht = new HttpTransportSE(wsdl, 30000); // 第四步，构建传输对象，并指明wsdl文档url
        try {
            ht.call(SOAP_ACTION, envelope);// 第五步，调用webservice（其中参数一SOAP_ACTION为命名空间+方法名，参数二为envelope）

        } catch (IOException e) {
            //Log.e("IOException:", e.getMessage());
            DebugUtil.longInfo("IOException:", e.getLocalizedMessage());
            throw e;
        } catch (XmlPullParserException e1) {
            //Log.e("XmlPullParserException", e1.getMessage());
            DebugUtil.longInfo("XmlPullParserException:",e1.getLocalizedMessage());
            throw e1;
        }catch (Exception e){
            DebugUtil.longInfo("WSUTIL",e.getLocalizedMessage());
        }
        try {
            resultObject = envelope.getResponse();// 第6步：使用getResponse方法获得WebService方法的返回结果
        } catch (SoapFault e) {
            Log.e("SoapFault", e.getMessage());
            throw e;
        }
        return resultObject;
    }

    public static SoapObject constructRequestObject(String nameSpace,
                                                    String methodName, Map<String, Object> params) {

        SoapObject request = new SoapObject(nameSpace, methodName);

        if (params != null && !params.isEmpty()) {
            for (Iterator<Map.Entry<String, Object>> it = params.entrySet()
                    .iterator(); it.hasNext();) {
                Map.Entry<String, Object> e = it.next();
                request.addProperty(e.getKey().toString(), e.getValue());
            }
        }
        return request;
    }

    public static SoapObject constructRequestObjectWithFile(String nameSpace,
                                                            String methodName, Map<String, Object> params) {

        SoapObject request = new SoapObject(nameSpace, methodName);
        if (params != null && !params.isEmpty()) {
            for (Iterator<Map.Entry<String, Object>> it = params.entrySet()
                    .iterator(); it.hasNext();) {
                Map.Entry<String, Object> e = it.next();
                if (e.getValue() instanceof byte[]) {
                    byte[] d = (byte[]) e.getValue();
                    String data = new String(Base64.encode(d));
                    request.addProperty(e.getKey(), new SoapPrimitive(
                            SoapEnvelope.ENC, "base64Binary", data));
                } else {
                    request.addProperty(e.getKey().toString(), e.getValue());
                }
            }
        }
        return request;
    }

    public static SoapSerializationEnvelope getEncEnvelope(SoapObject request) {

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        // envelope.bodyOut = request;
        envelope.dotNet = true;
        envelope.encodingStyle = SoapEnvelope.ENC;
        return envelope;
    }

    public static SoapSerializationEnvelope getEnvelope(SoapObject request) {

        // SoapSerializationEnvelope envelope=new
        // SoapSerializationEnvelope(SoapEnvelope.VER10);//SOAP 1.0
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);// SOAP 1.1
        // SoapSerializationEnvelope envelope=new
        // SoapSerializationEnvelope(SoapEnvelope.VER12);//SOAP 1.2

        envelope.dotNet = true;
        // envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);

        return envelope;
    }

    public static Map<String, String> getDatabyWebservicesMap(
            Activity activity, HashMap<String, Object> params, String servies, String namespace, String wsdl) {

        SoapObject queryResult = null;
        ArrayList<List<String>> rows = null;

        try {
            queryResult = WSUtil.getSoapObjectByCallingWS(
                    namespace, servies, params,
                    wsdl);
        } catch (Exception e) {
			/*
			 * new
			 * AlertDialog.Builder(activity.getApplicationContext()).setTitle
			 * (R.string.message_title) .setMessage(R.string.connection_error)
			 * .setPositiveButton(R.string.OK_text, null).show();
			 * activity.finish();
			 */
            // return;
        }

        return WSUtil.handleResultMap(queryResult);
    }

    public static Map<String, String> handleResultMap(SoapObject queryResult) {
        WSObjectUtil wsObjectUtil = new WSObjectUtil();
        SoapObject dataset = WSUtil.getDataRow(queryResult, 0);

        if (dataset == null) {
            // noResult();
            return null;
        }
        return WSUtil.getPropertiesMap(dataset);
    }

    public static SoapObject getDataRow(SoapObject o, Integer index) {
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

    public static Map<String, String> getPropertiesMap(SoapObject o) {
        Map<String, String> propertiesMap = new HashMap<String, String>();

        for (int i = 0; i < o.getPropertyCount(); i++) {
            PropertyInfo propertyInfo = new PropertyInfo();
            o.getPropertyInfo(i, propertyInfo);
            String propertyName = propertyInfo.getName();
            propertiesMap.put(propertyName, o.getProperty(i).toString());
        }

        return propertiesMap;
    }

    public static ArrayList<List<String>> handleResult(SoapObject queryResult) {
        WSObjectUtil wsObjectUtil = new WSObjectUtil();
        SoapObject dataset = WSObjectUtil.getDataTableObject(queryResult);

        if (dataset == null) {
            // noResult();
            return null;
        }
        ArrayList<List<String>> rowparameter = new ArrayList<List<String>>();
        rowparameter = (ArrayList<List<String>>) WSUtil
                .getPropertiesTableList(dataset);
        return rowparameter;
    }

    public static List<List<String>> getPropertiesTableList(SoapObject o) {
        List<List<String>> propertiesTableList = new ArrayList<List<String>>();

        try {
            for (int i = 0; i < o.getPropertyCount(); i++) {
                propertiesTableList.add(getPropertiesList((SoapObject) o
                        .getProperty(i)));
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return propertiesTableList;
    }

    public static List<String> getPropertiesList(SoapObject o) {
        List<String> propertiesList = new ArrayList<String>();

        try {
            for (int i = 0; i < o.getPropertyCount(); i++) {
                propertiesList.add(o.getProperty(i).toString());
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return propertiesList;
    }
}
