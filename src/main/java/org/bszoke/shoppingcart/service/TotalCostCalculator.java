package org.bszoke.shoppingcart.service;

import org.bszoke.shoppingcart.model.Order;

import java.math.BigDecimal;

public interface TotalCostCalculator {

    BigDecimal calculateTotalCost(Order order);
}