package org.bszoke.shoppingcart.service;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;
import org.bszoke.shoppingcart.model.discount.Discount;
import org.bszoke.shoppingcart.model.discount.DiscountRuleFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class TotalCostCalculatorImpl implements TotalCostCalculator {

    private final Discount rootDiscount;

    public TotalCostCalculatorImpl(DiscountRuleFactory discountRuleFactory) {
        this.rootDiscount = discountRuleFactory.createRuleChain();
    }

    private final ToDoubleFunction<Map.Entry<Product, Long>> summingFunc = entry -> {
        final Product product = entry.getKey();
        final Long numberOfProductsInTheGroup = entry.getValue();
        BigDecimal unitPrice = product.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(numberOfProductsInTheGroup));
        return totalPrice.doubleValue();
    };

    @Override
    public BigDecimal calculateTotalCost(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("The order cannot be null");
        }

        Map<Product, Long> productGroups = order.getItemGroups();

        Double summedCost = productGroups
                .entrySet()
                .stream()
                .collect(Collectors.summingDouble(summingFunc));

        BigDecimal totalDiscount = rootDiscount.calculate(Order.of(order));

        return BigDecimal.valueOf(summedCost).subtract(totalDiscount);
    }
}
