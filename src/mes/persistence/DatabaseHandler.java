/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mes.domain.IMesDatabaseFacade;
import mes.domain.Order;
import mes.domain.Production;
import shared.*;

/**
 *
 * @author chris
 */
public class DatabaseHandler implements IMesDatabaseFacade {
    
    private int port = 5432;
    private String url = "jdbc:postgresql://";
    private String host = "tek-mmmi-db0a.tek.c.sdu.dk";
    private String databaseName = "si3_2017_group_21_db";
    private String username = "si3_2017_group_21";
    private String password = "ear70.doling";
    
    private Connection conn = null;
    
    public DatabaseHandler(){
        try {
            this.conn = DriverManager.getConnection(this.url + this.host + ":" + this.port + "/" + this.databaseName, this.username, this.password);
        } catch (SQLException ex) {
            System.out.println("Error connecting to database, please check credentials listed in DatabaseHandler.java !");
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Connection test. Selects the definition of all triggers in postgresql.
     * @param args 
     */
    public static void main(String[] args){
        DatabaseHandler db = new DatabaseHandler();
        
        System.out.println(db.getProductionBlocks());
        
        System.out.println(db.getProductionBlock(1));
    }

    @Override
    public List<ProductionBlock> getProductionBlocks() {
        List<ProductionBlock> prodBlocks = new ArrayList<>();
        
        Statement getProdBlocksSt;
        ResultSet getProdBlocksRs;
        try {
            getProdBlocksSt = this.conn.createStatement();
            getProdBlocksRs = getProdBlocksSt.executeQuery("SELECT DISTINCT id,ip,port,name,growth_id FROM plc_conn NATURAL JOIN handles NATURAL JOIN production NATURAL JOIN requires;");
            while(getProdBlocksRs.next()){
                ProductionBlock localProdBlock = new ProductionBlock();
                localProdBlock.setId(getProdBlocksRs.getInt("id"));
                localProdBlock.setIpaddress(getProdBlocksRs.getString("ip"));
                localProdBlock.setPort(getProdBlocksRs.getInt("port"));
                localProdBlock.setName(getProdBlocksRs.getString("name"));
                localProdBlock.setGrowthConfigId(getProdBlocksRs.getInt("growth_id"));
                prodBlocks.add(localProdBlock);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prodBlocks;
    }
    
        @Override
    public ProductionBlock getProductionBlock(int productionBlockId) {
        ProductionBlock prodBlockToReturn = new ProductionBlock();
        PreparedStatement getProdBlockSt;
        ResultSet getProdBlockRs;
        String getProdBlockQuery = "SELECT ip,port,name FROM plc_conn WHERE id = ?";
        try{
            if(productionBlockId < 0){
                throw new IllegalArgumentException("Production block ID can only be positive!");
            }
            getProdBlockSt = this.conn.prepareStatement(getProdBlockQuery);
            getProdBlockSt.setInt(1, productionBlockId);
            getProdBlockRs = getProdBlockSt.executeQuery();
            getProdBlockRs.next();
            prodBlockToReturn.setId(productionBlockId);
            prodBlockToReturn.setIpaddress(getProdBlockRs.getString("ip"));
            prodBlockToReturn.setName(getProdBlockRs.getString("name"));
            prodBlockToReturn.setPort(getProdBlockRs.getInt("port"));
        } catch (SQLException ex) {
            System.out.println("Error fetching from database: (Code) " + ex.getErrorCode());
            return null;
        } catch(IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return prodBlockToReturn;
    }

    @Override
    public GrowthProfile getGrowthProfile(int profileId) {
        GrowthProfile profToReturn = new GrowthProfile();
        PreparedStatement getGrProfSt;
        ResultSet getGrProfRs;
        String getGrProfQuery = "SELECT celcius,water_lvl,moist,night_celcius,name FROM growthprofile WHERE growth_id = ?";
        try{
            if(profileId < 0){
                throw new IllegalArgumentException("Growth Profile ID can only be positive!");
            }
            getGrProfSt = this.conn.prepareStatement(getGrProfQuery);
            getGrProfSt.setInt(1, profileId);
            getGrProfRs = getGrProfSt.executeQuery();
            profToReturn.setId(profileId);
            profToReturn.setMoisture(getGrProfRs.getInt("moist"));
            profToReturn.setName(getGrProfRs.getString("name"));
            profToReturn.setTemperature(getGrProfRs.getInt("celcius"));
            profToReturn.setWaterLevel(getGrProfRs.getInt("water_lvl"));
            // Missing attribute night temperature on growthprofile in FLIB
            //profToReturn.setNightTemperature(getGrProfRs.getInt("night_celcius"));
            // Missing fetch light from DB too and assign variable profToReturn.lightSequence
        } catch (SQLException ex) {
            System.out.println("Error fetching from database: (Code) " + ex.getErrorCode());
            return null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return profToReturn;
    }
    
    /**
     * Internal method to fetch the Light objects from DB to be used in a growthProfile
     * @param growthProfileId
     * @return 
     */
    private Light getLightSchedule(int growthProfileId){
        return null;
    }

    @Override
    public boolean saveDataLog(Log dataObjectToSave) {
        return false;
    }
    
    @Override
    public boolean saveGrowthProfile(GrowthProfile profileToSave) {
        return false;
    }

    @Override
    public boolean deleteDataLog(int dataLogIdToDelete) {
        return false;
    }
    
    
    
    
    /**
     * Method for saving internal light schedules for a given growthProfile object
     * @return 
     */
    private boolean saveLightSchedule(GrowthProfile objToSave){
        return true;
    }

    @Override
    public boolean saveOrders(List<Order> orderObjectsToSave) {
        return false;
    }

    @Override
    public boolean saveOrder(Order orderObjectToSave) {
        return false;
    }

    @Override
    public boolean saveProduction(Production prodObjectToSave) {
        return false;
    }

    @Override
    public List<Order> fetchOrders(String dateStringRepresentation) {
        /*
            Dummy data begin
        */
        List<Order> ordersToReturn = new ArrayList<>();
        
        Order testOrder = new Order();
        testOrder.setId(1);
        testOrder.setFetchedTime(dateStringRepresentation);
        testOrder.setProductionName("Test objekt græskar");
        testOrder.setProductionBegin("18-11-2017");
        testOrder.setProductionEnd("19-11-2017");
        testOrder.setQuantity(10);
        testOrder.setStatus("Fetched");
        
        ordersToReturn.add(testOrder);
        
        return ordersToReturn;
        /*
            Dummy data end
        */
    }
}
