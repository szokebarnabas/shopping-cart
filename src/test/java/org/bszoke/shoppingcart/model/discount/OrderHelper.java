package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;
import org.bszoke.shoppingcart.model.discount.DiscountType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;


class OrderHelper {

    public static Order createOrder(BigDecimal unitPrice, long numberOfProducts, DiscountType... discounts) {
        HashMap<Product, Long> itemGroups = new HashMap<>();
        Product product = Product.of("product", unitPrice, Arrays.asList(discounts));
        itemGroups.put(product, numberOfProducts);
        return Order.of(itemGroups);
    }
}
