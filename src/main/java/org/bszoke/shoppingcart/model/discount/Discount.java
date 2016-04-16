package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;

import java.math.BigDecimal;

public interface Discount {

    void setSuccessor(Discount successor);

    BigDecimal calculate(Order order);

    boolean canApply(Order order, Product product);

}
