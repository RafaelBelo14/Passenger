package com.example.passenger;
import java.util.ArrayList;

public class ReadNwriteDB {
    private ArrayList<User> userList = new ArrayList<>();

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        userList.add(user);
    }
}
