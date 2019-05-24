package api;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MySQL;

import org.apache.commons.lang3.SerializationUtils;
import utils.EncryptTool;
import utils.TokenTool;

/**
 *
 * @author tungnguyen
 */
public class AuthAPI {
    
    /**
     * Register for new user
     * @param userInfo a HashMap must contains String fields: username, password, fullName
     * @return true if register is successful, false if not
     * @throws Exception Every exception
     */
    public static boolean register(HashMap<String, String> userInfo) throws Exception {
        boolean isRegisterSuccess = false;
        
        if (MySQL.isConnected()) {
            
            if (
                userInfo.containsKey("username")
                && userInfo.containsKey("password")
                && userInfo.containsKey("fullName")
            ) {
                String username = userInfo.get("username");
                String password = userInfo.get("password");
                String fullName = userInfo.get("fullName");

                if(
                    username.length() > 0 
                    && password.length() > 0 
                    && fullName.length() > 0
                ) {

                    String salt = EncryptTool.getRandomSalt();
                    String hashPassword = EncryptTool.MD5Hash(password + salt);

                    
                    int updateAuthStatusCode = MySQL.executeUpdate(
                        "insert into auth(username, hashPassword, salt)"
                        + "values('" 
                        + username + "', '"
                        + hashPassword + "', '"
                        + salt +"');"
                    );
                    
                    if (updateAuthStatusCode != -1) {
                        System.out.println("auth table is updated");
                        
                        int updateUsersStatusCode = MySQL.executeUpdate(
                            "insert into users(userid, fullName, totalCash)"
                            + "values(" 
                            + "LAST_INSERT_ID(), '"
                            + fullName + "', "
                            + "0);"
                        );
                        
                        if (updateUsersStatusCode != -1) {
                            isRegisterSuccess = true;
                            System.out.println("users table is updated");
                        } else {
                            isRegisterSuccess = false;
                            MySQL.executeUpdate(
                                "delete from auth where username = '" + username + "';"
                            );
                            System.err.println("Update failed: users table");
                        }
                    } else {
                        isRegisterSuccess = false;
                        System.err.println("Update failed: auth table");
                    }
                } else {
                    isRegisterSuccess = false;
                    throw new Exception("Missing information values to register");
                }
            } else {
                isRegisterSuccess = false;
                throw new Exception("Missing information fields to register");
            }
        } else {
            isRegisterSuccess = false;
            throw new Exception("Database is not connected");
        }
        return isRegisterSuccess;
    }
    
    /**
     * Login for user
     * @param userInfo a HashMap<String, String>, must contains fields: username, password
     * @return true or false
     * @throws Exception 
     */
    public static boolean login(HashMap<String, String> userInfo) throws Exception {
        boolean isLoginSuccess = false;
        
        if (MySQL.isConnected()) {
        
            if (
                userInfo.containsKey("username")
                && userInfo.containsKey("password")
            ) {
                String username = userInfo.get("username");
                String password = userInfo.get("password");

                if (
                    username.length() > 0
                    && password.length() > 0
                ) {
                    ResultSet user = MySQL.executeQuery(
                        "select * from auth where username = '" + username + "';"
                    );
                    
                    if (user.isBeforeFirst()) {
                        user.next();
                        int userId = user.getInt("id");
                        String userSalt = user.getString("salt");
                        String userHashPassword = user.getString("hashPassword");
                        String hashPassword = EncryptTool.MD5Hash(password + userSalt);
                        
                        if (hashPassword.equals(userHashPassword)) {
                            String accessKey = EncryptTool.getRandomSalt();
                            
                            int updateSessionsStatusCode = MySQL.executeUpdate(
                                "insert into sessions(userid, accessKey)"
                                + "values ("
                                + Integer.toString(userId) + ", '"
                                + accessKey + "');"
                            );
                            
                            if (updateSessionsStatusCode != -1) {
                                HashMap<String, String> token = new HashMap<>();
                                token.put("userid", Integer.toString(userId));
                                token.put("accessKey", accessKey);
                                
                                if (TokenTool.saveToken(token)) {
                                    isLoginSuccess = true;
                                } else {
                                    isLoginSuccess = false;
                                    MySQL.executeUpdate(
                                        "delete from sessions where userid = "
                                        + Integer.toString(userId) + " and accessKey = '"
                                        + accessKey + "';"
                                    );
                                    System.err.println("Failed to save token");
                                }
                            } else {
                                isLoginSuccess = false;
                                System.err.println("Update failed: sessions table");
                            }
                            
                        } else {
                            isLoginSuccess = false;
                            throw new Exception("Invalid password");
                        }
                        
                    } else {
                        isLoginSuccess = false;
                        throw new Exception("Invalid username");
                    }
                    
                } else {
                    isLoginSuccess = false;
                    throw new Exception("Missing information values to login");
                }
            } else {
                isLoginSuccess = false;
                throw new Exception("Missing information fields to login");
            }
        } else {
            isLoginSuccess = false;
            throw new Exception("Database is not connected");
        }
        
        return isLoginSuccess;
    }
    
