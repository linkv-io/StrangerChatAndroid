package com.linkv.strangerchat.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiaohong on 2020/11/5.
 * desc:
 */
public class JsonUtil {

    private final static String KEY_STRING = "KEY_STRING";

    // List转json
    public static String listToJson(List<String> uids) {
        if (uids == null) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < uids.size(); i++) {
                jsonArray.put(new JSONObject().put(KEY_STRING, uids.get(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }


    // json转List
    public static List<String> jsonToList(String json) {
        ArrayList<String> arrayList = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonArray == null) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject != null) {
                arrayList.add(jsonObject.optString(KEY_STRING));
            }
        }
        return arrayList;
    }


}
