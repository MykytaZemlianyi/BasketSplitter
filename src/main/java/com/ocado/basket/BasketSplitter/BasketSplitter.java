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

	private String getAbsolutePathToConfigFile() {
		return absolutePathToConfigFile;
	}

	public Map<String, List<String>> split(List<String> items) {

		Map<String, List<String>> deliveryMap = createDeliveryMap(items, config);

		return filterDeliveryMap(deliveryMap);
	}

	/*
	 * This method reads a JSON configuration file, converts its contents into a
	 * Map<String, List<String>> object and returns it.
	 */
	private Map<String, List<String>> setConfig() {

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

	public Map<String, List<String>> setConfigReflector() {
		return setConfig();
	}

	// Find the keys that match the Item table in the config map and if they are not
	// already in the map, add them there and add the item as a value anyway.
	private Map<String, List<String>> createDeliveryMap(List<String> items, Map<String, List<String>> config) {
		Map<String, List<String>> deliveryMap = new HashMap<>();

		for (String item : items) {
			List<String> deliveryMethods = config.get(item);

			if (deliveryMethods != null) {
				for (String deliveryMethod : deliveryMethods) {
					if (!deliveryMap.containsKey(deliveryMethod)) {
						deliveryMap.put(deliveryMethod, new ArrayList<>());
					}
					deliveryMap.get(deliveryMethod).add(item);
				}
			}
		}
		return deliveryMap;
	}

	// The method returns the list of keys sorted by descending number of items in
	// each key
	private List<String> createSortedDeliveryMapKeysList(Map<String, List<String>> deliveryMap) {

		List<Map.Entry<String, List<String>>> SortedDeliveryMap = new ArrayList<>(deliveryMap.entrySet());

		SortedDeliveryMap.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

		List<String> sortedKeys = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : SortedDeliveryMap) {
			sortedKeys.add(entry.getKey());
		}

		return sortedKeys;
	}

	/*
	 * This method filters the main delivery map by maximizing the number of items
	 * in 1 delivery method while minimizing the total number of deliveries
	 */
	private Map<String, List<String>> filterDeliveryMap(Map<String, List<String>> deliveryMap) {

		List<String> sortedKeys = createSortedDeliveryMapKeysList(deliveryMap);
		for (int i = sortedKeys.size() - 1; i >= 0; i--) {

			String currentKey = sortedKeys.get(i);
			List<String> currentItems = deliveryMap.get(currentKey);

			if (currentItems != null && !currentItems.isEmpty()) {

				for (int j = 0; j < i; j++) {
					String largerKey = sortedKeys.get(j);
					List<String> largerItems = deliveryMap.get(largerKey);

					if (largerItems != null && !largerItems.isEmpty()) {
						// Create a copy of the list of items in the current key to avoid
						// ConcurrentModificationException
						List<String> copyOfCurrentItems = new ArrayList<>(currentItems);

						for (String item : copyOfCurrentItems) {
							if (largerItems.contains(item)) {
								currentItems.remove(item);
							}
						}
					}
				}

				if (currentItems.isEmpty()) {
					deliveryMap.remove(currentKey);
				}
			}
		}

		return deliveryMap;
	}

}
