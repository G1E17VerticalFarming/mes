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
public interface DomainReadWriteScadaConnections {
    
    /**
     * Implemented fully
     *
     * Method to fetch scada connections from the database. Is formatted as
     * "ip:port" and returned as a list of strings.
     *
     * @return List of Strings representing scada connections.
     */
    public List fetchScadaConnections();

    /**
     * Implemented fully
     *
     * Method to save a new scada connection in the database. Takes an IP
     * address and a port as parameters and parses that on to the database.
     *
     * @param ip
     * @param port
     */
    public void saveScadaConnection(String ip, int port);

    /**
     * Implemented fully
     *
     * Method to delete a scada connection in the database. Takes a complete
     * "ip:port" scada entry string and splits it at ":". Then, the ip and port
     * are parsed on to the database.
     *
     * @param scadaEntry
     */
    public void deleteScadaConnection(String scadaEntry);
}
