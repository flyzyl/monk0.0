package cn.andye.fragmenttabhost.model;



import java.io.Serializable;


public class Time {

    private java.util.Date onScreenTime;
    private java.util.Date offScreenTime;
    private String date;

    public java.util.Date getOnScreenTime() {
        return onScreenTime;
    }

    public void setOnScreenTime(java.util.Date onScreenTime) {
        this.onScreenTime = onScreenTime;
    }

    public java.util.Date getOffScreenTime() {
        return offScreenTime;
    }

    public void setOffScreenTime(java.util.Date offScreenTime) {
        this.offScreenTime = offScreenTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
