/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import mes.persistence.DatabaseHandler;
import shared.*;

/**
 *
 * @author chris
 */
public class SingletonMES implements IMes, IMesApi {

    private List orders;
    private List growthProfiles;
    private List retrievedLogs;
    private List scadaConnections;
    private ArrayList<Light> tempGrowthProfileLights;

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
    public List<ProductionBlock> fetchActiveProductionBlocks() {
        List<ProductionBlock> pBlocks = new ArrayList<>();
        pBlocks.addAll(dbHandler.getActiveProductionBlocks());
        
        return pBlocks;
    }
    
    @Override
    public Production fetchProduction(Order orderToFetchProdFor) {
        return dbHandler.getProduction(orderToFetchProdFor);
    }
    
    @Override
    public void removeTempGrowthProfileLight(int index) {
        tempGrowthProfileLights.remove(index);
    }
    
    @Override
    public ArrayList getTempGrowthProfileLights() {
        return this.tempGrowthProfileLights;
    }
    
    @Override
    public void setTempGrowthProfileLights(ArrayList lights) {
        this.tempGrowthProfileLights = lights;
    }
    
    @Override
    public void setGrowthProfileLights(GrowthProfile gp) {
        gp.setLightSequence(tempGrowthProfileLights);
    }
    
    @Override
    public void addGrowthProfileLight(Light light) {
        this.tempGrowthProfileLights.add(light);
    }
    
    @Override
    public Light createGrowthProfileLight(int type, int time, int value) {
        Light newLight = new Light();
        newLight.setType(type);
        newLight.setPowerLevel(value);
        newLight.setRunTimeUnix(time * 3600);
        
        return newLight;
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
    public boolean prepareOrder(Order currentOrder, Status status, GrowthProfile growthProfile, int prodBlock) {
        currentOrder.setStatus(status);
        
        ProductionBlock newProdBlock = new ProductionBlock();
        newProdBlock.setId(prodBlock);
        
        Production newProd = new Production();
        newProd.setBlock(newProdBlock);
        newProd.setOrder(currentOrder);
        newProd.setGrowthProfile(growthProfile);
        
        dbHandler.saveProduction(newProd);
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
        profileToSave.setLightSequence(tempGrowthProfileLights);
        dbHandler.saveGrowthProfile(profileToSave);
    } 
    
    @Override
    public void saveScadaConnection(String ip, int port) {
        dbHandler.saveScadaEntry(ip, port);
    }
    
    @Override
    public void deleteScadaConnection(String scadaEntry) {
        String elements[] = scadaEntry.split(":");
        dbHandler.deleteScadaEntry(elements[0], Integer.parseInt(elements[1]));
    }
    
    @Override
    public List fetchLogFilters() {
        return dbHandler.getDataLogFilterOptions();
    }
    
    @Override
    public void saveDataLog(int block, String value, int prodId) {
        Log dataLogToSave = new Log();
        dataLogToSave.setBlock(block);
        dataLogToSave.setType("manuel kommentar");
        dataLogToSave.setUnixTimestamp((int)Instant.now().getEpochSecond());
        dataLogToSave.setCmd(0);
        dataLogToSave.setValue(value);
        dataLogToSave.setProdId(prodId);
        
        dbHandler.saveDataLog(dataLogToSave);
        
    }
    
    @Override
    public String[] getGrowthProfileLightTypes() {
        String[] lightTypes = {"Ingen lys", "Rødt lys", "Blåt lys", "Blåt og rødt lys"};
        
        return lightTypes;
    }

    @Override
    public boolean saveProductionBlock(ProductionBlock prodBlockToSave) {
        return this.dbHandler.saveProductionBlock(prodBlockToSave);
    }

    @Override
    public GrowthProfile getGrowthProfile(int growthProfileId) {
        return this.dbHandler.getGrowthProfile(growthProfileId);
    }

    @Override
    public boolean saveDataLog(Log dataObjectToSave) {
        return this.dbHandler.saveDataLog(dataObjectToSave);
    }
    
}
