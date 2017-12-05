package ca.qc.lbpsb.fusion.fcmnotification.Model;

import java.util.Date;

/**
 * Created by Administrator on 12/3/2017.
 */

public class Notifications {

    private int id_notification;
    private int id_channel;
    private Date date;
    private  String title;
    private  String message;
    private  String priority;
    private  String image;

    //-------------------------------------------

    public Notifications() {
    }
    //--------------------------------------------

    public Notifications(int id_notification, int id_channel, Date date, String title, String message, String priority, String image) {
        this.id_notification = id_notification;
        this.id_channel = id_channel;
        this.date = date;
        this.title = title;
        this.message = message;
        this.priority = priority;
        this.image = image;
    }

    public Notifications( String title, String message ) {
        this.title = title;
        this.message = message;

    }


    public Notifications( String title,String priority,String message ) {
        this.title = title;
        this.priority = priority;
        this.message = message;

    }
    //------------------------------------------


    public int getId_notification() {
        return id_notification;
    }

    public void setId_notification(int id_notification) {
        this.id_notification = id_notification;
    }

    public int getId_channel() {
        return id_channel;
    }

    public void setId_channel(int id_channel) {
        this.id_channel = id_channel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //------------------------------------------------


    @Override
    public String toString() {
        return "Notifications{" +
                "id_notification=" + id_notification +
                ", id_channel=" + id_channel +
                ", date=" + date +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
