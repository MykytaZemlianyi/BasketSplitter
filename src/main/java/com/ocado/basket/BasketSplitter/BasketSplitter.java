package com.ocado.basket.BasketSplitter;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

/**
 * BasketSplitter class represents a utility for splitting items into delivery
 * methods based on a configuration file. It reads a JSON configuration file and
 * performs the splitting of items into delivery methods accordingly.
 */
public class BasketSplitter {
	private String absolutePathToConfigFile;
	private static Map<String, List<String>> config;

	/**
	 * Constructs a BasketSplitter object with the absolute path to the
	 * configuration file. Uses the private method setConfig() to create a
	 * configuration map.
	 * 
	 * @param absolutePathToConfigFile The absolute path to the configuration file.
	 * @see #setConfig()
	 */
	public BasketSplitter(String absolutePathToConfigFile) {
		this.absolutePathToConfigFile = absolutePathToConfigFile;
		BasketSplitter.config = setConfig();
	}

	private String getAbsolutePathToConfigFile() {
		return absolutePathToConfigFile;
	}

	/**
	 * Gets an Object of type List&lt;String&gt;, <br>
	 * writes a Map&lt;String, List&lt;String&gt;&gt; from the config.json file.
	 * <br>
	 * On the basis of config and itemList creates a new object of type
	 * Map&lt;String, List&lt;String&gt;&gt; deliveryMap which stores as keys all
	 * possible values for all items in list. <br>
	 * Sorts itemList by values from config file maximizing the number of items in
	 * one key while minimizing the total number of keys.
	 * 
	 * @param items Items list of values for which values are stored in the config.
	 * @return deliveryMap - Sorted map of config items and itemList
	 */

	public Map<String, List<String>> split(List<String> items) {

		Map<String, List<String>> deliveryMap = createDeliveryMap(items, config);

		return filterDeliveryMap(deliveryMap);
	}

	/**
	 * Reads a JSON configuration file, converts its contents into a
	 * Map&lt;String,List&lt;String&gt;&gt; object, and returns it.
	 * 
	 * @return A Map containing the configuration data, where keys are strings and
	 *         values are lists of strings.
	 * @throws IOException   If an I/O error occurs while reading the configuration
	 *                       file.
	 * @throws JsonException If there is an error in the JSON syntax or structure.
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
		} catch (JsonException e1) {
			e1.printStackTrace();
		}

		return config;
	}

	/**
	 * Provides a reflector for the setConfig() function, specifically intended for
	 * use in unit tests. <br>
	 * This method is designed to be used only for testing purposes and should not
	 * be used in production code.
	 * 
	 * @return The setConfig() function <b>(for testing purposes only)</b>.
	 * @see #setConfig()
	 */
	Map<String, List<String>> setConfigReflector() {
		return setConfig();
	}

	/**
	 * Find the keys that match the Item table in the config map and if they are not
	 * already in the map, add them there and add the item as a value anyway.
	 */
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

	/**
	 * Creates a list of delivery map keys sorted by the descending number of items
	 * in each key.
	 * 
	 * @param deliveryMap The delivery map to be sorted.
	 * @return A list of keys sorted by the number of items in descending order.
	 */

	private List<String> createSortedDeliveryMapKeysList(Map<String, List<String>> deliveryMap) {

		List<Map.Entry<String, List<String>>> SortedDeliveryMap = new ArrayList<>(deliveryMap.entrySet());

		SortedDeliveryMap.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

		List<String> sortedKeys = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : SortedDeliveryMap) {
			sortedKeys.add(entry.getKey());
		}

		return sortedKeys;
	}

	/**
	 * Filters the delivery map by maximizing the number of items in one delivery
	 * method while minimizing the total number of delivery methods.
	 * 
	 * @param deliveryMap The unfiltered delivery map.
	 * @return The filtered delivery map.
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
