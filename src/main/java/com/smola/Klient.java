package com.smola;

import java.math.BigDecimal;
import java.util.Objects;

public final class Klient {
    private final String name;
    private final String surname;
    private final int age;
    private final BigDecimal balance;

    public Klient(String name, String surname, int age, BigDecimal balance) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.balance = balance;
    }

    public Klient(String name, String surname, Integer age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.balance = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Klient klient = (Klient) o;
        return age == klient.age &&
                Objects.equals(name, klient.name) &&
                Objects.equals(surname, klient.surname) &&
                Objects.equals(balance, klient.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age, balance);
    }

    @Override
    public String toString() {
        return "Klient{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", balance=" + balance +
                '}';
    }
}
