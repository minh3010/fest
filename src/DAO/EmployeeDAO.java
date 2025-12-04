/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import cinema.Database;
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
    public EmployeeDAO(){ 
        
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
    private Employee extractEmployee(ResultSet rs) throws SQLException{
        Employee emp=new Employee(
            rs.getString("emp_id"),
            rs.getString("emp_name"),
            rs.getString("emp_id"),
            rs.getString("emp_id"),
            rs.getString("emp_id"),
            rs.getString("emp_id"),
            rs.getString("emp_id")
        );
        return emp;
    }    
}
