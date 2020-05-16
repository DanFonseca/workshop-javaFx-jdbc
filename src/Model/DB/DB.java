package Model.DB;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection conn = null;

    public static Connection getConnection ()  {

        Properties properties = loadProperties();
        String url = properties.getProperty("dburl");

        try{
            conn =  DriverManager.getConnection(url, properties);
            return conn;
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }
    }

    public   static void closeConnection (){
        if(conn != null){
            try{
                conn.close();
            }catch (SQLException e){
                throw new DBException(e.getMessage());
            }
        }
    }


    private static Properties loadProperties (){
        try(FileInputStream fs = new FileInputStream("db.properties")){
            Properties properties = new Properties();
            properties.load(fs);
            return properties;
        }catch (IOException e){
            throw new DBException(e.getMessage());
        }
    }

    public static void closeStatement (Statement st){
        if(st!=null){
            try{
                st.close();
            }catch (SQLException e){
                throw  new DBException(e.getMessage());
            }
        }
    }

    public static void closeResultSet (ResultSet rs){
        if(rs!=null){
            try{
                rs.close();
            }catch (SQLException e){
                throw  new DBException(e.getMessage());
            }
        }
    }

}
