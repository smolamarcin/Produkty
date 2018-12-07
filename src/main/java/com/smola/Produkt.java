package com.smola;

import java.math.BigDecimal;
import java.util.Objects;

public final class Produkt {
    private final String productName;
    private final String category;
    private final BigDecimal price;

    public Produkt(String productName, String category, BigDecimal price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produkt produkt = (Produkt) o;
        return Objects.equals(productName, produkt.productName) &&
                Objects.equals(category, produkt.category) &&
                Objects.equals(price, produkt.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, category, price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Produkt{" +
                "productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }
}
