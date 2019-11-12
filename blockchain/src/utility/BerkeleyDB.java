package utility;

/*
 *berkeleyDB操作工具类
 *对数据库进行增查
 * */

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockConflictException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;

public class BerkeleyDB {
	// 数据库环境
	private Environment env = null;
	// 数据库
	private static Database blockchainDatabase = null;
	// 数据库名字
	private static String dbName = "blockchain";

	public BerkeleyDB(String homeDirectory) {
		// 1.创建EnvironmentConfig
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);

		// 2.使用EnvironmentConfig配置Environment
		env = new Environment(new File(homeDirectory), envConfig);

		// 3.创建DatabaseConfig
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);

		// 4.使用Environment和DatabaseConfig打开Database
		blockchainDatabase = env.openDatabase(null, dbName, dbConfig);
	}
	
	 /* 
     * 向数据库中写入记录，并判断是否可以有重复数据。 传入key和value 
     * 若可以有重复数据，则直接使用put()即可，若不能有重复数据，则使用putNoOverwrite()。 
     */  
    public boolean writeToDatabase(String key, String value, boolean isOverwrite) {  
        try {  
            // 设置key/value,注意DatabaseEntry内使用的是bytes数组  
            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("ISO-8859-1"));  
            DatabaseEntry theData = new DatabaseEntry(value.getBytes("ISO-8859-1"));
            OperationStatus status = null;  
            Transaction txn = null;  
            try {  
                // 1、Transaction配置  
                TransactionConfig txConfig = new TransactionConfig();  
                txConfig.setSerializableIsolation(true);  
                txn = env.beginTransaction(null, txConfig);  
                // 2、写入数据  
                if (isOverwrite) {  
                    status = blockchainDatabase.put(txn, theKey, theData); 
                } else {  
                    status = blockchainDatabase.putNoOverwrite(txn, theKey,  
                            theData);  
                }  
                txn.commit();  
                if (status == OperationStatus.SUCCESS) {  
                    System.out.println("向数据库" + dbName + "中写入:" + key + ","  
                            + value);  
                    return true;  
                } else if (status == OperationStatus.KEYEXIST) {  
                    System.out.println("向数据库" + dbName + "中写入:" + key + ","  
                            + value + "失败,该值已经存在");  
                    return false;  
                } else {  
                    System.out.println("向数据库" + dbName + "中写入:" + key + ","  
                            + value + "失败");  
                    return false;  
                }  
            } catch (LockConflictException lockConflict) {  
                txn.abort();  
                System.out.println("向数据库" + dbName + "中写入:" + key + "," + value  
                        + "出现lock异常");  
                return false;  
            }  
        } catch (Exception e) {  
            // 错误处理  
            System.out.println("向数据库" + dbName + "中写入:" + key + "," + value  
                    + "出现错误");  
  
            return false;  
        }  
    }  
  
    /* 
     * 从数据库中读出数据 传入key 返回value 
     */  
    public String readFromDatabase(String key) {  
        try {  
            DatabaseEntry theKey = new DatabaseEntry(key.getBytes("ISO-8859-1"));  
            DatabaseEntry theData = new DatabaseEntry();  
            Transaction txn = null;  
            try {  
                // 1、配置 Transaction相关信息  
                TransactionConfig txConfig = new TransactionConfig();  
                txConfig.setSerializableIsolation(true);  
                txn = env.beginTransaction(null, txConfig);  
                // 2、读取数据  
                OperationStatus status = blockchainDatabase.get(txn, theKey,  
                        theData, LockMode.DEFAULT);  
                txn.commit();  
                if (status == OperationStatus.SUCCESS) {  
                    // 3、将字节转换成String  
                    byte[] retData = theData.getData();  
                    String value = new String(retData, "ISO-8859-1");  
                    System.out.println("从数据库" + dbName + "中读取:" + key + ","  
                            + value);  
                    return value;  
                } else {  
                    System.out  
                            .println("No record found for key '" + key + "'.");  
                    return "";  
                }  
            } catch (LockConflictException lockConflict) {  
                txn.abort();  
                System.out.println("从数据库" + dbName + "中读取:" + key + "出现lock异常");  
                return "";  
            }  
  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
  
            return "";  
        }  
    }  
    /* 
     * 关闭数据库 
     */  
    public void closeDB() {  
        if (blockchainDatabase != null) {  
            blockchainDatabase.close();  
        }  
        if (env != null) {  
            env.close();  
        }  
    }  
}
