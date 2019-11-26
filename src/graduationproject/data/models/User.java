/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.data.models;

import java.util.List;

/**
 *
 * @author cloud
 */
public class User {
    private int id;
    private String account;
    private String password;
    private String name;
    private int age;
    private String position;
    private String email;
    private String phone;
    
    private List<RecoveryQuestion> recoveryQuestionList;
    private Setting setting;
    
    public User() {        
    }

    public User(String account, String password, String name, int age, String position, String email, String phone, List<RecoveryQuestion> recoveryQuestionList) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.age = age;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.recoveryQuestionList = recoveryQuestionList;
        this.setting = new Setting();
    }

    public int getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<RecoveryQuestion> getRecoveryQuestionList() {
        return recoveryQuestionList;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRecoveryQuestionList(List<RecoveryQuestion> recoveryQuestionList) {
        this.recoveryQuestionList = recoveryQuestionList;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    
}
