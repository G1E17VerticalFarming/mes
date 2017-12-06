/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import java.time.LocalDate;
import java.util.*;
import shared.GrowthProfile;
import shared.Light;
import shared.ProductionBlock;

/**
 *
 * @author chris
 */
public interface IMes {
    
    /**
     * Implemented fully
     *
     * Method to set date.
     * This method takes a LocalDate variable and formats it
     * to a String. The date variable in SingletonMes is then
     * set to be equal to this String.
     * @param date LocalDate selected in GUI and parsed through SingletonMes.
     */
    public void setDate(LocalDate date);
    
    /**
     * Implemented fully
     *
     * Method to fetch orders from the database for the selected date.
     * @return List consisting of order objects.
     */
    public List fetchOrders();
    
    /**
     * Not implemented
     *
     * Method to fetch all order statuses from the database.
     * @return List 
     */
    public List fetchStatuses();
    
    /**
     * Almost implemented
     *
     * Method to fetch all growth profiles from the database.
     * @return List consisting of GrowthProfile objects.
     */
    public List fetchGrowthProfiles();
    
    /**
     * Not implemented
     *
     * Method to prepare and save an order in the database.
     * @return boolean reporting on whether the call failed or not.
     */
    public boolean prepareOrder();
    
    /**
     * Not implemented
     *
     * Method to fetch all data logs from the database.
     * @param filter
     * @return List consisting of Log objects.
     */
    public List fetchDataLogs(String filter);
    
    /**
     * Implemented fully
     *
     * Method to check if login is certified.
     * @param username
     * @param password
     * @return boolean reporting on whether or not the call failed.
     */
    public boolean loginCertified(String username, String password);
    
    /**
     * Not implemented
     *
     * Method to allocate a production block for an order.
     * Calls a method in AllocationController and returns a suggested production block.
     * @return ProductionBlock object consisting of a production block ID and name.
     */
    public ProductionBlock allocateProductionBlock();
    
    public void deleteGrowthProfile(int id);
    
    public void saveGrowthProfile(GrowthProfile profileToSave);
    
    public List fetchScadaConnections();
    
    public void saveScadaConnection(String ip, int port);
    
    public void deleteScadaConnection(String scadaEntry);
    
    public List fetchLogFilters();
    
    public void saveDataLog(int block, String value);
    
    public String[] getGrowthProfileLightTypes();
    
    public void setGrowthProfileLights(GrowthProfile gp);
    
    public void setTempGrowthProfileLights(ArrayList lights);
    
    public ArrayList getTempGrowthProfileLights();
    
    public void removeTempGrowthProfileLight(int index);
    
    public Production fetchProduction(Order orderToFetchProdFor);
    
    public void addGrowthProfileLight(Light light);
    
    public Light createGrowthProfileLight(int type, int time, int value);
    
}
