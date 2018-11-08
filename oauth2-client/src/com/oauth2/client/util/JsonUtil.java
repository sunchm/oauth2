package com.oauth2.client.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * json工具类
 */
public class JsonUtil {

    /**
     * 将json信息转换成jsonObject
     * 
     * @param json json数据信息
     * @return jsonObject
     */
    public static JSONObject parseJson(String json) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        if (json != null&&!(json.trim().length()==0)) {
            try {
                obj = (JSONObject) parser.parse(json);
            } catch (ParseException e) {}
        }
        return obj;
    }

}
