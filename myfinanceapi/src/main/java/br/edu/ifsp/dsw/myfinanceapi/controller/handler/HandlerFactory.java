package br.edu.ifsp.dsw.myfinanceapi.controller.handler;

import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HandlerFactory {
	public static Handler chain() throws Throwable {
		InputStreamReader reader = new InputStreamReader(
			HandlerFactory.class.getClassLoader().getResourceAsStream("chain-config.json")
		);
		
		JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
		JsonArray array = json.getAsJsonArray("chain");
		List<Handler> handlers = new ArrayList<Handler>();
		
		for (JsonElement el : array) {
			String className = el.getAsString();
			Class<?> clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			Handler handler = (Handler) constructor.newInstance();
			handlers.add(handler);
		}
		
		for (int i = 0; i < handlers.size()-1; i++) {
			Handler curr = handlers.get(i);
			Handler next = handlers.get(i+1);
			curr.setNext(next);
		}
		
		return handlers.get(0);
	}
}
