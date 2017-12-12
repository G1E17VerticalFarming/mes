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
public interface DomainReadWriteProductionBlock {
    
    /**
     * Not implemented
     *
     * Method to allocate a production block for an order. Calls a method in
     * AllocationController and returns a suggested production block.
     *
     * @return ProductionBlock object consisting of a production block ID and
     * name.
     */
    public abstract ProductionBlock allocateProductionBlock();
    
    /**
     * Implemented fully
     *
     * Fetches ProductionBlocks that are currently in use from the database.
     *
     * @return List of ProductionBlock objects.
     */
    public abstract List<ProductionBlock> fetchActiveProductionBlocks();
}
