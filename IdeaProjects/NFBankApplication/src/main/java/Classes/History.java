package Classes;

import java.sql.Date;

public class History {
    public enum ObjectType{
        CLIENT(1), ACCOUNT(2);
        String num;

        ObjectType(int num){
            this.num = Integer.toString(num);
        }

        public String getId(){
            return num;
        }

        public static History.ObjectType getStatus(int id){
            return id == 1 ? CLIENT : ACCOUNT;
        }
    }

    public enum Action{
        CREATE(1),
        UPDATE(2),
        DELETE(3);

        String num;

        Action(int num){
            this.num = Integer.toString(num);
        }

        public String getId(){
            return num;
        }

        public static History.Action getStatus(int id){
            return id == 1 ? CREATE : id == 2 ? UPDATE : DELETE;
        }
    }
    private long object;
    private ObjectType objectType;
    private Action action;
    private Date actionDate;
    private String new_value;

    public History(long object, ObjectType objectType, Action action, Date actionDate, String new_value) {
        this.object = object;
        this.objectType = objectType;
        this.action = action;
        this.actionDate = actionDate;
        this.new_value = new_value;
    }

    public long getObject() {
        return object;
    }

    public void setObject(long object) {
        this.object = object;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getNew_value() {
        return new_value;
    }

    public void setNew_value(String new_value) {
        this.new_value = new_value;
    }
}
