/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

import graduationproject.data.DataManager;
import graduationproject.data.models.RecoveryQuestion;
import graduationproject.data.models.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cloud
 */
public class UserManagementController {
    private String resultMessage;
    
    public String getResultMessage() {
        return this.resultMessage;
    }
    
    public boolean processCreatingUser(List<String> data) {
        ResultMessageGenerator messageGenerator = new ResultMessageGenerator();
        
        if (data.get(DataOrders.ACCOUNT.getValue()).isEmpty() || data.get(DataOrders.PASSWORD.getValue()).isEmpty() 
                || data.get(DataOrders.CONFIRM.getValue()).isEmpty()) {
            this.resultMessage = messageGenerator.CREATING_FAILED_DATA;
            return false;
        }
        
        if (!data.get(DataOrders.PASSWORD.getValue()).equals(data.get(DataOrders.CONFIRM.getValue()))) {
            this.resultMessage = messageGenerator.CREATING_FAILED_CONFIRM;
            return false;
        }
        
        if (!data.get(DataOrders.CONDITION.getValue()).equals(String.valueOf(true))) {
            this.resultMessage = messageGenerator.CREATING_FAILED_CONDITION;
            return false;
        }
        
        List<User> userList = DataManager.getInstance().getUserManager().getUsers(data.get(DataOrders.ACCOUNT.getValue()));
        if (userList != null && userList.size() != 0) {
            this.resultMessage = messageGenerator.CREATING_FAILED_ACCOUNT;
            return false;
        }
        
        List<RecoveryQuestion> questionList = null;
        if (data.size() > DataOrders.QUESTION_ANSWER.getValue()) {
            int i = DataOrders.QUESTION_ANSWER.getValue();
            questionList = new ArrayList<RecoveryQuestion>();
            while (i < data.size()) {
                questionList.add(new RecoveryQuestion(data.get(i), data.get(i + 1)));
                i += 2;
            }
        }
        
        int creatingResult = DataManager.getInstance().getUserManager().createUser(
                data.get(DataOrders.ACCOUNT.getValue()),
                data.get(DataOrders.PASSWORD.getValue()),
                data.get(DataOrders.NAME.getValue()),
                Integer.parseInt(data.get(DataOrders.AGE.getValue())),
                data.get(DataOrders.POSITION.getValue()),
                data.get(DataOrders.EMAIL.getValue()),
                data.get(DataOrders.PHONE.getValue()),
                questionList
        );
        
        if (creatingResult <= -1) {
            this.resultMessage = messageGenerator.CREATING_FAILED_OTHER;
            return false;
        }
        
        return true;
    }   
    
    public enum DataOrders {
        ACCOUNT(0),
        PASSWORD(1),
        CONFIRM(2),
        NAME(3),
        AGE(4), 
        POSITION(5),
        EMAIL(6),
        PHONE(7), 
        CONDITION(8),
        QUESTION_ANSWER(9); //after this enum, each question will be followed by an answer
        
        private final int value;
        private DataOrders(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
    
    public class ResultMessageGenerator {
        //creating user 
        public String CREATING_FAILED_CONDITION = "Please accept our terms and conditions in order to use this application.";
        public String CREATING_FAILED_CONFIRM = "Your confirmation is mismatched. Please input again.";
        public String CREATING_FAILED_ACCOUNT = "This account has already been created. Choose other name for your account.";
        public String CREATING_FAILED_DATA = "Please fill all the required information such account, password and confirm.";
        public String CREATING_FAILED_OTHER = "Some error happened when saving user data. Please try again.";        
    }   
    
}
