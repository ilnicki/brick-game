package me.ilnicki.bg.core.data.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.ilnicki.bg.core.data.DataBean;

import java.lang.reflect.Type;

public class DataBeanSerializer implements JsonSerializer<DataBean>, JsonDeserializer<DataBean> {

    @Override
    public JsonElement serialize(DataBean dataCluster, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }

    @Override
    public DataBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
