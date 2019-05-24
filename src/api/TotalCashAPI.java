package api;

import java.sql.ResultSet;
import model.MySQL;

/**
 * This API allows you to get and set total cash of user
 * @author tungnguyen
 */
public class TotalCashAPI {
    
    /**
     * Get amount of total cash
     * @param userid if of user
     * @return total cash of user as an integer
     * @throws Exception 
     */
    public static int getTotalCash(String userid) throws Exception {
        int totalCash = 0;
        
        if (MySQL.isConnected()) {
            ResultSet user = MySQL.executeQuery( 
                "select totalCash from users "
                + "where userid = " + userid + ";"
            );
            if (user.isBeforeFirst()) {
                user.next();
                totalCash = user.getInt("totalCash");
            } else {
                totalCash = 0;
                throw new Exception("User not found");
            }
        } else {
            totalCash = 0;
            throw new Exception("Database is not connected");
        }
        
        return totalCash;
    }
    
    /**
     * Set new value to total cash
     * @param userid id of user
     * @param newTotalCash new value to set
     * @return true if action is successful, false if not
     * @throws Exception 
     */
    public static boolean setTotalCash(String userid, String newTotalCash) throws Exception {
        boolean setSuccess = false;
        
        if (MySQL.isConnected()) {
            int updateUsersStatusCode = MySQL.executeUpdate( 
                "update users set "
                + "totalCash = " + newTotalCash + " " 
                + "where userid = " + userid + ";"
            );
            if (updateUsersStatusCode != -1) {
                setSuccess = true;
            } else {
                setSuccess = false;
                throw new Exception("Update failed: users table");
            }
        } else {
            setSuccess = false;
            throw new Exception("Database is not connected");
        }
        
        return setSuccess;
    }
   
}
