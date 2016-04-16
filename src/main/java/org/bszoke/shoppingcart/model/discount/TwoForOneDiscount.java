package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;

import java.math.BigDecimal;

public class TwoForOneDiscount extends AbstractQuantityBasedDiscount {

    private static final int DISCOUNT_PRODUCT_NUMBER = 2;
    private static final BigDecimal DISCOUNT_MULTIPLIER = BigDecimal.valueOf(0.5);

    public TwoForOneDiscount() {
        super(DISCOUNT_PRODUCT_NUMBER);
    }

    public BigDecimal quantityBasedDiscount(Product product, Long numberOfProducts, long remainder) {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(numberOfProducts - remainder))
                .multiply(DISCOUNT_MULTIPLIER);
    }

    @Override
    public boolean canApply(Order order, Product product) {
        long numberOfProducts = order.getItemGroups().getOrDefault(product, 0L);
        return product.getDiscountTypes().contains(DiscountType.BUY_ONE_GET_ONE_FREE) && numberOfProducts >= discountProductNumber;
    }
}
