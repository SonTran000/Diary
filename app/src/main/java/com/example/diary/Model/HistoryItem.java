package com.example.diary.Model;

public class HistoryItem {
    private String id;
    private String uid;
    private String changed;
    private String time;

    public String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getChanged() {
        return this.changed;
    }

    public void setChanged(final String changed) {
        this.changed = changed;
    }

    public HistoryItem(){}
    public HistoryItem(String id,String uid, String changed, String time)
    {
        this.id=id;
        this.uid=uid;
        this.changed=changed;
        this.time=time;
    }
}
