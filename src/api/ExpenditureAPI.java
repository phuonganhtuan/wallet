package api;

import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import model.MySQL;

/**
 *
 * @author tungnguyen
 */
public class ExpenditureAPI {
    
    /**
     * Insert new record to Database
     * @param newRecord HashMap<String, String> must contain userid, onDate, content, amount, positive, paid
     * @return true if insert successful, false if not
     * @throws Exception 
     */
    public static boolean insert(HashMap<String, String> newRecord) throws Exception {
        boolean insertSuccess = false;
        if (MySQL.isConnected()) {
            if (
                newRecord.containsKey("userid")
                && newRecord.containsKey("onDate")
                && newRecord.containsKey("content")
                && newRecord.containsKey("amount")
                && newRecord.containsKey("positive")
            ) {

                String userid = newRecord.get("userid");
                String onDate = newRecord.get("onDate");
                String content = newRecord.get("content");
                String amount = newRecord.get("amount");
                String positive = newRecord.get("positive");

                if (
                    userid.length() > 0
                    && onDate.length() > 0
                    && content.length() > 0
                    && amount.length() > 0
                    && positive.length() > 0
                ) {
                    int updateExpenditureStatusCode = MySQL.executeUpdate(
                        "insert into expenditure(userid, onDate, content, amount, positive)"
                        + "values (" 
                        + userid + ", '"
                        + onDate + "', '"
                        + content + "', "
                        + amount + ", "
                        + positive + ");"
                    );
                    
                    if(updateExpenditureStatusCode != -1) {
                        int totalCash = TotalCashAPI.getTotalCash(userid);
                        int amountNumber = 0;
                        try {
                            amountNumber = NumberFormat.getInstance().parse(amount.trim()).intValue();
                        } catch (Exception e) {
                            amountNumber = 0;
                            e.printStackTrace();
                        }
                        
                        if (positive.trim().equals("0")) {
                            totalCash -= amountNumber;
                        } else {
                            totalCash += amountNumber;
                        }
                        
                        TotalCashAPI.setTotalCash(userid, Integer.toString(totalCash));
                        
                        insertSuccess = true;
                    } else {
                        insertSuccess = false;
                        throw new Exception("Update database failed");
                    }
                } else {
                    insertSuccess = false;
                    throw new Exception("Missing some values in expenditure record");
                }
            } else {
                insertSuccess = false;
                throw new Exception("Missing some fields in expenditure record");
            }
        } else {
            insertSuccess = false;
            throw new Exception("Database is not connected");
        }
        return insertSuccess;
    }
    
    /**
     * Delete record from database
     * @param id id of record
     * @return true if delete successful, false if not
     * @throws Exception 
     */
    public static boolean delete(String id) throws Exception {
        boolean deleteSuccess = false;
        
        if (MySQL.isConnected()) {
            
            ResultSet oldRecord = MySQL.executeQuery(
                "select * from expenditure where id = " + id + ";"
            );

            if (oldRecord.isBeforeFirst()) {
                oldRecord.next();
                // Get old data
                String oldUserid = Integer.toString(oldRecord.getInt("userid"));
                int oldAmount = oldRecord.getInt("amount");
                String oldPositive = Integer.toString(oldRecord.getInt("positive"));

                int totalCash = TotalCashAPI.getTotalCash(oldUserid);

                // Khoi phuc tien cu
                if(oldPositive.trim().equals("0")) {
                    totalCash += oldAmount;
                } else {
                    totalCash -= oldAmount;
                }
                
                int updateExpenditureStatusCode = MySQL.executeUpdate(
                    "delete from expenditure "
                    + "where id = " + id + ";"
                );

                if (updateExpenditureStatusCode != -1) {
                    TotalCashAPI.setTotalCash(oldUserid, Integer.toString(totalCash));
                    deleteSuccess = true;
                } else {
                    deleteSuccess = false;
                    throw new Exception("Update database failed");
                }
                
            } else {
                deleteSuccess = false;
                throw new Exception("Cannot retrieve old record from expenditure table");
            }
            
            
        } else {
            deleteSuccess = false;
            throw new Exception("Database is not connected");
        }
        
        return deleteSuccess;
    }
    
