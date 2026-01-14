package org.example.controller;

import org.example.*;
import org.example.util.ProductIdGenerator;

import java.io.*;
import java.util.List;

public class PersistenceController {
    private static final String DATA_FILE = "data_store.dat";
    //Define las los nombres para los valores del .dat
    private static class DataContainer implements Serializable {
        List<CatalogItem> products;
        List<Client> clients;
        List<Cashier> cashiers;
        List<Ticket<?>> tickets;
        //Asigna los valores a las posiciones correspondientes
        public DataContainer(List<CatalogItem> p, List<Client> c, List<Cashier> ca, List<Ticket<?>> t) {
            this.products = p;
            this.clients = c;
            this.cashiers = ca;
            this.tickets = t;
        }
    }

    // Recopila todos los datos activos y los escribe en un archivo en forma de byte para mayor comodidad a la hora de cargarlos
    public static void saveData() {
        try(ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            DataContainer data = new DataContainer(ProductController.getInstance().getProducts(), ClientController.getInstance().getClients(),
                    CashierController.getInstance().getCashiers(),
                    TicketController.tickets
            );
            write.writeObject(data);
            System.out.println("Data saved successfully.");
        }catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    //Lee el archivo y carga la informacion necesaria
    public static void loadData() {
        File file = new File(DATA_FILE);
        if(!file.exists()) return;

        try(ObjectInputStream read = new ObjectInputStream(new FileInputStream(file))) {
            DataContainer data = (DataContainer) read.readObject();

            ProductController.getInstance().setProducts(data.products);

            ClientController.getInstance().getClients().clear();
            ClientController.getInstance().getClients().addAll(data.clients);

            CashierController.getInstance().getCashiers().clear();
            CashierController.getInstance().getCashiers().addAll(data.cashiers);

            TicketController.tickets.clear();
            TicketController.tickets.addAll(data.tickets);

            recalculateGenerators(data.products);
            System.out.println("Data loaded successfully.");
        }catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void recalculateGenerators(List<CatalogItem> items) {
        int maxServiceId = 0;
        for (CatalogItem item : items) {
            if(item instanceof Product) {  //Quitar IDs del pool disponible
                try{
                    ProductIdGenerator.validateId(item.getIdAsInt());
                }catch (Exception e) { /* Ignorar si ya está ocupado*/}
            }else if (item instanceof Service) { //Buscar el ID más alto para el contador
                try{
                    String idStr = item.getId().replace("S", "");
                    int idVal = Integer.parseInt(idStr);
                    if(idVal > maxServiceId) {
                        maxServiceId = idVal;
                    }
                }catch (NumberFormatException e) { /* Ignorar */ }
            }
        }
        Service.setServiceCounter(maxServiceId + 1);
    }
}