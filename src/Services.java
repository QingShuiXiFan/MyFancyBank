/*
 * @Description: In User Settings Edit
 * @Author: your name
 * @Date: 2019-10-19 16:26:38
 * @LastEditTime: 2019-10-19 17:04:43
 * @LastEditors: Please set LastEditors
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Services {
    public static String[] ACTIONS_ARRAY = {"deposit", "withdraw", "loan", "open", "close", "view", "transfer"};
    public static ArrayList<String> ACTIONS_ARRAYLIST = new ArrayList<String>(Arrays.asList(ACTIONS_ARRAY));
    public static HashMap<String, Integer> SERVICE_FEE_MAP = new HashMap<>();
    public static ArrayList<Customer> CUSTOMERS_ARRAYLIST = new ArrayList<>();
    public static ArrayList<Manager> MANAGER_ARRAYLIST = new ArrayList<>();
    public static HashMap<Record,Customer> RECORD_MAP = new HashMap<>();
    public static int ACCOUNT_NUM_MAX = 5;
    public static HashMap<String,Double> CURRENCY_MAP = new HashMap<>();
    public static HashMap<String,String> CURRENCY_SYMBOL_MAP = new HashMap<>();
    public static int MAX_DEPOSIT_AMOUNT = 1000000;
    public static int MIN_INTEREST_BALANCE = 10000;
    public static float INTEREST_RATE = (float) 0.001;

    public Services(){
        // initialize SERVICE_FEE_MAP
        SERVICE_FEE_MAP.put("deposit", 1);
        SERVICE_FEE_MAP.put("withdraw", 1);
        SERVICE_FEE_MAP.put("loan", 10);
        SERVICE_FEE_MAP.put("open", 0);
        SERVICE_FEE_MAP.put("close", 0);
        SERVICE_FEE_MAP.put("view", 1);
        SERVICE_FEE_MAP.put("transfer", 2);

        // add two customers for test
        CUSTOMERS_ARRAYLIST = Tool.createCustomer(CUSTOMERS_ARRAYLIST);

        //initialize CURRENCY_MAP
        CURRENCY_MAP.put("USD",1.0);
        CURRENCY_MAP.put("CNY",7.1);
        CURRENCY_MAP.put("JPY",108.8);

        //initialize CURRENCY_SYMBOL_MAP
        CURRENCY_SYMBOL_MAP.put("USD","$");
        CURRENCY_SYMBOL_MAP.put("CNY","CNY");
        CURRENCY_SYMBOL_MAP.put("JPY","JPY");
    }

}
