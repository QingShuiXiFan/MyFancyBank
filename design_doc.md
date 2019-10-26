<!--
 * @Description: In User Settings Edit
 * @Author:Jun Li
 * @BUID: U73344054
 * @Date: 2019-10-19 20:47:14
 * @LastEditTime: 2019-10-20 00:14:53
 * @LastEditors: Please set LastEditors
 -->
# My Fancy Bank --- CS591 P1

Hey, man. Thank you for taking time to read this design document.

In this document, I will explain my design choices, object model,  benefit of my design and object and GUI relationship.

## Design Choices

1. This is a ATM program developed mainly for people to deal with daily financial issues. People who involed in this scenario can be classified into customers and managers. So **Customer** and **Manager** class should be implemented (also their parent class **Person**).

2. Also, every financial transaction should be made between accounts, and records for each transaction should be documented. So **Account** and **Record** class should be implemented.

3. Bank business logic and operation mechanism should be set. Therefore, **Services** is implemented.

4. Assiting methods are built in **Tool** class.

## Object Model and Design Benefits

1. **Name**

    Everyone has a name. And names always consist of first name, last name and sometimes, middle name. This class abstracts certain features for a name, and can to be used as an attribute in **Person**.

    Attributes:

    String firstName;<br>
    String lastName;<br>
    String middleName;<br>

2. **Person**

    Almost every program are designed for some certain group of people. For example, this ATM program is mainly developed for customers and bank managers. And these roles share some common attributes of person -- name, age, email address, mailing address, phone number ,etc.

    Attributes:

    Name name;<br>
    int age;<br>
    String emailAddress;<br>
    String mailingAddress;<br>
    String billingAddress;<br>
    String phoneNum;<br>

3. **Customer**

    Extends from **Person**. Login information and accounts are added.

    Attributes:

    String pin;<br>
    float balance;<br>
    ArrayList<**Account**> accounts;<br>
    float loan;<br>

4. **Manager**

    Extended from **Person**.  Login information, profit are added.

    Attributes:

    String managerNumer;<br>
    String pin;<br>
    pfloat profit;<br>

5. **Account**

    Bank accounts share many common attributes. So account object should be abstracted. An array of **Account** objects is an attribute of **Customer**.

6. **Services**

    All data generated is stored in this object. This setting allows all data to be managed in one database-like object, which can prevent data synchronization problem when some complex business is dealt with.

    Attributes:
    
    String[] ACTIONS_ARRAY;<br>
    public static ArrayList<String> ACTIONS_ARRAYLIST;<br>
    public static HashMap<String, Integer> SERVICE_FEE_MAP;<br>
    public static ArrayList<Customer> CUSTOMERS_ARRAYLIST;<br>
    public static ArrayList<Manager> MANAGER_ARRAYLIST;<br>
    public static HashMap<Record,Customer> RECORD_MAP;<br>
    public static int ACCOUNT_NUM_MAX;<br>
    public static HashMap<String,Double> CURRENCY_MAP;<br>
    public static HashMap<String,String> CURRENCY_SYMBOL_MAP;<br>
    public static int MAX_DEPOSIT_AMOUNT;<br>
    public static int MIN_INTEREST_BALANCE;<br>
    public static float INTEREST_RATE;<br>

7. **Tool**

    All assisting methods are implemented in this class, such as formatFloat(), findCheckingAccount(), hasUnion(), etc. These methods can be called from anywhere.

    No attributes, and opreation methods are all static.

# Object and GUI Relationship

|GUI Java Class|Related Objects|
|---|---|
|Home|None|
|Login_customer|Customer, Services|
|Login_manager|Manager, Services|
|Sign_up_customer|Customer, Account, Services|
|Sign_up_manager|Customer, Account, Services|
|Service_customer|Customer, Services|
|Service_manager|Manager, Services|
|Account_manage|Customer, Account, Record, Services|
|Deposit|Customer, Account, Record, Services|
|Withfraw|Customer, Account, Record, Services|
|Transfer|Customer, Account, Record, Services|
|Loan|Customer, Account, Record, Services|
|Transaction_customer|Customer, Account, Record, Services|
|Transaction_manager|Manager, Account, Record, Services|
|Check_customer|Record, Services|

Related objects are either read or changed in corresponding GUI Java class. **Services** object is involved in every GUI because it is used as a database. So an **Services** object is instantiated before GUI starts.