    /**
     * Log out for user
     * @return true or false
     */
    public static boolean logout() throws Exception {
        boolean loggedOut = false;
        
        if (MySQL.isConnected()) {
            HashMap<String, String> token = null;
            
            try {
                token = TokenTool.getToken();
            } catch (Exception e) {
                token = null;                
            }
            
            if ( !(token == null) ) {
                if (
                    token.containsKey("userid")
                    && token.containsKey("accessKey")
                ) {
                    String userid = token.get("userid");
                    String accessKey = token.get("accessKey");

                    int updateSessionStatusCode = MySQL.executeUpdate(
                        "delete from sessions where userid = "
                        + userid + " and accessKey = '"
                        + accessKey + "';"
                    );
                    
                    if (updateSessionStatusCode != -1) {
                        TokenTool.deleteToken();
                        loggedOut = true;
                    } else {
                        loggedOut = false;
                        throw new Exception("Update database failed");
                    }
                } else {
                    loggedOut = false;
                    throw new Exception("Invalid token");
                }
            } else {
                loggedOut = false;
                throw new Exception("Missing token");
            }
        } else {
            loggedOut = false;
            throw new Exception("Database is not connected");
        }
        
        return loggedOut;
    }
    
    /**
     * Check if user is logged in the system
     * @return true or false
     */
    public static boolean isLoggedIn() { 
        boolean loggedIn = false;
        if (MySQL.isConnected()) {
            HashMap<String, String> token = null;
            
            try {
                token = TokenTool.getToken();
            } catch (Exception e) {
                token = null;                
            }

            if ( !(token == null) ) {
                if (
                    token.containsKey("userid")
                    && token.containsKey("accessKey")
                ) {
                    String userid = token.get("userid");
                    String accessKey = token.get("accessKey");

                    ResultSet session = MySQL.executeQuery(
                        "select * from sessions where userid = "
                        + userid + " and accessKey = '"
                        + accessKey + "';"
                    );
                    
                    try {
                        if (session.isBeforeFirst()) {
                            loggedIn = true;
                        } else {
                            loggedIn = false;
                        }
                    } catch (Exception e) {
                        loggedIn = false;
                        e.printStackTrace();
                    }
                } else {
                    loggedIn = false;
                }
            } else {
                loggedIn = false;
            }
        } else {
            loggedIn = false;
        }
        
        return loggedIn;
    }
    
    
//    public static void main(String[] args) {
//        
//        if (!MySQL.isConnected()) {
//            MySQL.connect("localhost:3306", "walletjava", "root", "toor");
//        }
//        
//        HashMap<String, String> user = new HashMap<>();
//        
//        user.put("username", "tungnguyen");
//        user.put("password", "123456");
//        user.put("fullName", "Nguyễn Văn Tùng");
//        
//        try {
//            AuthAPI.register(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        try {
//            AuthAPI.login(user);            
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
