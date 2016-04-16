package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bszoke.shoppingcart.model.discount.OrderHelper.createOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ThreeForTwoDiscountUTest {

    private ThreeForTwoDiscount threeForTwoDiscount;
    @Mock private Discount successor;

    @Before
    public void setUp() throws Exception {
        threeForTwoDiscount = new ThreeForTwoDiscount();
        threeForTwoDiscount.setSuccessor(successor);
    }

    @Test
    public void shouldCalculateTheDiscountWhenTheNumberOfProductsIsThree() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 3L, DiscountType.THREE_FOR_THE_PRICE_OF_TWO);

        //when
        BigDecimal result = threeForTwoDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(10), result);
    }

    @Test
    public void shouldNotCalculateTheDiscountWhenTheNumberOfProductsLessThanThree() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 2L, DiscountType.THREE_FOR_THE_PRICE_OF_TWO);
        when(successor.calculate(order)).thenReturn(BigDecimal.ZERO);

        //when
        BigDecimal result = threeForTwoDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(0), result);
    }

    @Test
    public void shouldDelegateToTheNextCalculatorIfCannotApplyTheDiscount() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 1L, DiscountType.THREE_FOR_THE_PRICE_OF_TWO);
        when(successor.calculate(order)).thenReturn(BigDecimal.ZERO);

        //when
        threeForTwoDiscount.calculate(order);

        //then
        verify(successor).calculate(order);
    }

    @Test
    public void shouldDelegateToTheNextCalculatorIfThereAreProductsThatCanBeDiscounted() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 4L, DiscountType.THREE_FOR_THE_PRICE_OF_TWO);
        Order remainingProducts = createOrder(BigDecimal.valueOf(10), 1L, DiscountType.THREE_FOR_THE_PRICE_OF_TWO);
        when(successor.calculate(remainingProducts)).thenReturn(BigDecimal.ZERO);

        //when
        threeForTwoDiscount.calculate(order);

        //then
        verify(successor).calculate(remainingProducts);
    }

    @Test
    public void shouldCalculateTheDiscountWithMultipleProductGroups() {
        //given
        Map<Product, Long> products = new HashMap<>();
        Product bananas = Product.of("Banana", BigDecimal.ONE, Arrays.asList(DiscountType.THREE_FOR_THE_PRICE_OF_TWO));
        Product lime = Product.of("Lime", BigDecimal.ONE);
        products.put(bananas, 3L);
        products.put(lime, 1L);
        Order order = Order.of(products);
        when(successor.calculate(order)).thenReturn(BigDecimal.ZERO);

        //when
        BigDecimal result = threeForTwoDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(1.0), result);

    }
}