package Accounts;

import Users.User;

import javax.persistence.*;

@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="account_id")
    private int id;

    @Column(name="user_id",nullable = false)
    private int userId;

    @Column(name="USD")
    private double USD;

    @Column(name="EUR")
    private double EUR;

    @Column(name="UAH")
    private double UAH;

    public Account(){}

    public Account(int userId, double usd, double eur, double uah) {
        this.userId = userId;
        this.USD = usd;
        this.EUR = eur;
        this.UAH = uah;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getUsd() {
        return USD;
    }

    public void setUsd(double usd) {
        this.USD = usd;
    }

    public double getEur() {
        return EUR;
    }

    public void setEur(double eur) {
        this.EUR = eur;
    }

    public double getUah() {
        return UAH;
    }

    public void setUah(double uah) {
        this.UAH = uah;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", usd=" + USD +
                ", eur=" + EUR +
                ", uah=" + UAH +
                '}';
    }
}
