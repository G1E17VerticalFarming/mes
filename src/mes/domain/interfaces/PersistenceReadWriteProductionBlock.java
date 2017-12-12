/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.util.List;
import shared.ProductionBlock;

/**
 *
 * @author chris
 */
public interface PersistenceReadWriteProductionBlock {
    
    /**
     * Implemented fully
     *
     * Method to get a list of all active production blocks in database
     * @return List of all actively working productionblocks in the database
     */
    public abstract List<ProductionBlock> getActiveProductionBlocks();
    
    /**
     * Implemented fully
     * 
     * Method to get a list of all idle production blocks in database
     * @return List of all productionblocks present in plc_conn, but only those idle.
     */
    public abstract List<ProductionBlock> getIdleProductionBlocks();
    
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
     * Inserts a new row into plc_conn.
     * Will use all attributes in prodBlockToSave as arguments for this SQL statement.
     * @param prodBlockToSave ProductionBlock object to save to database
     * @param scadaIp SCADA IP this production block is connected to
     * @param scadaPort SCADA port this production block is connected to
     * @return True on successful save to database
     */
    public abstract boolean saveProductionBlock(ProductionBlock prodBlockToSave,String scadaIp, int scadaPort);
    
    /**
     * Implemented fully
     * 
     * Method to recieve every single plc_conn based on a ip and port. 
     * Used if a SCADA program crashes and needs to recieve all its production blocks
     * again after restart
     * @param ip SCADA IP to fetch all prod blocks from
     * @param port SCADA port to fetch all prod blocks from
     * @return List of all production blocks for a given ip and port.
     */
    public abstract List<ProductionBlock> getAllProductionBlocks(String ip,int port);
    
    /**
     * Implemented fully
     * 
     * Method to delete a production block from database. This is used if a PLC
     * goes from online to offline
     * @param prodBlockIdToDelete The ID of the production block to delete from database
     * @return True on succesful delete from database, false otherwise
     */
    public abstract boolean deleteProductionBlock(int prodBlockIdToDelete);
}
