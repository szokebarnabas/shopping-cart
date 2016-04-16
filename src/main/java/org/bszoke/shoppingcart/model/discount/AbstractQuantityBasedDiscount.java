package org.bszoke.shoppingcart.model.discount;

import org.bszoke.shoppingcart.model.Order;
import org.bszoke.shoppingcart.model.Product;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.*;

abstract class AbstractQuantityBasedDiscount implements Discount {

    private final static MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);
    private Discount successor;
    final long discountProductNumber;

    AbstractQuantityBasedDiscount(long discountProductNumber) {
        this.discountProductNumber = discountProductNumber;
    }

    public void setSuccessor(Discount successor) {
        this.successor = successor;
    }

    public BigDecimal calculate(Order order) {
        Map<Product, Long> itemGroups = order.getItemGroups();
        Optional<BigDecimal> totalDiscount = itemGroups.entrySet()
                .stream()
                .map(discountChainCalculatorFunc(order))
                .reduce((x, y) -> x.add(y));
        return totalDiscount.get();
    }

    private Function<Map.Entry<Product, Long>, BigDecimal> discountChainCalculatorFunc(Order order) {
        return entry -> {
            Product product = entry.getKey();
            Long numberOfProducts = entry.getValue();

            if (canApply(order, product)) {
                long remainder = numberOfProducts % discountProductNumber;
                BigDecimal rootDiscount = quantityBasedDiscount(product, numberOfProducts, remainder);
                if (remainder > 0 && successor != null) {
                    order.changeProductNumberOf(product, remainder);
                    BigDecimal discountOfSuccessor = successor.calculate(order);
                    return rootDiscount.add(discountOfSuccessor);
                } else {
                    order.changeProductNumberOf(product, 0);
                    return rootDiscount.round(MATH_CONTEXT);
                }
            } else if (successor != null) {
                return successor.calculate(order);
            }
            return BigDecimal.ZERO;
        };
    }

    protected abstract BigDecimal quantityBasedDiscount(Product product, Long numberOfProducts, long remainder);

}
