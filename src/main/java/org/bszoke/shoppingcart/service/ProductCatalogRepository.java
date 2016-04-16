package org.bszoke.shoppingcart.service;

import org.bszoke.shoppingcart.model.ProductCatalogItem;
import org.bszoke.shoppingcart.model.discount.DiscountType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductCatalogRepository {

    Optional<ProductCatalogItem> findByName(String productName);
}
