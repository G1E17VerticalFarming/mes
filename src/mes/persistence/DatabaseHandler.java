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
import mes.domain.Order;
import mes.domain.Production;
import shared.Log;
import shared.GrowthProfile;
import shared.Light;
import shared.ProductionBlock;
import mes.domain.Status;
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
public class DatabaseHandler implements PersistenceReadWriteProductionBlock, PersistenceReadWriteGrowthProfile, PersistenceReadWriteDataLog, PersistenceReadWriteProduction, PersistenceReadWriteScadaConnections, PersistenceReadWriteOrder {

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
            this.conn.setAutoCommit(true);
        } catch (SQLException ex) {
            System.out.println("Error connecting to database, please check credentials listed in DatabaseHandler.java !");
            System.out.println("Error Message: " + ex);
            System.exit(1);
        }
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
        String getLightQuery = "SELECT light_id,type,time,value FROM growthlight_view WHERE growth_id = ? ORDER BY light_id;";
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
     * Implemented fully
     *
     * Method for saving internal light schedules for a given growthProfile object
     *
     * @param growthProfile Integer representing a GrowthProfile ID. This is the growthprofile which lightObjectToSave will be stored with
     * @param lightObjectToSave Light object to be stored in database
     * @return True on succesful save in database, false otherwise
     */
    private boolean saveLightSchedule(int growthProfile, Light lightObjectToSave) {
        String saveLightQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "INSERT INTO growthlight_view (growth_id,type,time,value) VALUES (?,?,?,?);"
                + "COMMIT;";
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
     * Internal method to store an Order object in the database This method is called from saveOrders only
     *
     * @param orderObjectToSave Order object to store in database
     * @return True if orderObjectToSave is stored succesfully in database, false otherwise
     */
    private boolean saveOrder(Order orderObjectToSave) {
        String saveOrderQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "INSERT INTO order_view (fetched_time,prod_name,qty,status_id) VALUES (?,?,?,?);"
                + "COMMIT;";
        try (PreparedStatement saveOrderSt = this.conn.prepareStatement(saveOrderQuery)) {
            saveOrderSt.setString(1, orderObjectToSave.getFetchedTime());
            saveOrderSt.setString(2, orderObjectToSave.getProductionName());
            saveOrderSt.setInt(3, orderObjectToSave.getQuantity());
            saveOrderSt.setInt(4, orderObjectToSave.getStatus().getId());
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
     * Internal method to check if the required format for string dates is valid The given parameter must follow format: SimpleDateFormat("dd-MM-yyyy"). To be considered a valid date.
     *
     * @param dateToCheck string date to check if correctly formatted
     * @return True if dateToCheck is correctly formatted, false otherwise
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

    /**
     * Implemented fully
     *
     * Internal method to generate a lot number for a finished production order
     *
     * lot number will be generated with following format: PLC ID - Production ID - WeekInYear DayInWeek Year Example: PLC ID: 1, Production ID: 2, EndDate: 23-11-2017 lot = 1-2-47417
     *
     * @param orderToCreateLotFor Order object to create lot number for
     * @return The lot number
     */
    private String generateLot(Order orderToCreateLotFor) {
        String generatedLot;
        ResultSet fetchDataAssocRs;
        String fetchDataAssocQuery = "SELECT plc_id,prod_id FROM prod_overview WHERE order_id = ?;";
        try (PreparedStatement fetchDataAssocSt = this.conn.prepareStatement(fetchDataAssocQuery)) {
            fetchDataAssocSt.setInt(1, orderToCreateLotFor.getId());
            fetchDataAssocRs = fetchDataAssocSt.executeQuery();
            fetchDataAssocRs.next();
            generatedLot = fetchDataAssocRs.getInt("plc_id") + "-" + fetchDataAssocRs.getInt("prod_id") + "-" + new SimpleDateFormat("wuyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(orderToCreateLotFor.getProductionEnd()));
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        } catch (ParseException ex) {
            System.out.println("Illegal date input! Required format violated.");
            return null;
        }
        return generatedLot;
    }

    /**
     * Used for test
     *
     * @param args
     */
    public static void main(String[] args) {
        DatabaseHandler handler = new DatabaseHandler();
        System.out.println("Testing time taken with 2 statements.");
        long beginTime = System.currentTimeMillis();
        
        ProductionBlock pb = new ProductionBlock();
        pb.setName("Local Virtual #99");
        pb.setIpaddress("localhost");
        pb.setPort(5555);
        System.out.println(handler.saveProductionBlock(pb, "127.0.0.1", 81));
//        System.out.println(handler.getProductionBlocks());
//
//        System.out.println(handler.getProductionBlock(1));
//
//        System.out.println(handler.getGrowthProfile(2).getLightSequence());
//
//        System.out.println(handler.fetchOrders("22-11-2017"));

//        Order ord = new Order();
//        ord.setId(2);
//        ord.setProductionEnd("23-11-2017");
//        ord.setStatus(1);
//        System.out.println(db.updateOrderEndDate(ord));
        System.out.println(handler.getAllProductionBlocks("127.0.0.1", 81));
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (endTime - beginTime));
    }

    @Override
    public List<ProductionBlock> getActiveProductionBlocks() {
        List<ProductionBlock> prodBlocks = new ArrayList<>();
        String getProdBlockQuery = "SELECT DISTINCT ON (plc_id) plc_id,prod_id,ip,port,name,growth_id FROM plc_conn NATURAL JOIN handles NATURAL JOIN production NATURAL JOIN requires;";
        try (Statement getProdBlocksSt = this.conn.createStatement();
                ResultSet getProdBlocksRs = getProdBlocksSt.executeQuery(getProdBlockQuery)) {
            while (getProdBlocksRs.next()) {
                ProductionBlock localProdBlock = new ProductionBlock();
                localProdBlock.setId(getProdBlocksRs.getInt("plc_id"));
                localProdBlock.setBatchId(getProdBlocksRs.getInt("prod_id"));
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

    @Override
    public List<ProductionBlock> getIdleProductionBlocks() {
        List<ProductionBlock> prodBlocks = new ArrayList<>();
        String getProdBlockQuery = "SELECT plc_id,ip,port,name FROM plc_conn WHERE plc_id NOT IN (SELECT plc_id FROM handles);";
        try (Statement getProdBlocksSt = this.conn.createStatement();
                ResultSet getProdBlocksRs = getProdBlocksSt.executeQuery(getProdBlockQuery)) {
            while (getProdBlocksRs.next()) {
                ProductionBlock localProdBlock = new ProductionBlock();
                localProdBlock.setId(getProdBlocksRs.getInt("plc_id"));
                localProdBlock.setIpaddress(getProdBlocksRs.getString("ip"));
                localProdBlock.setPort(getProdBlocksRs.getInt("port"));
                localProdBlock.setName(getProdBlocksRs.getString("name"));
                prodBlocks.add(localProdBlock);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
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
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return prodBlockToReturn;
    }

    @Override
    public GrowthProfile getGrowthProfile(int growthProfileId) {
        GrowthProfile profToReturn = new GrowthProfile();
        ResultSet getGrProfRs;
        String getGrProfQuery = "SELECT celcius,water_lvl,moist,night_celcius,name FROM growthprofile WHERE growth_id = ?";
        try (PreparedStatement getGrProfSt = this.conn.prepareStatement(getGrProfQuery)) {
            if (growthProfileId < 0) {
                throw new IllegalArgumentException("Growth Profile ID can only be positive!");
            }
            getGrProfSt.setInt(1, growthProfileId);
            getGrProfRs = getGrProfSt.executeQuery();
            getGrProfRs.next();
            profToReturn.setId(growthProfileId);
            profToReturn.setMoisture(getGrProfRs.getInt("moist"));
            profToReturn.setName(getGrProfRs.getString("name"));
            profToReturn.setTemperature(getGrProfRs.getInt("celcius"));
            profToReturn.setWaterLevel(getGrProfRs.getInt("water_lvl"));
            profToReturn.setNightTemperature(getGrProfRs.getInt("night_celcius"));
            profToReturn.setLightSequence(this.getLightSchedule(growthProfileId));
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return profToReturn;
    }

    @Override
    public boolean saveDataLog(Log dataObjectToSave) {
        String saveDatQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "INSERT INTO datalogs_view (prod_block,type,timestamp,cmd,value,prod_id) VALUES (?,?,?,?,?,?);"
                + "COMMIT;";
        try (PreparedStatement saveDatSt = this.conn.prepareStatement(saveDatQuery)) {
            saveDatSt.setInt(1, dataObjectToSave.getBlock());
            saveDatSt.setString(2, dataObjectToSave.getType());
            saveDatSt.setInt(3, dataObjectToSave.getUnixTimestamp());
            saveDatSt.setInt(4, dataObjectToSave.getCmd());
            saveDatSt.setString(5, dataObjectToSave.getValue());
            saveDatSt.setInt(6, dataObjectToSave.getProdId());
            saveDatSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
    }

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
            if(profileToSave.getLightSequence() != null){
                for (Light light : profileToSave.getLightSequence()) {
                    this.saveLightSchedule(profileToSave.getId(), light);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrders(List<Order> orderObjectsToSave) {
        for (Order order : orderObjectsToSave) {
            if (!this.saveOrder(order)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveProduction(Production prodObjectToSave) {
        if (prodObjectToSave.getBlock() == null || prodObjectToSave.getGrowthProfile() == null || prodObjectToSave.getOrder() == null) {
            return false;
        }
        String saveProdQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "INSERT INTO prod_overview (plc_id,growth_id,order_id,prod_begin,status_id) VALUES (?,?,?,?,?);"
                + "COMMIT;";
        try (PreparedStatement saveProdSt = this.conn.prepareStatement(saveProdQuery)) {
            saveProdSt.setInt(1, prodObjectToSave.getBlock().getId());
            saveProdSt.setInt(2, prodObjectToSave.getGrowthProfile().getId());
            saveProdSt.setInt(3, prodObjectToSave.getOrder().getId());
            saveProdSt.setString(4, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            saveProdSt.setInt(5, prodObjectToSave.getOrder().getStatus().getId());
            saveProdSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        }
        return true;
    }

    @Override
    public List<Order> fetchOrders(String dateStringRepresentation) {
        List<Order> ordersToReturn = new ArrayList<>();
        ResultSet fetchOrdersRs;
        String fetchOrdersQuery = "SELECT order_id,prod_begin,prod_end,prod_name,qty,status_id,order_status FROM order_view WHERE fetched_time = ?;";
        try (PreparedStatement fetchOrdersSt = this.conn.prepareStatement(fetchOrdersQuery)) {
            if (!isDateCorrectFormat(dateStringRepresentation)) {
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
                Status localOrderStatus = new Status();
                localOrderStatus.setId(fetchOrdersRs.getInt("status_id"));
                localOrderStatus.setValue(fetchOrdersRs.getString("order_status"));
                localOrder.setStatus(localOrderStatus);
                ordersToReturn.add(localOrder);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        }
        return ordersToReturn;
    }

    @Override
    public boolean saveProductionBlock(ProductionBlock prodBlockToSave, String scadaIp, int scadaPort) {
        String saveProdBlockQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "INSERT INTO plc_conn (ip,port,name) VALUES (?,?,?);"
                + "INSERT INTO controls (plc_id,scada_id) VALUES (currval('plc_conn_id_seq'),(SELECT scada_id FROM scada_conn WHERE ip = ? AND port = ?));"
                + "COMMIT;";
        try (PreparedStatement saveProdBlockSt = this.conn.prepareStatement(saveProdBlockQuery)) {
            if(scadaIp.isEmpty() || scadaPort == 0){
                throw new IllegalArgumentException("SCADA IP and port cannot be empty.");
            }
            saveProdBlockSt.setString(1, prodBlockToSave.getIpaddress());
            saveProdBlockSt.setInt(2, prodBlockToSave.getPort());
            saveProdBlockSt.setString(3, prodBlockToSave.getName());
            saveProdBlockSt.setString(4, scadaIp);
            saveProdBlockSt.setInt(5, scadaPort);
            saveProdBlockSt.execute();
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return false;
        } catch (IllegalArgumentException ex){
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean updateOrderEndDate(Order orderObjectToUpd) {
        if ((orderObjectToUpd.getId() < 0) || (orderObjectToUpd.getProductionEnd() == null)) {
            return false;
        }
        String updOrderEndQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "UPDATE prod_overview SET status_id = ?, prod_end = ?, lot = ? WHERE order_id = ?;"
                + "COMMIT;";
        try (PreparedStatement updOrderEndSt = this.conn.prepareStatement(updOrderEndQuery)) {
            if (!isDateCorrectFormat(orderObjectToUpd.getProductionEnd())) {
                return false;
            }
            String lot = this.generateLot(orderObjectToUpd);
            if (lot == null) {
                return false;       // Invalid lot number returned
            }
            updOrderEndSt.setInt(1, orderObjectToUpd.getStatus().getId());
            updOrderEndSt.setString(2, orderObjectToUpd.getProductionEnd());
            updOrderEndSt.setString(3, lot);
            updOrderEndSt.setInt(4, orderObjectToUpd.getId());
            if (updOrderEndSt.executeUpdate() <= 0) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Error updating order in database:\n" + ex);
            return false;
        }
        return true;
    }

    @Override
    public List<GrowthProfile> getGrowthProfiles() {
        List<GrowthProfile> growthProfiles = new ArrayList<>();
        String getGrowthProfsQuery = "SELECT growth_id,name,celcius,water_lvl,moist,night_celcius FROM growthprofile ORDER BY growth_id;";
        try (Statement getGrowthProfsSt = this.conn.createStatement();
                ResultSet getGrowthProfsRs = getGrowthProfsSt.executeQuery(getGrowthProfsQuery)) {
            while (getGrowthProfsRs.next()) {
                GrowthProfile localProf = new GrowthProfile();
                localProf.setId(getGrowthProfsRs.getInt("growth_id"));
                localProf.setMoisture(getGrowthProfsRs.getInt("moist"));
                localProf.setName(getGrowthProfsRs.getString("name"));
                localProf.setTemperature(getGrowthProfsRs.getInt("celcius"));
                localProf.setNightTemperature(getGrowthProfsRs.getInt("night_celcius"));
                localProf.setWaterLevel(getGrowthProfsRs.getInt("water_lvl"));
                localProf.setLightSequence(this.getLightSchedule(localProf.getId()));
                growthProfiles.add(localProf);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        }
        return growthProfiles;
    }

    @Override
    public boolean deleteGrowthProfile(int growthProfileIdToDelete) {
        String delGrowthProfQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "DELETE FROM growthprofile WHERE growth_id = ?;"
                + "COMMIT;";
        try (PreparedStatement delGrowthProfSt = this.conn.prepareStatement(delGrowthProfQuery)) {
            if (growthProfileIdToDelete < 0) {
                throw new IllegalArgumentException("Data ID can only be positive!");
            }
            delGrowthProfSt.setInt(1, growthProfileIdToDelete);
            delGrowthProfSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting from database:\n" + ex);
            return false;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Status> getOrderStatuses() {
        List<Status> orderStatuses = new ArrayList<>();
        String getOrderStatusesQuery = "SELECT status_id,value FROM statuses;";
        try (Statement getOrderStatusesSt = this.conn.createStatement();
                ResultSet getOrderStatusesRs = getOrderStatusesSt.executeQuery(getOrderStatusesQuery)) {
            while (getOrderStatusesRs.next()) {
                Status localStatus = new Status();
                localStatus.setId(getOrderStatusesRs.getInt("status_id"));
                localStatus.setValue(getOrderStatusesRs.getString("value"));
                orderStatuses.add(localStatus);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        }
        return orderStatuses;
    }

    @Override
    public List<Log> getDataLogs(String filter) {
        if(filter == null) {
            filter = "";
        }
        
        String getLogsQuery;
        if (filter.isEmpty()) { // No filter provided, fetch all logs
            getLogsQuery = "SELECT data_id,prod_block,type,cmd,value,timestamp,prod_id FROM data NATURAL JOIN logs ORDER BY data_id;";
        } else {
            getLogsQuery = "SELECT data_id,prod_block,type,cmd,value,timestamp,prod_id FROM data NATURAL JOIN logs WHERE type = ? ORDER BY data_id;";
        }
        List<Log> dataLogs = new ArrayList<>();
        try (PreparedStatement getLogsSt = this.conn.prepareStatement(getLogsQuery)) {
            ResultSet getLogsRs;
            if (!filter.isEmpty()) {
                getLogsSt.setString(1, filter);
            }
            getLogsRs = getLogsSt.executeQuery();
            while (getLogsRs.next()) {
                Log localLog = new Log();
                localLog.setId(getLogsRs.getInt("data_id"));
                localLog.setBlock(getLogsRs.getInt("prod_block"));
                localLog.setCmd(getLogsRs.getInt("cmd"));
                localLog.setProdId(getLogsRs.getInt("prod_id"));
                localLog.setType(getLogsRs.getString("type"));
                localLog.setUnixTimestamp(getLogsRs.getInt("timestamp"));
                localLog.setValue(getLogsRs.getString("value"));
                dataLogs.add(localLog);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return new ArrayList<>();
        }
        return dataLogs;
    }

    @Override
    public List<String> getScadaEntries() {
        List<String> scadaEntries = new ArrayList<>();
        String getScadaEntriesQuery = "SELECT scada_id,ip,port FROM scada_conn;";
        try (Statement getScadaEntriesSt = this.conn.createStatement();
                ResultSet getScadaEntriesRs = getScadaEntriesSt.executeQuery(getScadaEntriesQuery)) {
            while (getScadaEntriesRs.next()) {
                scadaEntries.add(getScadaEntriesRs.getInt("scada_id") + ":" + getScadaEntriesRs.getString("ip") + ":" + getScadaEntriesRs.getString("port"));
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        }
        return scadaEntries;
    }

    @Override
    public boolean saveScadaEntry(String ip, int port) {
        String saveScadaEntryQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "INSERT INTO scada_conn (ip,port) VALUES (?,?);"
                + "COMMIT;";
        try (PreparedStatement saveScadaEntrySt = this.conn.prepareStatement(saveScadaEntryQuery)) {
            if (port <= 1024) {
                throw new IllegalArgumentException("Port cannot be 1024 or lower. Port range reserved.");
            }
            saveScadaEntrySt.setString(1, ip);
            saveScadaEntrySt.setInt(2, port);
            saveScadaEntrySt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error inserting into database:\n" + ex);
            return false;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteScadaEntry(String ip, int port) {
        String delScadaEntryQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "DELETE FROM scada_conn WHERE ip = ? AND port = ?;"
                + "COMMIT;";
        try (PreparedStatement delScadaEntrySt = this.conn.prepareStatement(delScadaEntryQuery)) {
            if (port <= 1024) {
                throw new IllegalArgumentException("Port cannot be 1024 or lower. Port range reserved.");
            }
            delScadaEntrySt.setString(1, ip);
            delScadaEntrySt.setInt(2, port);
            delScadaEntrySt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting from database:\n" + ex);
            return false;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteProductionBlock(int prodBlockIdToDelete) {
        String delProdBlockQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "DELETE FROM plc_conn WHERE plc_id = ?;"
                + "COMMIT;";
        try (PreparedStatement delProdBlockSt = this.conn.prepareStatement(delProdBlockQuery)) {
            delProdBlockSt.setInt(1, prodBlockIdToDelete);
            delProdBlockSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting from database:\n" + ex);
            return false;
        }
        return true;
    }

    @Override
    public Production getProduction(Order orderToFetchProdFor) {
        Production prodToReturn = new Production();
        String getProdQuery = "SELECT prod_id,lot,plc_id,plc_name,ip,port,growth_id FROM prod_overview WHERE order_id = ?;";
        ResultSet getProdRs;
        try(PreparedStatement getProdSt = this.conn.prepareStatement(getProdQuery)){
            if(orderToFetchProdFor.getId() <= 0){
                throw new IllegalArgumentException("Order ID cannot be negative or zero.");
            }
            getProdSt.setInt(1, orderToFetchProdFor.getId());
            getProdRs = getProdSt.executeQuery();
            getProdRs.next();
            ProductionBlock prodBlock = new ProductionBlock();
            prodBlock.setId(getProdRs.getInt("plc_id"));
            prodBlock.setName(getProdRs.getString("plc_name"));
            prodBlock.setPort(getProdRs.getInt("port"));
            prodBlock.setIpaddress(getProdRs.getString("ip"));
            
            prodToReturn.setBlock(prodBlock);
            prodToReturn.setOrder(orderToFetchProdFor);
            prodToReturn.setGrowthProfile(this.getGrowthProfile(getProdRs.getInt("growth_id")));
            prodToReturn.setDataLogs(null); // Not filled in
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        } catch (IllegalArgumentException ex){
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return prodToReturn;
    }

    @Override
    public List<String> getDataLogFilterOptions() {
        List<String> filters = new ArrayList<>();
        String getDatFiltersQuery = "SELECT DISTINCT type FROM data;";
        try(Statement getDatFiltersSt = this.conn.createStatement();
                ResultSet getDatFiltersRs = getDatFiltersSt.executeQuery(getDatFiltersQuery)){
            while(getDatFiltersRs.next()){
                filters.add(getDatFiltersRs.getString("type"));
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        }
        return filters;
    }

    @Override
    public boolean deleteLightFromProfile(int growthProfileId, int lightToDelete) {
        String delLightQuery = "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;"
                + "DELETE FROM growthlight_view WHERE growth_id = ? AND light_id = ?;"
                + "COMMIT;";
        try(PreparedStatement delLightSt = this.conn.prepareStatement(delLightQuery)){
            if(growthProfileId <= 0 || lightToDelete <= 0){
                throw new IllegalArgumentException("Growthprofile ID or Light ID cannot be zero or less.");
            }
            delLightSt.setInt(1, growthProfileId);
            delLightSt.setInt(2, lightToDelete);
            delLightSt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting from database:\n" + ex);
            return false;
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<ProductionBlock> getAllProductionBlocks(String ip, int port) {
        List<ProductionBlock> prodBlocks = new ArrayList<>();
        ResultSet getAllPbRs;
        String getAllPbQuery = "SELECT plc_id,ip,port,name FROM plc_conn WHERE plc_id IN("
                + "SELECT plc_id FROM controls WHERE scada_id IN("
                    + "SELECT scada_id FROM scada_conn WHERE ip = ? AND port = ?"
                    + ")"
                + ");";
        try(PreparedStatement getAllPbSt = this.conn.prepareStatement(getAllPbQuery)){
            if(ip.isEmpty() || port == 0){
                throw new IllegalArgumentException("IP and port cannot be empty.");
            }
            getAllPbSt.setString(1, ip);
            getAllPbSt.setInt(2, port);
            getAllPbRs = getAllPbSt.executeQuery();
            while(getAllPbRs.next()){
                ProductionBlock localPb = new ProductionBlock();
                localPb.setId(getAllPbRs.getInt("plc_id"));
                localPb.setName(getAllPbRs.getString("name"));
                localPb.setPort(getAllPbRs.getInt("port"));
                localPb.setIpaddress(getAllPbRs.getString("ip"));
                prodBlocks.add(localPb);
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching from database:\n" + ex);
            return null;
        } catch (IllegalArgumentException ex){
            System.out.println("Invalid input: " + ex.getMessage());
            return null;
        }
        return prodBlocks;
    }

    @Override
    public boolean updateProductionBlock(ProductionBlock objectToUpdate) {
        String updProdBlockQuery = "UPDATE plc_conn SET ip = ?, port = ?, name = ? WHERE plc_id = ?;";
        try(PreparedStatement updProdBlockSt = this.conn.prepareStatement(updProdBlockQuery)){
            if(objectToUpdate.getId() <= 0){
                throw new IllegalArgumentException("Production block ID cannot be less than or equal to zero.");
            }
            updProdBlockSt.setString(1, objectToUpdate.getIpaddress());
            updProdBlockSt.setInt(2, objectToUpdate.getPort());
            updProdBlockSt.setString(3, objectToUpdate.getName());
            updProdBlockSt.setInt(4, objectToUpdate.getId());
            int result = updProdBlockSt.executeUpdate();
            if(result == 0){
                System.out.println("Query succesful. However, no rows were updated in database, ID may not exist.");
            }
        } catch (SQLException ex) {
            System.out.println("Error updating production block in database:\n" + ex);
            return false;
        } catch (IllegalArgumentException ex){
            System.out.println("Invalid input: " + ex.getMessage());
            return false;
        }
        return true;
    }
}
