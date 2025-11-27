/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Lenovo
 */
public class Movie {
    private String id;
    private String title;
    private String genre;
    private String director;
    private int duration;
    private boolean isShowing;
    public Movie(){
    }
    public Movie(String id,String title,String genre,String director,int duration,boolean isShowing){
        this.id=id;
        this.title=title;
        this.genre=genre;
        this.director=director;
        this.duration=duration;
        this.isShowing=isShowing;
    }
    public String getId(){return id;}
    public void setId(String id){this.id=id;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
    public String getGenre(){return genre;}
    public void setGenre(String genre){this.genre=genre;}
    public String getDirector(){return director;}
    public void setDirector(String director){this.director=director;}
    public int getDuration(){return duration;}
    public void setDuration(int duration){this.duration=duration;}
    public boolean isShowing(){return isShowing;}
    public void setShowing(boolean isShowing){this.isShowing=isShowing;}
}
