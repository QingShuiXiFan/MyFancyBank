import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;

    }

    // create a built-in customer for test
    public static ArrayList<Customer> createCustomer(ArrayList<Customer> cusArr){
        Customer customer = new Customer();

        customer.setName(new Name("Jun","Li"));
        customer.setAge(22);
        customer.setEmailAddress("junli@bu.edu");
        customer.setMailingAddress("175 Freeman St, Brookline, MA 02446");
        customer.setBillingAddress("175 Freeman St, Brookline, MA 02446");
        customer.setPhoneNum("5116172897");
        customer.setBalance(0);
        customer.setPin("000000");

        cusArr.add(customer);

        customer = new Customer();
        customer.setEmailAddress("aaa@bu.edu");
        customer.setName(new Name("Jun","Li"));
        customer.setAge(22);
        customer.setMailingAddress("175 Freeman St, Brookline, MA 02446");
        customer.setBillingAddress("175 Freeman St, Brookline, MA 02446");
        customer.setPhoneNum("5116172897");
        customer.setBalance(0);
        customer.setPin("000000");
        cusArr.add(customer);

        return cusArr;
    }

    // check e-mail and PIN when customer login
    public static boolean isLoginValid(String e_mail, String PIN){
        for(int i=0;i<Services.CUSTOMERS_ARRAYLIST.size();i++){

            if (Services.CUSTOMERS_ARRAYLIST.get(i).getEmailAddress().equals(e_mail)){
                // find the e_mail
                if(Services.CUSTOMERS_ARRAYLIST.get(i).getPin().equals(PIN)) {
                    //PIN right
                    return true;
                }
                // PIN wrong
                else return false;
            }
        }
        return false;
    }

    // check MNum and PIN when manager login
    public static boolean isLoginValidManager(String MNum, String PIN){
        for(int i=0;i<Services.MANAGER_ARRAYLIST.size();i++){
            if (Services.MANAGER_ARRAYLIST.get(i).getManagerNumer().equals(MNum)){
                // find the MNum
                if(Services.MANAGER_ARRAYLIST.get(i).getPin().equals(PIN)) {
                    //PIN right
                    return true;
                }
                // PIN wrong
                else return false;
            }
        }
        return false;
    }

    public static Customer findCustomer(String e_mail){
        for(int i=0;i<Services.CUSTOMERS_ARRAYLIST.size();i++){
            if (Services.CUSTOMERS_ARRAYLIST.get(i).getEmailAddress().equals(e_mail)){
                return Services.CUSTOMERS_ARRAYLIST.get(i);
            }
        }
        return null;
    }

    public static Manager findManager(String MNum){
        for(int i=0;i<Services.MANAGER_ARRAYLIST.size();i++){
            if (Services.MANAGER_ARRAYLIST.get(i).getManagerNumer().equals(MNum)){
                return Services.MANAGER_ARRAYLIST.get(i);
            }
        }
        return null;
    }

    public static String formatDouble(double s) {
        DecimalFormat fmt = new DecimalFormat("##0.00");
        return fmt.format(s);
    }

    public static String formatFloat(float s) {
        DecimalFormat fmt = new DecimalFormat("##0.00");
        return fmt.format(s);
    }

    public static ArrayList<Record> getAllRecord(){
        ArrayList<Record> records = new ArrayList<>();
        for (HashMap.Entry<Record, Customer> entry : Services.RECORD_MAP.entrySet()) {
            records.add(entry.getKey());
        }
        return records;
    }

    public static ArrayList<Record> getOneCustomerRecord(Customer customer){
        ArrayList<Record> records = new ArrayList<>();
        for (HashMap.Entry<Record, Customer> entry : Services.RECORD_MAP.entrySet()) {
            if(entry.getValue().equals(customer)) {
                records.add(entry.getKey());
            }
        }
        return records;
    }

    public static ArrayList<Record> getOneAccountRecord(String accountNum){
        ArrayList<Record> records = new ArrayList<>();
        for (HashMap.Entry<Record, Customer> entry : Services.RECORD_MAP.entrySet()) {
            if(entry.getKey().getAccountNum().equals(accountNum)) {
                records.add(entry.getKey());
            }
        }
        return records;
    }

    public static int getRecordNumForAccount(String accountNum){
        int count = 0;
        for (HashMap.Entry<Record, Customer> entry : Services.RECORD_MAP.entrySet()) {
            if(entry.getKey().getAccountNum().equals(accountNum)) {
                count++;
            }
        }
        return count;
    }
    public static ArrayList<Account> getAllSavingAccount(){
        ArrayList<Account> saving_accounts = new ArrayList<>();
        for(int i=0;i<Services.CUSTOMERS_ARRAYLIST.size();i++){
            for(int j=0;j<Services.CUSTOMERS_ARRAYLIST.get(i).getAccounts().size();j++){
                if(Services.CUSTOMERS_ARRAYLIST.get(i).getAccounts().get(j).getAccountType() == 1){
                    saving_accounts.add(Services.CUSTOMERS_ARRAYLIST.get(i).getAccounts().get(j));
                }
            }
        }
        return saving_accounts;
    }
    public static ArrayList<Customer> getCustomerwithKeyWord_isWithLoan(String keyword, boolean withLoan){
        ArrayList<Customer> result = new ArrayList<>();
        ArrayList<Customer> customers = Services.CUSTOMERS_ARRAYLIST;
        if(withLoan) {
            for (Customer customer : customers) {
                if(customer.getAccounts().size() == 0) continue;
                if (customer.getLoan() != 0 && hasUnion(customer.getName().getFirstName(), keyword)) {
                    result.add(customer);
                }
            }
        }
        else {
            for (Customer customer : customers) {
                if(customer.getAccounts().size() == 0) continue;
                if (hasUnion(customer.getName().getFirstName(), keyword)) {
                    result.add(customer);
                }
            }
        }
        return result;
    }

    public static boolean hasUnion(String s1, String keyword){
        return s1.contains(keyword);
    }

    public static ArrayList<Account> getCustomersAccounts(ArrayList<Customer> customers){
        ArrayList<Account> accounts = new ArrayList<>();
        for (Customer customer : customers) {
            accounts.addAll(customer.getAccounts());
        }
        return accounts;
    }
    public static Account findAccount(String accountNum){
        for(int i = 0;i<Services.CUSTOMERS_ARRAYLIST.size();i++){
            int index = Services.CUSTOMERS_ARRAYLIST.get(i).findAccount(accountNum);
            if(index != -1){
                return Services.CUSTOMERS_ARRAYLIST.get(i).getAccounts().get(index);
            }
        }
        return null;
    }
}
