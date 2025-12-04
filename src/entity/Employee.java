/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Lenovo
 */
public class Employee {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String role;
    private String username;
    private String password;
    public Employee(){}
    public Employee(String id,String name,String phone,String email,String role,String username,String password){
       this.id=id;
       this.name=name;
       this.phone=phone;
       this.email=email;
       this.role=role;
       this.username=username;
       this.password=password;
    }
    public String getId(){
       return id;
    }
    public void setId(String id){
       this.id=id;
    }
    public String getName(){
       return name;
    }
    public void setName(String name){
       this.name=name;
    }
    public String getPhone(){
       return phone;
    }
    public void setPhone(String phone){
       this.phone=phone;
    }    
    public String getEmail(){
       return email;
    }
    public void setEmail(String email){
       this.email=email;
    }
    public String getRole(){
       return role;
    }
    public void setRole(String role){
       this.role=role;
    }
    public String getUsername(){
       return username;
    }
    public void setUsername(String username){
       this.username=username;
    }
    public String getPassword(){
       return password;
    }
    public void setPassword(String password){
       this.password=password;
    }    
}
