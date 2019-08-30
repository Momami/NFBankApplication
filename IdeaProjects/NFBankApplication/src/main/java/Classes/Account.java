package Classes;

import NewExceptions.DateException;
import NewExceptions.IdNotValidException;

import java.sql.Date;



public class Account {

    public enum AccountStatus{
        OPEN(1),
        CLOSED(2),
        SUSPEND(3);
        String num;

        AccountStatus(int num){
            this.num = Integer.toString(num);
        }

        public String getId(){
            return num;
        }

        public static AccountStatus getStatus(int id){
            return id == 1 ? OPEN : id == 2 ? CLOSED : SUSPEND;
        }
    }

    private String idAccount;
    private float balance;
    private Date open_date;
    private Date close_date;
    private AccountStatus status;
    private String idClient;

    public Account(String idAccount, float balance, Date open_date, Date close_date, AccountStatus status,
                   String idClient) throws DateException, IdNotValidException{
        setIdAccount(idAccount);
        setBalance(balance);
        setOpen_date(open_date);
        setClose_date(close_date);
        setStatus(status);
        setIdClient(idClient);
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) throws IdNotValidException{
        if(idAccount == null || idAccount.length() != 20){
            throw new IdNotValidException("Идентификатор должен состоять из 20 цифр!");
        }
        this.idAccount = idAccount;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Date getOpen_date() {
        return open_date;
    }

    public void setOpen_date(Date open_date) throws DateException{
        if(open_date == null){
            throw new DateException("Введите дату открытия счета!");
        }
        this.open_date = open_date;
    }

    public Date getClose_date() {
        return close_date;
    }

    public void setClose_date(Date close_date) {
        this.close_date = close_date;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        if (status == null){
            return;
        }
        this.status = status;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
}
