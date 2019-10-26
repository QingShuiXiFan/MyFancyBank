import java.time.*;
import java.time.format.DateTimeFormatter;

public class Record {
    private String transactionCode;
    private Customer customer;
    private String accountNum;
    private LocalDateTime localDateTime;
    private String action; // "deposit", "withdraw", "loan", "open", "close"
    private float sum;
    private int serviceFee;
    private String note;

    public Record(Customer customer, String accountNum, String action, float sum){
        this.transactionCode = Integer.toString((int) ((Math.random())*100000000)); // get a random 8-digit number string
        this.customer = customer;
        this.accountNum = accountNum;
        this.localDateTime = LocalDateTime.now();
        this.action = action;
        this.sum = sum;
        this.serviceFee = Services.SERVICE_FEE_MAP.get(action);
        this.note = "";
    }

    public Record(Customer customer, String accountNum, String action, float sum, String note){
        this.transactionCode = Integer.toString((int) ((Math.random())*100000000)); // get a random 8-digit number string
        this.customer = customer;
        this.accountNum = accountNum;
        this.localDateTime = LocalDateTime.now();
        this.action = action;
        this.sum = sum;
        this.serviceFee = Services.SERVICE_FEE_MAP.get(action);
        this.note = note;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getAction() {
        return action;
    }

    public int getServiceFee() {
        return serviceFee;
    }

    public float getSum() {
        return sum;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public String getLocalDateTime() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(localDateTime);
    }
    public String getNote(){
        return this.note;
    }
}
