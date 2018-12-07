package com.smola;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Calculator {
    public static Klient calculateMostValuableClient(Map<Klient,Map<Produkt,Integer>> orders) {
        Map<Klient, BigDecimal> clientWithOrderSummary = orders
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().entrySet().stream().map(calculateTotalOrderPrice())
                        .max(Comparator.naturalOrder())
                        .orElse(BigDecimal.ZERO)));

        return clientWithOrderSummary.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    public static Klient calculateMostValuableClient(Map<Klient, Map<Produkt, Integer>> orders, String category) {
        Map<Klient, BigDecimal> clientWithOrderSummary = orders
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .filter(e->e.getKey().getCategory().equalsIgnoreCase(category))
                                .map(calculateTotalOrderPrice())
                                .max(Comparator.naturalOrder())
                                .orElse(BigDecimal.ZERO)));
        return clientWithOrderSummary.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    public static Map<String,BigDecimal> calculateAveragePriceForProductCategories(Map<Klient, Map<Produkt, Integer>> orders) {

        Map<String, List<Produkt>> categoriesWithProducts = groupProductsByCategories(orders);

        return categoriesWithProducts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        p->calculateAveragePrice(p.getValue())));
    }

    public static Map<String, Produkt> calculateMaxPricesForCategories(Map<Klient, Map<Produkt, Integer>> orders) {
        Map<String, List<Produkt>> categoriesWithProducts = groupProductsByCategories(orders);

        return categoriesWithProducts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,p->calculateMaxPrice(p.getValue())));
    }

    private static Produkt calculateMaxPrice(List<Produkt> singleCategoryProducts) {
        return singleCategoryProducts.stream().max(Comparator.comparing(Produkt::getPrice)).get();
    }

    private static Function<Map.Entry<Produkt, Integer>, BigDecimal> calculateTotalOrderPrice() {
        return p -> p.getKey().getPrice().multiply(BigDecimal
                .valueOf(p.getValue()));
    }

    private static BigDecimal calculateAveragePrice(List<Produkt> singleCategory) {
        return singleCategory
                .stream()
                .map(e->e.getPrice())
                .reduce(BigDecimal.ZERO,BigDecimal::add)
                .divide(BigDecimal.valueOf(singleCategory.size()));
    }
    private static Produkt calculateMinPrice(List<Produkt> singleCategory) {
        return singleCategory.stream().min(Comparator.comparing(Produkt::getPrice)).get();
    }

    private static Map<String, List<Produkt>> groupProductsByCategories(Map<Klient, Map<Produkt, Integer>> orders) {
        return orders.entrySet().stream()
                .flatMap(e -> e.getValue()
                        .keySet()
                        .stream())
                .collect(Collectors.groupingBy(p -> p.getCategory()));
    }

    public static Map<String, Produkt> calculateMinPricesForCategories(Map<Klient, Map<Produkt, Integer>> orders) {
        Map<String, List<Produkt>> categoriesWithProducts = groupProductsByCategories(orders);

        return categoriesWithProducts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,p->calculateMinPrice(p.getValue())));
    }
}
