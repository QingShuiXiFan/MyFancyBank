public class Name {
    private String firstName;
    private String lastName;
    private String middleName;

    public Name(String fn, String ln){
        this.firstName = fn;
        this.lastName = ln;
    }

    public Name(String fn, String ln, String mn){
        this.firstName = fn;
        this.lastName = ln;
        this.middleName = mn;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getMiddleName(){
        return middleName;
    }
    public String getWholeName(){
        return lastName +", "+ firstName;
    }
}
