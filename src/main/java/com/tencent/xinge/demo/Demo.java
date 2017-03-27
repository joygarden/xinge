package com.tencent.xinge.demo;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.TagTokenPair;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

public class Demo {
    private XingeApp xinge;
    private Message message1;
    private Message message2;
    private MessageIOS messageIOS;

    public static void main(String[] args) {
        demoXingeSimple();
        Demo t = new Demo();
        System.out.println(t.demoPushSingleDeviceMessage());
        System.out.println(t.demoPushSingleDeviceNotification());
        System.out.println(t.demoPushSingleDeviceNotificationIntent());
        System.out.println(t.demoPushSingleAccount());
        System.out.println(t.demoPushSingleDeviceMessageIOS());
        System.out.println(t.demoPushSingleDeviceNotificationIOS());
        System.out.println(t.demoPushSingleAccountIOS());
        System.out.println(t.demoPushAccountListIOS());
        System.out.println(t.demoPushAccountList());
        System.out.println(t.demoPushAllDevice());
        System.out.println(t.demoPushTags());
        System.out.println(t.demoPushAccountListMultiple());
        System.out.println(t.demoPushDeviceListMultiple());
        System.out.println(t.demoQueryPushStatus());
        System.out.println(t.demoQueryDeviceCount());
        System.out.println(t.demoQueryTags());
        System.out.println(t.demoQueryTagTokenNum());
        System.out.println(t.demoQueryTokenTags());
        System.out.println(t.demoQueryInfoOfToken());
        System.out.println(t.demoQueryTokensOfAccount());
        System.out.println(t.demoCancelTimingPush());
        System.out.println(t.demoBatchSetTag());
        System.out.println(t.demoBatchDelTag());
        System.out.println(t.demoDeleteTokenOfAccount());
        System.out.println(t.demoDeleteAllTokensOfAccount());
    }

    public static void demoXingeSimple() {
        System.out.println(XingeApp.pushTokenAndroid(0L, "secretKey", "test", "测试", "token"));
        System.out.println(XingeApp.pushAccountAndroid(0L, "secretKey", "test", "测试", "account"));
        System.out.println(XingeApp.pushAllAndroid(0L, "secretKey", "test", "测试"));
        System.out.println(XingeApp.pushTagAndroid(0L, "secretKey", "test", "测试", "tag"));
        System.out.println(XingeApp.pushTokenIos(0L, "secretKey", "测试", "token", 2));
        System.out.println(XingeApp.pushAccountIos(0L, "secretKey", "测试", "account", 2));
        System.out.println(XingeApp.pushAllIos(0L, "secretKey", "测试", 2));
        System.out.println(XingeApp.pushTagIos(0L, "secretKey", "测试", "tag", 2));
    }

    protected JSONObject demoPushSingleDeviceMessage() {
        Message message = new Message();
        message.setTitle("title");
        message.setContent("content");
        message.setType(2);
        message.setExpireTime(86400);
        JSONObject ret = this.xinge.pushSingleDevice("token", message);
        return ret;
    }

    protected JSONObject demoPushSingleDeviceNotification() {
        JSONObject ret = this.xinge.pushSingleDevice("token", this.message1);
        return ret;
    }

    protected JSONObject demoPushSingleDeviceNotificationIntent() {
        JSONObject ret = this.xinge.pushSingleDevice("token", this.message2);
        return ret;
    }

    protected JSONObject demoPushSingleDeviceMessageIOS() {
        MessageIOS remoteMessageIOS = new MessageIOS();
        remoteMessageIOS.setType(12);
        return this.xinge.pushSingleDevice("token", this.messageIOS, 2);
    }

    protected JSONObject demoPushSingleDeviceNotificationIOS() {
        TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
        this.messageIOS.addAcceptTime(acceptTime1);
        HashMap custom = new HashMap();
        custom.put("key1", "value1");
        custom.put("key2", Integer.valueOf(2));
        this.messageIOS.setCustom(custom);
        JSONObject ret = this.xinge.pushSingleDevice("token", this.messageIOS, 2);
        return ret;
    }

