/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import mes.domain.Order;
import mes.domain.Production;

/**
 *
 * @author chris
 */
public interface DomainReadProduction {
    
    /**
     * Implemented fully
     *
     * Fetches a Production object for the given Order from the database.
     *
     * @param orderToFetchProdFor
     * @return Production
     */
    public abstract Production fetchProduction(Order orderToFetchProdFor);
}
