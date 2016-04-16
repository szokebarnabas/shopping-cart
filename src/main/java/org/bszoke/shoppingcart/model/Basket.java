package org.bszoke.shoppingcart.model;


import org.bszoke.shoppingcart.model.discount.DiscountType;
import org.bszoke.shoppingcart.service.ProductCatalogRepository;
import org.bszoke.shoppingcart.service.TotalCostCalculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Basket {

    private Order order;
    private final ProductCatalogRepository productCatalogRepository;
    private final TotalCostCalculator totalCostCalculator;

    public Basket(TotalCostCalculator totalCostCalculator, ProductCatalogRepository productCatalogRepository) {
        this.totalCostCalculator = totalCostCalculator;
        this.productCatalogRepository = productCatalogRepository;
    }

    public void createOrder(List<String> productNames) {
        if (productNames == null) {
            throw new IllegalArgumentException("The product list cannot be null.");
        }

        Map<Product, Long> itemGroups = productNames.stream()
                .map(name -> toProduct(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        order = Order.of(itemGroups);
    }

    private Optional<Product> toProduct(String name) {
        Optional<Product> product = Optional.empty();
        Optional<ProductCatalogItem> itemOptional = productCatalogRepository.findByName(name);
        if (itemOptional.isPresent()) {
            ProductCatalogItem item = itemOptional.get();
            List<DiscountType> discounts = item.getDiscounts();
            BigDecimal price = item.getPrice();
            product = Optional.of(Product.of(name, price, discounts));
        }
        return product;
    }

    public BigDecimal totalCost() {
        return totalCostCalculator.calculateTotalCost(order);
    }

    public Order getOrder() {
        return order;
    }
}
