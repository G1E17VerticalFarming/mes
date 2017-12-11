/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.api;

import shared.ProductionBlock;
import shared.GrowthProfile;
import shared.Log;

import mes.domain.IMesApi;
import mes.domain.SingletonMES;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author DanielToft
 */
public class ApiReceiveController {
    
    private IMesApi mes;
    
    public ApiReceiveController() {
        this.mes = SingletonMES.getInstance();
    }
    
    public ResponseEntity<ProductionBlock[]> getAllProductionBlocks(String ip, int port) {
        ProductionBlock[] pbArr = this.mes.fetchAllProductionBlocks(ip, port).toArray(new ProductionBlock[0]);
        return new ResponseEntity<ProductionBlock[]>(pbArr, HttpStatus.OK);
    }
    
    public ResponseEntity<ProductionBlock> getSpecificProductionBlock(int id, String ip, int port) {
        ProductionBlock pb = null;
        for(ProductionBlock productionBlock : this.mes.fetchAllProductionBlocks(ip, port)) {
            if(productionBlock.getId() == id) {
                pb = productionBlock;
                break;
            }
        }
        return new ResponseEntity<ProductionBlock>(pb, HttpStatus.OK);
    }
    
    public ResponseEntity<String> saveProductionBlock(ProductionBlock pb, String ip, int port) {
        this.mes.saveProductionBlock(pb, ip, port);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
    
    public ResponseEntity<String> deleteProductionBlock(ProductionBlock pb) {
        return new ResponseEntity<String>("" + this.mes.deleteProductionBlock(pb), HttpStatus.OK);
    }
    
    public ResponseEntity<GrowthProfile> getSpecificGrowthProfile(int id) {
        GrowthProfile gp = this.mes.getGrowthProfile(id);
        return new ResponseEntity<GrowthProfile>(gp, HttpStatus.OK);
    }
    
    public ResponseEntity<String> saveLog(Log log) {
        this.mes.saveDataLog(log);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
    
    public ResponseEntity<Boolean> ping() {
        //return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        return new ResponseEntity<Boolean>(this.mes != null, HttpStatus.OK);
    }
}
