package com.smola;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class ParserTest {

    @Test
    public void shouldRetrieveAllLinesFromFile() {
        List<String> lines = Parser.readLinesFromFile("zamowieniaTest.csv");
        assertFalse(lines.isEmpty());
        assertEquals(lines.size(),5);
    }

    @Test
    public void shouldRetrieveAllClientsFromFile() {
        List<String> lines = Parser.readLinesFromFile("zamowieniaTest.csv");
        List<Klient> clients = Parser.retrieveClientsData(lines);

        assertEquals(5,clients.size());
        assertTrue(clients.contains(new Klient("Jan","Kos",18,new BigDecimal("2000"))));
        assertTrue(clients.contains(new Klient("Jan","Kos",18,new BigDecimal("2000"))));
        assertTrue(clients.contains(new Klient("Milosz","Wrona",18,new BigDecimal("12850"))));
        assertTrue(clients.contains(new Klient("Michal","Kowalski",18,new BigDecimal("3423"))));
        assertTrue(clients.contains(new Klient("Janina","Nowak",38,new BigDecimal("3434"))));
    }

    @Test
    public void shouldRetrieveClientsWithOrders() {
        List<String> lines = Parser.readLinesFromFile("zamowieniaTest.csv");
        Parser.readClientsWithOrders(lines);
        Map<Klient,Map<Produkt,Integer>> clientsWithOrders =Parser.readClientsWithOrders(lines);

        Klient sampleClient = new Klient("Jan","Kos",18,new BigDecimal("2000"));
        Map<Produkt,Integer> expectedClientProducts = new HashMap<>();
        expectedClientProducts.put(new Produkt("Komputer","Elektronika",new BigDecimal("3400")),4);
        expectedClientProducts.put(new Produkt("PanTadeusz","Ksiazka",new BigDecimal("120")),2);

        assertEquals(clientsWithOrders.size(),4);
        assertEquals(clientsWithOrders.get(sampleClient),expectedClientProducts);
    }

    @Test
    public void shouldSumOrdersFromMultipleFiles() {
        List<String> lines = Parser.readLinesFromFile("zamowieniaTest.csv","zamowieniaTest2.csv");
        Parser.readClientsWithOrders(lines);
        Map<Klient,Map<Produkt,Integer>> actuaClientsWithOrders =Parser.readClientsWithOrders(lines);

        Klient expectedClient = new Klient("Jan","Kos",18,new BigDecimal("2000"));
        Map<Produkt,Integer> expectedClientProdcts = new HashMap<>();
        expectedClientProdcts.put(new Produkt("Komputer","Elektronika",new BigDecimal("3400")),6);
        expectedClientProdcts.put(new Produkt("PanTadeusz","Ksiazka",new BigDecimal("120")),3);

        assertEquals(actuaClientsWithOrders.size(),4);
        assertEquals(actuaClientsWithOrders.get(expectedClient),expectedClientProdcts);




    }
}