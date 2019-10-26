import java.util.ArrayList;

public class Manager extends Person{
    private String managerNumer; // 4-digit
    private String pin;
    private float profit = 0;

    public Manager(String pin, String firstName, String lastName){
        managerNumer = Integer.toString((int) (Math.random()*10000));
        this.pin = pin;
        Name name = new Name(firstName,lastName);
        this.setName(name);
    }

    public String getPin() {
        return pin;
    }

    public String getManagerNumer() {
        return managerNumer;
    }

    public float getProfit() {
        return profit;
    }

    public void settleProfit(){
        ArrayList<Record> records = Tool.getAllRecord();
        profit = 0;
        for(int i=0;i<records.size();i++){
            profit += records.get(i).getServiceFee();
        }
    }
    public float payInterest(){
        float sum = 0;
        ArrayList<Account> saving_accounts = Tool.getAllSavingAccount();
        for(int i=0;i<saving_accounts.size();i++){
            if(saving_accounts.get(i).getBalance() >= Services.MIN_INTEREST_BALANCE){
                float interst = saving_accounts.get(i).getBalance() * Services.INTEREST_RATE;
                saving_accounts.get(i).addBalance(interst);
                profit -= interst;
                sum += interst;
            }
        }
        return sum;
    }
}