    protected JSONObject demoPushSingleAccount() {
        Message message = new Message();
        message.setExpireTime(86400);
        message.setTitle("title");
        message.setContent("content");
        message.setType(2);
        JSONObject ret = this.xinge.pushSingleAccount(0, "joelliu", message);
        return ret;
    }

    protected JSONObject demoPushAccountList() {
        Message message = new Message();
        message.setExpireTime(86400);
        message.setTitle("title");
        message.setContent("content");
        message.setType(2);
        ArrayList accountList = new ArrayList();
        accountList.add("joelliu");
        JSONObject ret = this.xinge.pushAccountList(0, accountList, message);
        return ret;
    }

    protected JSONObject demoPushSingleAccountIOS() {
        MessageIOS message = new MessageIOS();
        message.setExpireTime(86400);
        message.setAlert("ios test");
        message.setBadge(1);
        message.setSound("beep.wav");
        TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
        message.addAcceptTime(acceptTime1);
        HashMap custom = new HashMap();
        custom.put("key1", "value1");
        custom.put("key2", Integer.valueOf(2));
        message.setCustom(custom);
        JSONObject ret = this.xinge.pushSingleAccount(0, "joelliu", this.messageIOS, 2);
        return ret;
    }

    protected JSONObject demoPushAccountListIOS() {
        ArrayList accountList = new ArrayList();
        accountList.add("joelliu");
        JSONObject ret = this.xinge.pushAccountList(0, accountList, this.messageIOS, 2);
        return ret;
    }

    protected JSONObject demoPushAllDevice() {
        JSONObject ret = this.xinge.pushAllDevice(0, this.message1);
        return ret;
    }

    protected JSONObject demoPushTags() {
        ArrayList tagList = new ArrayList();
        tagList.add("joelliu");
        tagList.add("phone");
        JSONObject ret = this.xinge.pushTags(0, tagList, "OR", this.message1);
        return ret;
    }

    protected JSONObject demoPushAccountListMultiple() {
        Message message = new Message();
        message.setExpireTime(86400);
        message.setTitle("title");
        message.setContent("content");
        message.setType(1);
        JSONObject ret = this.xinge.createMultipush(message);
        if(ret.getInt("ret_code") != 0) {
            return ret;
        } else {
            JSONObject result = new JSONObject();
            ArrayList accountList1 = new ArrayList();
            accountList1.add("joelliu1");
            accountList1.add("joelliu2");
            result.append("all", this.xinge.pushAccountListMultiple(ret.getJSONObject("result").getLong("push_id"), accountList1));
            ArrayList accountList2 = new ArrayList();
            accountList2.add("joelliu3");
            accountList2.add("joelliu4");
            result.append("all", this.xinge.pushAccountListMultiple(ret.getJSONObject("result").getLong("push_id"), accountList2));
            return result;
        }
    }

    protected JSONObject demoPushDeviceListMultiple() {
        Message message = new Message();
        message.setExpireTime(86400);
        message.setTitle("title");
        message.setContent("content");
        message.setType(1);
        JSONObject ret = this.xinge.createMultipush(message);
        if(ret.getInt("ret_code") != 0) {
            return ret;
        } else {
            JSONObject result = new JSONObject();
            ArrayList deviceList1 = new ArrayList();
            deviceList1.add("joelliu1");
            deviceList1.add("joelliu2");
            result.append("all", this.xinge.pushDeviceListMultiple(ret.getJSONObject("result").getLong("push_id"), deviceList1));
            ArrayList deviceList2 = new ArrayList();
            deviceList2.add("joelliu3");
            deviceList2.add("joelliu4");
            result.append("all", this.xinge.pushDeviceListMultiple(ret.getJSONObject("result").getLong("push_id"), deviceList2));
            return result;
        }
    }

