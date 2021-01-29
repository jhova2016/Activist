package com.example.activist;

public class ElementRecyclerActivist {


    String Id;
    String PhotoPath;
    String Name;
    String Direction;
    String Phone ;
    String ElectorKey ;
    String DirectBoss;
    String Notes ;
    String Date ;
    String Up ;

    public ElementRecyclerActivist(String id, String photoPath, String name, String direction, String phone, String electorKey, String directBoss, String notes, String date, String up) {
        Id = id;
        PhotoPath = photoPath;
        Name = name;
        Direction = direction;
        Phone = phone;
        ElectorKey = electorKey;
        DirectBoss = directBoss;
        Notes = notes;
        Date = date;
        Up = up;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUp() {
        return Up;
    }

    public void setUp(String up) {
        Up = up;
    }

    public String getPhotoPath() {
        return PhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getElectorKey() {
        return ElectorKey;
    }

    public void setElectorKey(String electorKey) {
        ElectorKey = electorKey;
    }

    public String getDirectBoss() {
        return DirectBoss;
    }

    public void setDirectBoss(String directBoss) {
        DirectBoss = directBoss;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
