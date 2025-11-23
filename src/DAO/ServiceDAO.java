/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import cinema.Database;
import java.sql.*;
import entity.Service;
/**
 *
 * @author Lenovo
 */
public class ServiceDAO {
    private Connection getConnect() throws ClassNotFoundException, SQLException{
       return Database.getDB().connect();
    }
    public void addService(Service service) throws SQLException, ClassNotFoundException{
        String sql="insert into service values(?,?,?,?)";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,service.getId());
            pst.setString(2,service.getName());
            pst.setDouble(3,service.getPrice());
            pst.setInt(4,service.getQuantity());
            pst.executeUpdate();
        }
    }
    public void editService(Service service) throws SQLException, ClassNotFoundException{
        String sql="update service set service_name=?,service_price=?,service_quantity=? where service_id=?";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){           
            pst.setString(1,service.getName());
            pst.setDouble(2,service.getPrice());
            pst.setInt(3,service.getQuantity());
            pst.setString(4,service.getId());
            pst.executeUpdate();
        }
    }
    public void deleteService(String id) throws SQLException, ClassNotFoundException{
        String sql="delete from service where service_id=?";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,id);
            pst.executeUpdate();
        }
    }
    public void updateQuantity(Service service,int quantity) throws SQLException, ClassNotFoundException{
        String sql="update service set service_quantity=? where service_id=?";
        int newQuantity=service.getQuantity()-quantity;
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){           
            pst.setInt(1,newQuantity);
            pst.setString(2,service.getId());
            pst.executeUpdate();
        }
    }    
    public Service findById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM service WHERE service_id=?";
        try (PreparedStatement stmt = getConnect().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Service service = extractService(rs);
                return service;
            }
        }
        return null;
    }         
     private Service extractService(ResultSet rs) throws SQLException {
        return new Service(
            rs.getString("service_id"),
            rs.getString("service_name"),
            rs.getDouble("service_price"),
            rs.getInt("service_quantity")       
        );
    }     
}
