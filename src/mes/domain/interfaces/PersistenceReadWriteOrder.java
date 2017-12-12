/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.util.List;
import mes.domain.Order;
import mes.domain.Status;

/**
 *
 * @author chris
 */
public interface PersistenceReadWriteOrder {
    
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
     * Method to get a map of all available orderStatuses in database
     * @return List of Status objects containing a key-value pair of an identifier and a value
     */
    public abstract List<Status> getOrderStatuses();
    
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
}
