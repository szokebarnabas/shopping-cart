package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;

import java.math.BigDecimal;

public class ThreeForTwoDiscount extends AbstractQuantityBasedDiscount {

    private static final int DISCOUNT_PRODUCT_NUMBER = 3;
    private static final BigDecimal DISCOUNT_MULTIPLIER = BigDecimal.valueOf(0.333);

    public ThreeForTwoDiscount() {
        super(DISCOUNT_PRODUCT_NUMBER);
    }

    @Override
    public BigDecimal quantityBasedDiscount(Product product, Long numberOfProducts, long remainder) {
        return product
                .getPrice()
                .multiply(BigDecimal.valueOf(numberOfProducts - remainder))
                .multiply(DISCOUNT_MULTIPLIER);
    }

    @Override
    public boolean canApply(Order order, Product product) {
        long numberOfProducts = order.getItemGroups().getOrDefault(product, 0L);
        return product.getDiscountTypes().contains(DiscountType.THREE_FOR_THE_PRICE_OF_TWO) && numberOfProducts >= discountProductNumber;
    }
}
