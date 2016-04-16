package org.bszoke.shoppingcart.model;

import java.util.Map;
import java.util.Objects;

public final class Order {

    private final Map<Product, Long> itemGroups;

    private Order(Map<Product, Long> itemGroups) {
        this.itemGroups = itemGroups;
    }

    private Order(Order order) {
        this.itemGroups = order.getItemGroups();
    }

    public static Order of(Map<Product, Long> itemGroups) {
        return new Order(itemGroups);
    }

    public static Order of(Order order) {
        return new Order(order);
    }

    public Map<Product, Long> getItemGroups() {
        return itemGroups;
    }

    public void changeProductNumberOf(Product product, long remainder) {
        itemGroups.replace(product, remainder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(itemGroups, order.itemGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemGroups);
    }
}
