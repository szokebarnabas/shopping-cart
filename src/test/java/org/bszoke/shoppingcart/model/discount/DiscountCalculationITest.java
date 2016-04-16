package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Basket;
import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.service.InMemoryProductCatalogRepository;
import org.bszoke.shoppingcart.service.TotalCostCalculator;
import org.bszoke.shoppingcart.service.TotalCostCalculatorImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class DiscountCalculationITest {

    private Basket basket;
    private InMemoryProductCatalogRepository productCatalogInMemoryRepository;

    @Before
    public void setUp() throws Exception {
        productCatalogInMemoryRepository = new InMemoryProductCatalogRepository();
    }

    @Test
    public void shouldCalculateWithTwoForOneDiscountWhenThereIsOneRemainingItem() throws Exception {
        //given
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        basket = new Basket(totalCostCalculator, productCatalogInMemoryRepository);
        basket.createOrder(Arrays.asList("Apple", "Apple", "Banana"));
        Order order = Order.of(basket.getOrder().getItemGroups());
        Discount rootDiscount = new DiscountRuleFactoryImpl().createRuleChain();

        //when
        BigDecimal discount = rootDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(35), discount);
    }

    @Test
    public void shouldCalculateWithTwoForOneDiscountWhenThereIsNoRemainingItem() {
        //given
        Discount rootDiscount = new DiscountRuleFactoryImpl().createRuleChain();
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        basket = new Basket(totalCostCalculator, productCatalogInMemoryRepository);
        basket.createOrder(Arrays.asList("Apple", "Apple"));
        Order order = Order.of(basket.getOrder().getItemGroups());

        //when
        BigDecimal discount = rootDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(35), discount);
    }

    @Test
    public void shouldNotCalculateDiscountWhenTheDiscountIsNotApplicable() throws Exception {
        //given
        Discount rootDiscount = new DiscountRuleFactoryImpl().createRuleChain();
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        basket = new Basket(totalCostCalculator, productCatalogInMemoryRepository);
        basket.createOrder(Arrays.asList("Apple"));
        Order order = Order.of(basket.getOrder().getItemGroups());

        //when
        BigDecimal discount = rootDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(0), discount);
    }

    @Test
    public void shouldApplyTheTwoForOneDiscountTwice() throws Exception {
        //given
        Discount rootDiscount = new DiscountRuleFactoryImpl().createRuleChain();
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        basket = new Basket(totalCostCalculator, productCatalogInMemoryRepository);
        basket.createOrder(Arrays.asList("Apple", "Apple", "Apple", "Apple"));
        Order order = Order.of(basket.getOrder().getItemGroups());

        //when
        BigDecimal discount = rootDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(70), discount);
    }

    @Test
    public void shouldApplyTheThreeForThePriceOfTwoDiscount() throws Exception {
        //given
        Discount rootDiscount = new DiscountRuleFactoryImpl().createRuleChain();
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        basket = new Basket(totalCostCalculator, productCatalogInMemoryRepository);
        basket.createOrder(Arrays.asList("Banana", "Banana", "Banana"));
        Order order = Order.of(basket.getOrder().getItemGroups());

        //when
        BigDecimal discount = rootDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(20), discount);
    }

    @Test
    public void shouldApplyTheThreeForThePriceOfTwoDiscountWhenThereAreRemainingItems() throws Exception {
        //given
        Discount rootDiscount = new DiscountRuleFactoryImpl().createRuleChain();
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        basket = new Basket(totalCostCalculator, productCatalogInMemoryRepository);
        basket.createOrder(Arrays.asList("Banana", "Banana", "Banana", "Banana", "Banana"));
        Order order = Order.of(basket.getOrder().getItemGroups());

        //when
        BigDecimal discount = rootDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(20), discount);
    }

    @Test
    public void shouldNotApplyTheThreeForThePriceOfTwoDiscountWhenItIsNotApplicable() throws Exception {
        //given
        Discount rootDiscount = new DiscountRuleFactoryImpl().createRuleChain();
        TotalCostCalculator totalCostCalculator = new TotalCostCalculatorImpl(new DiscountRuleFactoryImpl());
        basket = new Basket(totalCostCalculator, productCatalogInMemoryRepository);
        basket.createOrder(Arrays.asList("Banana", "Banana"));
        Order order = Order.of(basket.getOrder().getItemGroups());

        //when
        BigDecimal discount = rootDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(0), discount);
    }
}
