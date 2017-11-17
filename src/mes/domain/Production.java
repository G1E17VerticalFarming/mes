/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import shared.GrowthProfile;
import shared.ProductionBlock;

/**
 *
 * @author chris
 */
public class Production {
    private int id;
    private GrowthProfile growthProfile;
    private String lot;
    private ProductionBlock block;
    private Order order;

    public int getId() {
        return id;
    }

    public GrowthProfile getGrowthProfile() {
        return growthProfile;
    }

    public String getLot() {
        return lot;
    }

    public ProductionBlock getBlock() {
        return block;
    }

    public Order getOrder() {
        return order;
    }

    public void setGrowthProfile(GrowthProfile growthProfile) {
        this.growthProfile = growthProfile;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public void setBlock(ProductionBlock block) {
        this.block = block;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
