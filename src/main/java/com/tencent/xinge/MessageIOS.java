package com.tencent.xinge;

import com.tencent.xinge.TimeInterval;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageIOS {
    public static final int TYPE_APNS_NOTIFICATION = 11;
    public static final int TYPE_REMOTE_NOTIFICATION = 12;
    private int m_expireTime;
    private String m_sendTime = "2014-03-13 16:13:00";
    private Vector<TimeInterval> m_acceptTimes = new Vector();
    private int m_type = 11;
    private Map<String, Object> m_custom;
    private String m_raw = "";
    private String m_alertStr = "";
    private JSONObject m_alertJo = new JSONObject();
    private int m_badge = 0;
    private String m_sound = "";
    private String m_category = "";
    private int m_loopInterval = -1;
    private int m_loopTimes = -1;

    public MessageIOS() {
    }

    public void setType(int type) {
        this.m_type = type;
    }

    public int getType() {
        return this.m_type;
    }

    public void setExpireTime(int expireTime) {
        this.m_expireTime = expireTime;
    }

    public int getExpireTime() {
        return this.m_expireTime;
    }

    public void setSendTime(String sendTime) {
        this.m_sendTime = sendTime;
    }

    public String getSendTime() {
        return this.m_sendTime;
    }

    public void addAcceptTime(TimeInterval acceptTime) {
        this.m_acceptTimes.add(acceptTime);
    }

    public String acceptTimeToJson() {
        JSONArray json_arr = new JSONArray();
        Iterator i$ = this.m_acceptTimes.iterator();

        while(i$.hasNext()) {
            TimeInterval ti = (TimeInterval)i$.next();
            JSONObject jtmp = ti.toJsonObject();
            json_arr.put(jtmp);
        }

        return json_arr.toString();
    }

    public JSONArray acceptTimeToJsonArray() {
        JSONArray json_arr = new JSONArray();
        Iterator i$ = this.m_acceptTimes.iterator();

        while(i$.hasNext()) {
            TimeInterval ti = (TimeInterval)i$.next();
            JSONObject jtmp = ti.toJsonObject();
            json_arr.put(jtmp);
        }

        return json_arr;
    }

    public void setCustom(Map<String, Object> custom) {
        this.m_custom = custom;
    }

    public void setRaw(String raw) {
        this.m_raw = raw;
    }

    public void setAlert(String alert) {
        this.m_alertStr = alert;
    }

    public void setAlert(JSONObject alert) {
        this.m_alertJo = alert;
    }

    public void setBadge(int badge) {
        this.m_badge = badge;
    }

    public void setSound(String sound) {
        this.m_sound = sound;
    }

    public void setCategory(String category) {
        this.m_category = category;
    }

    public int getLoopInterval() {
        return this.m_loopInterval;
    }

    public void setLoopInterval(int loopInterval) {
        this.m_loopInterval = loopInterval;
    }

    public int getLoopTimes() {
        return this.m_loopTimes;
    }

    public void setLoopTimes(int loopTimes) {
        this.m_loopTimes = loopTimes;
    }

    public boolean isValid() {
        if(!this.m_raw.isEmpty()) {
            return true;
        } else if(this.m_type >= 11 && this.m_type <= 12) {
            if(this.m_expireTime >= 0 && this.m_expireTime <= 259200) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    sdf.parse(this.m_sendTime);
                } catch (ParseException var4) {
                    return false;
                }

                Iterator i$ = this.m_acceptTimes.iterator();

                while(i$.hasNext()) {
                    TimeInterval ti = (TimeInterval)i$.next();
                    if(!ti.isValid()) {
                        return false;
                    }
                }

                return this.m_type == 12?true:!this.m_alertStr.isEmpty() || this.m_alertJo.length() != 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String toJson() {
        if(!this.m_raw.isEmpty()) {
            return this.m_raw;
        } else {
            JSONObject json = new JSONObject(this.m_custom);
            json.put("accept_time", this.acceptTimeToJsonArray());
            JSONObject aps = new JSONObject();
            if(this.m_type == 12) {
                aps.put("content-available", 1);
            } else if(this.m_type == 11) {
                if(this.m_alertJo.length() != 0) {
                    aps.put("alert", this.m_alertJo);
                } else {
                    aps.put("alert", this.m_alertStr);
                }

                if(this.m_badge != 0) {
                    aps.put("badge", this.m_badge);
                }

                if(!this.m_sound.isEmpty()) {
                    aps.put("sound", this.m_sound);
                }

                if(!this.m_category.isEmpty()) {
                    aps.put("category", this.m_category);
                }
            }

            json.put("aps", aps);
            return json.toString();
        }
    }
}
