package com.smola;

import java.util.*;

public class Zakupy {
    Map<Client,Map<Product,Integer>> orders;

    public Zakupy(String... files) {
        fillMap(files);
    }

    private void fillMap(String[] files) {
        List<String> lines = Parser.readLinesFromFile(files);
        orders=  Parser.readClientsWithOrders(lines);
    }


}
