package Exchange;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="exchange")
public class Exchange {

    @Id
    @Column(name="corrency")
    private String corrency;

    @Column(name="rate")
    private double rate;

    public Exchange(){}

    public Exchange(String corrency, double rate) {
        this.corrency = corrency;
        this.rate = rate;
    }

    public String getCorrency() {
        return corrency;
    }

    public void setCorrency(String corrency) {
        this.corrency = corrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "corrency='" + corrency + '\'' +
                ", rate=" + rate +
                '}';
    }
}
