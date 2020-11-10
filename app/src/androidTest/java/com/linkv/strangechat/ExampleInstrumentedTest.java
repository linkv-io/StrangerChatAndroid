package com.linkv.strangechat;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.linkv.strangechat", appContext.getPackageName());

        ArrayList<String> uids = new ArrayList<>();
        uids.add("1111");
        uids.add("2222");
        uids.add("3333");


        String json = listToJson(uids);
        System.out.println("json array  = " + json);


        List<String> list = jsonToList(json);
        System.out.println("size = " + list.size());
        System.out.println("array = " + list);
    }

    // List转json
    private String listToJson(List<String> uids) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < uids.size(); i++) {
                jsonArray.put(new JSONObject().put("key", uids.get(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }


    // json转List
    private List<String> jsonToList(String json) {
        ArrayList<String> arrayList = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            arrayList.add(jsonArray.optJSONObject(i).optString("key"));
        }
        return arrayList;
    }
}
