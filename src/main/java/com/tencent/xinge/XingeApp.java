package com.tencent.xinge;

import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.TagTokenPair;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;

public class XingeApp {
    public static final String RESTAPI_PUSHSINGLEDEVICE = "http://openapi.xg.qq.com/v2/push/single_device";
    public static final String RESTAPI_PUSHSINGLEACCOUNT = "http://openapi.xg.qq.com/v2/push/single_account";
    public static final String RESTAPI_PUSHACCOUNTLIST = "http://openapi.xg.qq.com/v2/push/account_list";
    public static final String RESTAPI_PUSHALLDEVICE = "http://openapi.xg.qq.com/v2/push/all_device";
    public static final String RESTAPI_PUSHTAGS = "http://openapi.xg.qq.com/v2/push/tags_device";
    public static final String RESTAPI_QUERYPUSHSTATUS = "http://openapi.xg.qq.com/v2/push/get_msg_status";
    public static final String RESTAPI_QUERYDEVICECOUNT = "http://openapi.xg.qq.com/v2/application/get_app_device_num";
    public static final String RESTAPI_QUERYTAGS = "http://openapi.xg.qq.com/v2/tags/query_app_tags";
    public static final String RESTAPI_CANCELTIMINGPUSH = "http://openapi.xg.qq.com/v2/push/cancel_timing_task";
    public static final String RESTAPI_BATCHSETTAG = "http://openapi.xg.qq.com/v2/tags/batch_set";
    public static final String RESTAPI_BATCHDELTAG = "http://openapi.xg.qq.com/v2/tags/batch_del";
    public static final String RESTAPI_QUERYTOKENTAGS = "http://openapi.xg.qq.com/v2/tags/query_token_tags";
    public static final String RESTAPI_QUERYTAGTOKENNUM = "http://openapi.xg.qq.com/v2/tags/query_tag_token_num";
    public static final String RESTAPI_CREATEMULTIPUSH = "http://openapi.xg.qq.com/v2/push/create_multipush";
    public static final String RESTAPI_PUSHACCOUNTLISTMULTIPLE = "http://openapi.xg.qq.com/v2/push/account_list_multiple";
    public static final String RESTAPI_PUSHDEVICELISTMULTIPLE = "http://openapi.xg.qq.com/v2/push/device_list_multiple";
    public static final String RESTAPI_QUERYINFOOFTOKEN = "http://openapi.xg.qq.com/v2/application/get_app_token_info";
    public static final String RESTAPI_QUERYTOKENSOFACCOUNT = "http://openapi.xg.qq.com/v2/application/get_app_account_tokens";
    public static final String RESTAPI_DELETETOKENOFACCOUNT = "http://openapi.xg.qq.com/v2/application/del_app_account_tokens";
    public static final String RESTAPI_DELETEALLTOKENSOFACCOUNT = "http://openapi.xg.qq.com/v2/application/del_app_account_all_tokens";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";
    public static final int DEVICE_ALL = 0;
    public static final int DEVICE_BROWSER = 1;
    public static final int DEVICE_PC = 2;
    public static final int DEVICE_ANDROID = 3;
    public static final int DEVICE_IOS = 4;
    public static final int DEVICE_WINPHONE = 5;
    public static final int IOSENV_PROD = 1;
    public static final int IOSENV_DEV = 2;
    public static final long IOS_MIN_ID = 2200000000L;
    private long m_accessId;
    private String m_secretKey;

    public static void main(String[] args) {
        System.out.println("Hello Xinge!");
    }

    public XingeApp(long accessId, String secretKey) {
        this.m_accessId = accessId;
        this.m_secretKey = secretKey;
    }

    protected String generateSign(String method, String url, Map<String, Object> params) {
        ArrayList paramList = new ArrayList(params.entrySet());
        Collections.sort(paramList, new Comparator<Entry<String, Object>>() {
            public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
                return ((String)o1.getKey()).toString().compareTo((String)o2.getKey());
            }
        });
        String paramStr = "";
        String md5Str = "";

        Entry md5;
        for(Iterator e = paramList.iterator(); e.hasNext(); paramStr = paramStr + (String)md5.getKey() + "=" + md5.getValue().toString()) {
            md5 = (Entry)e.next();
        }

