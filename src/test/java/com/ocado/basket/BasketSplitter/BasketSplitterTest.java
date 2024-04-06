package com.ocado.basket.BasketSplitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

}
