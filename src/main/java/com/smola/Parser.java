package com.smola;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    static List<List<Product>> retrieveProducts(List<String> lines) {
        List<String> productsData = new ArrayList<>();
        for (String line : lines) {
            productsData.add(line.substring(line.indexOf("[") + 2, line.indexOf("]")));
        }
        List<List<Product>> products = new ArrayList<>();
        productsData.stream().map(line -> line.split(" ")).
                forEach(array -> {
                    List<Product> productsForSingleCLient = new ArrayList<>();
                    for (String s : array) {
                        String[] splitted = s.split(";");
                        productsForSingleCLient.add(new Product(splitted[0], splitted[1], new BigDecimal(splitted[2])));
                    }
                    products.add(productsForSingleCLient);
                });
        return products;
    }

    static List<String> readLinesFromFile(String... files) {
        List<String> lines = new ArrayList<>();
        for (String file : files) {
            try (Stream<String> linesStream = Files.lines(Paths.get(file))) {
                lines.addAll(linesStream.collect(Collectors.toList()));
            } catch (IOException e) {
                System.err.println("File not found!");
            }
        }
        return lines;
    }

    static List<Client> retrieveClientsData(List<String> lines) {
        List<String> clientsData = new ArrayList<>();
        for (String line : lines) {
            clientsData.add(line.substring(0, line.indexOf(" [")));
        }

        return clientsData.stream().map(line -> line.split(";"))
                .map(line -> new Client(line[0], line[1], Integer.valueOf(line[2]), new BigDecimal(line[3])))
                .collect(Collectors.toList());
    }

    static Map<Client, Map<Product, Integer>> readClientsWithOrders(List<String> lines) {
        Map<Client, Map<Product, Integer>> clientsWithOrders = new HashMap<>();
        List<Client> clients = retrieveClientsData(lines);
        List<List<Product>> products = retrieveProducts(lines);

        Map<Product, Integer> orders;
        for (int i = 0; i < products.size(); i++) {
            orders = new HashMap<>();
            Client client = clients.get(i);
            for (int j = 0; j < products.get(i).size(); j++) {
                Product key = products.get(i).get(j);
                orders.put(key, orders.getOrDefault(key, 0) + 1);
            }
            Map<Product, Integer> mergeMaps = mergeMaps(clientsWithOrders.get(client), orders);
            clientsWithOrders.put(client, mergeMaps);
        }
        return clientsWithOrders;
    }


    private static Map<Product, Integer> mergeMaps(Map<Product, Integer> existingMap, Map<Product, Integer>
            mapToMerge) {
        if (existingMap == null){
            return mapToMerge;
        }else {
            for (Product product : existingMap.keySet()) {
                Integer count = mapToMerge.containsKey(product) ? mapToMerge.get(product) : 0;
                existingMap.put(product,existingMap.get(product) + count);
            }
            mapToMerge.forEach((k,v)->existingMap.putIfAbsent(k,v));
            return existingMap;
        }


    }
}

