package api;

import java.sql.ResultSet;
import java.util.HashMap;
import model.MySQL;

/**
 * This API allows you to get user information in users table
 * @author tungnguyen
 */
public class UsersAPI {
    
    /**
     * Get user information
     * @param userid id of user
     * @return a HashMap<String, String> contains userid, fullName, totalCash of user
     * @throws Exception 
     */
    public static HashMap<String, String> getUserInfo(String userid) throws Exception {
        HashMap<String, String> userInfo = null;
        
        if (MySQL.isConnected()) {
            userInfo = new HashMap<>();
            ResultSet userInfoInSet = MySQL.executeQuery( 
                "select * from users "
                + "where userid = " + userid + ";"
            );
            if (userInfoInSet.isBeforeFirst()) {
                userInfoInSet.next();
                
                userInfo.put("userid", userInfoInSet.getString("userid"));
                userInfo.put("fullName", userInfoInSet.getString("fullName"));
                userInfo.put("totalCash", Integer.toString(userInfoInSet.getInt("totalCash")));

            } else {
                userInfo = null;
                throw new Exception("User not found");
            }
        } else {
            userInfo = null;
            throw new Exception("Database is not connected");
        }
        
        return userInfo;
    }    
    
}
