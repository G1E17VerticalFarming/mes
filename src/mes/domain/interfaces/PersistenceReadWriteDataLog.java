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
public interface PersistenceReadWriteDataLog {
    
    /**
     * Implemented fully
     * 
     * Method to get a list of all data logs in the database, filtered with filter parameter
     * @param filter String filter to use as filter in database query (Will filter on data.type)
     * @return List of all Log objects which fulfill the filter parameter
     */
    public abstract List<Log> getDataLogs(String filter);
    
    /**
     * Implemented fully!
     *
     * Method to save a data log.
     * @param dataObjectToSave Log object to store in the database
     * @return True if dataObjectToSave is stored succesfully in database
     */
    public abstract boolean saveDataLog(Log dataObjectToSave);
    
    /**
     * Implemented fully
     * 
     * Method to fetch a list of filter options for data logs
     * @return List of String objects represented in table data.type
     */
    public abstract List<String> getDataLogFilterOptions();
}
