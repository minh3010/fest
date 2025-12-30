/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import database.Database;
import entity.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class EmployeeDAO {
    private Connection getConnect() throws ClassNotFoundException, SQLException {
        return Database.getDB().connect();
    }
    public EmployeeDAO(){}
    public Employee Login(String username,String password) throws ClassNotFoundException, SQLException{
        String sql="select * from employee where emp_username=? and emp_password=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractEmployee(rs);
            }
        }        
        return null;
    }
    public boolean DuplicateUsername(String username) throws ClassNotFoundException, SQLException{
        String sql="select * from employee where emp_username=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        }        
        return false;        
    }
    public List<Employee> getEmployee() throws ClassNotFoundException, SQLException{
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (PreparedStatement pst = getConnect().prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
               employees.add(extractEmployee(rs));
            }
        }
        return employees;       
    }
    public void addEmployee(Employee emp) throws ClassNotFoundException, SQLException{
        String sql = "INSERT INTO employee VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, emp.getId());
            pst.setString(2, emp.getName());
            pst.setString(3, emp.getPhone());
            pst.setString(4, emp.getEmail());
            pst.setString(5, emp.getRole());
            pst.setString(6, emp.getUsername());
            pst.setString(7, emp.getPassword());
            pst.executeUpdate();
        }
    }
    public void editEmployee(Employee emp) throws ClassNotFoundException, SQLException{
        String sql = "Update employee set emp_name=?,emp_phone=?,emp_email=?,emp_role=?,emp_username=?,emp_password=? where emp_id=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {          
            pst.setString(1, emp.getName());
            pst.setString(2, emp.getPhone());
            pst.setString(3, emp.getEmail());
            pst.setString(4, emp.getRole());
            pst.setString(5, emp.getUsername());
            pst.setString(6, emp.getPassword());
            pst.setString(7, emp.getId());
            pst.executeUpdate();
        }    
    }
    public void deleteEmployee(String empId) throws ClassNotFoundException, SQLException{
        String sql = "DELETE FROM employee WHERE Emp_ID=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, empId);
            pst.executeUpdate();
        }    
    }
    public Employee findById(String id) throws ClassNotFoundException, SQLException{
        String sql = "SELECT * FROM employee WHERE Emp_ID=?";
        try (PreparedStatement pst = getConnect().prepareStatement(sql)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractEmployee(rs);
            }
        }
        return null;      
    }
    private Employee extractEmployee(ResultSet rs) throws SQLException{
        Employee emp=new Employee(
            rs.getString("emp_id"),
            rs.getString("emp_name"),
            rs.getString("emp_phone"),
            rs.getString("emp_email"),
            rs.getString("emp_role"),
            rs.getString("emp_username"),
            rs.getString("emp_password")
        );
        return emp;
    }    
}
