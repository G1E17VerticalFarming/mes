/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;


/**
 *
 * @author AKT
 */
public class Order {
    private int id;
    private String fetchedTime;
    private int quantity;
    private int status;
    private String productionName;
    private String productionBegin;
    private String productionEnd;

    public void setId(int id) {
        this.id = id;
    }

    public void setFetchedTime(String fetchedTime) {
        this.fetchedTime = fetchedTime;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }

    public void setProductionBegin(String productionBegin) {
        this.productionBegin = productionBegin;
    }

    public void setProductionEnd(String productionEnd) {
        this.productionEnd = productionEnd;
    }

    public int getId() {
        return id;
    }

    public String getFetchedTime() {
        return fetchedTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStatus() {
        return status;
    }

    public String getProductionName() {
        return productionName;
    }

    public String getProductionBegin() {
        return productionBegin;
    }

    public String getProductionEnd() {
        return productionEnd;
    }
}
