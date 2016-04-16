package org.bszoke.shoppingcart.model;

import org.bszoke.shoppingcart.service.ProductCatalogRepository;
import org.bszoke.shoppingcart.service.TotalCostCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasketUTest {

    private Basket basket;
    @Mock private TotalCostCalculator totalCostCalculator;
    @Mock private ProductCatalogRepository productCatalogRepository;

    private static final ProductCatalogItem APPLE = ProductCatalogItem.of("Apple", BigDecimal.valueOf(35), new ArrayList<>());
    private static final ProductCatalogItem BANANA = ProductCatalogItem.of("Banana", BigDecimal.valueOf(20), new ArrayList<>());

    @Before
    public void setUp() {
        basket = new Basket(totalCostCalculator, productCatalogRepository);
        when(productCatalogRepository.findByName("Apple")).thenReturn(Optional.of(APPLE));
        when(productCatalogRepository.findByName("Banana")).thenReturn(Optional.of(BANANA));
    }

    @Test
    public void shouldCollectTheBasketItemsIntoProductGroups() {
        //given
        List<String> products = new ArrayList<>(Arrays.asList("Apple", "Apple", "Apple", "Banana"));

        //when
        basket.createOrder(products);

        //then
        Order order = basket.getOrder();
        Map<Product, Long> items = order.getItemGroups();
        assertEquals(2, items.size());
        assertThat(items, allOf(
                hasEntry(Product.of("Apple", BigDecimal.valueOf(35)), 3L),
                hasEntry(Product.of("Banana", BigDecimal.valueOf(20)), 1L)));
    }

    @Test
    public void shouldFilterOutTheProductsThatAreNotAvailableInTheCatalog() {
        //given
        List<String> products = new ArrayList<>(Arrays.asList("Apple", "Cucumber", "Banana"));
        when(productCatalogRepository.findByName("Cucumber")).thenReturn(Optional.empty());

        //when
        basket.createOrder(products);

        //then
        Map<Product, Long> items = basket.getOrder().getItemGroups();
        assertEquals(2, items.size());
        assertThat(items, allOf(
                hasEntry(Product.of("Apple", BigDecimal.valueOf(35)), 1L),
                hasEntry(Product.of("Banana", BigDecimal.valueOf(20)), 1L)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfTheInputIsNull() {
        //when
        basket.createOrder(null);
    }
}