/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.util.ArrayList;
import java.util.List;
import shared.GrowthProfile;
import shared.Light;

/**
 *
 * @author chris
 */
public interface DomainReadWriteGrowthProfile {
    
    /**
     * Implemented fully
     *
     * Method to fetch all growth profiles from the database.
     * @return List consisting of GrowthProfile objects.
     */
    public abstract List<GrowthProfile> fetchGrowthProfiles();
    
    /**
     * Implemented fully
     *
     * Method to delete growth profile from the database, based on an id.
     * @param id
     */
    public abstract void deleteGrowthProfile(int id);
    
    /**
     * Implemented fully
     *
     * Method to save a growth profile in the database. 
     * Each profile is saved as a new entry in the database.
     * @param profileToSave
     */
    public abstract void saveGrowthProfile(GrowthProfile profileToSave);
    
    /**
     * Implemented fully
     *
     * Fetches the possible light types from the database and returns
     * them as an array of strings.
     * @return String[] array of light types.
     */
    public abstract String[] getGrowthProfileLightTypes();
    
    /**
     * Implemented fully
     *
     * Sets the light sequence of the growth profile given in the parameter, to 
     * be equal to the tempGrowthProfileLights variable in SingletonMES.
     * @param gp The growth profile for which lights are being set.
     */
    public abstract void setGrowthProfileLights(GrowthProfile gp);
    
    /**
     * Implemented fully
     *
     * Sets the variable tempGrowthProfileLights to be equal to the List lights
     * given in the parameter.
     * @param lights A list of Light objects
     */
    //public abstract void setTempGrowthProfileLights(ArrayList lights);
    
    /**
     * Implemented fully
     *
     * returns tempGrowthProfileLights, which is an ArrayList containing Light objects.
     * @return ArrayList
     */
    //public abstract ArrayList<Light> getTempGrowthProfileLights();
    
    /**
     * Implemented fully
     *
     * Removes a Light object from tempGrowthProfileLights at the index given in 
     * the parameter.
     * @param index of the light to be removed.
     */
    //public abstract void removeTempGrowthProfileLight(int index);
    
    /**
     * Implemented fully
     *
     * Adds the Light object from the parameter to the variable tempGrowthProfileLights.
     * @param light to be added
     */
    public abstract void addGrowthProfileLight(Light light);
    
    /**
     * Implemented fully
     *
     * Creates an instance of Light from the given parameters.
     * @param type
     * @param time
     * @param value
     * @return Light
     */
    public abstract Light createGrowthProfileLight(int type, int time, int value);
}
