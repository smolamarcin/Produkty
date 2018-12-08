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
        List<String> lines = Parser.readLinesFromFile("src/test/resources/testFile.csv");
        assertFalse(lines.isEmpty());
        assertEquals(lines.size(),5);
    }

    @Test
    public void shouldRetrieveAllClientsFromFile() {
        List<String> lines = Parser.readLinesFromFile("src/test/resources/testFile.csv");
        List<Client> clients = Parser.retrieveClientsData(lines);

        assertEquals(5,clients.size());
        assertTrue(clients.contains(new Client("Jan","Kos",18,new BigDecimal("2000"))));
        assertTrue(clients.contains(new Client("Jan","Kos",18,new BigDecimal("2000"))));
        assertTrue(clients.contains(new Client("Milosz","Wrona",18,new BigDecimal("12850"))));
        assertTrue(clients.contains(new Client("Michal","Kowalski",18,new BigDecimal("3423"))));
        assertTrue(clients.contains(new Client("Janina","Nowak",38,new BigDecimal("3434"))));
    }

    @Test
    public void shouldRetrieveClientsWithOrders() {
        List<String> lines = Parser.readLinesFromFile("src/test/resources/testFile.csv");
        Parser.readClientsWithOrders(lines);
        Map<Client,Map<Product,Integer>> clientsWithOrders =Parser.readClientsWithOrders(lines);

        Client sampleClient = new Client("Jan","Kos",18,new BigDecimal("2000"));
        Map<Product,Integer> expectedClientProducts = new HashMap<>();
        expectedClientProducts.put(new Product("Komputer","Elektronika",new BigDecimal("3400")),4);
        expectedClientProducts.put(new Product("PanTadeusz","Ksiazka",new BigDecimal("120")),2);

        assertEquals(clientsWithOrders.size(),4);
        assertEquals(clientsWithOrders.get(sampleClient),expectedClientProducts);
    }

    @Test
    public void shouldSumOrdersFromMultipleFiles() {
        List<String> lines = Parser.readLinesFromFile("src/test/resources/testFile.csv", "src/test/resources/testFile2.csv");
        Parser.readClientsWithOrders(lines);
        Map<Client,Map<Product,Integer>> actuaClientsWithOrders =Parser.readClientsWithOrders(lines);

        Client expectedClient = new Client("Jan","Kos",18,new BigDecimal("2000"));
        Map<Product,Integer> expectedClientProdcts = new HashMap<>();
        expectedClientProdcts.put(new Product("Komputer","Elektronika",new BigDecimal("3400")),6);
        expectedClientProdcts.put(new Product("PanTadeusz","Ksiazka",new BigDecimal("120")),3);

        assertEquals(actuaClientsWithOrders.size(),4);
        assertEquals(actuaClientsWithOrders.get(expectedClient),expectedClientProdcts);
    }
}