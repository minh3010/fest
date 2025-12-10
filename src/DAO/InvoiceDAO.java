/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import cinema.Database;
import entity.Customer;
import entity.Invoice;
import entity.InvoiceService;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Lenovo
 */
public class InvoiceDAO {
    
    private Connection getConnect() throws ClassNotFoundException, SQLException{
       return Database.getDB().connect();
    }
    private String parseDate(String str) throws ParseException {
        DateTimeFormatter inputFormat  = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(str, inputFormat);
        String output = date.format(outputFormat);
        return output;  
    }    
    public String generateId() throws ClassNotFoundException, SQLException{
          LocalDate now=LocalDate.now();
          DateTimeFormatter format=DateTimeFormatter.ofPattern("yyMMdd");
          String dateNow=now.format(format);
          String sql="select max(inv_id) from invoice where inv_id like ?";
          try(PreparedStatement pst = getConnect().prepareStatement(sql)){
              pst.setString(1,"%"+dateNow+"%");
              ResultSet rs=pst.executeQuery();
              rs.next();
              if(rs.getString("max(inv_id)")==null){
                  return "INV"+dateNow+"001";
              }
              else{
                long id=Long.parseLong(rs.getString("max(inv_id)").substring(9,rs.getString("max(inv_id)").length()));
                id++;
                return "INV"+dateNow+String.format("%03d",id);
              }
              
          }                 
    }
    public List<Invoice> getAllInvoice() throws ClassNotFoundException, SQLException{
        List<Invoice> invs = new ArrayList<>();
        String sql = "SELECT * FROM invoice ORDER BY inv_date";
        try (Statement stmt = getConnect().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Invoice inv = extractInvoice(rs);
                invs.add(inv);
            }
        }        
        return invs;
    }
    public void addInvoice(Invoice inv) throws SQLException, ClassNotFoundException{
        String sql="insert into invoice(inv_id,cus_id,ticket_subtotal,service_subtotal,discount,inv_total) values(?,?,?,?,?,?)";
        try(PreparedStatement pst=getConnect().prepareStatement(sql)){
            pst.setString(1,inv.getId());
            if(inv.getCus()!=null){
               pst.setString(2,inv.getCus().getCusID());
            }
            else{pst.setString(2,null);}
            pst.setDouble(3,inv.getTicket_sub());
            pst.setDouble(4,inv.getService_sub());
            pst.setDouble(5,inv.getDiscount());
            pst.setDouble(6,inv.getTotal());
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
    public List<Invoice> getInvoicesByDate(java.util.Date from, java.util.Date to) throws ClassNotFoundException {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT inv_id, cus_id, inv_date, ticket_subtotal, service_subtotal,discount,inv_total " +
                     "FROM invoice " +
                     "WHERE Date(inv_date) BETWEEN ? AND ? " +
                     "ORDER BY inv_date DESC";

        try (Connection conn = Database.getDB().connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

           
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invoice stat = extractInvoice(rs);
                    list.add(stat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hóa đơn theo ngày: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public List<Object[]> getRevenueByDate() throws ClassNotFoundException, SQLException{      
        String sql = "SELECT DATE(inv_date) as date , SUM(inv_total) as revenue FROM  invoice GROUP BY DATE(inv_date) ORDER BY DATE(inv_date)";
        List<Object[]> results=new ArrayList<>();
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try (Statement stmt = getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                double revenue = rs.getDouble("revenue");
                results.add(new Object[]{date.format(format), revenue});
            }            
        }
        return results;
    }
    public List<Object[]> getRevenueByDateRange(String start,String end) throws ClassNotFoundException, SQLException, ParseException{      
        String sql = "SELECT DATE(inv_date) as date , SUM(inv_total) as revenue FROM  invoice where DATE(inv_date) between ? and ? GROUP BY DATE(inv_date) ORDER BY DATE(inv_date)";
        List<Object[]> results=new ArrayList<>();
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try (PreparedStatement stmt = getConnect().prepareStatement(sql)) {
            String startDate=parseDate(start);
            String endDate=parseDate(end);
            stmt.setString(1,startDate);
            stmt.setString(2,endDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                double revenue = rs.getDouble("revenue");
                results.add(new Object[]{date.format(format), revenue});
            }            
        }
        return results;
    }
    public List<Object[]> getRevenueByMovie() throws ClassNotFoundException, SQLException{      
        String sql = "SELECT m.Mov_title AS title, SUM(t.Ticket_price) AS revenue\n" +
                     "FROM Ticket t\n" +
                     "JOIN Showtime s ON t.Show_ID = s.Show_ID\n" +
                     "JOIN Movie m ON s.Mov_ID = m.Mov_ID\n" +
                     "GROUP BY m.Mov_title\n" +
                     "ORDER BY revenue DESC;";
        List<Object[]> results=new ArrayList<>();
        try (Statement stmt = getConnect().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String title = rs.getString("title");
                double revenue = rs.getDouble("revenue");
                results.add(new Object[]{title, revenue});
            }            
        }
        return results;
    }    
    private Invoice extractInvoice(ResultSet rs) throws SQLException, ClassNotFoundException {
        Invoice inv=new Invoice();
        CustomerDAO cusDAO=new CustomerDAO();
        Customer cus=cusDAO.findById(rs.getString("cus_id")).orElse(null);
        inv.setId(rs.getString("inv_id"));
        inv.setCustomer(cus);
        inv.setDate(rs.getTimestamp("inv_date"));
        inv.setTicket_sub(rs.getDouble("ticket_subtotal"));
        inv.setService_sub(rs.getDouble("service_subtotal"));
        inv.setDiscount(rs.getDouble("discount"));
        inv.setTotal(rs.getDouble("inv_total"));
        return inv;
    }
}
