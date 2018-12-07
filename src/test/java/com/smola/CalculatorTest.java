package com.smola;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

public class CalculatorTest {
    Map<Klient,Map<Produkt,Integer>> orders;
    @BeforeClass
    public void setUp() {
        orders = new HashMap<>();
        Klient sampleClientOne = new Klient("Jan","Kos",18,new BigDecimal("20000"));
        Map<Produkt,Integer> sampleCLientOneProducts = new HashMap<>();
        sampleCLientOneProducts.put(new Produkt("Komputer","Elektronika",new BigDecimal("3400")),6);
        sampleCLientOneProducts.put(new Produkt("PanTadeusz","Ksiazka",new BigDecimal("120")),1);

        Klient sampleClientTwo = new Klient("Jan","Kowalski",19,new BigDecimal("1000"));
        Map<Produkt,Integer> sampleCLientTwoProducts = new HashMap<>();
        sampleCLientTwoProducts.put(new Produkt("Potop","Ksiazka",new BigDecimal("300")),1);
        sampleCLientTwoProducts.put(new Produkt("PanTadeusz","Ksiazka",new BigDecimal("120")),1);
        sampleCLientTwoProducts.put(new Produkt("Tablet","Elektronika",new BigDecimal("1000")),3);


        orders.put(sampleClientOne,sampleCLientOneProducts);
        orders.put(sampleClientTwo,sampleCLientTwoProducts);

    }

    @Test
    public void calculateClientWhoPaidTheMost() {
        Klient actualClient = Calculator.calculateMostValuableClient(orders);
        assertEquals(actualClient,new Klient("Jan","Kos",18,new BigDecimal("20000")));
    }

    @Test
    public void calculateCLientWhoPaidTheMost_inGivenCategory() {
        Klient actualClient_Books = Calculator.calculateMostValuableClient(orders,"Ksiazka");
        Klient actualClient_Electronic = Calculator.calculateMostValuableClient(orders,"Elektronika");

        assertEquals(actualClient_Books,new Klient("Jan","Kowalski",19,new BigDecimal("1000")));
        assertEquals(actualClient_Electronic,new Klient("Jan","Kos",18,new BigDecimal("20000")));
    }

    @Test
    public void shouldCreateMapWithAveragePriceForEachCategory() {
        Map<String,BigDecimal> expectedPricesWithCategories = new HashMap<>();
        expectedPricesWithCategories.put("Elektronika",BigDecimal.valueOf(2200));
        expectedPricesWithCategories.put("Ksiazka",BigDecimal.valueOf(180));

        Map<String, BigDecimal> actualMap = Calculator.calculateAveragePriceForProductCategories(orders);

        assertEquals(actualMap,expectedPricesWithCategories);
    }

    @Test
    public void shouldCreateMap_withMostExpensiveProducts_forEachCategory() {
        Map<String,Produkt> expectedMaxPricesWithCategories = new HashMap<>();
        expectedMaxPricesWithCategories.put("Elektronika",new Produkt("Komputer","Elektronika",new BigDecimal("3400")));
        expectedMaxPricesWithCategories.put("Ksiazka",new Produkt("Potop","Ksiazka",new BigDecimal("300")));

        Map<String,Produkt> actualMap = Calculator.calculateMaxPricesForCategories(orders);
        assertEquals(actualMap,expectedMaxPricesWithCategories);
    }

    @Test
    public void shouldCreateMap_withCheapestProducts_forEachCategory() {
        Map<String,Produkt> expectedMinPricesWithCategories = new HashMap<>();
        expectedMinPricesWithCategories.put("Elektronika",new Produkt("Tablet","Elektronika",new BigDecimal("1000")));
        expectedMinPricesWithCategories.put("Ksiazka",new Produkt("PanTadeusz","Ksiazka",new BigDecimal("120")));

        Map<String,Produkt> actualMap = Calculator.calculateMinPricesForCategories(orders);
        assertEquals(actualMap,expectedMinPricesWithCategories);
    }
}