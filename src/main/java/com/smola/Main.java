package com.smola;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Zakupy zakupy = new Zakupy("zamowieniaTest.csv");
        System.out.println(zakupy);

        Client client = Calculator.calculateMostValuableClient(zakupy.getOrders());
        Client client2 = new Client("sd","sd", 1000,new BigDecimal(1200));
        System.out.println(client2);
        BigDecimal balance = client2.getBalance();
        balance.subtract(BigDecimal.valueOf(200));
        System.out.println(client);
    }
}
