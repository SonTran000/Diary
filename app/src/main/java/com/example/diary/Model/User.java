package com.example.diary.Model;

public class User {
    private String id;
    private String name;
    private String email;

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
    public User(){}
    public User(String id,String name, String email)
    {
        this.id=id;
        this.name=name;
        this.email=email;
    }
}
