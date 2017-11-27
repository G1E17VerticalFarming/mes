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

    public DatabaseHandler() {
        try {
            this.conn = DriverManager.getConnection(this.url + this.host + ":" + this.port + "/" + this.databaseName, this.username, this.password);
        } catch (SQLException ex) {
            System.out.println("Error connecting to database, please check credentials listed in DatabaseHandler.java !");
            System.out.println("Error Message: " + ex);
            System.exit(1);
        }
    }

    /**
     * Connection test. Selects the definition of all triggers in postgresql.
     *
     * @param args
     */
    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();

        System.out.println(db.getProductionBlocks());

        System.out.println(db.getProductionBlock(1));

        System.out.println(db.getGrowthProfile(2).getLightSequence());

        System.out.println(db.deleteDataLog(9));
    }

    @Override
    public List<ProductionBlock> getProductionBlocks() {
        List<ProductionBlock> prodBlocks = new ArrayList<>();
        String getProdBlockQuery = "SELECT DISTINCT plc_id,ip,port,name,growth_id FROM plc_conn NATURAL JOIN handles NATURAL JOIN production NATURAL JOIN requires;";
        try (Statement getProdBlocksSt = this.conn.createStatement();
            ResultSet getProdBlocksRs = getProdBlocksSt.executeQuery(getProdBlockQuery)) {
            while (getProdBlocksRs.next()) {
                ProductionBlock localProdBlock = new ProductionBlock();
                localProdBlock.setId(getProdBlocksRs.getInt("plc_id"));
                localProdBlock.setIpaddress(getProdBlocksRs.getString("ip"));
                localProdBlock.setPort(getProdBlocksRs.getInt("port"));
                localProdBlock.setName(getProdBlocksRs.getString("name"));
                localProdBlock.setGrowthConfigId(getProdBlocksRs.getInt("growth_id"));
                prodBlocks.add(localProdBlock);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database: (Code) " + ex.getErrorCode());
            return null;
        }
        return prodBlocks;
    }

    @Override
    public ProductionBlock getProductionBlock(int productionBlockId) {
        ProductionBlock prodBlockToReturn = new ProductionBlock();
        ResultSet getProdBlockRs;
        String getProdBlockQuery = "SELECT ip,port,name FROM plc_conn WHERE plc_id = ?";
        try (PreparedStatement getProdBlockSt = this.conn.prepareStatement(getProdBlockQuery)) {
            if (productionBlockId < 0) {
                throw new IllegalArgumentException("Production block ID can only be positive!");
            }
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
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return prodBlockToReturn;
    }

    @Override
    public GrowthProfile getGrowthProfile(int profileId) {
        GrowthProfile profToReturn = new GrowthProfile();
        ResultSet getGrProfRs;
        String getGrProfQuery = "SELECT celcius,water_lvl,moist,night_celcius,name FROM growthprofile WHERE growth_id = ?";
        try (PreparedStatement getGrProfSt = this.conn.prepareStatement(getGrProfQuery)) {
            if (profileId < 0) {
                throw new IllegalArgumentException("Growth Profile ID can only be positive!");
            }
            getGrProfSt.setInt(1, profileId);
            getGrProfRs = getGrProfSt.executeQuery();
            getGrProfRs.next();
            profToReturn.setId(profileId);
            profToReturn.setMoisture(getGrProfRs.getInt("moist"));
            profToReturn.setName(getGrProfRs.getString("name"));
            profToReturn.setTemperature(getGrProfRs.getInt("celcius"));
            profToReturn.setWaterLevel(getGrProfRs.getInt("water_lvl"));
            profToReturn.setNightTemperature(getGrProfRs.getInt("night_celcius"));
            profToReturn.setLightSequence(this.getLightSchedule(profileId));
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
     *
     * @param growthProfileId growth profile ID to fetch Light objects with
     * @return ArrayList containing all Light objects corresponding to growthProfileId
     * @throws SQLException
     */
    private ArrayList<Light> getLightSchedule(int growthProfileId) throws SQLException {
        String getLightQuery = "SELECT light_id,type,time,value FROM growthlight_view WHERE growth_id = ?;";
        try (PreparedStatement getLightSt = this.conn.prepareStatement(getLightQuery)) {
            ResultSet getLightRs;
            ArrayList<Light> lightSeq = new ArrayList<>();
            getLightSt.setInt(1, growthProfileId);
            getLightRs = getLightSt.executeQuery();
            while (getLightRs.next()) {
                Light lightToAdd = new Light();
                lightToAdd.setId(getLightRs.getInt("light_id"));
                lightToAdd.setType(getLightRs.getInt("type"));
                lightToAdd.setRunTimeUnix(getLightRs.getInt("time"));
                lightToAdd.setPowerLevel(getLightRs.getInt("value"));
                lightSeq.add(lightToAdd);
            }
            return lightSeq;
        }
    }

    @Override
    public boolean saveDataLog(Log dataObjectToSave) {
        ResultSet saveDatRs;
        String saveDatQuery = "INSERT INTO datalogs_view () VALUES ();";
        try (PreparedStatement saveDatSt = this.conn.prepareStatement(saveDatQuery)) {
            saveDatRs = saveDatSt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Error fetching from database: (Code) " + ex.getErrorCode());
            return false;
        }
        return false;
    }

    @Override
    public boolean saveGrowthProfile(GrowthProfile profileToSave) {
        return false;
    }

    @Override
    public boolean deleteDataLog(int dataLogIdToDelete) {

        String delDatQuery = "DELETE FROM datalogs_view WHERE data_id = ?";
        try (PreparedStatement delDatSt = this.conn.prepareStatement(delDatQuery)) {
            if (dataLogIdToDelete < 0) {
                throw new IllegalArgumentException("Data ID can only be positive!");
            }
            delDatSt.setInt(1, dataLogIdToDelete);
            if (delDatSt.executeUpdate() > 0) {
                System.out.println("No rows deleted. ID may not exist in database.");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database: (Code) " + ex.getErrorCode());
            return false;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Method for saving internal light schedules for a given growthProfile object
     *
     * @return True on succesful save in database, false otherwise
     */
    private boolean saveLightSchedule(List<Light> lightObjectsToSave) {
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
        String saveProdQuery = "INSERT INTO prod_overview (plc_id,growth_id,order_id) VALUES (?,?,?)";
        try (PreparedStatement saveProdSt = this.conn.prepareStatement(saveProdQuery)) {
            saveProdSt.setInt(1, prodObjectToSave.getBlock().getId());
            saveProdSt.setInt(2, prodObjectToSave.getGrowthProfile().getId());
            saveProdSt.setInt(3, prodObjectToSave.getOrder().getId());
            saveProdSt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Error fetching from database: (Code) " + ex.getErrorCode());
            return false;
        }
        return true;
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

    @Override
    public boolean saveProductionBlock(ProductionBlock prodBlockToSave) {
        String saveProdBlockQuery = "INSERT INTO plc_conn (ip,port,name) VALUES (?,?,?);";
        try (PreparedStatement saveProdBlockSt = this.conn.prepareStatement(saveProdBlockQuery)) {
            saveProdBlockSt.setString(1, prodBlockToSave.getIpaddress());
            saveProdBlockSt.setInt(2, prodBlockToSave.getPort());
            saveProdBlockSt.setString(3, prodBlockToSave.getName());
            saveProdBlockSt.execute();
        } catch (SQLException ex) {
            System.out.println("Error fetching from database: (Code) " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    @Override
    public boolean updateProductionLot(Production prodObjectToUpd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateOrderEndDate(Order orderObjectToUpd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
