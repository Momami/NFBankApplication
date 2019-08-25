package Managers;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;


import Classes.Account;
import Classes.Client;
import Classes.History;
import commonj.sdo.DataObject;
import commonj.sdo.helper.DataFactory;
import commonj.sdo.helper.XMLHelper;
import commonj.sdo.helper.XSDHelper;

public class HistoryManager {
    private static final String CLIENT_MODEL = "XMLSchemas/ClientCreateDelete.xsd";
    private static final String ACCOUNT_MODEL = "XMLSchemas/AccountCreateDelete.xsd";
    private static final String UPDATE_MODEL = "XMLSchemas/UpdateSchema.xsd";
    private static final String PO_NAMESPACE = "http://www.example.com/ClientCreateDelete";
    private static final String UPD_NAMESPACE = "http://www.example.com/UpdateSchema";
    private static final String ACC_NAMESPACE = "http://www.example.com/AccountCreateDelete";
    private static final String PO_XML = "buffer.xml";

    private static void defineTypes(String model) throws Exception {
        FileInputStream fis = new FileInputStream(model);
        XSDHelper.INSTANCE.define(fis, null);
        fis.close();
    }

    public static void createHistoryClient(Connection con, Client cl, Date date, History.Action act){
        try {
            String clientInfo = createXmlClient(cl);
            History history = new History(cl.getId(), History.ObjectType.CLIENT, act, date, clientInfo);
            createHistory(con, history);
        }
        catch (Exception e){}
    }

    public static void createHistoryAccount(Connection con, Account acc, Date date, History.Action act){
        try {
            String accountInfo = createXmlAccount(acc);
            History history = new History(acc.getId(), History.ObjectType.ACCOUNT, act, date, accountInfo);
            createHistory(con, history);
        }
        catch (Exception e){}
    }

    public static void createHistoryUpdate(Connection con, History.ObjectType objectType, List<String> updElements,
                                           long id, Date date){
        try {
            String upd = updateXml(updElements.get(0), updElements.get(1), updElements.get(2));
            History history = new History(id, objectType, History.Action.UPDATE, date, upd);
            createHistory(con, history);
        }
        catch (Exception e){}
    }

    private static String createXmlClient(Client cl) throws Exception {
        defineTypes(CLIENT_MODEL);

        DataObject clientInfo =
                DataFactory.INSTANCE.create(PO_NAMESPACE, "clientType");

        clientInfo.setString("unique_id", cl.getIdClient());
        clientInfo.setString("username", cl.getUsername());
        clientInfo.setString("password", cl.getPassword());
        clientInfo.setString("birth_date", cl.getBirthOfDate().toString());
        clientInfo.setString("name", cl.getFirstName());
        clientInfo.setString("surname", cl.getLastName());

        OutputStream stream = new FileOutputStream(PO_XML);
        XMLHelper.INSTANCE.save(clientInfo, PO_NAMESPACE, "clientInfo", stream);
        return readFile();
    }

    private static String createXmlAccount(Account acc) throws Exception {
        defineTypes(ACCOUNT_MODEL);

        DataObject accountInfo =
                DataFactory.INSTANCE.create(ACC_NAMESPACE, "accountType");

        accountInfo.setString("unique_id", acc.getIdAccount());
        accountInfo.setFloat("balance", acc.getBalance());
        accountInfo.setString("open_date", acc.getOpen_date().toString());
        if (acc.getClose_date() != null) {
            accountInfo.setString("close_date", acc.getClose_date().toString());
        }
        else{
            accountInfo.setString("close_date", "empty");
        }
        accountInfo.setString("status", acc.getStatus().toString());

        OutputStream stream = new FileOutputStream(PO_XML);
        XMLHelper.INSTANCE.save(accountInfo, PO_NAMESPACE, "accountInfo", stream);

        return readFile();
    }

    private static String updateXml(String name, String oldValue, String newValue) throws Exception{
        defineTypes(UPDATE_MODEL);

        DataObject upd =
                DataFactory.INSTANCE.create(UPD_NAMESPACE, "updateType");

        upd.setString("name", name);
        if (oldValue != null) {
            upd.setString("old_value", oldValue);
        }
        else{
            upd.setString("old_value", "empty");
        }
        if (newValue != null) {
            upd.setString("new_value", newValue);
        }
        else{
            upd.setString("new_value", "empty");
        }

        OutputStream stream = new FileOutputStream(PO_XML);
        XMLHelper.INSTANCE.save(upd, PO_NAMESPACE, "updateInfo", stream);

        return readFile();
    }


    private static String readFile() throws Exception{
        FileInputStream inFile = new FileInputStream(PO_XML);
        byte[] str = new byte[inFile.available()];
        inFile.read(str);
        return new String(str);
    }

    private static void createHistory(Connection con, History history) {
        try{
            String hisSql = "INSERT INTO [audit] ([object_id], object_type, action_date, action_id, new_value) " +
                    "VALUES (?, ?, ?, ?, ?);";
            PreparedStatement psstmt = con.prepareStatement(hisSql);
            psstmt.setString(1, Long.toString(history.getObject()));
            psstmt.setString(2, history.getObjectType().getId());
            psstmt.setString(3, history.getActionDate().toString());
            psstmt.setString(4, history.getAction().getId());
            psstmt.setString(5, history.getNew_value());
            psstmt.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
