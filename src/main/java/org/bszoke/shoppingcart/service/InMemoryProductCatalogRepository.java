package org.bszoke.shoppingcart.service;

import org.bszoke.shoppingcart.model.ProductCatalogItem;
import org.bszoke.shoppingcart.model.discount.DiscountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.bszoke.shoppingcart.model.discount.DiscountType.*;

public class InMemoryProductCatalogRepository implements ProductCatalogRepository {

    private final List<ProductCatalogItem> items;

    public InMemoryProductCatalogRepository() {
        this.items = new ArrayList<>();
        items.add(ProductCatalogItem.of("Apple", BigDecimal.valueOf(35), Arrays.asList(BUY_ONE_GET_ONE_FREE)));
        items.add(ProductCatalogItem.of("Banana", BigDecimal.valueOf(20), Arrays.asList(THREE_FOR_THE_PRICE_OF_TWO)));
        items.add(ProductCatalogItem.of("Melon", BigDecimal.valueOf(50)));
        items.add(ProductCatalogItem.of("Lime", BigDecimal.valueOf(15)));
        items.add(ProductCatalogItem.of("Pineapples", BigDecimal.valueOf(10), Arrays.asList(BUY_ONE_GET_ONE_FREE, THREE_FOR_THE_PRICE_OF_TWO)));
    }

    @Override
    public Optional<ProductCatalogItem> findByName(String productName) {
        Optional<ProductCatalogItem> item = items.stream()
                .filter(productCatalogItem -> productCatalogItem.getProductName().equals(productName))
                .findFirst();
        return item;
    }
}