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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mes.domain.IMesDatabaseFacade;
import mes.domain.Order;
import mes.domain.Production;
import shared.*;

/**
 *
 * @author chris
 */
public class DatabaseHandler implements IMesDatabaseFacade {

    private final int port = 5432;
    private final String url = "jdbc:postgresql://";
    private final String host = "tek-mmmi-db0a.tek.c.sdu.dk";
    private final String databaseName = "si3_2017_group_21_db";
    private final String username = "si3_2017_group_21";
    private final String password = "ear70.doling";

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
     * Used for test
     *
     * @param args
     */
    public static void main(String[] args) {
        IMesDatabaseFacade db = new DatabaseHandler();

        System.out.println(db.getProductionBlocks());

        System.out.println(db.getProductionBlock(1));

        System.out.println(db.getGrowthProfile(2).getLightSequence());

        System.out.println(db.deleteDataLog(9));

        System.out.println(db.fetchOrders("22-11-2017"));

        Order ord = new Order();
        ord.setId(1);
        ord.setProductionEnd("23-11-2017");
        System.out.println(db.updateOrderEndDate(ord));
    }

    /**
     * Implemented fully
     *
     * @return
     */
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
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        }
        return prodBlocks;
    }

    /**
     * Implemented fully
     *
     * @param productionBlockId
     * @return
     */
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
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return prodBlockToReturn;
    }

    /**
     * Implemented fully
     *
     * @param profileId
     * @return
     */
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
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return profToReturn;
    }

    /**
     * Implemented fully
     *
     * Internal method to fetch the Light objects from DB to be used in a growthProfile
     *
     * @param growthProfileId growth profile ID to fetch Light objects with
     * @return ArrayList containing all Light objects corresponding to growthProfileId
     * @throws SQLException Possibility to throw SQLException within this method body. Exception handling to be done where this is called
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

    /**
     * Not implemented
     *
     * Will fetch all data a Production object requires to be completely filled
     *
     * @param productionId
     * @return Full Production object
     */
    @Override
    public Production getProduction(int productionId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Not implemented fully!
     *
     * @param dataObjectToSave
     * @return
     */
    @Override
    public boolean saveDataLog(Log dataObjectToSave) {
        String saveDatQuery = "INSERT INTO datalogs_view (prod_block,type,timestamp,cmd,value,prod_id) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement saveDatSt = this.conn.prepareStatement(saveDatQuery)) {
            saveDatSt.setInt(1, dataObjectToSave.getBlock());
            //saveDatSt.setInt(2, dataObjectToSave.getType());      -- Wrong datatype on Log.type
            saveDatSt.setInt(3, dataObjectToSave.getUnixTimestamp());
            //saveDatSt.setInt(4, dataObjectToSave.getCmd());       -- Wrong datatype on Log.cmd
            //saveDatSt.setInt(5, dataObjectToSave.getValue());     -- Wrong datatype on Log.value
            //saveDatSt.setInt(6, dataObjectToSave.getProdId());    -- Missing attribute on Log.prodId
            saveDatSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
    }

    /**
     * Implemented fully
     *
     * @param profileToSave GrowthProfile object to split apart and save in Database
     * @return True on succesful save in database, false otherwise
     */
    @Override
    public boolean saveGrowthProfile(GrowthProfile profileToSave) {
        ResultSet saveProfRs;
        String saveProfQuery = "INSERT INTO growthprofile (celcius,water_lvl,moist,night_celcius,name) VALUES (?,?,?,?,?) RETURNING growth_id;";
        try (PreparedStatement saveProfSt = this.conn.prepareStatement(saveProfQuery)) {
            saveProfSt.setInt(1, profileToSave.getTemperature());
            saveProfSt.setInt(2, profileToSave.getWaterLevel());
            saveProfSt.setInt(3, profileToSave.getMoisture());
            saveProfSt.setInt(4, profileToSave.getNightTemperature());
            saveProfSt.setString(5, profileToSave.getName());
            saveProfRs = saveProfSt.executeQuery();
            saveProfRs.next();
            profileToSave.setId(saveProfRs.getInt("growth_id"));
            for (Light light : profileToSave.getLightSequence()) {
                this.saveLightSchedule(profileToSave.getId(), light);
            }
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
    }

    /**
     * Implemented fully
     *
     * @param dataLogIdToDelete
     * @return
     */
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
            System.out.println("Error fetching from database:\n" + ex);
            return false;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Implemented fully
     *
     * Method for saving internal light schedules for a given growthProfile object
     *
     * @return True on succesful save in database, false otherwise
     */
    private boolean saveLightSchedule(int growthProfile, Light lightObjectToSave) {
        String saveLightQuery = "INSERT INTO growthlight_view (growth_id,type,time,value) VALUES (?,?,?,?,?);";
        try (PreparedStatement saveLightSt = this.conn.prepareStatement(saveLightQuery)) {
            saveLightSt.setInt(1, growthProfile);
            saveLightSt.setInt(2, lightObjectToSave.getType());
            saveLightSt.setInt(3, lightObjectToSave.getRunTimeUnix());
            saveLightSt.setInt(4, lightObjectToSave.getPowerLevel());
            saveLightSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
    }

    /**
     * Implemented fully
     *
     * @param orderObjectsToSave
     * @return
     */
    @Override
    public boolean saveOrders(List<Order> orderObjectsToSave) {
        for (Order order : orderObjectsToSave) {
            if (!this.saveOrder(order)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Implemented fully
     *
     * @param orderObjectToSave
     * @return
     */
    private boolean saveOrder(Order orderObjectToSave) {
        String saveOrderQuery = "INSERT INTO \"order\" (fetched_time,prod_name,qty,status) VALUES (?,?,?,?);";
        try (PreparedStatement saveOrderSt = this.conn.prepareStatement(saveOrderQuery)) {
            saveOrderSt.setString(1, orderObjectToSave.getFetchedTime());
            saveOrderSt.setString(2, orderObjectToSave.getProductionName());
            saveOrderSt.setInt(3, orderObjectToSave.getQuantity());
            saveOrderSt.setInt(4, orderObjectToSave.getStatus());
            saveOrderSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
    }

    /**
     * Implemented fully
     *
     * @param prodObjectToSave
     * @return
     */
    @Override
    public boolean saveProduction(Production prodObjectToSave) {
        String saveProdQuery = "INSERT INTO prod_overview (plc_id,growth_id,order_id,prod_begin) VALUES (?,?,?,?)";
        try (PreparedStatement saveProdSt = this.conn.prepareStatement(saveProdQuery)) {
            saveProdSt.setInt(1, prodObjectToSave.getBlock().getId());
            saveProdSt.setInt(2, prodObjectToSave.getGrowthProfile().getId());
            saveProdSt.setInt(3, prodObjectToSave.getOrder().getId());
            saveProdSt.setString(4, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            saveProdSt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return false;
        }
        return true;
    }

    /**
     * Implemented fully
     *
     * @param dateStringRepresentation
     * @return
     */
    @Override
    public List<Order> fetchOrders(String dateStringRepresentation) {
        List<Order> ordersToReturn = new ArrayList<>();
        ResultSet fetchOrdersRs;
        String fetchOrdersQuery = "SELECT order_id,prod_begin,prod_end,prod_name,qty,status FROM \"order\" WHERE fetched_time = ?;";
        try (PreparedStatement fetchOrdersSt = this.conn.prepareStatement(fetchOrdersQuery)) {
            if(!isDateCorrectFormat(dateStringRepresentation)){
                return null;
            }
            fetchOrdersSt.setString(1, dateStringRepresentation);
            fetchOrdersRs = fetchOrdersSt.executeQuery();
            while (fetchOrdersRs.next()) {
                Order localOrder = new Order();
                localOrder.setId(fetchOrdersRs.getInt("order_id"));
                localOrder.setFetchedTime(dateStringRepresentation);
                localOrder.setProductionBegin(fetchOrdersRs.getString("prod_begin"));
                localOrder.setProductionEnd(fetchOrdersRs.getString("prod_end"));
                localOrder.setProductionName(fetchOrdersRs.getString("prod_name"));
                localOrder.setQuantity(fetchOrdersRs.getInt("qty"));
                localOrder.setStatus(fetchOrdersRs.getInt("status"));
                ordersToReturn.add(localOrder);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        }
        return ordersToReturn;
    }

    /**
     * Implemented fully
     *
     * @param prodBlockToSave
     * @return
     */
    @Override
    public boolean saveProductionBlock(ProductionBlock prodBlockToSave) {
        String saveProdBlockQuery = "INSERT INTO plc_conn (ip,port,name) VALUES (?,?,?);";
        try (PreparedStatement saveProdBlockSt = this.conn.prepareStatement(saveProdBlockQuery)) {
            saveProdBlockSt.setString(1, prodBlockToSave.getIpaddress());
            saveProdBlockSt.setInt(2, prodBlockToSave.getPort());
            saveProdBlockSt.setString(3, prodBlockToSave.getName());
            saveProdBlockSt.execute();
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return false;
        }
        return true;
    }

    /**
     * Not implemented
     *
     * @param prodObjectToUpd
     * @return
     */
    @Override
    public boolean updateProductionLot(Production prodObjectToUpd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Implemented Fully
     *
     * Will only update End Date for the given Order object. Will fail if ID or productionEnd is not set on given Order object.
     *
     * @param orderObjectToUpd Order object to update in database. Must have valid ID and productionEnd set to be eligible
     * @return If orderObjectToUpd.productionEnd == null this returns false, true on successful update in database
     */
    @Override
    public boolean updateOrderEndDate(Order orderObjectToUpd) {
        if ((orderObjectToUpd.getId() < 0) || (orderObjectToUpd.getProductionEnd() == null)) {
            return false;
        }
        String updOrderEndQuery = "UPDATE \"order\" SET prod_end = ? WHERE order_id = ?;";
        try (PreparedStatement updOrderEndSt = this.conn.prepareStatement(updOrderEndQuery)) {
            if(!isDateCorrectFormat(orderObjectToUpd.getProductionEnd())){
                return false;
            }
            updOrderEndSt.setString(1, orderObjectToUpd.getProductionEnd());
            updOrderEndSt.setInt(2, orderObjectToUpd.getId());
            if (updOrderEndSt.executeUpdate() <= 0) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error updating order in database:\n" + ex);
            return false;
        }
        return true;
    }

    /**
     * Internal method to check if the required format for string dates is valid
     * @param dateToCheck
     * @return 
     */
    private boolean isDateCorrectFormat(String dateToCheck) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date dateString = format.parse(dateToCheck);
            if (!dateToCheck.equals(format.format(dateString))) {
                return false;            // Illegal date from parameter
            }
        } catch (ParseException ex) {
            System.out.println("Illegal date input! Required format violated.");
            return false;
        }
        return true;
    }
}
