/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.util.List;
import shared.Log;

/**
 *
 * @author chris
 */
public interface DomainReadWriteDataLog {
    
    /**
     * Implemented fully
     *
     * Fetches data log filters from the database. These are used to filter the
     * data log tableview in the GUI. Getting filters directly from the database
     * means that, if filters are added or changed there, then this is also done
     * in the MES application.
     *
     * @return List of data log filters
     */
    public abstract List<String> fetchLogFilters();

    /**
     * Implemented fully
     *
     * Saves a new data log in the database. Only production block, value and
     * production id can be set by the person creating the log. CMD, type and
     * timestamp is all set by this method. CMD is 0 for no command, type is
     * "manuel kommentar" and the timestamp is set when the entry is created.
     *
     * @param block
     * @param value
     * @param prodId
     */
    public abstract void saveDataLog(int block, String value, int prodId);
    
    /**
     * Implemented fully
     *
     * Method to fetch all data logs from the database.
     *
     * @param filter
     * @return List consisting of Log objects.
     */
    public abstract List<Log> fetchDataLogs(String filter);
}
