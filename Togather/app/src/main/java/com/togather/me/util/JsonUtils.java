package com.togather.me.util;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.Map;

public class JsonUtils {
    private static final String TAG = LogUtils.makeLogTag(JsonUtils.class);

    public static JsonObject updateJson(JsonObject oldObject, JsonObject newObject) {
        if (oldObject == null) {
            oldObject = new JsonObject();
        }
        if (newObject != null) {
            for (Map.Entry<String, JsonElement> entry : newObject.entrySet()) {
                oldObject.add(entry.getKey(), entry.getValue());
            }
        }
        return oldObject;
    }

    public static boolean areJsonEqual(JsonObject firstObject, JsonObject secondObject) {
        if (firstObject == null) {
            return (secondObject == null);
        } else {
            return firstObject.equals(secondObject);
        }
    }

    public static boolean areJsonEqual(JsonElement firstObject, JsonElement secondObject) {
        if (firstObject == null) {
            return (secondObject == null);
        } else {
            return firstObject.equals(secondObject);
        }
    }


    public static JsonObject extractJsonObject(JsonObject parentObject, String[] keys) {
        LogUtils.LOGD(TAG, "keys: " + Arrays.toString(keys));
        if (keys == null || keys.length == 0) {
            return null;
        } else {
            String[] copyKeys = keys.clone();
            JsonObject subObject = new JsonObject();
            for (String key : copyKeys) {
                if (parentObject.has(key)) {
                    subObject.add(key, parentObject.get(key));
                }
            }
            return subObject;
        }
    }

    public static JsonArray filterJsonArray(JsonArray jsonArray, JsonObject filter) {
        if (jsonArray == null) {
            LogUtils.LOGD(TAG, "jsonArray passed is null");
            return null;
        } else {
            if (filter == null || areJsonEqual(filter, new JsonObject())) {
                LogUtils.LOGD(TAG, "Empty Filter returning entire list");
                return copyJsonElement(jsonArray, JsonArray.class);
            } else {
                JsonArray result = new JsonArray();
                LogUtils.LOGD(TAG, "filtering jsonArray using filter: " + filter);
                for (JsonElement jsonObject : jsonArray) {
                    if (jsonObject.isJsonObject()) {
                        if (firstContainsSecond((JsonObject) jsonObject, filter)) {
                            result.add(jsonObject);
                        }
                    } else {
                        LogUtils.LOGE(TAG, "Can't filter. Array doesn't consist only jsonObjects. Only jsonObjects are added in result.");
                    }
                }
                return result;
            }
        }
    }

    private static boolean firstContainsSecond(JsonElement first, JsonElement second) {
        if(first == null) {
            LogUtils.LOGD(TAG, "both are null");
            return second==null;
        } else if(first.isJsonNull()) {
            LogUtils.LOGD(TAG, "both are json null");
            return second.isJsonNull();
        } else if(first.isJsonPrimitive()) {
            if(second.isJsonNull()) {
                LogUtils.LOGD(TAG, "first is primitive, second is json null");
                return true;
            } else if(second.isJsonPrimitive()) {
                return first.getAsString().equals(second.getAsString());
            } else {
                LogUtils.LOGD(TAG, "first is a primitive, second isn't");
                return false;
            }
        } else if(first.isJsonObject()) {
            if(second.isJsonNull()) {
                LogUtils.LOGD(TAG, "Both are json null");
                return true;
            } else if(second.isJsonPrimitive()) {
                LogUtils.LOGD(TAG, "first is an object, second is a primitive");
                return false;
            } else if(second.isJsonObject()) {
                return firstContainsSecond(first.getAsJsonObject(), second.getAsJsonObject());
            } else {
                LogUtils.LOGD(TAG, "first is an object. second is a higher data type");
                return false;
            }
        } else if(first.isJsonArray()) {
            if(second.isJsonNull()) {
                LogUtils.LOGD(TAG, "first is a JsonArray, second is a Json null");
                return true;
            } else if(second.isJsonArray()) {
                LogUtils.LOGD(TAG, "both are JsonArray");
                return arrayContainsArray(first.getAsJsonArray(), second.getAsJsonArray());
            } else if(second.isJsonObject()) {
                LogUtils.LOGD(TAG, "first is an array, second is an object");
                return arrayContainsObject(first.getAsJsonArray(), second.getAsJsonObject());
            } else if(second.isJsonPrimitive()) {
                LogUtils.LOGD(TAG, "first is an array, second is a Primitive");
                return arrayContainsPrimitive(first.getAsJsonArray(), second.getAsJsonPrimitive());
            }
        }
        LogUtils.LOGE(TAG, "No condition met. Shouldn't happen");
        return false;
    }

