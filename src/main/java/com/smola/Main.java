package com.smola;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Zakupy zakupy = new Zakupy("zamowieniaTest.csv", "zamowieniaTest2.csv");
        System.out.println(zakupy);
        //Wyznaczanie klienta, ktory zaplacil najwiecej za zakupy
        Client mostValuableClient = Calculator.calculateMostValuableClient(zakupy.getOrders());
        System.out.println(mostValuableClient);
        //Wyznacz klienta, ktory zaplacil najwiecej za zakupy w danej kategorii
        Client mostValuableCLientInCategory = Calculator.calculateMostValuableClient(zakupy.getOrders(), "ksiazka");
        System.out.println(mostValuableCLientInCategory);
        //Wykonaj zestawienie (mape), w ktorej pokazesz kategorie produktow, ktore najchetniej w tym wieku kupowano
        Map<String, Integer> agesOfClientsForEachCategory = Calculator
                .findAgesOfClientsForEachCategory(zakupy.getOrders());
        System.out.println(agesOfClientsForEachCategory);
        //Wykonaj zestawienie(mape), w ktorej pokazesz srednia cene produktow w danej kategorii
        // Dodatkowo wyznacz dla kazdej kategorii produkt najtanszy oraz najdrozszy.
        Map<String, BigDecimal> averagePriceForProductCategories = Calculator
                .calculateAveragePriceForProductCategories(zakupy.getOrders());
        System.out.println(averagePriceForProductCategories);
        //Wyznacz klientow, ktorzy najczesciej kupowali produkty danej kategorii
        Map<String, Client> mostActiveClientsForEachCategory = Calculator
                .findMostActiveClientsForEachCategory(zakupy.getOrders());
        System.out.println(mostActiveClientsForEachCategory);
        //Sprawdz, czy klient jest w stanie zaplacic za zakupy.
        // Mapa z pogrupowanymi ludzmi (sa w stanie zaplacic lub nie)
        Map<Boolean, List<Client>> clientAbleToPayForOrders = Calculator
                .areClientAbleToPayForOrders(zakupy.getOrders());
        System.out.println(clientAbleToPayForOrders);
        // Czy konkretny klient jest w stanie zaplacic
        Client client = new Client("Janina", "Nowak", 38, new BigDecimal(3434));
        boolean ableToPay = Calculator.isClientAbleToPay(client, zakupy.getOrders());
        System.out.println(ableToPay);
        // Wykonaj mape, w ktorej jako klucz podasz klienta, a jako wartosc jego dlug
        Map<Client, BigDecimal> mapWithCLientsDebt = Calculator.createMapWithCLientsDebt(zakupy.getOrders());
        System.out.println(mapWithCLientsDebt);
    }
}
