package org.bszoke.shoppingcart.model;

import org.bszoke.shoppingcart.model.discount.DiscountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductCatalogItem {

    private final String productName;
    private final BigDecimal price;
    private final List<DiscountType> discounts;

    private ProductCatalogItem(String productName, BigDecimal price, List<DiscountType> discounts) {
        this.productName = productName;
        this.price = price;
        this.discounts = discounts;
    }

    public static ProductCatalogItem of(String productName, BigDecimal price, List<DiscountType> discounts) {
        return new ProductCatalogItem(productName, price, discounts);
    }

    public static ProductCatalogItem of(String productName, BigDecimal price) {
        return new ProductCatalogItem(productName, price, new ArrayList<>());
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<DiscountType> getDiscounts() {
        return discounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCatalogItem that = (ProductCatalogItem) o;
        return Objects.equals(productName, that.productName) &&
                Objects.equals(price, that.price) &&
                Objects.equals(discounts, that.discounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, price, discounts);
    }
}