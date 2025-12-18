package DAO;

import database.Database;
import entity.Movie;
import entity.Room;
import entity.Showtime;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ShowtimeDAO {
    private final MovieDAO movieDAO;
    private final RoomDAO roomDAO;
    private Connection getConnect() throws ClassNotFoundException, SQLException {
        return Database.getDB().connect();
    }
    public ShowtimeDAO(){
        movieDAO=new MovieDAO();
        roomDAO=new RoomDAO();
    }
    // Thêm suất chiếu mới
    public void addShowtime(Showtime showtime) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO showtime (Show_ID, Mov_ID, Theater_ID, Show_date, Start_time, Show_price) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, showtime.getId());
            pst.setString(2, showtime.getMovie().getId());
            pst.setString(3, showtime.getRoom().getId());
            pst.setDate(4, Date.valueOf(showtime.getDate()));
            pst.setTime(5, Time.valueOf(showtime.getStart_time()));
            //pst.setTime(6, Time.valueOf(showtime.getEnd_time()));
            pst.setDouble(6, showtime.getShow_price());
            pst.executeUpdate();
        }
    }

    // Sửa suất chiếu
    public void editShowtime(Showtime showtime) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE showtime SET Mov_ID=?, Theater_ID=?, Show_date=?, Start_time=?, Show_price=? WHERE Show_ID=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, showtime.getMovie().getId());
            pst.setString(2, showtime.getRoom().getId());
            pst.setDate(3, Date.valueOf(showtime.getDate()));
            pst.setTime(4, Time.valueOf(showtime.getStart_time()));
            //pst.setTime(5, Time.valueOf(showtime.getEnd_time()));
            pst.setDouble(5, showtime.getShow_price());
            pst.setString(6, showtime.getId());
            pst.executeUpdate();
        }
    }

    // Xóa suất chiếu
    public void deleteShowtime(String showId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM showtime WHERE Show_ID=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, showId);
            pst.executeUpdate();
        }
    }

    // Lấy tất cả suất chiếu
    public List<Showtime> getAllShowtimes() throws SQLException, ClassNotFoundException {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtime";
        try (PreparedStatement pst = getConnect().prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
               showtimes.add(extractShowTime(rs));
            }
        }
        return showtimes;
    }

    public List<Showtime> getShowtimesByDate(Date date) throws SQLException, ClassNotFoundException {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "select show_id,s.mov_id,s.theater_id,theater_name,show_date,start_time,show_price from showtime s "
                + "join movie m on s.Mov_ID=m.mov_id join theater t on s.Theater_ID=t.Theater_ID where show_date=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setDate(1,date);   
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
               showtimes.add(extractShowTime(rs));
            }
        }
        return showtimes;
    }    
    public List<Showtime> getShowtimesByMovieId(String movieId) throws SQLException, ClassNotFoundException {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "select show_id,s.mov_id,theater_id,show_date,start_time,show_price from showtime s join movie m on s.Mov_ID=m.mov_id where s.Mov_ID=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1,movieId);   
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
               showtimes.add(extractShowTime(rs));
            }
        }
        return showtimes;
    }
    // Tìm suất chiếu theo ID
    public Showtime getShowtimeById(String showId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM showtime WHERE Show_ID=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, showId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractShowTime(rs);
            }
        }
        return null;
    }
    public Showtime getDailyShowtimeByStartTime(String showDate) throws SQLException, ClassNotFoundException {
        String sql = "select * from showtime s join theater t on s.Theater_ID=t.Theater_ID where start_time=? and show_date=curdate()";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, showDate);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractShowTime(rs);
            }
        }
        return null;
    }
    public void bookSeat(String showtimeId, String seatNumber) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO showtime_booked_seats (show_id, seat_num) VALUES (?, ?)";
        try (PreparedStatement stmt = getConnect().prepareStatement(sql)) {
            stmt.setString(1, showtimeId);
            stmt.setString(2, seatNumber);
            stmt.executeUpdate();
        }
    }
    public Set<String> getBookedSeats(String showTimeId) throws SQLException, ClassNotFoundException {
        Set<String> bookedSeats = new HashSet<>();
        String sql = "SELECT seat_num FROM showtime_booked_seats WHERE show_id=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, showTimeId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getString("seat_num"));
            }
        }
        return bookedSeats;
    }    
    private Showtime extractShowTime(ResultSet rs) throws SQLException, ClassNotFoundException {
        String movieId = rs.getString("mov_id");
        String roomId = rs.getString("theater_id");
        
        Movie movieOpt = movieDAO.findById(movieId);
        Room roomOpt = roomDAO.findById(roomId);
        
        if (movieOpt==null || roomOpt==null) {
            throw new SQLException("không tồn tại phim hoặc phòng");
        }
        
        Showtime showTime = new Showtime(
            rs.getString("show_id"),
            movieOpt,
            roomOpt,
            rs.getDate("show_date").toLocalDate(),    
            rs.getTime("start_time").toLocalTime(),
            rs.getDouble("show_price")
        );
        Set<String> bookedSeats = getBookedSeats(showTime.getId());
        for (String seat : bookedSeats) {
            showTime.bookSeat(seat);
        }       
        return showTime;
    }
}