/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java;

import API.IMesFacade;
import shared.ProductionBlock;

import mes.api.Greeting;
import java.util.concurrent.atomic.AtomicLong;
import mes.api.Json;
import mes.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /*@RequestMapping(value = "/log", method = RequestMethod.POST)
    public String saveLog(@RequestBody Log log) {
        mes.saveLog(log);
        return "lblSucccess";
    }*/
    
    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Test> createUser(@RequestBody Greeting greeting, UriComponentsBuilder ucBuilder) {
        return new ResponseEntity<Test>(greeting.getTest(), HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/prod_block/", method = RequestMethod.GET)
    public ResponseEntity<ProductionBlock> getAllProductionBlocks() {
        ProductionBlock prod_block = new ProductionBlock();
        prod_block.setName("hej");
        prod_block.setPort(2000);
        return new ResponseEntity<ProductionBlock>(prod_block, HttpStatus.OK);
    }
}
