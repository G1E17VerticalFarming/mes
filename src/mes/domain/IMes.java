/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain;

import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author chris
 */
public interface IMes {
    
    public void setDate(LocalDate date);
    public List fetchOrders();
    public List fetchStatuses();
    public List fetchGrowthProfiles();
    public boolean prepareOrder();
    public List fetchDataLogs();
    public boolean loginCertified(String username, String password);
    
}
