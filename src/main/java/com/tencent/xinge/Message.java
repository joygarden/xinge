package com.tencent.xinge;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Style;
import com.tencent.xinge.TimeInterval;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

public class Message {
    public static final int TYPE_NOTIFICATION = 1;
    public static final int TYPE_MESSAGE = 2;
    private String m_title = "";
    private String m_content = "";
    private int m_expireTime;
    private String m_sendTime = "2013-12-20 18:31:00";
    private Vector<TimeInterval> m_acceptTimes = new Vector();
    private int m_type;
    private int m_multiPkg = 0;
    private Style m_style = new Style(0);
    private ClickAction m_action = new ClickAction();
    private Map<String, Object> m_custom;
    private String m_raw = "";
    private int m_loopInterval = -1;
    private int m_loopTimes = -1;

    public Message() {
    }

    public void setTitle(String title) {
        this.m_title = title;
    }

    public void setContent(String content) {
        this.m_content = content;
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

    public void setType(int type) {
        this.m_type = type;
    }

    public int getType() {
        return this.m_type;
    }

    public void setMultiPkg(int multiPkg) {
        this.m_multiPkg = multiPkg;
    }

    public int getMultiPkg() {
        return this.m_multiPkg;
    }

    public void setStyle(Style style) {
        this.m_style = style;
    }

    public void setAction(ClickAction action) {
        this.m_action = action;
    }

    public void setCustom(Map<String, Object> custom) {
        this.m_custom = custom;
    }

    public void setRaw(String raw) {
        this.m_raw = raw;
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
        } else if(this.m_type >= 1 && this.m_type <= 2) {
            if(this.m_multiPkg >= 0 && this.m_multiPkg <= 1) {
                if(this.m_type == 1) {
                    if(!this.m_style.isValid()) {
                        return false;
                    }

                    if(!this.m_action.isValid()) {
                        return false;
                    }
                }

                if(this.m_expireTime >= 0 && this.m_expireTime <= 259200) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try {
                        sdf.parse(this.m_sendTime);
                    } catch (ParseException var4) {
                        return false;
                    }

                    Iterator i$ = this.m_acceptTimes.iterator();

                    TimeInterval ti;
                    do {
                        if(!i$.hasNext()) {
                            if(this.m_loopInterval > 0 && this.m_loopTimes > 0 && (this.m_loopTimes - 1) * this.m_loopInterval + 1 > 15) {
                                return false;
                            }

                            return true;
                        }

                        ti = (TimeInterval)i$.next();
                    } while(ti.isValid());

                    return false;
                } else {
                    return false;
                }
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
            JSONObject json = new JSONObject();
            if(this.m_type == 1) {
                json.put("title", this.m_title);
                json.put("content", this.m_content);
                json.put("accept_time", this.acceptTimeToJsonArray());
                json.put("builder_id", this.m_style.getBuilderId());
                json.put("ring", this.m_style.getRing());
                json.put("vibrate", this.m_style.getVibrate());
                json.put("clearable", this.m_style.getClearable());
                json.put("n_id", this.m_style.getNId());
                json.put("ring_raw", this.m_style.getRingRaw());
                json.put("lights", this.m_style.getLights());
                json.put("icon_type", this.m_style.getIconType());
                json.put("icon_res", this.m_style.getIconRes());
                json.put("style_id", this.m_style.getStyleId());
                json.put("small_icon", this.m_style.getSmallIcon());
                json.put("action", this.m_action.toJsonObject());
            } else if(this.m_type == 2) {
                json.put("title", this.m_title);
                json.put("content", this.m_content);
                json.put("accept_time", this.acceptTimeToJsonArray());
            }

            json.put("custom_content", this.m_custom);
            return json.toString();
        }
    }
}
