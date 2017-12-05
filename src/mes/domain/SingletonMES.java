/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import mes.persistence.DatabaseHandler;
import shared.*;

/**
 *
 * @author chris
 */
public class SingletonMES implements IMes {

    private List orders;
    private List growthProfiles = new ArrayList<>();
    private List retrievedLogs;
    private List scadaConnections;
    private Queue orderQueue;

    private String date;
    private IMesDatabase dbHandler = DatabaseHandler.getInstance();

    private static SingletonMES instance = null;

    protected SingletonMES() {
        // Is here to prevent instantiation
    }

    public static SingletonMES getInstance() {
        if (instance == null) {
            instance = new SingletonMES();
        }
        return instance;
    }
    
    @Override
    public boolean loginCertified(String username, String password) {
        // Just a dummy implementation
        return username.equals("admin") && password.equals("123");
    }

    @Override
    public List fetchOrders() {
        this.orders = dbHandler.fetchOrders(date);
        return this.orders;
    }
    
    @Override
    public List fetchStatuses() {
        return dbHandler.getOrderStatuses();
    }
    
    @Override
    public List fetchGrowthProfiles() {
        this.growthProfiles = dbHandler.getGrowthProfiles();
        
        return this.growthProfiles;
    }
    
    @Override
    public List fetchScadaConnections() {
        this.scadaConnections = dbHandler.getScadaEntries();
        
        return this.scadaConnections;
    }
    
    @Override
    public List fetchDataLogs(String filter) {
        this.retrievedLogs = dbHandler.getDataLogs(filter);
        return this.retrievedLogs;
    }

    @Override
    public boolean prepareOrder() {
        // Call DB handler
        System.out.println("Prepare order called");
        return true;
    }

    @Override
    public void setDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LLLL-YYYY");
        
        // Just some error handling, to prevent a date being null
        if (date != null) {
            this.date = date.format(formatter);
        } else {
            LocalDate defaultDate = LocalDate.now();
            this.date = defaultDate.format(formatter);
        }
    }

    @Override
    public ProductionBlock allocateProductionBlock() {
        return null;
    }
    
    @Override
    public void deleteGrowthProfile(int id) {
        dbHandler.deleteGrowthProfile(id);
    }
    
    @Override
    public void saveGrowthProfile(GrowthProfile profileToSave) {
        dbHandler.saveGrowthProfile(profileToSave);
    } 
    
    @Override
    public void saveScadaConnection(String ip, int port) {
        dbHandler.saveScadaEntry(ip, port);
    }
    
}
