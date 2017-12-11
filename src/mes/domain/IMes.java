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
     * Implemented fully
     *
     * Method to fetch all order statuses from the database.
     * @return List containing order statuses
     */
    public List<Status> fetchStatuses();
    
    /**
     * Implemented fully
     *
     * Method to fetch all growth profiles from the database.
     * @return List consisting of GrowthProfile objects.
     */
    public List fetchGrowthProfiles();
    
    /**
     * Not implemented
     *
     * Method to prepare and save an order in the database.
     * @param currentOrder
     * @param status
     * @param growthProfile
     * @param prodBlock
     * @return boolean reporting on whether the call failed or not.
     */
    public boolean prepareOrder(Order currentOrder, Status status, GrowthProfile growthProfile, int prodBlock);
    
    /**
     * Implemented fully
     *
     * Method to fetch all data logs from the database.
     * @param filter 
     * @return List consisting of Log objects.
     */
    public List fetchDataLogs(String filter);
    
    /**
     * Implemented fully
     *
     * Method to check if login is certified. Just a dummy method.
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
    
    /**
     * Implemented fully
     *
     * Method to delete growth profile from the database, based on an id.
     * @param id
     */
    public void deleteGrowthProfile(int id);
    
    /**
     * Implemented fully
     *
     * Method to save a growth profile in the database. 
     * Each profile is saved as a new entry in the database.
     * @param profileToSave
     */
    public void saveGrowthProfile(GrowthProfile profileToSave);
    
    /**
     * Implemented fully
     *
     * Method to fetch scada connections from the database.
     * Is formatted as "ip:port" and returned as a list of strings.
     * @return List of Strings representing scada connections.
     */
    public List fetchScadaConnections();
    
    /**
     * Implemented fully
     *
     * Method to save a new scada connection in the database.
     * Takes an IP address and a port as parameters and parses that on to the database.
     * @param ip
     * @param port
     */
    public void saveScadaConnection(String ip, int port);
    
    /**
     * Implemented fully
     *
     * Method to delete a scada connection in the database.
     * Takes a complete "ip:port" scada entry string and splits it at ":".
     * Then, the ip and port are parsed on to the database.
     * @param scadaEntry
     */
    public void deleteScadaConnection(String scadaEntry);
    
    /**
     * Implemented fully
     *
     * Fetches data log filters from the database. These are used to filter the
     * data log tableview in the GUI. Getting filters directly from the database
     * means that, if filters are added or changed there, then this is also
     * done in the MES application.
     * @return List of data log filters
     */
    public List fetchLogFilters();
    /**
     * Implemented fully
     *
     * Saves a new data log in the database. Only production block, value and production id 
     * can be set by the person creating the log. 
     * CMD, type and timestamp is all set by this method. 
     * CMD is 0 for no command, type is "manuel kommentar" and the timestamp is set
     * when the entry is created.
     * @param block
     * @param value
     * @param prodId
     */
    public void saveDataLog(int block, String value, int prodId);
    
    /**
     * Implemented fully
     *
     * Fetches the possible light types from the database and returns
     * them as an array of strings.
     * @return String[] array of light types.
     */
    public String[] getGrowthProfileLightTypes();
    
    /**
     * Implemented fully
     *
     * Sets the light sequence of the growth profile given in the parameter, to 
     * be equal to the tempGrowthProfileLights variable in SingletonMES.
     * @param gp The growth profile for which lights are being set.
     */
    public void setGrowthProfileLights(GrowthProfile gp);
    
    /**
     * Implemented fully
     *
     * Sets the variable tempGrowthProfileLights to be equal to the List lights
     * given in the parameter.
     * @param lights A list of Light objects
     */
    public void setTempGrowthProfileLights(ArrayList<Light> lights);
    
    /**
     * Implemented fully
     *
     * returns tempGrowthProfileLights, which is an ArrayList containing Light objects.
     * @return ArrayList
     */
    public ArrayList<Light> getTempGrowthProfileLights();
    
    /**
     * Implemented fully
     *
     * Removes a Light object from tempGrowthProfileLights at the index given in 
     * the parameter.
     * @param index of the light to be removed.
     */
    public void removeTempGrowthProfileLight(int index);
    
    /**
     * Implemented fully
     *
     * Fetches a Production object for the given Order from the database.
     * @param orderToFetchProdFor
     * @return Production
     */
    public Production fetchProduction(Order orderToFetchProdFor);
    
    /**
     * Implemented fully
     *
     * Fetches ProductionBlocks that are currently in use from the database.
     * @return List of ProductionBlock objects.
     */
    public List<ProductionBlock> fetchActiveProductionBlocks();
    
    /**
     * Implemented fully
     *
     * Adds the Light object from the parameter to the variable tempGrowthProfileLights.
     * @param light to be added
     */
    public void addGrowthProfileLight(Light light);
    
    /**
     * Implemented fully
     *
     * Creates an instance of Light from the given parameters.
     * @param type
     * @param time
     * @param value
     * @return Light
     */
    public Light createGrowthProfileLight(int type, int time, int value);
    
    public abstract int getSuggestedProductionBlock();
}
