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
    private int[] accountIds;
    private final int DEFAULT_USER_AGE = 0;
    private final String DEFAULT_USER_STRING_DATA = "";
    private boolean accountRemembered;
    

    public int getCheckingAccountId() {
        return accountIds[0];
    }

    public int[] getAccountIds() {
        return this.accountIds;
    }

    public String getResultMessage() {
        return this.resultMessage;
    }

    public boolean isAccountRemembered() {
        return accountRemembered;
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
                data.get(DataOrders.AGE.getValue()).isEmpty() ? DEFAULT_USER_AGE : Integer.parseInt(data.get(DataOrders.AGE.getValue())),
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

    public List<String> processGettingQuestionsForRecovery(List<String> data) {
        List<User> users = DataManager.getInstance().getUserManager().getUsers(data.get(DataOrders.ACCOUNT.getValue()));
        ResultMessageGenerator messageGenerator = new ResultMessageGenerator();

        if (users == null) {
            this.resultMessage = messageGenerator.RECOVERY_FAILED_OTHER;
            return null;
        }

        if (users.size() == 0) {
            this.resultMessage = messageGenerator.RECOVERY_FAILED_NON_EXISTED_ACCOUNT;
            return null;
        }

        User foundUser = users.get(0);
        if (foundUser.getRecoveryQuestionList().size() == 0) {
            this.resultMessage = messageGenerator.RECOVERY_FAILED_NO_QUESTIONS;
            return null;
        }

        List<String> result = new ArrayList<String>();
        this.accountIds = new int[1];
        this.accountIds[0] = foundUser.getId();

        for (int i = 0; i < foundUser.getRecoveryQuestionList().size(); i++) {
            result.add(foundUser.getRecoveryQuestionList().get(i).getQuestion());
        }

        return result;
    }

    public String processRecoveringPassword(int accountId, String... answers) {
        ResultMessageGenerator messageGenerator = new ResultMessageGenerator();
        User checkingUser = DataManager.getInstance().getUserManager().getUser(accountId);

        if (checkingUser == null) {
            this.resultMessage = messageGenerator.RECOVERY_FAILED_OTHER;
            return null;
        }

        if (answers == null || answers.length < checkingUser.getRecoveryQuestionList().size()) {
            this.resultMessage = messageGenerator.RECOVERY_FAILED_LACK_ANSWER;
            return null;
        }

        for (int i = 0; i < answers.length; i++) {
            if (!answers[i].equals(checkingUser.getRecoveryQuestionList().get(i).getAnswer())) {
                this.resultMessage = messageGenerator.RECOVERY_FAILED_INCORRECT_ANSWER;
                return null;
            }
        }

        return messageGenerator.RECOVERY_SUCCESS_INFORM_PREFIX + checkingUser.getPassword();
    }

    public List<String> processGettingRememberedAccounts() {
        List<String> result = null;
        List<User> rememberedUsers = DataManager.getInstance().getUserManager().getUsers(true);

        if (rememberedUsers != null && rememberedUsers.size() > 0) {
            result = new ArrayList<String>();
            int tempSize = rememberedUsers.size();
            this.accountIds = new int[tempSize];

            for (int i = 0; i < tempSize; i++) {
                result.add(rememberedUsers.get(i).getAccount());
                this.accountIds[i] = rememberedUsers.get(i).getId();
            }
        }
        
        return result;
    }

    public String processGettingPassword(int accountId) {
        User user = DataManager.getInstance().getUserManager().getUser(accountId);
        ResultMessageGenerator messageGenerator = new ResultMessageGenerator();

        if (user == null) {
            this.resultMessage = messageGenerator.LOGIN_FAILED_OTHER;
            return null;
        }

        return user.getPassword();
    }

    public boolean processLogin(String account, String password, boolean accountRemembered) {
        ResultMessageGenerator messageGenerator = new ResultMessageGenerator();

        if (account.trim().isEmpty() || password.trim().isEmpty()) {
            this.resultMessage = messageGenerator.LOGIN_FAILED_LACK_DATA;
            return false;
        }
        
        List<User> users = DataManager.getInstance().getUserManager().getUsers(account);
        
        if (users == null) {
            this.resultMessage = messageGenerator.LOGIN_FAILED_OTHER;
            return false;
        }

        if (users.size() == 0) {
            this.resultMessage = messageGenerator.LOGIN_FAILED_NON_EXISTED_ACCOUNT;
            return false;
        }

        User checkingUser = users.get(0);
        if (!checkingUser.getPassword().equals(password)) {
            this.resultMessage = messageGenerator.LOGIN_FAILED_ACCOUNT_PASSWORD;
            return false;
        }
        
        DataManager.getInstance().getSettingManager().updateSetting(checkingUser.getSetting(), accountRemembered);
        DataManager.getInstance().setActiveAccountId(checkingUser.getId());
        return true;
    }

    public List<String> processGettingUserProfile(int accountId) {
        User user = DataManager.getInstance().getUserManager().getUser(accountId);
        if (user == null) {
            this.resultMessage = new ResultMessageGenerator().EDITING_PROFILE_FAILED_OTHER;
            return null;
        }
        
        List<String> result = new ArrayList<String>();
        result.add(DataOrders.ACCOUNT.getValue(), user.getAccount());
        result.add(DataOrders.PASSWORD.getValue(), DEFAULT_USER_STRING_DATA);
        result.add(DataOrders.CONFIRM.getValue(), DEFAULT_USER_STRING_DATA);
        result.add(DataOrders.NAME.getValue(), user.getName());
        result.add(DataOrders.AGE.getValue(), String.valueOf(user.getAge()));
        result.add(DataOrders.POSITION.getValue(), user.getPosition());
        result.add(DataOrders.EMAIL.getValue(), user.getEmail());
        result.add(DataOrders.PHONE.getValue(), user.getPhone());
        //result.add(DataOrders.CONDITION.getValue(), String.valueOfuser.getSetting().isHasPasswordRemembered());

        this.accountRemembered = user.getSetting().isHasPasswordRemembered();
        return result;
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
        public String CREATING_FAILED_OTHER = "Some errors happened when saving user data. Please try again.";

        public String RECOVERY_FAILED_NON_EXISTED_ACCOUNT = "This account has not been registered.";
        public String RECOVERY_FAILED_NO_QUESTIONS = "This account doesn't contains any recovery questions";
        public String RECOVERY_FAILED_INCORRECT_ANSWER = "Your answer is incorrect, please try again.";
        public String RECOVERY_FAILED_LACK_ANSWER = "Please answer all the displayed questions in order to recover your password.";
        public String RECOVERY_FAILED_OTHER = "Some errors happened when processing your input data. Please try again.";
        public String RECOVERY_SUCCESS_INFORM_PREFIX = "Recovering is success. Your password is ";

        public String LOGIN_FAILED_LACK_DATA = "Please input all the required information.";
        public String LOGIN_FAILED_NON_EXISTED_ACCOUNT = "This account has not been registered. Register it please.";
        public String LOGIN_FAILED_ACCOUNT_PASSWORD = "Your account or password is not correct. Please try again.";
        public String LOGIN_FAILED_OTHER = "Some errors happened when getting this account data. Please try again.";
        
        public String EDITING_PROFILE_FAILED_OTHER = "Some errors happened when getting user profile from database.";
    }

}
