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
 * @author AKT
 */
public class Production {
    private int id;
    private GrowthProfile growthProfile;
    private String lot;
    private ProductionBlock block;
    private Order order;
    private List<Log> dataLogs;

    public void setId(int id) {
        this.id = id;
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

    public void setDataLogs(List<Log> dataLogs) {
        this.dataLogs = dataLogs;
    }

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

    public List<Log> getDataLogs() {
        return dataLogs;
    }
}
