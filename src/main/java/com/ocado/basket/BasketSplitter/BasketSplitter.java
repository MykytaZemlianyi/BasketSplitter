package com.ocado.basket.BasketSplitter;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class BasketSplitter {
	private String absolutePathToConfigFile;
	private Map<String, List<String>> config;

	public BasketSplitter(String absolutePathToConfigFile) {
		this.absolutePathToConfigFile = absolutePathToConfigFile;
		this.config = setConfig();
	}

	public Map<String, List<String>> getConfig() {
		return config;
	}

	public String getAbsolutePathToConfigFile() {
		return absolutePathToConfigFile;
	}

	public void setAbsolutePathToConfigFile(String absolutePathToConfigFile) {
		this.absolutePathToConfigFile = absolutePathToConfigFile;
	}

	public Map<String, List<String>> split(List<String> items) {

		return null;
	}

	public Map<String, List<String>> createDeliveryMap(List<String> items) {
		Map<String, List<String>> deliveryMap = new HashMap<>();

		for (String item : items) {
			List<String> deliveryMethods = getConfig().get(item);

			if (deliveryMethods != null) {
				for (String deliveryMethod : deliveryMethods) {
					if (!deliveryMap.containsKey(deliveryMethod)) {
						// Создаем новый список для данного способа доставки
						deliveryMap.put(deliveryMethod, new ArrayList<>());
					}
					// Добавляем товар в список для данного способа доставки
					deliveryMap.get(deliveryMethod).add(item);
				}
			}
		}

		return deliveryMap;
	}

	public Map<String, List<String>> setConfig() {

		Map<String, List<String>> config = new HashMap<>();

		try (JsonReader reader = Json.createReader(new FileReader(getAbsolutePathToConfigFile()))) {
			JsonObject jsonObject = reader.readObject();

			for (String key : jsonObject.keySet()) {
				JsonArray jsonArray = jsonObject.getJsonArray(key);
				List<String> values = new ArrayList<>();

				for (int i = 0; i < jsonArray.size(); i++) {
					values.add(jsonArray.getString(i));
				}

				config.put(key, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return config;
	}
}
