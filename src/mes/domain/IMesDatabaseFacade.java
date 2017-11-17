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
    
    public List<ProductionBlock> getProductionBlocks();
    public List<Order> fetchOrders(String dateStringRepresentation);
    public ProductionBlock getProductionBlock(int productionBlockId);
    public GrowthProfile getGrowthProfile(int growthProfileId);
    
    public boolean saveDataLog(Log dataObjectToSave);
    public boolean saveGrowthProfile(GrowthProfile profileToSave);
    public boolean saveOrders(List<Order> orderObjectsToSave);
    public boolean saveOrder(Order orderObjectToSave);
    public boolean saveProduction(Production prodObjectToSave);
    
    public boolean deleteDataLog(int dataLogIdToDelete);
}
