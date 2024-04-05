package com.ocado.basket.BasketSplitter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasketSplitterTest {
	

private BasketSplitter BasketSplitter;
	
@BeforeEach
void setUp() {
	BasketSplitter = new BasketSplitter( com.ocado.basket.BasketSplitter.BasketSplitter.absolutePathToConfigFile);
}

	@Test
	void testAdd() {
        fail();
    }
}
