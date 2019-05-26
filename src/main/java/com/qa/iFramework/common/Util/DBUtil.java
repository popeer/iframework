package com.qa.iFramework.common.Util;

import com.qa.iFramework.common.Util.StringUtils;
import com.qa.iFramework.common.processor.CommandEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.database.IDatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

/**
 * Created by haijia on 6/21/17.
 */
public class DBUtil {

    private static Logger log = LogManager.getLogger(DBUtil.class);

//    DBUtil(){
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("数据库驱动载入失败", e);
//        }
//    }

    DBUtil(String driver){
        try {
            if(StringUtils.isNotEmpty(driver)){
                switch (driver.toLowerCase().trim()){
                    case "pg":
                        Class.forName("org.postgresql.Driver");
                        break;
                    case "sqlserver":
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        break;
                    case "mysql":
                        Class.forName("com.mysql.jdbc.Driver");
                        break;
                    default:
                        Class.forName("org.postgresql.Driver");
                        break;
                }
            } else {
                Class.forName("org.postgresql.Driver");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动载入失败", e);
        }
    }

    public static Connection getConnection(String dbUrl, String uid, String pwd){
        try {
            return DriverManager.getConnection(dbUrl, uid, pwd);
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接创建失败", e);
        }
    }

    public static void close(Connection conn){
        if(null != conn){
            try {
                conn.close();
                if(conn.isClosed()){
                    log.info(("此数据库连接已关闭-->" + conn));
                }else{
                    System.err.println("此数据库连接关闭失败-->" + conn);
                }
            } catch (SQLException e) {
                System.err.println("数据库连接关闭失败，堆栈轨迹如下：");
                e.printStackTrace();
            }
        }
    }

    public static void closeAll(ResultSet rs, PreparedStatement pstmt, Connection conn){
        if(null != rs){
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("数据库操作的ResultSet关闭失败，堆栈轨迹如下：");
                e.printStackTrace();
            }
        }
        if(null != pstmt){
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("数据库操作的PreparedStatement关闭失败，堆栈轨迹如下：");
                e.printStackTrace();
            }
        }
        close(conn);
    }

    public static ArrayList<HashMap<String,Object>> execute(CommandEntity entity){
        try {
            if(StringUtils.isNotEmpty(entity.getSqlDriver())){
                switch (entity.getSqlDriver().toLowerCase().trim()){
                    case "pg":
                        Class.forName("org.postgresql.Driver");
                        break;
                    case "sqlserver":
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        break;
                    case "mysql":
                        Class.forName("com.mysql.jdbc.Driver");
                        break;
                    default:
                        Class.forName("org.postgresql.Driver");
                        break;
                }
            } else {
                Class.forName("org.postgresql.Driver");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动载入失败", e);
        }

        IDatabaseConnection connection =null;
        ArrayList<HashMap<String,Object>> result = null;
        try{
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                conn = getConnection(entity.getDbURL(), entity.getUid(), entity.getPwd());
                log.info(("数据库连接-->" + conn));

                if(supportBatch(conn)){
                    result = executeBatch(conn, entity);
                } else {

                    for (String sql : entity.getSqlList()) {
                        pstmt = conn.prepareStatement(sql);
                        if(sql.toLowerCase().trim().startsWith("select")){
                            pstmt.executeQuery();
                        } else {
                            pstmt.executeUpdate();
                        }
                    }
                }

            } catch (SQLException e) {
                log.error("数据库查询时发生异常,堆栈轨迹如下");
                e.printStackTrace();
            } finally {
                closeAll(rs, pstmt, conn);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(connection!=null) {
                    connection.close();
                }
            }catch(SQLException e){

            }
        }
        return result;
    }

    public static ArrayList<HashMap<String,Object>> executeBatch(Connection conn, CommandEntity entity) throws Exception{
        Statement sm = null;
        ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String, Object>>();

        try {
            log.info(("数据库连接-->" + conn));
            sm = conn.createStatement(TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);

            for(String sql : entity.getSqlList()){
//                sm.addBatch(sql);
                log.info("To Execute Sql: " + sql);
                boolean flag = sm.execute(sql);

                if(flag){
                    ResultSet resultSet = sm.getResultSet();

                    while (resultSet.next()) {
                        String[] keyNames = entity.getSqlKeyName().split(",");
                        HashMap<String, Object> sqlResult = new HashMap<String, Object>();
                        for(String keyName : keyNames){
                            sqlResult.put(keyName, resultSet.getString(keyName));
                        }
                        log.info("Result: " + result + " for SQL " + sql);
                        result.add(sqlResult);
                    }
                }
            }

//            sm.executeBatch();
        } catch (SQLException e) {
            log.error("数据库查询时发生异常,堆栈轨迹如下");
            e.printStackTrace();
        } finally {
            sm.close();
        }

        return result;
    }

    /** 判断数据库是否支持批处理 */
    public static boolean supportBatch(Connection con) {
        try {
            // 得到数据库的元数据
            DatabaseMetaData md = con.getMetaData();
            return md.supportsBatchUpdates();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
