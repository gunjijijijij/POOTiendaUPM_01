package org.example.controller;

import org.example.*;
import org.example.strategy.CompanyCombinedPrinter;
import org.example.strategy.CompanyServicePrinter;
import org.example.strategy.ITicketPrinter;
import org.example.strategy.StandardPrinter;
import org.example.util.ProductIdGenerator;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.example.util.TicketIdGenerator;
import org.example.util.Utils;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersistenceController {
    private static final String DATA_FILE = "data_store.dat";
    private static final Kryo kryo = new Kryo();

    public static void register() {
        kryo.register(DataContainer.class);
        kryo.register(Product.class);
        kryo.register(Service.class);
        kryo.register(Client.class);
        kryo.register(Cashier.class);
        kryo.register(Ticket.class);
        kryo.register(CommonTicket.class);
        kryo.register(CompanyTicket.class);
        kryo.register(PeopleProduct.class);
        kryo.register(FoodProduct.class);
        kryo.register(MeetingProduct.class);
        kryo.register(TicketIdGenerator.class);
        kryo.register(ProductIdGenerator.class);
        kryo.register(Utils.class);
        kryo.register(CatalogItem.class);
        kryo.register(Category.class);
        kryo.register(User.class);
        kryo.register(CashierController.class);
        kryo.register(ClientController.class);
        kryo.register(ProductController.class);
        kryo.register(TicketController.class);
        kryo.register(IndividualClient.class);
        kryo.register(CompanyClient.class);
        kryo.register(ArrayList.class);
        kryo.register(StandardPrinter.class);
        kryo.register(CompanyServicePrinter.class);
        kryo.register(ITicketPrinter.class);
        kryo.register(CompanyCombinedPrinter.class);
        kryo.register(Ticket.Status.class);
        kryo.register(CustomProduct.class);
        kryo.register(ServiceTicket.class);
        kryo.register(LocalDate.class);
        kryo.register(PersistenceController.class);
    }
    
    //Define las los nombres para los valores del .dat
    private static class DataContainer {
        DataContainer(){}
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
        register();
        try(Output output = new Output(new FileOutputStream(DATA_FILE))) {
            DataContainer data = new DataContainer(ProductController.getInstance().getProducts(), ClientController.getInstance().getClients(),
                    CashierController.getInstance().getCashiers(),
                    TicketController.tickets
            );

            kryo.writeObject(output, data);
            System.out.println("Data saved successfully.");
        }catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    //Lee el archivo y carga la informacion necesaria
    public static void loadData() {
        register();
        File file = new File(DATA_FILE);
        if(!file.exists()) return;

        try(Input input = new Input(new FileInputStream(file))) {
            DataContainer data = kryo.readObject(input, DataContainer.class);

            ProductController.getInstance().setProducts(data.products);

            ClientController.getInstance().getClients().clear();
            ClientController.getInstance().getClients().addAll(data.clients);

            CashierController.getInstance().getCashiers().clear();
            CashierController.getInstance().getCashiers().addAll(data.cashiers);

            TicketController.tickets.clear();
            TicketController.tickets.addAll(data.tickets);

            recalculateGenerators(data.products);
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
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