package com.jojo.crud;

public class studentDetails {
    int id, sem;
    String name;

    public  studentDetails(){

    }
    public studentDetails(int id, String name, int sem){
        this.id = id;
        this.name = name;
        this.sem = sem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSem() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem = sem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
