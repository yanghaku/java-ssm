package edu.study.model;

public class UserKeyword extends Keyword{

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

}