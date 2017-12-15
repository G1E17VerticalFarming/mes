/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

/**
 *
 * @author chris
 */
public interface DomainReadLogin {
    
    /**
     * Implemented fully
     *
     * Method to check if login is certified.
     * Currently only has a dummy implementation
     *
     * @param username
     * @param password
     * @return boolean reporting on whether or not the call failed.
     */
    public abstract boolean loginCertified(String username, String password);
}
