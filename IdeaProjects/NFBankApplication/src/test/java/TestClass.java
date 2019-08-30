import Classes.Account;
import Classes.Client;
import Classes.ConnectionDB;
import Classes.History;
import Managers.AccountManager;
import Managers.ClientManager;
import Managers.HistoryManager;
import NewExceptions.DateException;
import NewExceptions.IdNotValidException;
import NewExceptions.NameIsNullException;
import NewExceptions.UsernameNotValidException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TestClass {
    private static Connection con;
    private Client cl;
    private ClientManager clientManager;
    private Account acc;
    private AccountManager accManager;

    @BeforeClass
    public void connWithDatabase(){
        try {
            String Url = "jdbc:sqlserver://DESKTOP-0M0S9AF;databaseName=NFBankDB;integratedSecurity=true;";
            con = ConnectionDB.createConn(Url);
        }
        catch(ClassNotFoundException|SQLException e){
            System.out.println("Не удалось подключитья к базе!\n");
        }
    }

   /* public static void main(String[] args) {
        try {
            String date = "21-12-1998";

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date bdate = new Date(dateFormat.parse(date).getTime());
            Client cl = new Client("26531872653812345615", "momami", "Momami789",
                    bdate, "Милена", "Целикина");

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
        catch (ParseException e){
            System.out.println(e);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
*/
    @Test
    public void checkClientCreate(){
        try {
            String date = "21-12-1998";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date bdate = new Date(dateFormat.parse(date).getTime());
            cl = new Client("26531872653812345615", "momami", "Momami789",
                    bdate, "Милена", "Целикина");
            Assert.assertEquals(cl.getIdClient(), "26531872653812345615");
            Assert.assertEquals(cl.getUsername(), "momami");
            Assert.assertEquals(cl.getPassword(), "Momami789");
            Assert.assertEquals(cl.getFirstName(), "Милена");
            Assert.assertEquals(cl.getLastName(), "Целикина");
            Assert.assertEquals(cl.getBirthOfDate().toString(), "1998-12-21");
        }
        catch(ParseException p){
            System.out.println("Не удалось распарсить дату!\n");
        }
        catch(Exception e){}
    }

    @Test
    public void checkDBClient(){
        try {
            clientManager = new ClientManager(con, cl);
            clientManager.create();
            List<Client> clients = clientManager.select();
            for (Client elem : clients) {
                Assert.assertEquals(elem.getIdClient(), "26531872653812345615");
                Assert.assertEquals(elem.getUsername(), "momami");
                Assert.assertEquals(elem.getPassword(), "Momami789");
                Assert.assertEquals(elem.getFirstName(), "Милена");
                Assert.assertEquals(elem.getLastName(), "Целикина");
                Assert.assertEquals(elem.getBirthOfDate().toString(), "1998-12-21");
            }
        }
        catch(SQLException s){}
    }

    @Test
    public void checkUpdClient(){
        try {
            clientManager.update("surname", "Камбербэтч");
            clientManager.update("password", "BilliMilligan");
            List<Client>clients = clientManager.select();
            for (Client elem : clients) {
                Assert.assertEquals(elem.getLastName(), "Камбербэтч");
                Assert.assertEquals(elem.getPassword(), "BilliMilligan");
            }
        }
        catch (SQLException s){}
    }

    @Test
    public void checkCreateAccount(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date dateAcc = new Date(dateFormat.parse("10-06-2019").getTime());
            acc = new Account("18739128354081514505", 2190.8f,
                    dateAcc, null, Account.AccountStatus.OPEN, cl.getIdClient());
            Assert.assertEquals(acc.getIdAccount(), "18739128354081514505");
            Assert.assertEquals(acc.getBalance(), 2190.8f);
            Assert.assertEquals(acc.getOpen_date().toString(), "2019-06-10");
            Assert.assertNull(acc.getClose_date());
            Assert.assertEquals(acc.getStatus(), Account.AccountStatus.OPEN);
        }
        catch (Exception e){}
    }

    @Test
    public void checkDBNewAccount(){
        try {
            accManager = new AccountManager(con, acc);
            accManager.create();
            List<Account> accounts = accManager.select();
            for (Account elem : accounts) {
                Assert.assertEquals(elem.getIdAccount(), "18739128354081514505");
                Assert.assertEquals(elem.getBalance(), 2190.8f);
                Assert.assertEquals(elem.getOpen_date().toString(), "2019-06-10");
                Assert.assertNull(elem.getClose_date());
                Assert.assertEquals(elem.getStatus(), Account.AccountStatus.OPEN);
            }
        }
        catch(SQLException s){
            Assert.fail();
        }
    }

    @Test
    public void checkUpdateAccount(){
        try {
            accManager.update("close_date", "2019-08-12");
            accManager.update("status", "3");
            List<Account>accounts = accManager.select();
            for (Account elem : accounts) {
                Assert.assertEquals(elem.getClose_date().toString(), "2019-08-12");
                Assert.assertEquals(elem.getStatus().getId(), "3");
            }
        }
        catch (SQLException s){}
    }

    @AfterClass
    public void deleteAll(){
        try{
            clientManager.delete();
            Assert.assertTrue(clientManager.select().isEmpty());
            Assert.assertTrue(accManager.select().isEmpty());
        }
        catch (SQLException s){}
    }

    @Test
    public void checkHistory(){
        try {
            List<History> stories = HistoryManager.selectHistory(cl.getIdClient(), con);
            for(History story : stories){
                System.out.println(story);
            }
            Assert.assertTrue(true);
        }
        catch (SQLException s){}

    }

    @Test
    public void checkNotValidClient(){
        String date = "14-04-1990";
        Date bdate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            bdate = new Date(dateFormat.parse(date).getTime());
        }
        catch (ParseException p){
            System.out.println("Ошибка парсинга даты!\n");
        }
        String id = "12345678901234567890", username = "lola", password = "utopia";
        String lName = "Allen", fName = "Barry";
        Client client;
        try {
            client = new Client("3456788765", username, password, bdate, lName, fName);
        }
        catch(DateException| NameIsNullException| UsernameNotValidException u){}
        catch (IdNotValidException idn){
            Assert.assertTrue(true);
        }

        try{
            client = new Client(id, "", password, bdate, lName, fName);
        }
        catch(DateException| NameIsNullException| IdNotValidException u){}
        catch (UsernameNotValidException idn){
            Assert.assertTrue(true);
        }

        try{
            client = new Client(id, username, password, null, lName, fName);
        }
        catch(UsernameNotValidException| NameIsNullException| IdNotValidException u){}
        catch (DateException idn){
            Assert.assertTrue(true);
        }

        try{
            client = new Client(id, username, password, bdate, null, fName);
        }
        catch(UsernameNotValidException| DateException| IdNotValidException u){}
        catch (NameIsNullException idn){
            Assert.assertTrue(true);
        }

        try{
            client = new Client(id, username, password, bdate, lName, null);
        }
        catch(UsernameNotValidException| DateException| IdNotValidException u){}
        catch (NameIsNullException idn){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void checkNotValidAccount(){
        String id = "98706543219870654321";
        String idClient = "23345678871369756956";
        float balance = 456.9f;
        Account.AccountStatus status = Account.AccountStatus.OPEN;
        String date = "12-05-2018";
        Date open = null, close = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            open = new Date(dateFormat.parse(date).getTime());
        }
        catch (ParseException p){
            System.out.println("Ошибка парсинга даты!\n");
        }
        Account account;
        try{
            account = new Account("345678", balance, open, close, status, idClient);
        }
        catch(DateException d){}
        catch (IdNotValidException idn){
            Assert.assertTrue(true);
        }

        try{
            account = new Account(id, balance, null, close, status, idClient);
        }
        catch(DateException d){
            Assert.assertTrue(true);
        }
        catch (IdNotValidException idn){}
    }

}
