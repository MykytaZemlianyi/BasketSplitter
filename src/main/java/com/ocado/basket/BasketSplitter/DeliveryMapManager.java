package com.ocado.basket.BasketSplitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryMapManager {

//Find the keys that match the Item table in the config map and if they are not
	// already in the map, add them there and add the item as a value anyway.
	public static Map<String, List<String>> createDeliveryMap(List<String> items) {
		Map<String, List<String>> deliveryMap = new HashMap<>();

		for (String item : items) {
			List<String> deliveryMethods = BasketSplitter.getConfig().get(item);

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
	public static List<String> sortDeliveryMapKeys(Map<String, List<String>> deliveryMap) {

		List<Map.Entry<String, List<String>>> SortedDeliveryMap = new ArrayList<>(deliveryMap.entrySet());

		SortedDeliveryMap.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

		List<String> sortedKeys = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : SortedDeliveryMap) {
			sortedKeys.add(entry.getKey());
		}

		return sortedKeys;
	}

}