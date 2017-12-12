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
import mes.domain.interfaces.DomainReadLogin;
import mes.domain.interfaces.DomainReadProduction;
import mes.domain.interfaces.DomainReadWriteDataLog;
import mes.domain.interfaces.DomainReadWriteGrowthProfile;
import mes.domain.interfaces.DomainReadWriteOrder;
import mes.domain.interfaces.DomainReadWriteProductionBlock;
import mes.domain.interfaces.DomainReadWriteScadaConnections;
import mes.domain.interfaces.DomainWriteDate;
import mes.persistence.DatabaseHandler;
import shared.Log;
import shared.GrowthProfile;
import shared.Light;
import shared.ProductionBlock;
import mes.domain.interfaces.PersistenceReadWriteDataLog;
import mes.domain.interfaces.PersistenceReadWriteGrowthProfile;
import mes.domain.interfaces.PersistenceReadWriteOrder;
import mes.domain.interfaces.PersistenceReadWriteProduction;
import mes.domain.interfaces.PersistenceReadWriteProductionBlock;
import mes.domain.interfaces.PersistenceReadWriteScadaConnections;

/**
 *
 * @author chris
 */
public class SingletonMES implements DomainReadWriteOrder, DomainReadWriteGrowthProfile, DomainReadWriteScadaConnections, DomainReadWriteProductionBlock, DomainWriteDate, DomainReadProduction, DomainReadLogin, DomainReadWriteDataLog, IMesApi {

    private List orders;
    private List growthProfiles;
    private List retrievedLogs;
    private List scadaConnections;
    private ArrayList<Light> tempGrowthProfileLights;

    private String date;
    private PersistenceReadWriteProductionBlock prodBlockHandler;
    private PersistenceReadWriteGrowthProfile groProfHandler;
    private PersistenceReadWriteDataLog datLogHandler;
    private PersistenceReadWriteProduction prodHandler;
    private PersistenceReadWriteOrder orderHandler;
    private PersistenceReadWriteScadaConnections scadConnHandler;

    private static SingletonMES instance = null;

    protected SingletonMES() {
        // Is here to prevent instantiation
        DatabaseHandler mainDbHandler = new DatabaseHandler();
        this.prodBlockHandler = mainDbHandler;
        this.groProfHandler = mainDbHandler;
        this.datLogHandler = mainDbHandler;
        this.prodHandler = mainDbHandler;
        this.orderHandler = mainDbHandler;
        this.scadConnHandler = mainDbHandler;
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
        pBlocks.addAll(prodBlockHandler.getActiveProductionBlocks());
        
        return pBlocks;
    }
    
    @Override
    public Production fetchProduction(Order orderToFetchProdFor) {
        return prodHandler.getProduction(orderToFetchProdFor);
    }
    
    @Override
    public void setGrowthProfileLights(GrowthProfile gp) {
        gp.setLightSequence(tempGrowthProfileLights);
    }
    
    @Override
    public void addGrowthProfileLight(Light light) {
        if(this.tempGrowthProfileLights == null) {
            this.tempGrowthProfileLights = new ArrayList<>();
        }
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
    public List<Order> fetchOrders() {
        this.orders = orderHandler.fetchOrders(date);
        return this.orders;
    }
    
    @Override
    public List<Status> fetchStatuses() {
        return orderHandler.getOrderStatuses();
    }
    
    @Override
    public List<GrowthProfile> fetchGrowthProfiles() {
        this.growthProfiles = groProfHandler.getGrowthProfiles();
        
        return this.growthProfiles;
    }
    
    @Override
    public List<String> fetchScadaConnections() {
        this.scadaConnections = scadConnHandler.getScadaEntries();
        
        return this.scadaConnections;
    }
    
    @Override
    public List<Log> fetchDataLogs(String filter) {
        this.retrievedLogs = datLogHandler.getDataLogs(filter);
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
        
        prodHandler.saveProduction(newProd);
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
        groProfHandler.deleteGrowthProfile(id);
    }
    
    @Override
    public void saveGrowthProfile(GrowthProfile profileToSave) {
        groProfHandler.saveGrowthProfile(profileToSave);
    } 
    
    @Override
    public void saveScadaConnection(String ip, int port) {
        scadConnHandler.saveScadaEntry(ip, port);
    }
    
    @Override
    public void deleteScadaConnection(String scadaEntry) {
        String elements[] = scadaEntry.split(":");
        scadConnHandler.deleteScadaEntry(elements[0], Integer.parseInt(elements[1]));
    }
    
    @Override
    public List<String> fetchLogFilters() {
        return datLogHandler.getDataLogFilterOptions();
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
        
        datLogHandler.saveDataLog(dataLogToSave);
        
    }
    
    @Override
    public String[] getGrowthProfileLightTypes() {
        String[] lightTypes = {"Ingen lys", "Rødt lys", "Blåt lys", "Blåt og rødt lys"};
        
        return lightTypes;
    }

    @Override
    public boolean saveProductionBlock(ProductionBlock prodBlockToSave, String ip, int port) {
        return this.prodBlockHandler.saveProductionBlock(prodBlockToSave, ip, port);
    }

    @Override
    public GrowthProfile getGrowthProfile(int growthProfileId) {
        return this.groProfHandler.getGrowthProfile(growthProfileId);
    }

    @Override
    public boolean saveDataLog(Log dataObjectToSave) {
        return this.datLogHandler.saveDataLog(dataObjectToSave);
    }

    @Override
    public List<ProductionBlock> fetchAllProductionBlocks(String ip, int port) {
        return this.prodBlockHandler.getAllProductionBlocks(ip, port);
    }

    @Override
    public boolean deleteProductionBlock(ProductionBlock prodBlockToSave) {
        return this.prodBlockHandler.deleteProductionBlock(prodBlockToSave.getId());
    }
    
    @Override
    public int getSuggestedProductionBlock() {
        for(ProductionBlock pb : this.prodBlockHandler.getIdleProductionBlocks()) {
            return pb.getId();
        }
        return -1;
    }
    
}
