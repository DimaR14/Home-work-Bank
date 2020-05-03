package Users;

import Accounts.Account;
import Exchange.Exchange;
import Transactions.Transaction;
import com.sun.org.apache.xpath.internal.objects.XObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class Main {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("JPATest");
        em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1: put money");
            System.out.println("2: send money");
            System.out.println("3: exchange");
            System.out.println("4: get all money (UAH)");
            System.out.println("->  ");

            String s = scanner.nextLine();
            if ("1".equals(s)) {
                putMoney();
                break;
            } else if ("2".equals(s)) {
                sendMoney();
                break;
            } else if ("3".equals(s)) {
                exchange();
                break;
            } else if ("4".equals(s)) {
                System.out.println(getAllMoney());
                break;
            } else {
                return;
            }
        }

    }


    public static Transaction startSendMoney() {
        Scanner sc = new Scanner(System.in);
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Enter the Id, from which account do you want to send money? ");
        int id1 = sc.nextInt();
        System.out.println("Enter the id of the account you want to send money to:");
        int id2 = sc.nextInt();
        int x = 0;
        for (Account ac : getAccounts()) {
            if (ac.getId() == id1) x++;
            if (ac.getId() == id2) x++;
            if (x == 2) {
                System.out.println("Enter currency: //USD,EUR,UAH// ");
                String currency = sc1.nextLine();
                System.out.println("Enter sum: ");
                double sum = sc.nextDouble();
                Transaction tr = new Transaction(id1, id2, currency, sum);
                return tr;
            }
        }
        return null;
    }

    public static void sendMoney() {
        Transaction tr = startSendMoney();
        Query query = em.createQuery("SELECT " + tr.getCorrency() + " FROM Account WHERE id=" + "'" + tr.getId1() + "'");
        if ((Double) query.getSingleResult() >= tr.getSum()) {
            trPersist(tr);
            double newSum1 = (Double) query.getSingleResult() - tr.getSum();
            Query query1 = em.createQuery("SELECT " + tr.getCorrency() + " FROM Account WHERE id=" + "'" + tr.getId2() + "'");
            double newSum2 = (Double) query1.getSingleResult() + tr.getSum();
            changeMoney(tr, newSum1, tr.getId1());
            changeMoney(tr, newSum2, tr.getId2());
        } else System.out.println("NOT enough money!");
        return;
    }

    public static Transaction startPutMoney() {
        Scanner sc = new Scanner(System.in);
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Enter the account ID: ");
        int id = sc.nextInt();
        for (Account ac : getAccounts()) {
            if (ac.getId() == id) {
                System.out.println("Enter currency: //USD,EUR,UAH// ");
                String currency = sc1.nextLine();
                System.out.println("Enter sum: ");
                double sum = sc.nextDouble();
                Transaction tr = new Transaction(id, id, currency, sum);
                return tr;
            }
        }
        System.out.println("WRONG Id!");
        return null;
    }


    public static void putMoney() {
        Transaction tr = startPutMoney();
        trPersist(tr);
        Query query = em.createQuery("SELECT " + tr.getCorrency() + " FROM Account WHERE id=" + "'" + tr.getId1() + "'");
        double r = (Double) query.getSingleResult() + tr.getSum();
        changeMoney(tr, r, tr.getId1());
    }


    public static Transaction startExchange() {
        Scanner sc = new Scanner(System.in);
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Enter account id:");
        int id = sc.nextInt();
        for (Account ac : getAccounts()) {
            if (ac.getId() == id) {
                System.out.println("What currency do you want to sell?");
                String currency = sc1.nextLine();
                System.out.println("Hom much?");
                double sum = sc.nextDouble();
                Transaction tr = new Transaction(id, id, currency, sum);
                return tr;
            }
        }
        System.out.println("WRONG Id!");
        return null;
    }


    public static void exchange() {
        Transaction tr = startExchange();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter what currency do you want to buy?");
        String currency = sc.nextLine();
        Transaction tr1 = new Transaction();
        if (tr.getCorrency().equals("USD") || tr.getCorrency().equals("EUR") && currency.equals("UAH")) {
            double sum = tr.getSum() * getRate(tr.getCorrency());
            tr1 = new Transaction(tr.getId1(), tr.getId2(), currency, sum);
        }
        if (tr.getCorrency().equals("UAH") && currency.equals("EUR") || currency.equals("USD")) {
            double sum = tr.getSum() / getRate(currency);
            tr1 = new Transaction(tr.getId1(), tr.getId2(), currency, sum);
        }
        if (tr.getCorrency().equals("USD") || tr.getCorrency().equals("EUR") && currency.equals("USD") || currency.equals("EUR")) {
            double sum = tr.getSum() * getRate(tr.getCorrency()) / getRate(currency);
            tr1 = new Transaction(tr.getId1(), tr.getId2(), currency, sum);
        }
        Query query = em.createQuery("SELECT " + tr.getCorrency() + " FROM Account WHERE id=" + "'" + tr.getId1() + "'");
        if ((Double) query.getSingleResult() >= tr.getSum()) {
            trPersist(tr);
            trPersist(tr1);
            double newSum1 = (Double) query.getSingleResult() - tr.getSum();
            Query query1 = em.createQuery("SELECT " + tr1.getCorrency() + " FROM Account WHERE id=" + "'" + tr1.getId2() + "'");
            double newSum2 = (Double) query1.getSingleResult() + tr1.getSum();
            changeMoney(tr, newSum1, tr.getId1());
            changeMoney(tr1, newSum2, tr1.getId2());
        } else System.out.println("NOT enough money!");
        return;
    }

    public static double getAllMoney() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter account id:");
        int id = sc.nextInt();
        double sum = 0.0;
        Account account = getAccount(id);
        if (account.getEur() != 0) {
            double eurUah = account.getEur() * getRate("EUR");
            sum += eurUah;
        }
        if (account.getUsd() != 0) {
            double usdUah = account.getUsd() * getRate("USD");
            sum += usdUah;
        }
        sum += account.getUah();
        return sum;
    }

    public static void changeMoney(Transaction tr, double r, int id) {
        em.getTransaction().begin();
        try {
            if (tr.getCorrency().equals("UAH")) {
                getAccount(id).setUah(r);
            }
            if (tr.getCorrency().equals("EUR")) {
                getAccount(id).setEur(r);
            }
            if (tr.getCorrency().equals("USD")) {
                getAccount(id).setUsd(r);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
        System.out.println(getAccount(tr.getId1()));
    }


    public static double getRate(String currency) {
        Query query = em.createQuery("SELECT rate FROM  Exchange WHERE corrency =" + "'" + currency + "'");
        double r = (Double) query.getSingleResult();
        return (r);
    }

    public static Account getAccount(int id) {
        Account account = new Account();
        Query query = em.createQuery("SELECT account FROM Account account WHERE id=" + "'" + id + "'");
        return account = (Account) query.getSingleResult();
    }

    public static List<Account> getAccounts() {
        emf = Persistence.createEntityManagerFactory("JPATest");
        em = emf.createEntityManager();
        Query query = em.createQuery("SELECT c FROM Account c", Account.class);
        List<Account> list = (List<Account>) query.getResultList();
        return list;
    }

    public static void trPersist(Transaction tr) {
        em.getTransaction().begin();
        try {
            em.persist(tr);
            em.getTransaction().commit();

            System.out.println(tr.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }
}