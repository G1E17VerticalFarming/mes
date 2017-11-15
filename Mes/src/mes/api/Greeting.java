/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.api;

import shared.ProductionBlock;

/**
 *
 * @author DanielToft
 */
public class Greeting {

    private long id;
    private String content;
    private Test test;
    private ProductionBlock pb;

    public Greeting() {
        this.test = new Test(213333, "dsawo intter test");
        this.pb = new ProductionBlock();
    }
    
    public Greeting(long id, String content) {
        this();
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id + 27;
    }

    public String getContent() {
        return content;
    }
    
    public Test getTest() {
        return this.test;
    }

    @Override
    public String toString() {
        return this.id + ":" + this.content + ":" +this.test.getTestString();
    }
    
    
}
