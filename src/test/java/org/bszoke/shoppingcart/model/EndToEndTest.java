package org.bszoke.shoppingcart.model;

import org.bszoke.shoppingcart.model.discount.DiscountRuleFactoryImpl;
import org.bszoke.shoppingcart.service.InMemoryProductCatalogRepository;
import org.bszoke.shoppingcart.service.ProductCatalogRepository;
import org.bszoke.shoppingcart.service.TotalCostCalculator;
import org.bszoke.shoppingcart.service.TotalCostCalculatorImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class EndToEndTest {

    private Basket basket;

    @Before
    public void setUp() {
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        ProductCatalogRepository productCatalogRepository = new InMemoryProductCatalogRepository();
        basket = new Basket(totalCostCalculator, productCatalogRepository);
    }

    @Test
    public void shouldCalculateTheTotalCostWhenNoDiscountIsApplied() {
        //given
        basket.createOrder(Arrays.asList("Melon", "Melon", "Lime"));

        //when
        BigDecimal result = basket.totalCost();

        //then
        assertEquals(BigDecimal.valueOf(115.0), result);
    }

    @Test
    public void shouldCalculateTheTotalCostWhenTwoForThePriceOfOneDiscountIsApplied() {
        //given
        basket.createOrder(Arrays.asList("Apple", "Apple", "Lime"));

        //when
        BigDecimal result = basket.totalCost();

        //then
        assertEquals(BigDecimal.valueOf(50.0), result);
    }

    @Test
    public void shouldCalculateTheTotalCostWhenThreeForThePriceOfTwoDiscountIsApplied() {
        //given
        basket.createOrder(Arrays.asList("Banana", "Banana", "Banana", "Lime"));

        //when
        BigDecimal result = basket.totalCost();

        //then
        assertEquals(BigDecimal.valueOf(55.0), result);
    }

    @Test
    public void shouldCalculateTheTotalCostWhenMultipleDiscountsAreApplied() {
        //given
        basket.createOrder(Arrays.asList("Pineapples", "Pineapples", "Pineapples", "Pineapples", "Pineapples"));

        //when
        BigDecimal result = basket.totalCost();

        //then
        assertEquals(BigDecimal.valueOf(30.0), result);
    }
}