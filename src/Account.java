public class Account {
    private String accountNumber; // accountNumber is a 8-digit number string
    private int accountType; // 0 for checking, 1 for saving
    private float balance = 0;

    public Account(int accountType){
        this.accountNumber = Integer.toString((int) ((Math.random())*100000000)); // get a random 8-digit number string
        this.accountType = accountType;
    }

    public float getBalance() {
        return balance;
    }
    public String getBalanceInString(){ return String.valueOf(balance);}

    public int getAccountType() {
        return accountType;
    }
    public String printAcountType(){
        switch (accountType) {
            case 0: return "checking";
            case 1: return "saving";
            default: return "error";
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addBalance(float sum){
        this.balance += sum;
    }

    public void minusBalance(float sum){
        this.balance -= sum;
    }

    public void settleServiceFee(String service){
        int serviceFee = Services.SERVICE_FEE_MAP.get(service);
        this.balance -= serviceFee;
    }
}
