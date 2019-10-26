public class Person {
    private Name name;
    private int age = 0;
    private String emailAddress = "";
    private String mailingAddress = "";
    private String billingAddress = "";
    private String phoneNum = "";

    public Person(){}

    public void setAge(int age) {
        this.age = age;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getAge() {
        return age;
    }

    public Name getName() {
        return name;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public String getPhoneNum() {
        return phoneNum;
    }
}
