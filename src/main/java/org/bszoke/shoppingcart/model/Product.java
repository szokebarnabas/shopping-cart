package org.bszoke.shoppingcart.model;

import org.bszoke.shoppingcart.model.discount.DiscountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product {

    private final String name;
    private final BigDecimal price;
    private final List<DiscountType> discountTypes;

    private Product(String name, BigDecimal price, List<DiscountType> discountTypes) {
        this.name = name;
        this.price = price;
        this.discountTypes = discountTypes;
    }

    public static Product of(String name, BigDecimal price, List<DiscountType> discountTypes) {
        return new Product(name, price, discountTypes);
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price, new ArrayList<>());
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(discountTypes, product.discountTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, discountTypes);
    }

    public List<DiscountType> getDiscountTypes() {
        return discountTypes;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", discountTypes=" + discountTypes +
                '}';
    }
}
