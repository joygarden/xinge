package com.tencent.xinge;

import org.json.JSONObject;

public class ClickAction {
    public static final int TYPE_ACTIVITY = 1;
    public static final int TYPE_URL = 2;
    public static final int TYPE_INTENT = 3;
    private int m_actionType = 1;
    private String m_url = "";
    private int m_confirmOnUrl;
    private String m_activity = "";
    private String m_intent;
    private int m_atyAttrIntentFlag = 0;
    private int m_atyAttrPendingIntentFlag = 0;
    private String m_packageDownloadUrl = "";
    private int m_confirmOnPackageDownloadUrl = 1;
    private String m_packageName = "";

    public void setActionType(int actionType) {
        this.m_actionType = actionType;
    }

    public void setActivity(String activity) {
        this.m_activity = activity;
    }

    public void setUrl(String url) {
        this.m_url = url;
    }

    public void setConfirmOnUrl(int confirmOnUrl) {
        this.m_confirmOnUrl = confirmOnUrl;
    }

    public void setIntent(String intent) {
        this.m_intent = intent;
    }

    public void setAtyAttrIntentFlag(int atyAttrIntentFlag) {
        this.m_atyAttrIntentFlag = atyAttrIntentFlag;
    }

    public void setAtyAttrPendingIntentFlag(int atyAttrPendingIntentFlag) {
        this.m_atyAttrPendingIntentFlag = atyAttrPendingIntentFlag;
    }

    public void setPackageDownloadUrl(String packageDownloadUrl) {
        this.m_packageDownloadUrl = packageDownloadUrl;
    }

    public void setConfirmOnPackageDownloadUrl(int confirmOnPackageDownloadUrl) {
        this.m_confirmOnPackageDownloadUrl = confirmOnPackageDownloadUrl;
    }

    public void setPackageName(String packageName) {
        this.m_packageName = packageName;
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("action_type", this.m_actionType);
        JSONObject browser = new JSONObject();
        browser.put("url", this.m_url);
        browser.put("confirm", this.m_confirmOnUrl);
        json.put("browser", browser);
        json.put("activity", this.m_activity);
        json.put("intent", this.m_intent);
        JSONObject aty_attr = new JSONObject();
        aty_attr.put("if", this.m_atyAttrIntentFlag);
        aty_attr.put("pf", this.m_atyAttrPendingIntentFlag);
        json.put("aty_attr", aty_attr);
        return json.toString();
    }

    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        json.put("action_type", this.m_actionType);
        JSONObject browser = new JSONObject();
        browser.put("url", this.m_url);
        browser.put("confirm", this.m_confirmOnUrl);
        json.put("browser", browser);
        json.put("activity", this.m_activity);
        json.put("intent", this.m_intent);
        JSONObject aty_attr = new JSONObject();
        aty_attr.put("if", this.m_atyAttrIntentFlag);
        aty_attr.put("pf", this.m_atyAttrPendingIntentFlag);
        json.put("aty_attr", aty_attr);
        return json;
    }

    public boolean isValid() {
        return this.m_actionType >= 1 && this.m_actionType <= 3?(this.m_actionType == 2?!this.m_url.isEmpty() && this.m_confirmOnUrl >= 0 && this.m_confirmOnUrl <= 1:(this.m_actionType == 3?!this.m_intent.isEmpty():true)):false;
    }

    public ClickAction() {
    }
}
