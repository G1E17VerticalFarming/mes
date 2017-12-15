/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.util.List;
import mes.domain.Order;
import mes.domain.Status;
import shared.GrowthProfile;

/**
 *
 * @author chris
 */
public interface DomainReadWriteOrder {
    
    /**
     * Implemented fully
     *
     * Method to fetch orders from the database for the selected date.
     * @return List consisting of order objects.
     */
    public abstract List<Order> fetchOrders();
    
    /**
     * Implemented fully
     *
     * Method to fetch all order statuses from the database.
     * @return List containing order statuses
     */
    public abstract List<Status> fetchStatuses();
    
    /**
     * Implemente fully
     *
     * Method to prepare a production and save an order in the database.
     * @param currentOrder Order object to begin a production
     * @param status Status object to set to
     * @param growthProfile GrowthProfile object this production uses
     * @param prodBlock Production Block ID this production will be handled by
     * @return boolean reporting on whether the call failed or not.
     */
    public abstract boolean prepareOrder(Order currentOrder, Status status, GrowthProfile growthProfile, int prodBlock);
}
