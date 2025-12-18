
package database;

import java.sql.*;
public class Database {
       private Connection con;
       private static Database db;
       private String url;
       private String user;
       private String pass;
       public Database() throws ClassNotFoundException{
          Class.forName("com.mysql.cj.jdbc.Driver");
       }
       public static Database getDB() throws ClassNotFoundException{
          if(db==null){          
           db=new Database();
          }     
           return db;
       }
       public Connection connect() throws SQLException{
           url="jdbc:mysql://localhost:3306/cinema";
           user="root";
           pass="12345678";
           con=DriverManager.getConnection(url,user,pass);
           return con;
       } 
}
/*try {
            Connection con=Database.getDB().connect();      
            String sql="insert into movie values('M002','avatar 2','action','james','120','pending')";
            //String sql="SELECT * FROM movie";
            PreparedStatement stm= con.prepareStatement(sql);
            stm.executeUpdate();
            System.out.println("success");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }*/