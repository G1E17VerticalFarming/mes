/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java;

import API.IMesFacade;
import shared.ProductionBlock;
import shared.GrowthProfile;
import shared.Log;
import mes.api.ApiController;

//import mes.api.Greeting;
import java.util.concurrent.atomic.AtomicLong;
//import mes.api.Json;
//import mes.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author DanielToft
 */
@RestController
public class ApiFacade {
    
    ApiController apiController;
    
    public ApiFacade() {
        this.apiController = new ApiController();
    }
    
    // Get the MES singleton class

    /*@RequestMapping(value = "/log", method = RequestMethod.POST)
    public String saveLog(@RequestBody Log log) {
        mes.saveLog(log);
        return "lblSucccess";
    }*/
    
    /*@RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Test> createUser(@RequestBody Greeting greeting, UriComponentsBuilder ucBuilder) {
        return new ResponseEntity<Test>(greeting.getTest(), HttpStatus.CREATED);
    }*/
    
    @RequestMapping(value = "/production_block/", method = RequestMethod.GET)
    public ResponseEntity<ProductionBlock[]> getAllProductionBlocks() {
        // Fetch all production blocks from
        ResponseEntity<ProductionBlock[]> returnResp = this.apiController.getAllProductionBlocks();
        return returnResp;
    }
    
    //@PathVariable String userId
    @RequestMapping(value = "/production_block/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductionBlock> getAllProductionBlocks(@PathVariable int id) {
        // Fetch all production blocks from
        ResponseEntity<ProductionBlock> returnResp = this.apiController.getSpecificProductionBlock(id);
        return returnResp;
    }
    
    @RequestMapping(value = "/production_block/", method = RequestMethod.POST)
    public ResponseEntity<String> postLog(@RequestBody ProductionBlock pb) {
        ResponseEntity<String> returnResp = this.apiController.saveProductionBlock(pb);
        return returnResp;
    }
    
    @RequestMapping(value = "/growth_profile/{id}", method = RequestMethod.GET)
    public ResponseEntity<GrowthProfile> getSpecificGrowthProfile(@PathVariable int id) {
        // Fetch all production blocks from
        ResponseEntity<GrowthProfile> returnResp = this.apiController.getSpecificGrowthProfile(id);
        return returnResp;
    }
    
    @RequestMapping(value = "/log/", method = RequestMethod.POST)
    public ResponseEntity<String> postLog(@RequestBody Log log) {
        ResponseEntity<String> returnResp = this.apiController.saveLog(log);
        return returnResp;
    }
    
    
}
