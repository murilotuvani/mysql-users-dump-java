package org.example;


public class User {
    final String username;
    final String host;

    public User(String username, String host) {
        this.username = username;
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", host='" + host + '\'' +
                '}';
    }



}