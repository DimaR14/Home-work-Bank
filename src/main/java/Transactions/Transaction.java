package Transactions;

import javax.persistence.*;

@Entity
@Table(name="transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="transaction_id")
    private int id;

    @Column(name="account_id1")
    private int id1;

    @Column(name="account_id2")
    private int id2;

    @Column(name="corrency")
    private String corrency;

    @Column(name="sum")
    private double sum;

    public Transaction(){}

    public Transaction(int id1, int id2, String corrency, double sum) {
        this.id1 = id1;
        this.id2 = id2;
        this.corrency = corrency;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public String getCorrency() {
        return corrency;
    }

    public void setCorrency(String corrency) {
        this.corrency = corrency;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", id1=" + id1 +
                ", id2=" + id2 +
                ", corrency='" + corrency + '\'' +
                ", sum=" + sum +
                '}';
    }
}


