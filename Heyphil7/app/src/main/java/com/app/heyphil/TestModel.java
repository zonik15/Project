package com.app.heyphil;

/**
 * Created by HCNatividad on 8/26/2016.
 */
public class TestModel {
    public String name;
    public String hometown;

    public TestModel(){

    }
    /*
    public TestModel(String name, String hometown) {
        this.name = name;
        this.hometown = hometown;
    }

    public static ArrayList<TestModel> getUsers() {
        ArrayList<TestModel> users = new ArrayList<TestModel>();
        users.add(new TestModel("Harry", "San Diego"));
        users.add(new TestModel("Marla", "San Francisco"));
        users.add(new TestModel("Sarah", "San Marco"));
        return users;
    }*/

    public void setTitle(String title) {
        this.name = title;
    }
    public String getTitle() {
        return this.name;
    }

    public void setDescription(String desc){
        this.hometown = desc;
    }
    public String getDescription(){
        return this.hometown;
    }
}