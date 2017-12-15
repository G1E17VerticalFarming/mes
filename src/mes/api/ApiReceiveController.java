/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.api;

import shared.ProductionBlock;
import shared.GrowthProfile;
import shared.Log;

import mes.domain.interfaces.DomainReadWriteProductionBlock;
import mes.domain.interfaces.DomainReadWriteGrowthProfile;
import mes.domain.interfaces.DomainReadWriteDataLog;
import mes.domain.SingletonMES;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Class to handle HTTP requests. 
 * @author DanielToft
 */
public class ApiReceiveController {
    
    private DomainReadWriteProductionBlock prodBlockMes;
    private DomainReadWriteGrowthProfile groProfMes;
    private DomainReadWriteDataLog dataLogMes;
    
    /**
     * Constructor to fetch MES instances to perform method calls upon
     */
    public ApiReceiveController() {
        this.prodBlockMes = SingletonMES.getInstance();
        this.groProfMes = SingletonMES.getInstance();
        this.dataLogMes = SingletonMES.getInstance();
    }
    
    /**
     * Method to fetch all production blocks
     * @param ip SCADA IP to fetch all production blocks from
     * @param port SCADA port to fetch all production blocks from
     * @return Array of production blocks found in database and a HTTP response code
     */
    public ResponseEntity<ProductionBlock[]> getAllProductionBlocks(String ip, int port) {
        System.out.println("GET ALL PRODUCTION BLOCKS");
        ProductionBlock[] pbArr = this.prodBlockMes.fetchAllProductionBlocks(ip, port).toArray(new ProductionBlock[0]);
        return new ResponseEntity<ProductionBlock[]>(pbArr, HttpStatus.OK);
    }
    
    /**
     * Method to fetch a specific production block
     * @param id ID of production block to fetch
     * @param ip SCADA IP to fetch all production blocks from
     * @param port SCADA port to fetch all production blocks from
     * @return A single Production Block object with the given id, from SCADA connection with IP and Port and a HTTP response code.
     */
    public ResponseEntity<ProductionBlock> getSpecificProductionBlock(int id, String ip, int port) {
        ProductionBlock pb = null;
        for(ProductionBlock productionBlock : this.prodBlockMes.fetchAllProductionBlocks(ip, port)) {
            if(productionBlock.getId() == id) {
                pb = productionBlock;
                break;
            }
        }
        return new ResponseEntity<ProductionBlock>(pb, HttpStatus.OK);
    }
    
    /**
     * Method to save a production block.
     * @param pb Production block object to save in database
     * @param ip SCADA IP to save this production block with
     * @param port SCADA port to save this production block with
     * @return Result from trying to save production block and a HTTP response code
     */
    public ResponseEntity<String> saveProductionBlock(ProductionBlock pb, String ip, int port) {
        this.prodBlockMes.saveProductionBlock(pb, ip, port);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
    
    /**
     * Method to update a production block
     * @param pb Production block object to update
     * @return Result from trying to update production block and a HTTP response code
     */
    public ResponseEntity<String> updateProductionBlock(ProductionBlock pb) {
        return new ResponseEntity<String>("" + this.prodBlockMes.updateProductionBlock(pb), HttpStatus.OK);
    }
    
    /**
     * Method to delete a production block
     * @param pb Production block object to delete
     * @return Result from trying to delete production block and a HTTP response code
     */
    public ResponseEntity<String> deleteProductionBlock(ProductionBlock pb) {
        return new ResponseEntity<String>("" + this.prodBlockMes.deleteProductionBlock(pb), HttpStatus.OK);
    }
    
    /**
     * Method to fetch a specific growth profile
     * @param id The ID of which growthprofile to fetch
     * @return Growthprofile object with the given ID and a HTTP response code
     */
    public ResponseEntity<GrowthProfile> getSpecificGrowthProfile(int id) {
        GrowthProfile gp = this.groProfMes.getGrowthProfile(id);
        return new ResponseEntity<GrowthProfile>(gp, HttpStatus.OK);
    }
    
    /**
     * Method to save a datalog coming from SCADA system
     * @param log Log object to save in database
     * @return Result from trying to save datalog and HTTP response code (200)(OK)
     */
    public ResponseEntity<String> saveLog(Log log) {
        this.dataLogMes.saveDataLog(log);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
    
    /**
     * Ping method used to test if connection is still alive.
     * @return True or false if there is connection. 
     */
    public ResponseEntity<Boolean> ping() {
        return new ResponseEntity<Boolean>((this.prodBlockMes != null), HttpStatus.OK);
    }
}
