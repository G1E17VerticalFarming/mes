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
    private Queue orderQueue;

    private String date;
    private IMesDatabase dbHandler = new DatabaseHandler();

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
        return orders;
    }
    
    @Override
    public List fetchStatuses() {
        return null;
    }
    
    @Override
    public List fetchGrowthProfiles() {
        // Test implementation
        GrowthProfile profile = new GrowthProfile();
        profile.setId(1);
        profile.setMoisture(20);
        profile.setName("Grøntsager!");
        profile.setNightTemperature(10);
        profile.setTemperature(25);
        profile.setWaterLevel(50);
        profile.setLightSequence(null);
        growthProfiles.add(profile);
        
        return growthProfiles;
    }
    
    @Override
    public List fetchDataLogs() {
        return retrievedLogs;
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
    public HashMap allocateProductionBlock() {
        return null;
    }

}
