package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.bszoke.shoppingcart.model.discount.OrderHelper.createOrder;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwoForOneDiscountUTest {

    private TwoForOneDiscount twoForOneDiscount;
    @Mock private Discount successor;

    @Before
    public void setUp() throws Exception {
        twoForOneDiscount = new TwoForOneDiscount();
        twoForOneDiscount.setSuccessor(successor);
    }

    @Test
    public void shouldCalculateTheDiscountWhenTheNumberOfProductsIsEven() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 2L, DiscountType.BUY_ONE_GET_ONE_FREE);

        //when
        BigDecimal result = twoForOneDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(10), result);
    }

    @Test
    public void shouldCalculateTheDiscountWhenTheNumberOfProductsIsOdd() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 3L, DiscountType.BUY_ONE_GET_ONE_FREE);
        Order unprocessedOrder = createOrder(BigDecimal.valueOf(10), 1L, DiscountType.BUY_ONE_GET_ONE_FREE);
        when(successor.calculate(unprocessedOrder)).thenReturn(BigDecimal.ZERO);

        //when
        BigDecimal result = twoForOneDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(10.0), result);
    }

    @Test
    public void shouldNotCalculateTheDiscountIfTheProductNumberIsLessThenTheMinimum() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 1L, DiscountType.BUY_ONE_GET_ONE_FREE);
        when(successor.calculate(order)).thenReturn(BigDecimal.ZERO);

        //when
        BigDecimal result = twoForOneDiscount.calculate(order);

        //then
        assertEquals(BigDecimal.valueOf(0), result);
    }

    @Test
    public void shouldDelegateToTheNextCalculatorIfCannotApplyTheDiscount() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 1L, DiscountType.BUY_ONE_GET_ONE_FREE);
        when(successor.calculate(order)).thenReturn(BigDecimal.ZERO);

        //when
        twoForOneDiscount.calculate(order);

        //then
        verify(successor).calculate(order);
    }

    @Test
    public void shouldDelegateToTheNextCalculatorIfThereAreProductsThatCanBeDiscounted() {
        //given
        Order order = createOrder(BigDecimal.valueOf(10), 3L, DiscountType.BUY_ONE_GET_ONE_FREE);
        Order unprocessedOrder = createOrder(BigDecimal.valueOf(10), 1L, DiscountType.BUY_ONE_GET_ONE_FREE);
        when(successor.calculate(unprocessedOrder)).thenReturn(BigDecimal.ZERO);

        //when
        twoForOneDiscount.calculate(order);

        //then
        verify(successor).calculate(unprocessedOrder);
    }
}