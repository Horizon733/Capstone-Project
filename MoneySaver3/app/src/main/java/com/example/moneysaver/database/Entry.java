package com.example.moneysaver.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "entry")
public class Entry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private Date date;
    @NonNull
    private double salary;
    @NonNull
    private double expenses;
    @NonNull
    private double savings;
    @Ignore
    public Entry(Date date,double salary,double expenses, double savings){
        this.date = date;
        this.salary = salary;
        this.expenses = expenses;
        this.savings = savings;
    }
    public Entry(int id,Date date,double salary,double expenses, double savings){
        this.id = id;
        this.date = date;
        this.salary = salary;
        this.expenses = expenses;
        this.savings = savings;
    }
    public int getId(){return id;}
    public double getExpenses() {
        return expenses;
    }
    public double getSalary(){ return salary;}

    public Date getDate() {
        return date;
    }

    public double getSavings() {
        return savings;
    }
    public void setId(int id) {
        this.id = id;
    }
}
