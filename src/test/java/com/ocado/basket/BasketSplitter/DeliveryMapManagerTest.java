package com.ocado.basket.BasketSplitter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryMapManagerTest {
	private List<String> itemList1;
	private List<String> itemList2;

	private Map<String, List<String>> deliveryMap;

	@BeforeEach
	void setUp() throws Exception {
		String absolutePathToConfigFile = "C:\\workspace\\BasketSplitter\\src\\main\\resources\\config.json";

		new BasketSplitter(absolutePathToConfigFile);
		new DeliveryMapManager();

		// Initialize itemList1
		itemList1 = new ArrayList<>();
		itemList1.add("Cocoa Butter");
		itemList1.add("Tart - Raisin And Pecan");
		itemList1.add("Table Cloth 54x72 White");
		itemList1.add("Flower - Daisies");
		itemList1.add("Fond - Chocolate");
		itemList1.add("Cookies - Englishbay Wht");

		// Initialize itemList2
		itemList2 = new ArrayList<>();
		itemList2.add("Fond - Chocolate");
		itemList2.add("Chocolate - Unsweetened");
		itemList2.add("Nut - Almond");
		itemList2.add("Blanched, Whole");
		itemList2.add("Haggis");
		itemList2.add("Mushroom - Porcini Frozen");
		itemList2.add("Cake - Miini Cheesecake Cherry");
		itemList2.add("Sauce - Mint");
		itemList2.add("Longan");
		itemList2.add("Bag Clear 10 Lb");
		itemList2.add("Nantucket - Pomegranate Pear");
		itemList2.add("Puree - Strawberry");
		itemList2.add("Numi - Assorted Teas");
		itemList2.add("Apples - Spartan");
		itemList2.add("Garlic - Peeled");
		itemList2.add("Cabbage - Nappa");
		itemList2.add("Bagel - Whole White Sesame");
		itemList2.add("Tea - Apple Green Tea");

	}

	@Test
	public void testCreateDeliveryMap() {
		deliveryMap = DeliveryMapManager.createDeliveryMap(itemList1);

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
				.sortDeliveryMapKeys(DeliveryMapManager.createDeliveryMap(itemList1));

		assertEquals(expectedDeliveryMap, actualDeliveryMap);

		List<String> actualDeliveryMap2 = DeliveryMapManager
				.sortDeliveryMapKeys(DeliveryMapManager.createDeliveryMap(itemList2));

		List<String> expectedDeliveryMap2 = new ArrayList<>();
		expectedDeliveryMap2.add("Same day delivery");
		expectedDeliveryMap2.add("Express Collection");
		expectedDeliveryMap2.add("Pick-up point");
		expectedDeliveryMap2.add("In-store pick-up");
		expectedDeliveryMap2.add("Mailbox delivery");
		expectedDeliveryMap2.add("Courier");
		expectedDeliveryMap2.add("Next day shipping");
		expectedDeliveryMap2.add("Parcel locker");

		assertEquals(expectedDeliveryMap2, actualDeliveryMap2);
	}

}
