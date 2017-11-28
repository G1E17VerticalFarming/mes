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
public interface IMesDatabaseFacade {
    
    public abstract List<ProductionBlock> getProductionBlocks();
    public abstract List<Order> fetchOrders(String dateStringRepresentation);
    public abstract ProductionBlock getProductionBlock(int productionBlockId);
    public abstract GrowthProfile getGrowthProfile(int growthProfileId);
    public abstract Production getProduction(int productionId);
    
    public abstract boolean saveDataLog(Log dataObjectToSave);
    public abstract boolean saveGrowthProfile(GrowthProfile profileToSave);
    public abstract boolean saveOrders(List<Order> orderObjectsToSave);
    public abstract boolean saveProduction(Production prodObjectToSave);
    public abstract boolean saveProductionBlock(ProductionBlock prodBlockToSave);
    
    public abstract boolean updateProductionLot(Production prodObjectToUpd);
    public abstract boolean updateOrderEndDate(Order orderObjectToUpd);
    
    public abstract boolean deleteDataLog(int dataLogIdToDelete);
}
