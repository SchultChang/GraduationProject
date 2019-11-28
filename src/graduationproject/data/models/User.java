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
    
    private List<RecoveryQuestion> recoveryQuestions;
    private Setting setting;
    
    public User() {        
    }

    public User(String account, String password, String name, int age, String position, String email, String phone, List<RecoveryQuestion> recoveryQuestions) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.age = age;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.recoveryQuestions = recoveryQuestions;
        this.setting = new Setting();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<RecoveryQuestion> getRecoveryQuestions() {
        return recoveryQuestions;
    }

    public void setRecoveryQuestions(List<RecoveryQuestion> recoveryQuestions) {
        this.recoveryQuestions = recoveryQuestions;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    
}
