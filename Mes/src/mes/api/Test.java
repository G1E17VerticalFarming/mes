/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.api;

/**
 *
 * @author DanielToft
 */
public class Test {
    private int id;
    private String testString;
    
    public Test(){
        
    }
    
    public Test(int id, String testString) {
        this();
        this.id = id;
        this.testString = testString;
    }

    public int getId() {
        return id;
    }

    public String getTestString() {
        return testString;
    }
    
    @Override
    public String toString(){
        return this.id + ":" + this.testString;
    }
}
