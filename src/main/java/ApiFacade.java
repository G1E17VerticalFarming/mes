/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java;

import shared.ProductionBlock;
import shared.GrowthProfile;
import shared.Log;
import mes.api.ApiReceiveController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This is the class to recieve calls through the REST API.
 * Think of this as the methods that SCADA calls upon.
 * @author DanielToft
 */
@RestController
public class ApiFacade {
    
    ApiReceiveController apiReceiveController;
    
    /**
     * Constructor
     */
    public ApiFacade() {
        this.apiReceiveController = new ApiReceiveController();
    }
    
    /**
     * Method being called through REST API.
     * 
     * Method to get all production blocks based on ip and port
     * @param ip SCADA IP to fetch all production blocks from
     * @param port SCADA port to fetch all production blocks from
     * @return Array of production blocks found in database
     */
    @RequestMapping(value = "/{ip}/{port}/production_block/", method = RequestMethod.GET)
    public ResponseEntity<ProductionBlock[]> getAllProductionBlocks(@PathVariable String ip, @PathVariable int port) {
        ResponseEntity<ProductionBlock[]> returnResp = this.apiReceiveController.getAllProductionBlocks(ip, port);
        return returnResp;
    }
    
    /**
     * Method being called through REST API.
     * 
     * Method to fetch a specific production block
     * @param id ID of production block to fetch
     * @param ip SCADA IP to fetch all production blocks from
     * @param port SCADA port to fetch all production blocks from
     * @return A single Production Block object with the given id, from SCADA connection with IP and Port
     */
    @RequestMapping(value = "/{ip}/{port}/production_block/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductionBlock> getSpecificProductionBlocks(@PathVariable int id, @PathVariable String ip, @PathVariable int port) {
        ResponseEntity<ProductionBlock> returnResp = this.apiReceiveController.getSpecificProductionBlock(id, ip, port);
        return returnResp;
    }
    
    /**
     * Method being called through REST API.
     * 
     * Method to save a production block.
     * @param pb Production block object to save in database
     * @param ip SCADA IP to save this production block with
     * @param port SCADA port to save this production block with
     * @return Result from trying to save production block
     */
    @RequestMapping(value = "/{ip}/{port}/production_block/", method = RequestMethod.POST)
    public ResponseEntity<String> saveProductionBlock(@RequestBody ProductionBlock pb, @PathVariable String ip, @PathVariable int port) {
        ResponseEntity<String> returnResp = this.apiReceiveController.saveProductionBlock(pb, ip, port);
        return returnResp;
    }
    
    /**
     * Method being called through REST API.
     * 
     * Method to update a production block
     * @param pb Production block object to update
     * @return Result from trying to update production block
     */
    @RequestMapping(value = "/production_block/update/", method = RequestMethod.POST)
    public ResponseEntity<String> updateProductionBlock(@RequestBody ProductionBlock pb) {
        ResponseEntity<String> returnResp = this.apiReceiveController.updateProductionBlock(pb);
        return returnResp;
    }
    
    /**
     * Method being called through REST API.
     * 
     * Method to delete a production block
     * @param pb Production block object to delete
     * @return Result from trying to delete production block
     */
    @RequestMapping(value = "/production_block/delete/", method = RequestMethod.POST)
    public ResponseEntity<String> deleteProductionBlock(@RequestBody ProductionBlock pb) {
        ResponseEntity<String> returnResp = this.apiReceiveController.deleteProductionBlock(pb);
        return returnResp;
    }
    
    /**
     * Method being called through REST API.
     * 
     * Method to fetch a specific growth profile
     * @param id The ID of which growthprofile to fetch
     * @return Growthprofile object with the given ID
     */
    @RequestMapping(value = "/growth_profile/{id}", method = RequestMethod.GET)
    public ResponseEntity<GrowthProfile> getSpecificGrowthProfile(@PathVariable int id) {
        ResponseEntity<GrowthProfile> returnResp = this.apiReceiveController.getSpecificGrowthProfile(id);
        return returnResp;
    }
    
    /**
     * Method being called through REST API.
     * 
     * Method to save a datalog coming from SCADA system
     * @param log Log object to save in database
     * @return Result from trying to save datalog
     */
    @RequestMapping(value = "/log/", method = RequestMethod.POST)
    public ResponseEntity<String> postLog(@RequestBody Log log) {
        ResponseEntity<String> returnResp = this.apiReceiveController.saveLog(log);
        return returnResp;
    }
    
    /**
     * Method being called through REST API.
     * 
     * Ping method used to test if connection is still alive.
     * @return True or false if there is connection. 
     */
    @RequestMapping(value = "/ping/", method = RequestMethod.GET)
    public ResponseEntity<Boolean> ping() {
        ResponseEntity<Boolean> returnResp = this.apiReceiveController.ping();
        return returnResp;
    }
    
}
