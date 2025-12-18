/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import database.Database;
import entity.Ticket;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Lenovo
 */
public class TicketDAO {
    private Connection getConnect() throws ClassNotFoundException, SQLException{
       return Database.getDB().connect();
    }    
    public String generateId() throws ClassNotFoundException, SQLException{
          LocalDate now=LocalDate.now();
          DateTimeFormatter format=DateTimeFormatter.ofPattern("yyMMdd");
          String sql="select max(ticket_id) from ticket";
          try(PreparedStatement pst = getConnect().prepareStatement(sql)){  
              ResultSet rs=pst.executeQuery();
              rs.next();
              if(rs.getString("max(ticket_id)")==null){
                  return "T"+now.format(format)+"001";
              }//T252311001
              else{
                long id=Long.parseLong(rs.getString("max(ticket_id)").substring(7,rs.getString("max(ticket_id)").length()));
                id++;
                return "T"+now.format(format)+String.format("%03d",id);
              }              
          }                 
    }
    public void addTicket(Ticket ticket) throws SQLException, ClassNotFoundException{
        String sql="insert into ticket values(?,?,?,?,?)";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,ticket.getId());
            pst.setString(2,ticket.getShow().getId());
            pst.setString(3,ticket.getSeat());
            pst.setString(4,ticket.getInv().getId());
            pst.setDouble(5,ticket.getPrice());
            pst.executeUpdate();
        }
    }    
}
