/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.util.List;

/**
 *
 * @author chris
 */
public interface PersistenceReadWriteScadaConnections {
    
    /**
     * Implemented fully
     * 
     * This method will fetch current scada systems running and connected to this MES
     * @return List of Strings with scada entries represented as ip : port
     */
    public abstract List<String> getScadaEntries();
    
    /**
     * Implemented fully
     * 
     * Method to add a new SCADA entry to the database
     * @param ip IP of the new SCADA entry
     * @param port Port of the new SCADA entry
     * @return True on succesful save in database, false otherwise
     */
    public abstract boolean saveScadaEntry(String ip, int port);
    
    /**
     * Implemented fully
     * 
     * Method to remove a SCADA entry from the database
     * @param ip IP of the SCADA to remove
     * @param port Port of the SCADA to remove
     * @return True on succesful removal from database, false otherwise
     */
    public abstract boolean deleteScadaEntry(String ip, int port);
}
