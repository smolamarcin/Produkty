package com.smola;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.testng.Assert.*;

public class CalculatorTest {
    Map<Client, Map<Product, Integer>> orders;

    @BeforeClass
    public void setUp() {
        orders = new HashMap<>();
        Client sampleClientOne = new Client("Jan", "Kos", 18, new BigDecimal("20000"));
        Map<Product, Integer> sampleCLientOneProducts = new HashMap<>();
        sampleCLientOneProducts.put(new Product("Komputer", "Elektronika", new BigDecimal("3400")), 6);
        sampleCLientOneProducts.put(new Product("PanTadeusz", "Ksiazka", new BigDecimal("120")), 1);
        Client sampleClientTwo = new Client("Jan", "Kowalski", 19, new BigDecimal("1000"));
        Map<Product, Integer> sampleCLientTwoProducts = new HashMap<>();
        sampleCLientTwoProducts.put(new Product("Potop", "Ksiazka", new BigDecimal("300")), 1);
        sampleCLientTwoProducts.put(new Product("PanTadeusz", "Ksiazka", new BigDecimal("120")), 1);
        sampleCLientTwoProducts.put(new Product("Tablet", "Elektronika", new BigDecimal("1000")), 3);


        orders.put(sampleClientOne, sampleCLientOneProducts);
        orders.put(sampleClientTwo, sampleCLientTwoProducts);
    }

    @Test
    public void calculateClientWhoPaidTheMost() {
        Client actualClient = Calculator.calculateMostValuableClient(orders);
        assertEquals(actualClient, new Client("Jan", "Kos", 18, new BigDecimal("20000")));
    }


    @Test
    public void calculateCLientWhoPaidTheMost_inGivenCategory() {
        Client actualClient_Books = Calculator.calculateMostValuableClient(orders, "Ksiazka");
        Client actualClient_Electronic = Calculator.calculateMostValuableClient(orders, "Elektronika");

        assertEquals(actualClient_Books, new Client("Jan", "Kowalski", 19, new BigDecimal("1000")));
        assertEquals(actualClient_Electronic, new Client("Jan", "Kos", 18, new BigDecimal("20000")));
    }

    @Test
    public void shouldCreateMapWithAveragePriceForEachCategory() {
        Map<String, BigDecimal> expectedPricesWithCategories = new HashMap<>();
        expectedPricesWithCategories.put("Elektronika", BigDecimal.valueOf(2200));
        expectedPricesWithCategories.put("Ksiazka", BigDecimal.valueOf(180));

        Map<String, BigDecimal> actualMap = Calculator.calculateAveragePriceForProductCategories(orders);

        assertEquals(actualMap, expectedPricesWithCategories);
    }

    @Test
    public void shouldCreateMap_withMostExpensiveProducts_forEachCategory() {
        Map<String, Product> expectedMaxPricesWithCategories = new HashMap<>();
        expectedMaxPricesWithCategories.put("Elektronika", new Product("Komputer", "Elektronika", new BigDecimal("3400")));
        expectedMaxPricesWithCategories.put("Ksiazka", new Product("Potop", "Ksiazka", new BigDecimal("300")));

        Map<String, Product> actualMap = Calculator.calculateMaxPricesForCategories(orders);
        assertEquals(actualMap, expectedMaxPricesWithCategories);
    }

    @Test
    public void shouldCreateMap_withCheapestProducts_forEachCategory() {
        Map<String, Product> expectedMinPricesWithCategories = new HashMap<>();
        expectedMinPricesWithCategories.put("Elektronika", new Product("Tablet", "Elektronika", new BigDecimal("1000")));
        expectedMinPricesWithCategories.put("Ksiazka", new Product("PanTadeusz", "Ksiazka", new BigDecimal("120")));

        Map<String, Product> actualMap = Calculator.calculateMinPricesForCategories(orders);
        assertEquals(actualMap, expectedMinPricesWithCategories);
    }

    @Test
    public void findMostActiveClients_forEachCategory() {
        Map<String, Client> actualMap = Calculator.findMostActiveClientsForEachCategory(orders);

        Map<String,Client> expectedMap = new HashMap<>();
        expectedMap.put( "Elektronika",new Client("Jan", "Kos", 18, new BigDecimal("20000")));
        expectedMap.put( "Ksiazka",new Client("Jan", "Kowalski", 19, new BigDecimal("1000")));

        assertEquals(actualMap, expectedMap);
    }

    @Test
    public void findAgeOfMostActiveCLient_forEachCategory() {
        Map<String,Integer> actualMap = Calculator.findAgesOfClientsForEachCategory(orders);

        Map<String,Integer> expectedMap = new HashMap<>();
        expectedMap.put( "Elektronika",18);
        expectedMap.put( "Ksiazka",19);

        assertEquals(actualMap,expectedMap);
    }

    @Test
    public void areClientAbleToPayForOrders() {
        Map<Boolean,List<Client>> actualMap = Calculator.areClientAbleToPayForOrders(orders);

        Map<Boolean,List<Client>> expectedMap = new HashMap<>();
        expectedMap.put( false,Arrays.asList(new Client("Jan", "Kowalski", 19, new BigDecimal("1000")),
                new Client("Jan", "Kos", 18, new BigDecimal("20000"))));


        assertEquals(actualMap,expectedMap);
    }

    @Test
    public void shouldCreateMapWithClientsDebt() {
        Map<Client,BigDecimal> actualMap = Calculator.createMapWithCLientsDebt(orders);

        Map<Client,BigDecimal> expectedMap = new HashMap<>();
        expectedMap.put((new Client("Jan", "Kos", 18, new BigDecimal("20000"))),BigDecimal.valueOf(-520));
        expectedMap.put((new Client("Jan", "Kowalski", 19, new BigDecimal("1000"))),BigDecimal.valueOf(-2420));


        assertEquals(actualMap,expectedMap);    }
}