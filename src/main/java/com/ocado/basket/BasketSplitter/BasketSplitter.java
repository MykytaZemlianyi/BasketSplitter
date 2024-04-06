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
	private static Map<String, List<String>> config;

	public BasketSplitter(String absolutePathToConfigFile) {
		this.absolutePathToConfigFile = absolutePathToConfigFile;
		BasketSplitter.config = setConfig();
	}

	static Map<String, List<String>> getConfig() {
		if (config != null) {
			return config;
		} else {
			throw new IllegalStateException("Config is not initialized");
		}
	}

	private String getAbsolutePathToConfigFile() {
		return absolutePathToConfigFile;
	}

	public Map<String, List<String>> split(List<String> items) {
		List<String> itemList = items;
		Map<String, List<String>> config = getConfig();
		Map<String, List<String>> deliveryMap = DeliveryMapManager.createDeliveryMap(itemList, config);

		return DeliveryMapManager.filterDeliveryMap(deliveryMap);
	}

	/*
	 * This method reads a JSON configuration file, converts its contents into a
	 * Map<String, List<String>> object and returns it.
	 */
	Map<String, List<String>> setConfig() {

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