    protected JSONObject demoQueryPushStatus() {
        ArrayList pushIdList = new ArrayList();
        pushIdList.add("390");
        pushIdList.add("389");
        JSONObject ret = this.xinge.queryPushStatus(pushIdList);
        return ret;
    }

    protected JSONObject demoQueryDeviceCount() {
        JSONObject ret = this.xinge.queryDeviceCount();
        return ret;
    }

    protected JSONObject demoQueryTags() {
        JSONObject ret = this.xinge.queryTags();
        return ret;
    }

    protected JSONObject demoQueryTagTokenNum() {
        JSONObject ret = this.xinge.queryTagTokenNum("tag");
        return ret;
    }

    protected JSONObject demoQueryTokenTags() {
        JSONObject ret = this.xinge.queryTokenTags("token");
        return ret;
    }

    protected JSONObject demoCancelTimingPush() {
        JSONObject ret = this.xinge.cancelTimingPush("32");
        return ret;
    }

    protected JSONObject demoBatchSetTag() {
        ArrayList pairs = new ArrayList();
        pairs.add(new TagTokenPair("tag1", "token00000000000000000000000000000000001"));
        pairs.add(new TagTokenPair("tag2", "token00000000000000000000000000000000001"));
        JSONObject ret = this.xinge.BatchSetTag(pairs);
        return ret;
    }

    protected JSONObject demoBatchDelTag() {
        ArrayList pairs = new ArrayList();
        pairs.add(new TagTokenPair("tag1", "token00000000000000000000000000000000001"));
        pairs.add(new TagTokenPair("tag2", "token00000000000000000000000000000000001"));
        JSONObject ret = this.xinge.BatchDelTag(pairs);
        return ret;
    }

    protected JSONObject demoQueryInfoOfToken() {
        JSONObject ret = this.xinge.queryInfoOfToken("token");
        return ret;
    }

    protected JSONObject demoQueryTokensOfAccount() {
        JSONObject ret = this.xinge.queryTokensOfAccount("nickName");
        return ret;
    }

    protected JSONObject demoDeleteTokenOfAccount() {
        JSONObject ret = this.xinge.deleteTokenOfAccount("nickName", "token");
        return ret;
    }

    protected JSONObject demoDeleteAllTokensOfAccount() {
        JSONObject ret = this.xinge.deleteAllTokensOfAccount("nickName");
        return ret;
    }

    protected void buildMesssges() {
        this.message1 = new Message();
        this.message1.setType(1);
        new Style(1);
        Style style = new Style(3, 1, 0, 1, 0);
        ClickAction action = new ClickAction();
        action.setActionType(2);
        action.setUrl("http://xg.qq.com");
        HashMap custom = new HashMap();
        custom.put("key1", "value1");
        custom.put("key2", Integer.valueOf(2));
        this.message1.setTitle("title");
        this.message1.setContent("大小");
        this.message1.setStyle(style);
        this.message1.setAction(action);
        this.message1.setCustom(custom);
        TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
        this.message1.addAcceptTime(acceptTime1);
        this.message2 = new Message();
        this.message2.setType(1);
        this.message2.setTitle("title");
        this.message2.setContent("通知点击执行Intent测试");
        style = new Style(1);
        action = new ClickAction();
        action.setActionType(3);
        action.setIntent("intent:10086#Intent;scheme=tel;action=android.intent.action.DIAL;S.key=value;end");
        this.message2.setStyle(style);
        this.message2.setAction(action);
        this.messageIOS = new MessageIOS();
        this.messageIOS.setType(11);
        this.messageIOS.setExpireTime(86400);
        this.messageIOS.setAlert("ios test");
        this.messageIOS.setBadge(1);
        this.messageIOS.setCategory("INVITE_CATEGORY");
        this.messageIOS.setSound("beep.wav");
    }

    public Demo() {
        new XingeApp(0L, "secret_key");
        this.buildMesssges();
    }
}
