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
    static List<List<Produkt>> retrieveProducts(List<String> lines) {
        List<String> productsData = new ArrayList<>();
        for (String line : lines) {
            productsData.add(line.substring(line.indexOf("[") + 2, line.indexOf("]")));
        }
        List<List<Produkt>> products = new ArrayList<>();
        productsData.stream().map(line -> line.split(" ")).
                forEach(array -> {
                    List<Produkt> productsForSingleCLient = new ArrayList<>();
                    for (String s : array) {
                        String[] splitted = s.split(";");
                        productsForSingleCLient.add(new Produkt(splitted[0], splitted[1], new BigDecimal(splitted[2])));
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

    static List<Klient> retrieveClientsData(List<String> lines) {
        List<String> clientsData = new ArrayList<>();
        for (String line : lines) {
            clientsData.add(line.substring(0, line.indexOf(" [")));
        }

        return clientsData.stream().map(line -> line.split(";"))
                .map(line -> new Klient(line[0], line[1], Integer.valueOf(line[2]), new BigDecimal(line[3])))
                .collect(Collectors.toList());
    }

    static Map<Klient, Map<Produkt, Integer>> readClientsWithOrders(List<String> lines) {
        Map<Klient, Map<Produkt, Integer>> clientsWithOrders = new HashMap<>();
        List<Klient> clients = retrieveClientsData(lines);
        List<List<Produkt>> products = retrieveProducts(lines);

        Map<Produkt, Integer> orders;
        for (int i = 0; i < products.size(); i++) {
            orders = new HashMap<>();
            Klient client = clients.get(i);
            for (int j = 0; j < products.get(i).size(); j++) {
                Produkt key = products.get(i).get(j);
                orders.put(key, orders.getOrDefault(key, 0) + 1);
            }
            Map<Produkt, Integer> mergeMaps = mergeMaps(clientsWithOrders.get(client), orders);
            clientsWithOrders.put(client, mergeMaps);
        }
        return clientsWithOrders;
    }


    private static Map<Produkt, Integer> mergeMaps(Map<Produkt, Integer> existingMap, Map<Produkt, Integer>
            mapToMerge) {
        if (existingMap == null){
            return mapToMerge;
        }else {
            for (Produkt produkt : existingMap.keySet()) {
                Integer count = mapToMerge.containsKey(produkt) ? mapToMerge.get(produkt) : 0;
                existingMap.put(produkt,existingMap.get(produkt) + count);
            }
            mapToMerge.forEach((k,v)->existingMap.putIfAbsent(k,v));
            return existingMap;
        }


    }
}

