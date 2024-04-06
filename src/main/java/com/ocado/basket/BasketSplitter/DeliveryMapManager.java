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
	public static List<String> createSortedDeliveryMapKeysList(Map<String, List<String>> deliveryMap) {

		List<Map.Entry<String, List<String>>> SortedDeliveryMap = new ArrayList<>(deliveryMap.entrySet());

		SortedDeliveryMap.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));

		List<String> sortedKeys = new ArrayList<>();
		for (Map.Entry<String, List<String>> entry : SortedDeliveryMap) {
			sortedKeys.add(entry.getKey());
		}

		return sortedKeys;
	}

	public static Map<String, List<String>> filterDeliveryMap(Map<String, List<String>> deliveryMap,
			List<String> sortedKeys) {

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
					} else
						deliveryMap.remove(largerKey);
				}

				if (currentItems.isEmpty()) {
					deliveryMap.remove(currentKey);
				}
			} else
				deliveryMap.remove(currentKey);
		}

		return deliveryMap;
	}

}