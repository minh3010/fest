/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import cinema.Database;
import entity.Invoice;
import entity.InvoiceService;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author Lenovo
 */
public class InvoiceDAO {
    
    private Connection getConnect() throws ClassNotFoundException, SQLException{
       return Database.getDB().connect();
    }    
    public String generateId() throws ClassNotFoundException, SQLException{
          LocalDate now=LocalDate.now();
          DateTimeFormatter format=DateTimeFormatter.ofPattern("yyMMdd");
          String sql="select max(inv_id) from invoice";
          try(PreparedStatement pst = getConnect().prepareStatement(sql)){  
              ResultSet rs=pst.executeQuery();
              rs.next();
              if(rs.getString("max(inv_id)")==null){
                  return "INV"+now.format(format)+"001";
              }
              else{
                long id=Long.parseLong(rs.getString("max(inv_id)").substring(9,rs.getString("max(inv_id)").length()));
                id++;
                return "INV"+now.format(format)+String.format("%03d",id);
              }
              
          }                 
    }
    public void addInvoice(Invoice inv) throws SQLException, ClassNotFoundException{
        String sql="insert into invoice(inv_id,cus_id,ticket_subtotal,service_subtotal,inv_total) values(?,?,?,?,?)";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,inv.getId());
            pst.setString(2,inv.getCus().getCusID());
            pst.setDouble(3,inv.getTicket_sub());
            pst.setDouble(4,inv.getService_sub());
            pst.setDouble(5,inv.getTotal());
            pst.executeUpdate();
        }
    }    
    public void addInvLine(InvoiceService inv) throws SQLException, ClassNotFoundException{
        String sql="insert into serviceline values(?,?,?,?,?)";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,inv.getInv().getId());
            pst.setString(2,inv.getSer().getId());
            pst.setDouble(3,inv.getPrice());
            pst.setInt(4,inv.getQuantity());
            pst.setDouble(5,inv.getTotal());
            pst.executeUpdate();
        }
    }       
}
