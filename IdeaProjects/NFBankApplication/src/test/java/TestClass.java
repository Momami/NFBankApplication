import Classes.Account;
import Classes.Client;
import Classes.ConnectionDB;
import Managers.AccountManager;
import Managers.ClientManager;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TestClass {
    public static void main(String[] args) {
        try {
            String date = "21-12-1998";
            String Url = "jdbc:sqlserver://DESKTOP-0M0S9AF;databaseName=NFBankDB;integratedSecurity=true;";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date bdate = new Date(dateFormat.parse(date).getTime());
            Client cl = new Client("26531872653812345615", "momami", "Momami789",
                    bdate, "Милена", "Целикина");
            Connection con = ConnectionDB.createConn(Url);
            ClientManager clientManager = new ClientManager(con, cl);
            //clientManager.delete();
            clientManager.create();
            List<Client> clients = clientManager.select();
            for (Client elem: clients) {
                System.out.println(String.format("%s, %s, %s, %s, %s, %s", elem.getIdClient(), elem.getFirstName(),
                        elem.getLastName(), elem.getBirthOfDate(), elem.getUsername(), elem.getPassword()));
            }
            clientManager.update("surname", "Камбербэтч");
            System.out.println(bdate.toString());
            clients = clientManager.select();
            for (Client elem: clients) {
                System.out.println(String.format("%s, %s, %s, %s, %s, %s", elem.getIdClient(), elem.getFirstName(),
                        elem.getLastName(), elem.getBirthOfDate(), elem.getUsername(), elem.getPassword()));
            }

            Date dateAcc = new Date(dateFormat.parse("10-06-2019").getTime());
            Account acc = new Account("18739128354081514505", 2190.8f,
                    dateAcc, null, Account.AccountStatus.OPEN, cl.getId());
            AccountManager accManager = new AccountManager(con, acc);
            //accManager.delete();
            accManager.create();
            List<Account> accounts = accManager.select();
            for (Account elem: accounts) {
                System.out.println(String.format("%s, %s, %s, %s", elem.getIdAccount(), elem.getBalance(),
                        elem.getOpen_date(), elem.getStatus()));
            }
            accManager.update("close_date", "2019-08-12");
            accManager.update("status", "3");
            accounts = accManager.select();
            for (Account elem: accounts) {
                System.out.println(String.format("%s, %s, %s, %s, %s", elem.getIdAccount(), elem.getBalance(),
                        elem.getOpen_date(), elem.getClose_date(), elem.getStatus()));
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }
        catch (ParseException e){
            System.out.println(e);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
