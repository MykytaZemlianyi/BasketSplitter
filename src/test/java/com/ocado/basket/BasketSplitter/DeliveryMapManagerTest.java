package com.ocado.basket.BasketSplitter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryMapManagerTest {
	private List<String> itemList;

	private Map<String, List<String>> deliveryMap;

	@BeforeEach
	void setUp() throws Exception {
		String absolutePathToConfigFile = "C:\\workspace\\BasketSplitter\\src\\main\\resources\\config.json";

		new BasketSplitter(absolutePathToConfigFile);
		new DeliveryMapManager();

		// Initialize itemList1
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
		deliveryMap = DeliveryMapManager.createDeliveryMap(itemList);

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
	public void createSortedDeliveryMapKeysList() {
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
				.createSortedDeliveryMapKeysList(DeliveryMapManager.createDeliveryMap(itemList));

		assertEquals(expectedDeliveryMap, actualDeliveryMap);

	}

	@Test
	public void filterDeliveryMap() {

		Map<String, List<String>> expectedMap = new HashMap<>();

		List<String> courierList = new ArrayList<>();
		courierList.add("Cocoa Butter");
		courierList.add("Tart - Raisin And Pecan");
		courierList.add("Table Cloth 54x72 White");
		courierList.add("Flower - Daisies");
		courierList.add("Cookies - Englishbay Wht");
		expectedMap.put("Courier", courierList);

		List<String> mailboxDeliveryList = new ArrayList<>();
		mailboxDeliveryList.add("Fond - Chocolate");
		expectedMap.put("Mailbox delivery", mailboxDeliveryList);

		deliveryMap = DeliveryMapManager.createDeliveryMap(itemList);
		List<String> sortedKeys1 = DeliveryMapManager.createSortedDeliveryMapKeysList(deliveryMap);
		Map<String, List<String>> filteredDeliveryMap = DeliveryMapManager.filterDeliveryMap(deliveryMap, sortedKeys1);

		assertAll("Check filtered map", () -> assertEquals(expectedMap.size(), filteredDeliveryMap.size()), () -> {
			for (String key : expectedMap.keySet()) {
				assertEquals(expectedMap.get(key), filteredDeliveryMap.get(key));
			}
		});
	}

}
