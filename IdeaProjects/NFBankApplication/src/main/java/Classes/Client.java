package Classes;

import NewExceptions.DateException;
import NewExceptions.IdNotValidException;
import NewExceptions.NameIsNullException;
import NewExceptions.UsernameNotValidException;

import java.lang.String;
import java.math.BigInteger;
import java.sql.Date;


public class Client {
    private BigInteger id = null;
    private String IdClient;
    private String username;
    private String password;
    private Date birthOfDate;
    private String firstName;
    private String lastName;

    public Client(String IDClient, String username, String password,
                  Date birthOfDate, String firstName, String lastName)
    throws DateException, IdNotValidException, NameIsNullException, UsernameNotValidException{
        setIdClient(IDClient);
        setUsername(username);
        setPassword(password);
        setBirthOfDate(birthOfDate);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getIdClient() {
        return IdClient;
    }

    public void setIdClient(String IdClient) throws IdNotValidException{
        if (IdClient == null || IdClient.length() != 20){
            throw new IdNotValidException("Идентификатор должен состоять из 20 цифр!");
        }
        this.IdClient = IdClient;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws UsernameNotValidException{
        if (username == null || username.length() > 20){
            throw new UsernameNotValidException("Username не должен превышать 20 символов!");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthOfDate() {
        return birthOfDate;
    }

    public void setBirthOfDate(Date birthOfDate) throws DateException {
        if (birthOfDate == null){
            throw new DateException("Дата рождения введена неверно!");
        }
        this.birthOfDate = birthOfDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws NameIsNullException{
        if (firstName == null){
            throw new NameIsNullException("Имя не введено!");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws NameIsNullException{
        if(lastName == null){
            throw new NameIsNullException("Фамилия не введена!");
        }
        this.lastName = lastName;
    }
}
