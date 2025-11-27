/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import java.sql.*;
/**
 *
 * @author Lenovo
 */
public class Invoice {
    private String id;
    private Customer cus;
    private Double ticket_sub;
    private Double service_sub;
    private Double total;
    private Timestamp date;
    public Invoice(){
       this.ticket_sub=0.;
       this.service_sub=0.;
    }
    public Invoice(String id,Customer cus,Double ticket_sub,Double service_sub){
       this.id=id;
       this.cus=cus;
       this.ticket_sub=ticket_sub;
       this.service_sub=service_sub;
       this.total=ticket_sub+service_sub;
    }
    public String getId() {
        return id;
    }
    
     public void setId(String id) {
        this.id = id;
    }
    public Customer getCus() {
        return cus;
    }
    
     public void setCustomer(Customer cus) {
        this.cus = cus;
    }
    public Double getTicket_sub() {
        return ticket_sub;
    }
    public void setTicket_sub(Double ticket_sub) {
        this.ticket_sub = ticket_sub;
        total=ticket_sub+service_sub;
    }
    public Double getService_sub() {
        return service_sub;
    }
    public void setService_sub(Double service_sub) {
        this.service_sub = service_sub;
        total=ticket_sub+service_sub;
    }     
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }     

    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp Date) {
        this.date = Date;
    }
}
