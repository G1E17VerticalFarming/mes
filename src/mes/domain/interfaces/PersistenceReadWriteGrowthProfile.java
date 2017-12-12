/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mes.domain.interfaces;

import java.util.List;
import shared.GrowthProfile;

/**
 *
 * @author chris
 */
public interface PersistenceReadWriteGrowthProfile {
    
    /**
     * Implemented fully
     *
     * Method to store a new growthProfile to the database.
     * Whenever a growthprofile is fetched and edited in the GUI layer, it will be treated as a new growthprofile.
     * Therefore there are no UPDATE procedure for growthprofiles, only ability to insert a new one.
     * @param profileToSave GrowthProfile object to split apart and save in Database
     * @return True on succesful save in database, false otherwise
     */
    public abstract boolean saveGrowthProfile(GrowthProfile profileToSave);
    
    /**
     * Implemented fully
     *
     * Method to fetch a growthprofile.
     * This method will invoke getLightSchedule(), which will fill up a list of Light objects.
     * @param growthProfileId ID for growthprofile to be fetched from database
     * @return A complete GrowthProfile object, with Light sequence.
     */
    public abstract GrowthProfile getGrowthProfile(int growthProfileId);
    
    /**
     * Implemented fully
     * 
     * @param growthProfileIdToDelete GrowthProfile ID to delete from database
     * @return True if growth profile has been succesfully deleted
     */
    public abstract boolean deleteGrowthProfile(int growthProfileIdToDelete);
    
    /**
     * Implemented fully
     * 
     * Method to remove a light sequence from a given growthprofile
     * @param growthProfileId Growth profile id to remove a light from
     * @param lightToDelete Light ID to remove from database
     * @return True on succesful delete from Database, false otherwise
     */
    public abstract boolean deleteLightFromProfile(int growthProfileId, int lightToDelete);
    
    /**
     * Implemented fully
     * 
     * This method will fetch all growthprofiles stored in database with associated light schedules
     * @return A List containing GrowthProfile objects
     */
    public abstract List<GrowthProfile> getGrowthProfiles();
}
