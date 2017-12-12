/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import mes.domain.Order;
import mes.domain.Production;

/**
 *
 * @author chris
 */
public interface PersistenceReadWriteProduction {
    
    /**
     * Implemented fully
     * 
     * Method to fetch a Production object for a given Order.
     * This method will populate a Production object with an Order object, 
     * ProductionBlock object and GrowthProfile object
     * @param orderToFetchProdFor Order object to fetch production for
     * @return Full Production object
     */
    public abstract Production getProduction(Order orderToFetchProdFor);
    
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
    
    
}