    /**
     * Modify a record
     * @param id id of record need to modify
     * @param newRecord new record to substitute
     * @return true if modify successful, false if not
     * @throws Exception 
     */
    public static boolean modify(String id, HashMap<String, String> newRecord) throws Exception {
        boolean modifySuccess = false;
        
        if (MySQL.isConnected()) {
            if (
                newRecord.containsKey("userid")
                && newRecord.containsKey("onDate")
                && newRecord.containsKey("content")
                && newRecord.containsKey("amount")
                && newRecord.containsKey("positive")
            ) {

                String userid = newRecord.get("userid");
                String onDate = newRecord.get("onDate");
                String content = newRecord.get("content");
                String amount = newRecord.get("amount");
                String positive = newRecord.get("positive");

                if (
                    userid.length() > 0
                    && onDate.length() > 0
                    && content.length() > 0
                    && amount.length() > 0
                    && positive.length() > 0
                ) {
                    ResultSet oldRecord = MySQL.executeQuery(
                        "select * from expenditure where id = " + id + ";"
                    );
                    
                    if (oldRecord.isBeforeFirst()) {
                        oldRecord.next();
                        // Get old data
                        String oldUserid = Integer.toString(oldRecord.getInt("userid"));
                        int oldAmount = oldRecord.getInt("amount");
                        String oldPositive = Integer.toString(oldRecord.getInt("positive"));
                        
                        int totalCash = TotalCashAPI.getTotalCash(oldUserid);
                        
                        // Khoi phuc tien cu
                        if(oldPositive.trim().equals("0")) {
                            totalCash += oldAmount;
                        } else {
                            totalCash -= oldAmount;
                        }
                        
                        int updateExpenditureStatusCode = MySQL.executeUpdate(
                            "update expenditure set "
                            + "userid = " + userid + ", "
                            + "onDate = '" + onDate + "', "
                            + "content = '" + content + "', "
                            + "amount = " + amount + ", "
                            + "positive = " + positive + " "
                            + "where id = " + id + ";"
                        );

                        if (updateExpenditureStatusCode != -1) {
                            
                            // Tinh tien moi
                            int amountNumber = 0;
                            try {
                                amountNumber = NumberFormat.getInstance().parse(amount.trim()).intValue();
                            } catch (Exception e) {
                                amountNumber = 0;
                                e.printStackTrace();
                            }

                            if (positive.trim().equals("0")) {
                                totalCash -= amountNumber;
                            } else {
                                totalCash += amountNumber;
                            }
                            
                            TotalCashAPI.setTotalCash(userid, Integer.toString(totalCash));
                            
                            modifySuccess = true;
                        } else {
                            modifySuccess = false;
                            throw new Exception("Update database failed");
                        }
                    } else {
                        modifySuccess = false;
                        throw new Exception("Cannot retrieve old record from expenditure table");
                    }
                } else {
                    modifySuccess = false;
                    throw new Exception("Missing some values in expenditure record");
                }
            } else {
                modifySuccess = false;
                throw new Exception("Missing some fields in expenditure record");
            }
        } else {
            modifySuccess = false;
            throw new Exception("Database is not connected");
        }
        
        return modifySuccess;
    }
    
    /**
     * Get a record
     * @param id id of record
     * @return a HashMap<String, String> represents the record
     * @throws Exception 
     */
    public static HashMap<String, String> getRecord(String id) throws Exception {
        HashMap<String, String> record = null;
        
        if (MySQL.isConnected()) {
            ResultSet recordInSet = MySQL.executeQuery(
                "select * from expenditure "
                + "where id = " + id + ";"
            );
            
            if (recordInSet.isBeforeFirst()) {
                record = new HashMap<>();
                recordInSet.next();
                
                record.put("id", Integer.toString(recordInSet.getInt("id")));
                record.put("userid", Integer.toString(recordInSet.getInt("userid")));
                record.put("onDate", recordInSet.getString("onDate"));
                record.put("content", recordInSet.getString("content"));
                record.put("amount", Integer.toString(recordInSet.getInt("amount")));
                record.put("positive", Integer.toString(recordInSet.getInt("positive")));
                
            } else {
                record = null;
                throw new Exception("Record not found");
            }
            
        } else {
            record = null;
            throw new Exception("Database is not connected");
        }
        
        return record;
    }
    
    /**
     * Get list of records
     * @param userid id of user own these records
     * @return an ArrayList of HashMap<String, String>, each HashMap is a record
     * @throws Exception 
     */
    public static ArrayList<HashMap<String, String>> getListRecord(String userid) throws Exception {
        ArrayList<HashMap<String, String>> recordList = null;
        
        if (MySQL.isConnected()) {
            ResultSet recordListInSet = MySQL.executeQuery(
                "select * from expenditure "
                + "where userid = " + userid + " "
                + "order by onDate desc;"
            );
            if (recordListInSet.isBeforeFirst()) {
                recordList = new ArrayList<>();
                while (recordListInSet.next()) {
                    HashMap<String, String> record = new HashMap<>();
                    
                    record.put("id", Integer.toString(recordListInSet.getInt("id")));
                    record.put("userid", Integer.toString(recordListInSet.getInt("userid")));
                    record.put("onDate", recordListInSet.getString("onDate"));
                    record.put("content", recordListInSet.getString("content"));
                    record.put("amount", Integer.toString(recordListInSet.getInt("amount")));
                    record.put("positive", Integer.toString(recordListInSet.getInt("positive")));
                    
                    if (!recordList.add(record)) {
                        throw new Exception("Failed when adding record to array list");
                    };
                }
            } else {
                recordList = null;
                throw new Exception("No records found");
            }
        } else {
            recordList = null;
            throw new Exception("Database is not connected");
        }
        
        return recordList;
    }
    
}
