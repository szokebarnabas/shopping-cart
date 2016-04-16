package org.bszoke.shoppingcart.service;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;
import org.bszoke.shoppingcart.model.discount.DiscountRuleFactoryImpl;
import org.bszoke.shoppingcart.model.discount.DiscountType;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TotalCostCalculatorImplUTest {

    private TotalCostCalculatorImpl priceCalculator;
    private static final Product APPLE = Product.of("Apple", BigDecimal.valueOf(35));
    private static final Product BANANA = Product.of("Banana", BigDecimal.valueOf(20));

    @Before
    public void setUp() throws Exception {
        priceCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
    }

    @Test
    public void shouldCalculateTotalCostWhenDiscountIsNotApplied() throws Exception {
        //given
        Map<Product, Long> products = new HashMap<>();
        products.put(APPLE, 2L);
        products.put(BANANA, 1L);
        Order order = Order.of(products);

        //when
        BigDecimal result = priceCalculator.calculateTotalCost(order);

        //then
        assertEquals(BigDecimal.valueOf(90.0), result);
    }

    @Test
    public void shouldCalculateTheTotalCostWithDiscount() {
        //given
        Map<Product, Long> products = new HashMap<>();
        products.put(Product.of("Apple", BigDecimal.valueOf(35), Arrays.asList(DiscountType.BUY_ONE_GET_ONE_FREE)), 2L);
        Order order = Order.of(products);

        //when
        BigDecimal result = priceCalculator.calculateTotalCost(order);

        //then
        assertEquals(BigDecimal.valueOf(35.0), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfTheOrderIsNull() {
        //when
        priceCalculator.calculateTotalCost(null);
    }
}