package com.example.mylistview.model;

import java.io.Serializable;

public class Goods implements Serializable {
    private String type;
    private String date;
    private String number;
    private String person;
    private String state;

    @Override
    public String toString() {
        return "Goods{" +
                "type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", number='" + number + '\'' +
                ", person='" + person + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
