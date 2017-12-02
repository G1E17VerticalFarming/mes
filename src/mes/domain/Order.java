/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import javafx.beans.property.*;

/**
 *
 * @author AKT
 */
public class Order {
<<<<<<< HEAD
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty fetchedTime = new SimpleStringProperty();
    private SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleStringProperty productionName = new SimpleStringProperty();
    private SimpleStringProperty productionBegin = new SimpleStringProperty();
    private SimpleStringProperty productionEnd = new SimpleStringProperty();
=======
    private int id;
    private String fetchedTime;
    private int quantity;
    private int status;
    private String productionName;
    private String productionBegin;
    private String productionEnd;
>>>>>>> PersistenceLayerContinued

    public void setId(int id) {
        this.id.set(id);
    }

    public void setFetchedTime(String fetchedTime) {
        this.fetchedTime.set(fetchedTime);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

<<<<<<< HEAD
    public void setStatus(String status) {
        this.status.set(status);
=======
    public void setStatus(int status) {
        this.status = status;
>>>>>>> PersistenceLayerContinued
    }

    public void setProductionName(String productionName) {
        this.productionName.set(productionName);
    }

    public void setProductionBegin(String productionBegin) {
        this.productionBegin.set(productionBegin);
    }

    public void setProductionEnd(String productionEnd) {
        this.productionEnd.set(productionEnd);
    }

    public int getId() {
        return id.get();
    }

    public String getFetchedTime() {
        return fetchedTime.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

<<<<<<< HEAD
    public String getStatus() {
        return status.get();
=======
    public int getStatus() {
        return status;
>>>>>>> PersistenceLayerContinued
    }

    public String getProductionName() {
        return productionName.get();
    }

    public String getProductionBegin() {
        return productionBegin.get();
    }

    public String getProductionEnd() {
        return productionEnd.get();
    }
}
