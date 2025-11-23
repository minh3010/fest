/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

    package entity;

public class Customer {
    private String cusID;
    private String cusName;
    private String cusPhone;
    private String cusEmail;
    private int cusPoint;
    public Customer() {}

    public Customer(String cusID, String cusName, String cusPhone, String cusEmail) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.cusPhone = cusPhone;
        this.cusEmail = cusEmail;
        this.cusPoint=0;
    }
    public void addPoint(Invoice inv){
       cusPoint+=(int)(inv.getTotal()/1000);
    }
    public String getCusID() {
        return cusID;
    }

    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }
    
    public int getCusPoint() {
        return cusPoint;
    }

    public void setCusPoint(int cusPoint) {
        this.cusPoint = cusPoint;
    }    

    @Override
    public String toString() {
        return cusName + " (" + cusID + ")";
    }
}


