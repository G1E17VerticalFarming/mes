/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.api;

import shared.ProductionBlock;
import shared.GrowthProfile;
import shared.Log;

import mes.domain.IMes;
import mes.domain.SingletonMES;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author DanielToft
 */
public class ApiReceiveController {
    
    private IMes mes;
    
    public ApiReceiveController() {
        this.mes = SingletonMES.getInstance();
    }
    
    public ResponseEntity<ProductionBlock[]> getAllProductionBlocks() {
        ProductionBlock[] pbArr = this.mes.fetchActiveProductionBlocks().toArray(new ProductionBlock[0]);
        return new ResponseEntity<ProductionBlock[]>(pbArr, HttpStatus.OK);
    }
    
    public ResponseEntity<ProductionBlock> getSpecificProductionBlock(int id) {
        ProductionBlock pb = null;
        for(ProductionBlock productionBlock : this.mes.fetchActiveProductionBlocks()) {
            if(productionBlock.getId() == id) {
                pb = productionBlock;
                break;
            }
        }
        return new ResponseEntity<ProductionBlock>(pb, HttpStatus.OK);
    }
    
    public ResponseEntity<String> saveProductionBlock(ProductionBlock pb) {
        //Save log!
        System.out.println(pb.getName());
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
    
    
    public ResponseEntity<GrowthProfile> getSpecificGrowthProfile(int id) {
        GrowthProfile gp = new GrowthProfile();
        gp.setId(id);
        return new ResponseEntity<GrowthProfile>(gp, HttpStatus.OK);
    }
    
    public ResponseEntity<String> saveLog(Log log) {
        //Save log!
        System.out.println(log.getType());
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
    
    public ResponseEntity<Boolean> ping() {
        //return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        return new ResponseEntity<Boolean>(this.mes != null, HttpStatus.OK);
    }
}