        try {
            URL e1 = new URL(url);
            MessageDigest md51 = MessageDigest.getInstance("MD5");
            String s = method + e1.getHost() + e1.getPath() + paramStr + this.m_secretKey;
            byte[] bArr = s.getBytes("UTF-8");
            byte[] md5Value = md51.digest(bArr);
            BigInteger bigInt = new BigInteger(1, md5Value);

            for(md5Str = bigInt.toString(16); md5Str.length() < 32; md5Str = "0" + md5Str) {
                ;
            }

            return md5Str;
        } catch (Exception var13) {
            var13.printStackTrace();
            return "";
        }
    }

    protected JSONObject callRestful(String url, Map<String, Object> params) {
        String ret = "";
        JSONObject jsonRet = null;
        String sign = this.generateSign("POST", url, params);
        if(sign.isEmpty()) {
            return new JSONObject("{\"ret_code\":-1,\"err_msg\":\"generateSign error\"}");
        } else {
            params.put("sign", sign);

            try {
                URL e = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)e.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(10000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                StringBuffer param = new StringBuffer();
                Iterator isr = params.keySet().iterator();

                while(isr.hasNext()) {
                    String br = (String)isr.next();
                    param.append(br).append("=").append(URLEncoder.encode(params.get(br).toString(), "UTF-8")).append("&");
                }

                conn.getOutputStream().write(param.toString().getBytes("UTF-8"));
                conn.getOutputStream().flush();
                conn.getOutputStream().close();
                InputStreamReader isr1 = new InputStreamReader(conn.getInputStream());

                String temp;
                BufferedReader br1;
                for(br1 = new BufferedReader(isr1); (temp = br1.readLine()) != null; ret = ret + temp) {
                    ;
                }

                br1.close();
                isr1.close();
                conn.disconnect();
                jsonRet = new JSONObject(ret);
            } catch (SocketTimeoutException var12) {
                jsonRet = new JSONObject("{\"ret_code\":-1,\"err_msg\":\"call restful timeout\"}");
            } catch (Exception var13) {
                jsonRet = new JSONObject("{\"ret_code\":-1,\"err_msg\":\"call restful error\"}");
            }

            return jsonRet;
        }
    }

    protected boolean ValidateToken(String token) {
        return this.m_accessId >= 2200000000L?token.length() == 64:token.length() == 40 || token.length() == 64;
    }

    protected Map<String, Object> InitParams() {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return params;
    }

    protected boolean ValidateMessageType(Message message) {
        return this.m_accessId < 2200000000L;
    }

    protected boolean ValidateMessageType(MessageIOS message, int environment) {
        return this.m_accessId >= 2200000000L && (environment == 1 || environment == 2);
    }

    public static JSONObject pushTokenAndroid(long accessId, String secretKey, String title, String content, String token) {
        Message message = new Message();
        message.setType(1);
        message.setTitle(title);
        message.setContent(content);
        XingeApp xinge = new XingeApp(accessId, secretKey);
        JSONObject ret = xinge.pushSingleDevice(token, message);
        return ret;
    }

    public static JSONObject pushAccountAndroid(long accessId, String secretKey, String title, String content, String account) {
        Message message = new Message();
        message.setType(1);
        message.setTitle(title);
        message.setContent(content);
        XingeApp xinge = new XingeApp(accessId, secretKey);
        JSONObject ret = xinge.pushSingleAccount(0, account, message);
        return ret;
    }

    public static JSONObject pushAllAndroid(long accessId, String secretKey, String title, String content) {
        Message message = new Message();
        message.setType(1);
        message.setTitle(title);
        message.setContent(content);
        XingeApp xinge = new XingeApp(accessId, secretKey);
        JSONObject ret = xinge.pushAllDevice(0, message);
        return ret;
    }

    public static JSONObject pushTagAndroid(long accessId, String secretKey, String title, String content, String tag) {
        Message message = new Message();
        message.setType(1);
        message.setTitle(title);
        message.setContent(content);
        XingeApp xinge = new XingeApp(accessId, secretKey);
        ArrayList tagList = new ArrayList();
        tagList.add(tag);
        JSONObject ret = xinge.pushTags(0, tagList, "OR", message);
        return ret;
    }

    public static JSONObject pushTokenIos(long accessId, String secretKey, String content, String token, int env) {
        MessageIOS message = new MessageIOS();
        message.setAlert(content);
        message.setBadge(1);
        message.setSound("beep.wav");
        XingeApp xinge = new XingeApp(accessId, secretKey);
        JSONObject ret = xinge.pushSingleDevice(token, message, env);
        return ret;
    }

    public static JSONObject pushAccountIos(long accessId, String secretKey, String content, String account, int env) {
        MessageIOS message = new MessageIOS();
        message.setAlert(content);
        message.setBadge(1);
        message.setSound("beep.wav");
        XingeApp xinge = new XingeApp(accessId, secretKey);
        JSONObject ret = xinge.pushSingleAccount(0, account, message, env);
        return ret;
    }

    public static JSONObject pushAllIos(long accessId, String secretKey, String content, int env) {
        MessageIOS message = new MessageIOS();
        message.setAlert(content);
        message.setBadge(1);
        message.setSound("beep.wav");
        XingeApp xinge = new XingeApp(accessId, secretKey);
        JSONObject ret = xinge.pushAllDevice(0, message, env);
        return ret;
    }

    public static JSONObject pushTagIos(long accessId, String secretKey, String content, String tag, int env) {
        MessageIOS message = new MessageIOS();
        message.setAlert(content);
        message.setBadge(1);
        message.setSound("beep.wav");
        XingeApp xinge = new XingeApp(accessId, secretKey);
        ArrayList tagList = new ArrayList();
        tagList.add(tag);
        JSONObject ret = xinge.pushTags(0, tagList, "OR", message, env);
        return ret;
    }

    public JSONObject pushSingleDevice(String deviceToken, Message message) {
        if(!this.ValidateMessageType(message)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("multi_pkg", Integer.valueOf(message.getMultiPkg()));
            params.put("device_token", deviceToken);
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/single_device", params);
        }
    }

    public JSONObject pushSingleDevice(String deviceToken, MessageIOS message, int environment) {
        if(!this.ValidateMessageType(message, environment)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type or environment error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("device_token", deviceToken);
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            params.put("environment", Integer.valueOf(environment));
            if(message.getLoopInterval() > 0 && message.getLoopTimes() > 0) {
                params.put("loop_interval", Integer.valueOf(message.getLoopInterval()));
                params.put("loop_times", Integer.valueOf(message.getLoopTimes()));
            }

            return this.callRestful("http://openapi.xg.qq.com/v2/push/single_device", params);
        }
    }

    public JSONObject pushSingleAccount(int deviceType, String account, Message message) {
        if(!this.ValidateMessageType(message)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("multi_pkg", Integer.valueOf(message.getMultiPkg()));
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("account", account);
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/single_account", params);
        }
    }

    public JSONObject pushSingleAccount(int deviceType, String account, MessageIOS message, int environment) {
        if(!this.ValidateMessageType(message, environment)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type or environment error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("account", account);
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            params.put("environment", Integer.valueOf(environment));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/single_account", params);
        }
    }

    public JSONObject pushAccountList(int deviceType, List<String> accountList, Message message) {
        if(!this.ValidateMessageType(message)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("multi_pkg", Integer.valueOf(message.getMultiPkg()));
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("account_list", (new JSONArray(accountList)).toString());
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/account_list", params);
        }
    }

    public JSONObject pushAccountList(int deviceType, List<String> accountList, MessageIOS message, int environment) {
        if(!this.ValidateMessageType(message, environment)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type or environment error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("account_list", (new JSONArray(accountList)).toString());
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            params.put("environment", Integer.valueOf(environment));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/account_list", params);
        }
    }

    public JSONObject pushAllDevice(int deviceType, Message message) {
        if(!this.ValidateMessageType(message)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("multi_pkg", Integer.valueOf(message.getMultiPkg()));
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            if(message.getLoopInterval() > 0 && message.getLoopTimes() > 0) {
                params.put("loop_interval", Integer.valueOf(message.getLoopInterval()));
                params.put("loop_times", Integer.valueOf(message.getLoopTimes()));
            }

            return this.callRestful("http://openapi.xg.qq.com/v2/push/all_device", params);
        }
    }

    public JSONObject pushAllDevice(int deviceType, MessageIOS message, int environment) {
        if(!this.ValidateMessageType(message, environment)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type or environment error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            params.put("environment", Integer.valueOf(environment));
            if(message.getLoopInterval() > 0 && message.getLoopTimes() > 0) {
                params.put("loop_interval", Integer.valueOf(message.getLoopInterval()));
                params.put("loop_times", Integer.valueOf(message.getLoopTimes()));
            }

            return this.callRestful("http://openapi.xg.qq.com/v2/push/all_device", params);
        }
    }

    public JSONObject pushTags(int deviceType, List<String> tagList, String tagOp, Message message) {
        if(!this.ValidateMessageType(message)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type error!\'}");
        } else if(!message.isValid() || tagList.size() == 0 || !tagOp.equals("AND") && !tagOp.equals("OR")) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'param invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("multi_pkg", Integer.valueOf(message.getMultiPkg()));
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("tags_list", (new JSONArray(tagList)).toString());
            params.put("tags_op", tagOp);
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            if(message.getLoopInterval() > 0 && message.getLoopTimes() > 0) {
                params.put("loop_interval", Integer.valueOf(message.getLoopInterval()));
                params.put("loop_times", Integer.valueOf(message.getLoopTimes()));
            }

            return this.callRestful("http://openapi.xg.qq.com/v2/push/tags_device", params);
        }
    }

    public JSONObject pushTags(int deviceType, List<String> tagList, String tagOp, MessageIOS message, int environment) {
        if(!this.ValidateMessageType(message, environment)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type or environment error!\'}");
        } else if(!message.isValid() || tagList.size() == 0 || !tagOp.equals("AND") && !tagOp.equals("OR")) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'param invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("send_time", message.getSendTime());
            params.put("device_type", Integer.valueOf(deviceType));
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("tags_list", (new JSONArray(tagList)).toString());
            params.put("tags_op", tagOp);
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            params.put("environment", Integer.valueOf(environment));
            if(message.getLoopInterval() > 0 && message.getLoopTimes() > 0) {
                params.put("loop_interval", Integer.valueOf(message.getLoopInterval()));
                params.put("loop_times", Integer.valueOf(message.getLoopTimes()));
            }

            return this.callRestful("http://openapi.xg.qq.com/v2/push/tags_device", params);
        }
    }

    public JSONObject createMultipush(Message message) {
        if(!this.ValidateMessageType(message)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("multi_pkg", Integer.valueOf(message.getMultiPkg()));
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/create_multipush", params);
        }
    }

    public JSONObject createMultipush(MessageIOS message, int environment) {
        if(!this.ValidateMessageType(message, environment)) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message type or environment error!\'}");
        } else if(!message.isValid()) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'message invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("expire_time", Integer.valueOf(message.getExpireTime()));
            params.put("message_type", Integer.valueOf(message.getType()));
            params.put("message", message.toJson());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            params.put("environment", Integer.valueOf(environment));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/create_multipush", params);
        }
    }

    public JSONObject pushAccountListMultiple(long pushId, List<String> accountList) {
        if(pushId <= 0L) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'pushId invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("push_id", Long.valueOf(pushId));
            params.put("account_list", (new JSONArray(accountList)).toString());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/account_list_multiple", params);
        }
    }

    public JSONObject pushDeviceListMultiple(long pushId, List<String> deviceList) {
        if(pushId <= 0L) {
            return new JSONObject("{\'ret_code\':-1,\'err_msg\':\'pushId invalid!\'}");
        } else {
            HashMap params = new HashMap();
            params.put("access_id", Long.valueOf(this.m_accessId));
            params.put("push_id", Long.valueOf(pushId));
            params.put("device_list", (new JSONArray(deviceList)).toString());
            params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
            return this.callRestful("http://openapi.xg.qq.com/v2/push/device_list_multiple", params);
        }
    }

    public JSONObject queryPushStatus(List<String> pushIdList) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        JSONArray jArray = new JSONArray();
        Iterator i$ = pushIdList.iterator();

        while(i$.hasNext()) {
            String pushId = (String)i$.next();
            JSONObject js = new JSONObject();
            js.put("push_id", pushId);
            jArray.put(js);
        }

        params.put("push_ids", jArray.toString());
        return this.callRestful("http://openapi.xg.qq.com/v2/push/get_msg_status", params);
    }

    public JSONObject queryDeviceCount() {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/application/get_app_device_num", params);
    }

    public JSONObject queryTags(int start, int limit) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("start", Integer.valueOf(start));
        params.put("limit", Integer.valueOf(limit));
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/tags/query_app_tags", params);
    }

    public JSONObject queryTags() {
        return this.queryTags(0, 100);
    }

    public JSONObject queryTagTokenNum(String tag) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("tag", tag);
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/tags/query_tag_token_num", params);
    }

    public JSONObject queryTokenTags(String deviceToken) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("device_token", deviceToken);
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/tags/query_token_tags", params);
    }

    public JSONObject cancelTimingPush(String pushId) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("push_id", pushId);
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/push/cancel_timing_task", params);
    }

    public JSONObject BatchSetTag(List<TagTokenPair> tagTokenPairs) {
        Iterator params = tagTokenPairs.iterator();

        while(params.hasNext()) {
            TagTokenPair tag_token_list = (TagTokenPair)params.next();
            if(!this.ValidateToken(tag_token_list.token)) {
                String i$ = String.format("{\"ret_code\":-1,\"err_msg\":\"invalid token %s\"}", new Object[]{tag_token_list.token});
                return new JSONObject(i$);
            }
        }

        Map params1 = this.InitParams();
        ArrayList tag_token_list1 = new ArrayList();
        Iterator i$1 = tagTokenPairs.iterator();

        while(i$1.hasNext()) {
            TagTokenPair pair = (TagTokenPair)i$1.next();
            ArrayList singleTagToken = new ArrayList();
            singleTagToken.add(pair.tag);
            singleTagToken.add(pair.token);
            tag_token_list1.add(singleTagToken);
        }

        params1.put("tag_token_list", (new JSONArray(tag_token_list1)).toString());
        return this.callRestful("http://openapi.xg.qq.com/v2/tags/batch_set", params1);
    }

    public JSONObject BatchDelTag(List<TagTokenPair> tagTokenPairs) {
        Iterator params = tagTokenPairs.iterator();

        while(params.hasNext()) {
            TagTokenPair tag_token_list = (TagTokenPair)params.next();
            if(!this.ValidateToken(tag_token_list.token)) {
                String i$ = String.format("{\"ret_code\":-1,\"err_msg\":\"invalid token %s\"}", new Object[]{tag_token_list.token});
                return new JSONObject(i$);
            }
        }

        Map params1 = this.InitParams();
        ArrayList tag_token_list1 = new ArrayList();
        Iterator i$1 = tagTokenPairs.iterator();

        while(i$1.hasNext()) {
            TagTokenPair pair = (TagTokenPair)i$1.next();
            ArrayList singleTagToken = new ArrayList();
            singleTagToken.add(pair.tag);
            singleTagToken.add(pair.token);
            tag_token_list1.add(singleTagToken);
        }

        params1.put("tag_token_list", (new JSONArray(tag_token_list1)).toString());
        return this.callRestful("http://openapi.xg.qq.com/v2/tags/batch_del", params1);
    }

    public JSONObject queryInfoOfToken(String deviceToken) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("device_token", deviceToken);
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/application/get_app_token_info", params);
    }

    public JSONObject queryTokensOfAccount(String account) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("account", account);
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/application/get_app_account_tokens", params);
    }

    public JSONObject deleteTokenOfAccount(String account, String deviceToken) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("account", account);
        params.put("device_token", deviceToken);
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/application/del_app_account_tokens", params);
    }

    public JSONObject deleteAllTokensOfAccount(String account) {
        HashMap params = new HashMap();
        params.put("access_id", Long.valueOf(this.m_accessId));
        params.put("account", account);
        params.put("timestamp", Long.valueOf(System.currentTimeMillis() / 1000L));
        return this.callRestful("http://openapi.xg.qq.com/v2/application/del_app_account_all_tokens", params);
    }
}
