package com.example.asnaui.mobiledtr.Global;

public class User {
    public String id, name;

    public User(String id, String name) {
        this.id = id;
        this.name = formatName(name);
    }

    public String formatName(String name) {
        String[] name_array = name.split(" ");
        String result = "";
        for (int i = 0; i < name_array.length; i++) {

            result += name_array[i].toLowerCase().substring(0, 1).toUpperCase() + name_array[i].toLowerCase().substring(1) + " ";
        }
        return result.trim();
    }
}
