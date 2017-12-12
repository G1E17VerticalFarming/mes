/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.time.LocalDate;

/**
 *
 * @author chris
 */
public interface DomainWriteDate {
    
    /**
     * Implemented fully
     *
     * Method to set date. This method takes a LocalDate variable and formats it
     * to a String. The date variable in SingletonMes is then set to be equal to
     * this String.
     *
     * @param date LocalDate selected in GUI and parsed through SingletonMes.
     */
    public void setDate(LocalDate date);
}