    private static boolean arrayContainsArray(JsonArray first, JsonArray second) {
        if(first == null) {
            return second == null;
        } else if(first.isJsonNull()) {
            return second.isJsonNull();
        } else if(first.size() == 0) {
            return second.size() == 0;
        } else {
            for(int i=0; i < second.size(); i++) {
                if(!firstContainsSecond(first, second.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean arrayContainsObject(JsonArray first, JsonObject second) {
        if(first == null) {
            return second == null;
        } else if(first.isJsonNull()) {
            return second.isJsonNull();
        } else {
            for(int i=0; i < first.size(); i++) {
                if(firstContainsSecond(first.get(i), second)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean arrayContainsPrimitive(JsonArray first, JsonPrimitive second) {
        if(first==null) {
            return second==null;
        } else if(first.isJsonNull()) {
            return second.isJsonNull();
        } else {
            for(int i=0; i<first.size(); i++) {
                if(first.get(i).isJsonPrimitive()) {
                    if(first.get(i).getAsString().equals(second.getAsString())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * might not work well with nested JsonObjects.
     * doesn't care about Type of JsonPrimitive on first level.
     * @param first
     * @param second
     * @return
     */
    public static boolean firstContainsSecond(JsonObject first, JsonObject second) {
        if (first == null) {
            return second == null;
        } else {
            if (second == null || areJsonEqual(second, new JsonObject())) {
                return true;
            } else {
                LogUtils.LOGD(TAG, "Starting key compare");
                for (Map.Entry<String, JsonElement> entry : second.entrySet()) {
                    LogUtils.LOGD(TAG, "key: " + entry.getKey());
                    if (first.has(entry.getKey())) {
                        JsonElement firstValue = first.get(entry.getKey());
                        JsonElement secondValue = second.get(entry.getKey());
                        LogUtils.LOGD(TAG, "key found, checking if first contains second");
                        if (!firstContainsSecond(firstValue, secondValue)) {
                            LogUtils.LOGD(TAG, "returned false for first: " + firstValue + " second: " + secondValue);
                            return false;
                        }
                    } else {
                        LogUtils.LOGD(TAG, "key not found. return false");
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public static <T extends JsonElement> T copyJsonElement(T jsonElement, Class<T> type) {
        if (jsonElement == null) {
            return null;
        } else {
            String jsonString = jsonElement.toString();
            Gson gson = new Gson();
            return gson.fromJson(jsonString, type);
        }
    }

    public static void mergeJsonObjects(JsonObject fromObject, JsonObject toObject) {
        for (Map.Entry<String, JsonElement> property : fromObject.entrySet()) {
            toObject.add(property.getKey(), property.getValue());
        }
    }

    public static void parseJsonElement(final String json, final ParserCallback callback){
        AsyncTask<String, Integer, JsonElement> asyncTask = new AsyncTask<String, Integer, JsonElement>() {
            @Override
            protected JsonElement doInBackground(String[] params) {
                String jsonString = params[0];
                Gson gson = new Gson();
                return gson.fromJson(jsonString, JsonElement.class);
            }

            @Override
            protected void onPostExecute(JsonElement jsonElement) {
                callback.onParseComplete(jsonElement);
            }
        };

        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, json);
    }

    public interface ParserCallback {
        void onParseComplete(JsonElement jsonElement);
    }
}
