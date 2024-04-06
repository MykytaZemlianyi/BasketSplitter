package com.ocado.basket.BasketSplitter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryMapManagerTest {
	private List<String> itemList;

	private BasketSplitter basketSplitter;

	private DeliveryMapManager deliveryMapManager;
	private Map<String, List<String>> deliveryMap;

	@BeforeEach
	void setUp() throws Exception {
		String absolutePathToConfigFile = "C:\\workspace\\BasketSplitter\\src\\main\\resources\\config.json";

		this.basketSplitter = new BasketSplitter(absolutePathToConfigFile);
		this.deliveryMapManager = new DeliveryMapManager();

		// Initialize itemList
		itemList = new ArrayList<>();
		itemList.add("Cocoa Butter");
		itemList.add("Tart - Raisin And Pecan");
		itemList.add("Table Cloth 54x72 White");
		itemList.add("Flower - Daisies");
		itemList.add("Fond - Chocolate");
		itemList.add("Cookies - Englishbay Wht");
	}

	@Test
	public void testCreateDeliveryMap() {
		deliveryMap = deliveryMapManager.createDeliveryMap(itemList);

		assertTrue(deliveryMap.containsKey("Next day shipping"));

		boolean containsCocoaButter = false;
		for (List<String> items : deliveryMap.values()) {
			if (items.contains("Cocoa Butter")) {
				containsCocoaButter = true;
				break;
			}
		}
		assertTrue(containsCocoaButter);

		assertTrue(deliveryMap.containsKey("Mailbox delivery"));

		containsCocoaButter = false;
		for (List<String> items : deliveryMap.values()) {
			if (items.contains("Fond - Chocolate")) {
				containsCocoaButter = true;
				break;
			}
		}
		assertTrue(containsCocoaButter);
	}

	@Test
	public void sortDeliveryMapKeys() {
		List<String> expectedDeliveryMap = new ArrayList<>();
		expectedDeliveryMap.add("Courier");
		expectedDeliveryMap.add("Mailbox delivery");
		expectedDeliveryMap.add("Express Collection");
		expectedDeliveryMap.add("Pick-up point");
		expectedDeliveryMap.add("Parcel locker");
		expectedDeliveryMap.add("In-store pick-up");
		expectedDeliveryMap.add("Same day delivery");
		expectedDeliveryMap.add("Next day shipping");

		List<String> actualDeliveryMap = DeliveryMapManager
				.sortDeliveryMapKeys(deliveryMapManager.createDeliveryMap(itemList));

		assertEquals(expectedDeliveryMap, actualDeliveryMap);
	}

}
