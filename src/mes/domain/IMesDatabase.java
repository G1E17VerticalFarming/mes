/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import java.util.List;
import shared.*;          // Required when removing the dummy classes: ProductionBlock & GrowthProfile & Log in domain layer

/**
 *
 * @author chris
 */
public interface IMesDatabase {
    
    /**
     * Implemented fully
     *
     * Method to get a list of all active production blocks in database
     * @return List of all productionblocks present in the database
     */
    public abstract List<ProductionBlock> getProductionBlocks();
    
    /**
     * Implemented fully
     *
     * Will query the database for all Orders with the given parameter as their fetched_time.
     * 
     * Method will fail if dateStringRepresentation is not a valid string date (See method: isDateCorrectFormat(String dateToCheck))
     * @param dateStringRepresentation String date used to fetch all orders from database
     * @return List of Order objects 
     */
    public abstract List<Order> fetchOrders(String dateStringRepresentation);
    
    /**
     * Implemented fully
     *
     * Method to fetch a production block from database.
     * @param productionBlockId ID for production block to be fetched from database
     * @return Full ProductionBlock object
     */
    public abstract ProductionBlock getProductionBlock(int productionBlockId);
    
    /**
     * Implemented fully
     *
     * Method to fetch a growthprofile.
     * This method will invoke getLightSchedule(), which will fill up a list of Light objects.
     * @param growthProfileId ID for growthprofile to be fetched from database
     * @return A complete GrowthProfile object, with Light sequence.
     */
    public abstract GrowthProfile getGrowthProfile(int growthProfileId);
    
    /**
     * Implemented fully!
     *
     * Method to save a data log.
     * @param dataObjectToSave Log object to store in the database
     * @return True if dataObjectToSave is stored succesfully in database
     */
    public abstract boolean saveDataLog(Log dataObjectToSave);
    
    /**
     * Implemented fully
     *
     * Method to store a new growthProfile to the database.
     * Whenever a growthprofile is fetched and edited in the GUI layer, it will be treated as a new growthprofile.
     * Therefore there are no UPDATE procedure for growthprofiles, only ability to insert a new one.
     * @param profileToSave GrowthProfile object to split apart and save in Database
     * @return True on succesful save in database, false otherwise
     */
    public abstract boolean saveGrowthProfile(GrowthProfile profileToSave);
    
    /**
     * Implemented fully
     *
     * Method to store a list of Order objects.
     * This method will primarily be called when new orders from ERP needs to be stored locally.
     * @param orderObjectsToSave List of Order objects to store in database
     * @return True if the entire list has been succesfully saved, false otherwise
     */
    public abstract boolean saveOrders(List<Order> orderObjectsToSave);
    
    /**
     * Implemented fully
     *
     * This method will query the database and insert a new row into prod_overview.
     * This means that a production for a given order has begun. 
     * Method will fail if the attributes on prodObjectToSave are null
     * @param prodObjectToSave Production object to store in the database
     * @return True if stored succesfully in the database, false otherwise
     */
    public abstract boolean saveProduction(Production prodObjectToSave);
    
    /**
     * Implemented fully
     *
     * Inserts a new row into plc_conn.
     * Will use all attributes in prodBlockToSave as arguments for this SQL statement.
     * @param prodBlockToSave ProductionBlock object to save to database
     * @return True on successful save to database
     */
    public abstract boolean saveProductionBlock(ProductionBlock prodBlockToSave);
    
    /**
     * Implemented Fully
     * 
     * This method will call to generateLot, and receive a generated lot number for this Order object.
     * The method will then proceed to store production end date, status and the generated lot number to database.
     * 
     * This method will fail if orderObjectToUpd does not contain an ID greater than 0, productionEnd is not set, or if generateLot returns a null value.
     * @param orderObjectToUpd Order object to update in database. Must have valid ID and productionEnd set to be eligible
     * @return If orderObjectToUpd.productionEnd == null this returns false, true on successful update in database
     */
    public abstract boolean updateOrderEndDate(Order orderObjectToUpd);
    
    /**
     * Implemented fully
     *
     * Method to delete a dataLog from the database.
     * 
     * Method will fail if dataLogIdToDelete is not greater than 0
     * @param dataLogIdToDelete Data log ID to delete from database
     * @return True if data log is deleted succesfully, false otherwise
     */
    public abstract boolean deleteDataLog(int dataLogIdToDelete);
}
