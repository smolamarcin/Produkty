package com.smola;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Calculator {
    static Client calculateMostValuableClient(Map<Client, Map<Product, Integer>> orders) {
        Map<Client, BigDecimal> clientsWithSummaryOrder = orders.entrySet()
                .stream()
                .collect(Collectors.toMap(singleEntry -> singleEntry.getKey()
                        , map -> sumOrders(map.getValue())));

        return clientsWithSummaryOrder.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();
    }

    static Client calculateMostValuableClient(Map<Client, Map<Product, Integer>> orders, String category) {
        Map<Client, BigDecimal> clientWithOrderSummary = orders
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .filter(e -> e.getKey().getCategory().equalsIgnoreCase(category))
                                .map(calculateTotalOrderPrice())
                                .max(Comparator.naturalOrder())
                                .orElse(BigDecimal.ZERO)));
        return clientWithOrderSummary.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get()
                .getKey();
    }

    static Map<String, BigDecimal> calculateAveragePriceForProductCategories(Map<Client, Map<Product, Integer>>
                                                                                     orders) {

        Map<String, List<Product>> categoriesWithProducts = groupProductsByCategories(orders);

        return categoriesWithProducts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        p -> calculateAveragePrice(p.getValue())));
    }

    static Map<String, Product> calculateMaxPricesForCategories(Map<Client, Map<Product, Integer>> orders) {
        Map<String, List<Product>> categoriesWithProducts = groupProductsByCategories(orders);

        return categoriesWithProducts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, p -> calculateMaxPrice(p.getValue())));
    }

    static Map<String, Client> findMostActiveClientsForEachCategory(Map<Client, Map<Product, Integer>> orders) {
        Map<String, Map<Client, Integer>> categoryWithClientOrders = new HashMap<>();

        for (Map.Entry<Client, Map<Product, Integer>> entry : orders.entrySet()) {
            Set<Map.Entry<Product, Integer>> entries = entry.getValue().entrySet();
            for (Map.Entry<Product, Integer> productIntegerEntry : entries) {
                Map<Client, Integer> clientOrders = new HashMap<>();
                clientOrders.put(entry.getKey(), productIntegerEntry.getValue());
                String category = productIntegerEntry.getKey().getCategory();
                updateCategoryWithClientOrders(category, clientOrders, categoryWithClientOrders);
            }
        }

        return categoryWithClientOrders.entrySet().stream()
                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey()));
    }

    static Map<String, Product> calculateMinPricesForCategories(Map<Client, Map<Product, Integer>> orders) {
        Map<String, List<Product>> categoriesWithProducts = groupProductsByCategories(orders);

        return categoriesWithProducts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, p -> calculateMinPrice(p.getValue())));
    }

    static Map<String, Integer> findAgesOfClientsForEachCategory(Map<Client, Map<Product, Integer>> orders) {
        Map<String, Map<Client, Integer>> categoryWithClientOrders = new HashMap<>();

        for (Map.Entry<Client, Map<Product, Integer>> entry : orders.entrySet()) {
            Set<Map.Entry<Product, Integer>> entries = entry.getValue().entrySet();
            for (Map.Entry<Product, Integer> productIntegerEntry : entries) {
                Map<Client, Integer> singleClientOrders = new HashMap<>();
                singleClientOrders.put(entry.getKey(), productIntegerEntry.getValue());
                String category = productIntegerEntry.getKey().getCategory();
                updateCategoryWithClientOrders(category, singleClientOrders, categoryWithClientOrders);
            }
        }

        return categoryWithClientOrders.entrySet().stream()
                .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().entrySet().stream().max(Comparator.comparing(Map.Entry::getValue))
                        .get()
                        .getKey()
                        .getAge()));

    }

    static Map<Client, BigDecimal> createMapWithCLientsDebt(Map<Client, Map<Product, Integer>> orders) {
        Map<Client, BigDecimal> clientsWithSummaryOrder = orders.entrySet()
                .stream()
                .collect(Collectors.toMap(singleEntry -> singleEntry.getKey()
                        , map -> sumOrders(map.getValue())));

        return clientsWithSummaryOrder.entrySet()
                .stream()
                .collect(Collectors.toMap(e->e.getKey(),e->calculateDebt(e)));

    }

    static Map<Boolean, List<Client>> areClientAbleToPayForOrders(Map<Client, Map<Product, Integer>> orders) {
        Map<Client, BigDecimal> clientsWithSummaryOrder = orders.entrySet()
                .stream()
                .collect(Collectors.toMap(singleEntry -> singleEntry.getKey()
                        , map -> sumOrders(map.getValue())));

        return clientsWithSummaryOrder.entrySet()
                .stream()
                .collect(Collectors
                        .groupingBy(e -> isAbleToPay(e), Collectors.mapping(e -> e.getKey(), Collectors.toList())));
    }

    private static BigDecimal sumOrders(Map<Product, Integer> clientOrders) {
        return clientOrders.entrySet()
                .stream()
                .map(e -> calculateProductsPrice(e))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private static BigDecimal calculateProductsPrice(Map.Entry<Product, Integer> product) {
        return product.getKey().getPrice().multiply(BigDecimal.valueOf(product.getValue()));
    }

    private static Product calculateMaxPrice(List<Product> singleCategoryProducts) {
        return singleCategoryProducts.stream().max(Comparator.comparing(Product::getPrice)).get();
    }

    private static Function<Map.Entry<Product, Integer>, BigDecimal> calculateTotalOrderPrice() {
        return p -> p.getKey().getPrice().multiply(BigDecimal
                .valueOf(p.getValue()));
    }

    private static BigDecimal calculateAveragePrice(List<Product> singleCategory) {
        return singleCategory
                .stream()
                .map(e -> e.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(singleCategory.size()), RoundingMode.HALF_UP);
    }

    private static Product calculateMinPrice(List<Product> singleCategory) {
        return singleCategory.stream().min(Comparator.comparing(Product::getPrice)).get();
    }

    private static Map<String, List<Product>> groupProductsByCategories(Map<Client, Map<Product, Integer>> orders) {
        return orders.entrySet().stream()
                .flatMap(e -> e.getValue()
                        .keySet()
                        .stream())
                .collect(Collectors.groupingBy(p -> p.getCategory()));
    }

    private static void updateCategoryWithClientOrders(String category, Map<Client, Integer> singleClientOrder, Map
            <String, Map<Client, Integer>> categoryWithClientOrders) {
        if (categoryWithClientOrders.containsKey(category)) {
            Map<Client, Integer> actualMap = categoryWithClientOrders.get(category);
            actualMap.putAll(singleClientOrder);
        } else {
            categoryWithClientOrders.put(category, singleClientOrder);
        }
    }


    private static boolean isAbleToPay(Map.Entry<Client, BigDecimal> clientWithOrderPrice) {
        BigDecimal totalPrice = clientWithOrderPrice.getValue();
        BigDecimal balance = clientWithOrderPrice.getKey().getBalance();
        return balance.compareTo(totalPrice) > 0;
    }

    private static BigDecimal calculateDebt(Map.Entry<Client, BigDecimal> entry) {
        Client client = entry.getKey();
        BigDecimal orderSUmmary = entry.getValue();
        return client.getBalance().subtract(orderSUmmary);
    }
}
