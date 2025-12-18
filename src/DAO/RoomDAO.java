/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Lenovo
 */
import database.Database;
import java.sql.*;
import entity.Room;
import java.util.ArrayList;
import java.util.List;
public class RoomDAO {
    private Connection getConnect() throws ClassNotFoundException, SQLException{
       return Database.getDB().connect();
    }
    public void addRoom(Room room) throws SQLException, ClassNotFoundException{
        String sql="insert into theater values(?,?,?,?,?)";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,room.getId());
            pst.setString(2,room.getName());
            pst.setInt(3,room.getRowNum());
            pst.setInt(4,room.getSeatPerRow());
            pst.setInt(5,room.getSeatCount());
            pst.executeUpdate();
        }
    }
    public void editRoom(Room room) throws SQLException, ClassNotFoundException{
        String sql="update theater set theater_name=?,row_num=?,seat_per_row=?,seat_total=? where theater_id=?";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){        
            pst.setString(1,room.getName());
            pst.setInt(2,room.getRowNum());
            pst.setInt(3,room.getSeatPerRow());
            pst.setInt(4,room.getSeatCount());
            pst.setString(5,room.getId());
            pst.executeUpdate();
        }
    }
    public void deleteRoom(String id) throws SQLException, ClassNotFoundException{
        String sql="delete from theater where theater_id=?";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,id);
            pst.executeUpdate();
        }
    }
    public Room findById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM theater WHERE theater_id=?";
        try (PreparedStatement stmt = getConnect().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractRoom(rs);
            }
        }
        return null;
    }
    public Room getRoomByStartTime(String date) throws SQLException, ClassNotFoundException{
        String sql = "select * from theater t join showtime s on s.Theater_ID=t.Theater_ID where start_time=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)){
            pst.setString(1,date);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
               return extractRoom(rs);
            }
        }
        return null;    
    }
    public List<Room> findAll() throws SQLException, ClassNotFoundException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM theater ORDER BY theater_name";
        try (Statement stmt = getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(extractRoom(rs));
            }
        }
        return rooms;
    }    
    private Room extractRoom(ResultSet rs) throws SQLException {
        return new Room(
            rs.getString("theater_id"),
            rs.getString("theater_name"),
            rs.getInt("row_num"),
            rs.getInt("seat_per_row")
        );
    }    
}
