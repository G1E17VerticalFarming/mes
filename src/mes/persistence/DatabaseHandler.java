/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
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
        Statement st = null;
        try {
            DatabaseHandler db = new DatabaseHandler();
            st = db.conn.createStatement();
            ResultSet rs = st.executeQuery("select prosrc from pg_trigger,pg_proc where pg_proc.oid=pg_trigger.tgfoid;");
            while(rs.next()){
                System.out.println(rs.getString("prosrc"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<ProductionBlock> getProductionBlocks() {
        List<ProductionBlock> prodBlocks = new ArrayList<>();
        
        try {
            Statement st = this.conn.createStatement();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prodBlocks;
    }

    @Override
    public GrowthProfile getGrowthProfile(int profileId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProductionBlock getProductionBlock(int productionBlockId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean saveDataLog(Log dataObjectToSave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean saveGrowthProfile(GrowthProfile profileToSave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteDataLog(int dataLogIdToDelete) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean saveOrder(Order orderObjectToSave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean saveProduction(Production prodObjectToSave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
