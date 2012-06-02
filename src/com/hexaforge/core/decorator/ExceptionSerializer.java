package com.hexaforge.core.decorator;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hexaforge.core.ExceptionMessage;

public class ExceptionSerializer implements JsonSerializer<ExceptionMessage> {

    public JsonElement serialize(ExceptionMessage src, Type typeOfSrc,
	    JsonSerializationContext context) {
	JsonObject result = new JsonObject();
	result.addProperty("success", false);
	result.addProperty("message", src.getMessage());
	return result;
    }

}
