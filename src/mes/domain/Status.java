/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

/**
 * Class used to hold status for an order object
 * @author chris
 */
public class Status {
    private int id;
    private String value;
    
    public Status(){
        
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
