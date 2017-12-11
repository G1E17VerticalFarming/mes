/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import java.util.List;
import shared.GrowthProfile;
import shared.Log;
import shared.ProductionBlock;

/**
 *
 * @author Daniel
 */
public interface IMesApi {
    
    /**
     * Implemented fully
     *
     * Fetches ProductionBlocks that are currently in use from the database.
     * @return List of ProductionBlock objects.
     */
    public abstract List<ProductionBlock> fetchAllProductionBlocks(String ip, int port);
    
    /**
     * Implemented fully
     *
     * Inserts a new row into plc_conn.
     * Will use all attributes in prodBlockToSave as arguments for this SQL statement.
     * @param prodBlockToSave ProductionBlock object to save to database
     * @return True on successful save to database
     */
    public abstract boolean saveProductionBlock(ProductionBlock prodBlockToSave, String ip, int port);
    
    public abstract boolean deleteProductionBlock(ProductionBlock prodBlockToSave);
    
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
}
