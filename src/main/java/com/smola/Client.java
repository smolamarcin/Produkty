package com.smola;

import java.math.BigDecimal;
import java.util.Objects;

public final class Client {
    private final String name;
    private final String surname;
    private final int age;
    private final BigDecimal balance;

    public Client(String name, String surname, int age, BigDecimal balance) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return age == client.age &&
                Objects.equals(name, client.name) &&
                Objects.equals(surname, client.surname) &&
                Objects.equals(balance, client.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age, balance);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", balance=" + balance +
                '}';
    }

    public int getAge() {
        return age;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
