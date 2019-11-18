package com.example.diary.Model;



public class Item {
    private String id;
    private String title;
    private String content;
    private String color;
    private String time;
    private String uID;

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public String getuID() {
        return this.uID;
    }

    public void setuID(final String uID) {
        this.uID = uID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Item(){}
    public Item(String id,String title, String content,String color, String time, String uID){
        this.id=id;
        this.title=title;
        this.content=content;
        this.color=color;
        this.time=time;
        this.uID=uID;
    }
}
