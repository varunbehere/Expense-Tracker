package com.example.expensetracker;

public class Student {
    public Student(){}
    String password;
    String email;
    String username;
    String spend;
    String income;
    String color_;
    String total_saving;
    String total_spending;
    String friend;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSpend() {
        return spend;
    }

    public void setSpend(String spend) {
        this.spend = spend;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getColor_() {
        return color_;
    }

    public void setColor_(String color_) {
        this.color_ = color_;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getTotal_saving() {
        return total_saving;
    }

    public void setTotal_saving(String total_saving) {
        this.total_saving = total_saving;
    }

    public String getTotal_spending() {
        return total_spending;
    }

    public void setTotal_spending(String total_spending) {
        this.total_spending = total_spending;
    }

    public Student(String password, String email, String username, String spend, String income, String color_, String friend,
                   String total_spending, String total_saving) {
        this.password = password;
        this.email = email;
        this.username = username;
        this.spend = spend;
        this.income = income;
        this.color_ = color_;
        this.friend = friend;
        this.total_spending=total_spending;
        this.total_saving=total_saving;
    }
}
