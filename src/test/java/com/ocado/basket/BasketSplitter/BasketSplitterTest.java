package com.ocado.basket.BasketSplitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketSplitterTest {

	private BasketSplitter basketSplitter;

	@BeforeEach
	void setUp() {
		String absolutePathToConfigFile = "C:\\workspace\\BasketSplitter\\src\\main\\resources\\config.json";
		this.basketSplitter = new BasketSplitter(absolutePathToConfigFile);

	}

	@Test
	public void testSetConfig() {

		Map<String, List<String>> config = basketSplitter.setConfig();

		assertTrue(!config.isEmpty());

		assertEquals(100, config.size());

		assertTrue(config.containsKey("Cookies Oatmeal Raisin"));
		assertEquals(2, config.get("Cookies Oatmeal Raisin").size());

		assertTrue(config.containsKey("Cheese Cloth"));
		assertEquals(5, config.get("Cheese Cloth").size());

	}

	@Test
	public void testSplit() {
		List<String> itemList = new ArrayList<String>();
		itemList.add("Cocoa Butter");
		itemList.add("Tart - Raisin And Pecan");
		itemList.add("Table Cloth 54x72 White");
		itemList.add("Flower - Daisies");
		itemList.add("Fond - Chocolate");
		itemList.add("Cookies - Englishbay Wht");

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

		assertEquals(expectedMap, basketSplitter.split(itemList));
	}

}
