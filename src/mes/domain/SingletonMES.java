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

/**
 *
 * @author chris
 */
public class SingletonMES implements IMes {

    private List orders;
    private List growthProfiles;
    private List retrievedLogs;
    private Queue orderQueue;

    private String date;
    private IMesDatabaseFacade dbHandler = new DatabaseHandler();

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

    public List fetchOrders() {
        this.orders = dbHandler.fetchOrders(date);
        return orders;
    }

    public void sendOrderToPreparation(Order order) {

    }

    public boolean prepareOrder() {
        // Call DB handler
        return true;
    }

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

}
