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
import entity.Movie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MovieDAO {
    private Connection getConnect() throws ClassNotFoundException, SQLException{
       return Database.getDB().connect();
    }

    /**
     *
     * @param movie
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void addMovie(Movie movie) throws SQLException, ClassNotFoundException{
        String sql="insert into movie values(?,?,?,?,?,?)";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,movie.getId());
            pst.setString(2,movie.getTitle());
            pst.setString(3,movie.getGenre());
            pst.setString(4,movie.getDirector());
            pst.setInt(5,movie.getDuration());
            pst.setBoolean(6,movie.isShowing());
            pst.executeUpdate();
        }
    }
    public void editMovie(Movie movie) throws SQLException, ClassNotFoundException{
        String sql="update movie set mov_title=?,mov_genre=?,mov_director=?,mov_duration=?,mov_is_showing=? where mov_id=?";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,movie.getTitle());
            pst.setString(2,movie.getGenre());
            pst.setString(3,movie.getDirector());
            pst.setInt(4,movie.getDuration());
            pst.setBoolean(5,movie.isShowing());
            pst.setString(6,movie.getId());
            pst.executeUpdate();
        }
    }
    public void deleteMovie(String id) throws SQLException, ClassNotFoundException{
        String sql="delete from movie where mov_id=?";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,id);
            pst.executeUpdate();
        }
    }
    public List<Movie> findAll() throws SQLException, ClassNotFoundException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie ORDER BY mov_id";
        try (Statement stmt = getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = extractMovie(rs);
                movies.add(movie);
            }
        }
        return movies;
    }
    public List<Object[]> findDailyMovieByShowtime() throws SQLException, ClassNotFoundException {
        List<Object[]> results=new ArrayList<>();
        String sql = "select distinct m.Mov_ID,Mov_title,Show_date from movie m join showtime s on m.Mov_ID=s.Mov_ID where show_date=curdate()";
        try (Statement stmt = getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {                      
            while (rs.next()) {
                  results.add(new Object[]{rs.getString("mov_id"),rs.getString("mov_title")});
            }
        }
        return results;
    }     
    public Movie findById(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM movie WHERE mov_id=?";
        try (PreparedStatement stmt = getConnect().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Movie movie = extractMovie(rs);
                return movie;
            }
        }
        return null;
    }   
    private Movie extractMovie(ResultSet rs) throws SQLException {
        return new Movie(
            rs.getString("mov_id"),
            rs.getString("mov_title"),
            rs.getString("mov_genre"),
            rs.getString("mov_director"),    
            rs.getInt("mov_duration"),
            rs.getBoolean("mov_is_showing")    
        );
    }    
}
