import java.util.ArrayList;
import java.util.Iterator;

public class Customer extends Person {
    private String pin; // 6-digit number
    private float balance = 0;
    private ArrayList<Account> accounts = new ArrayList<Account>();
    private float loan = 0;

    public Customer(){}

    public String getPin() {
        return pin;
    }

    public float getBalance() {
        return balance;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public float getLoan() {
        return loan;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setLoan(float loan) {
        this.loan = loan;
    }

    // add a checking or saving account
    public String addAccount(int accountType) {
        Account account = new Account(accountType);

        // if customer has owned max num of accounts
        if(accountType == 0){
            if(getCheckingNum()==Services.ACCOUNT_NUM_MAX)return "";
        }
        else if(accountType == 1){
            if(getSavingNum()==Services.ACCOUNT_NUM_MAX)return "";
        }

        accounts.add(account);
        System.out.println(account.getAccountNumber());
        return account.getAccountNumber();
    }

    // delete account
    public boolean deleteAccount(String accountNum){
        int index = findAccount(accountNum);
        accounts.remove(index);
        return true;
    }

    public int getCheckingNum(){
        ArrayList<Account> accounts = this.getAccounts();
        int count = 0;
        for(int i=0;i<accounts.size();i++){
            if(accounts.get(i).getAccountType() == 0){
                count++;
            }
        }
        return count;
    }

    public int getSavingNum(){
        ArrayList<Account> accounts = this.getAccounts();
        int count = 0;
        for(int i=0;i<accounts.size();i++){
            if(accounts.get(i).getAccountType() == 1){
                count++;
            }
        }
        return count;
    }

    // return account index
    public int findAccount(String accountNum){
        int length = accounts.size();
        for(int i=0;i<length;i++){
            if(accounts.get(i).getAccountNumber().equals(accountNum)){
                return i;
            }
        }
        return -1;
    }

    //calculate balance when deposit or withdraw or make a loan
    public void settleBalacne(){
        float sum = 0;
        for(int i=0;i<accounts.size();i++){
            sum += accounts.get(i).getBalance();
        }
        this.balance = sum;
    }

    // get checking account
    public ArrayList<Account> getCheckingAccount(){
        ArrayList<Account> checking_account = new ArrayList<>();
        for(Account account : accounts){
            if(account.getAccountType() == 0){
                checking_account.add(account);
            }
        }
        return checking_account;
    }
}
