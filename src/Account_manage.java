import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.ArrayList;

public class Account_manage extends JFrame {
    public static int FORM_WIDTH = 400;
    public static int FORM_Height = 350;

    private JPanel wholePanel;
    private JLabel welcomeLabel;
    private JButton backButton;
    private JLabel checking_num;
    private JLabel saving_num;
    private JButton openChecking_button;
    private JButton openSaving_button;
    private JTable checking_table;
    private JTable saving_table;
    private JButton delete_checking_button;
    private JButton delete_saving_button;
    private JScrollPane checking_scroll;
    private JScrollPane saving_scroll;
    private Customer customer;

    public Account_manage(Service_customer service_customer, Customer customer) {
        this.customer = customer;

        this.setSize(FORM_WIDTH, FORM_Height);
        this.setLocationRelativeTo(null);
        this.setTitle("Profiteer Bank - Account");
        this.setContentPane(wholePanel);

        welcomeLabel.setText(customer.getName().getFirstName() + " ,here are your accounts.");
        checking_num.setText(Integer.toString(customer.getCheckingNum()));
        saving_num.setText(Integer.toString(customer.getSavingNum()));

        // add rows to tables
        refreshTables();

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                closeFrame();
                service_customer.setVisible(true);
            }
        });
        openChecking_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String service_fee = Integer.toString(Services.SERVICE_FEE_MAP.get("open"));
                int result = JOptionPane.showConfirmDialog(null,
                        "Do you want to open a checking account?\nThis operation will charge $" + service_fee + " for service fee",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) {//YES
                    String accountNum = customer.addAccount(0);
                    if (accountNum.equals("")) {
                        //checking accounts num exceeds ACCOUNT_NUM_MAX
                        JOptionPane.showMessageDialog(null,
                                "Sorry, you can own " + Services.ACCOUNT_NUM_MAX + " checking accounts at most.");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Congratulations, " + customer.getName().getFirstName() + "\nAccount num: " + accountNum);
                        refreshTables();
                        refreshForm(service_customer, customer);

                        //add a record
                        Record record = new Record(customer, accountNum, "open", 0);
                        Services.RECORD_MAP.put(record, customer);
                    }
                }
            }
        });
        openSaving_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String service_fee = Integer.toString(Services.SERVICE_FEE_MAP.get("open"));
                int result = JOptionPane.showConfirmDialog(null,
                        "Do you want to open a saving account?\nThis operation will charge $" + service_fee + " for service fee",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) {//YES
                    String accountNum = customer.addAccount(1);
                    if (accountNum.equals("")) {
                        //checking accounts num exceeds ACCOUNT_NUM_MAX
                        JOptionPane.showMessageDialog(null,
                                "Sorry, you can own " + Services.ACCOUNT_NUM_MAX + " saving accounts at most.");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Congratulations, " + customer.getName().getFirstName() + "\nAccount num: " + accountNum);
                        refreshTables();
                        refreshForm(service_customer, customer);

                        //add a record
                        Record record = new Record(customer, accountNum, "open", 0);
                        Services.RECORD_MAP.put(record, customer);
                    }
                }
            }
        });
        delete_checking_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int row = checking_table.getSelectedRow();
                System.out.println(checking_table.getRowCount());
                if (row != -1) {
                    String accountNum = checking_table.getValueAt(row, 0).toString();

                    System.out.println(accountNum);
                    String service_fee = Integer.toString(Services.SERVICE_FEE_MAP.get("close"));
                    int result = JOptionPane.showConfirmDialog(null,
                            "Close this account?\nThis operation will charge $" + service_fee + " service fee",
                            "Confirm",
                            JOptionPane.YES_NO_OPTION);
                    if (result == 0) {//YES
                        // delete account
                        customer.deleteAccount(accountNum);

                        // show message
                        JOptionPane.showMessageDialog(null,
                                " Account closed." + "\nAccount num: " + accountNum);
                        refreshTables();
                        refreshForm(service_customer, customer);

                        //add record
                        Record record = new Record(customer, accountNum, "close", 0);
                        Services.RECORD_MAP.put(record, customer);
                    }
                }
            }
        });
        delete_saving_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int row = saving_table.getSelectedRow();
                System.out.println(saving_table.getRowCount());
                if (row != -1) {
                    String accountNum = saving_table.getValueAt(row, 0).toString();

                    System.out.println(accountNum);
                    String service_fee = Integer.toString(Services.SERVICE_FEE_MAP.get("close"));
                    int result = JOptionPane.showConfirmDialog(null,
                            "Close this account?\nThis operation will charge $" + service_fee + " service fee",
                            "Confirm",
                            JOptionPane.YES_NO_OPTION);
                    if (result == 0) {//YES
                        // delete account
                        customer.deleteAccount(accountNum);

                        // show message
                        JOptionPane.showMessageDialog(null,
                                " Account closed." + "\nAccount num: " + accountNum);
                        refreshTables();
                        refreshForm(service_customer, customer);

                        //add record
                        Record record = new Record(customer, accountNum, "close", 0);
                        Services.RECORD_MAP.put(record, customer);
                    }
                }
            }
        });
    }

    public void refreshTables() {
        String[] columnNames = {"Account Num", "Balance/ $"};

        DefaultTableModel model1 = (DefaultTableModel) checking_table.getModel();
        DefaultTableModel model2 = (DefaultTableModel) saving_table.getModel();

        model1.setDataVector(initializeData_checking(customer), columnNames);
        model2.setDataVector(initializeData_saving(customer), columnNames);

        checking_scroll = new JScrollPane(new JTable(model1));
        saving_scroll = new JScrollPane(new JTable(model2));
    }

    public void refreshForm(Service_customer service_customer, Customer customer) {
        Account_manage account_manage = new Account_manage(service_customer, customer);
        account_manage.setVisible(true);
        this.dispose();
    }

    public Object[][] initializeData_checking(Customer customer) {
        Object[][] data = new Object[customer.getCheckingNum()][2];
        ArrayList<Account> accounts = customer.getAccounts();
        int count = 0;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountType() == 0) {
                data[count][0] = accounts.get(i).getAccountNumber();
                data[count][1] = accounts.get(i).getBalanceInString();
                count++;
            }
        }
        return data;
    }

    public Object[][] initializeData_saving(Customer customer) {
        Object[][] data = new Object[customer.getSavingNum()][2];
        ArrayList<Account> accounts = customer.getAccounts();
        int count = 0;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountType() == 1) {
                data[count][0] = accounts.get(i).getAccountNumber();
                data[count][1] = accounts.get(i).getBalanceInString();
                count++;
            }
        }
        return data;
    }

    public void closeFrame() {
        this.dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        wholePanel = new JPanel();
        wholePanel.setLayout(new GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        wholePanel.setBackground(new Color(-524801));
        final Spacer spacer1 = new Spacer();
        wholePanel.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 9, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-524801));
        wholePanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        welcomeLabel = new JLabel();
        welcomeLabel.setForeground(new Color(-16777216));
        welcomeLabel.setText("XXX, here are your accounts:");
        panel1.add(welcomeLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setBackground(new Color(-1118482));
        backButton.setForeground(new Color(-16777216));
        backButton.setText("Back");
        panel1.add(backButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel1.add(spacer7, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel1.add(spacer8, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 10, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-524801));
        panel2.setForeground(new Color(-16777216));
        wholePanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-16777216));
        label1.setText("Checking:");
        panel2.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel2.add(spacer9, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel2.add(spacer10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel2.add(spacer11, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel2.add(spacer12, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        panel2.add(spacer13, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer14 = new Spacer();
        panel2.add(spacer14, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        checking_num = new JLabel();
        checking_num.setForeground(new Color(-16777216));
        checking_num.setText("Label");
        panel2.add(checking_num, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openChecking_button = new JButton();
        openChecking_button.setBackground(new Color(-1118482));
        openChecking_button.setForeground(new Color(-16777216));
        openChecking_button.setText("+");
        panel2.add(openChecking_button, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(16, 20), null, 0, false));
        delete_checking_button = new JButton();
        delete_checking_button.setBackground(new Color(-1118482));
        delete_checking_button.setForeground(new Color(-16777216));
        delete_checking_button.setText("-");
        panel2.add(delete_checking_button, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(16, 20), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 10, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setBackground(new Color(-524801));
        wholePanel.add(panel3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setForeground(new Color(-16777216));
        label2.setText("Saving:");
        panel3.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer15 = new Spacer();
        panel3.add(spacer15, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer16 = new Spacer();
        panel3.add(spacer16, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer17 = new Spacer();
        panel3.add(spacer17, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer18 = new Spacer();
        panel3.add(spacer18, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer19 = new Spacer();
        panel3.add(spacer19, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer20 = new Spacer();
        panel3.add(spacer20, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        saving_num = new JLabel();
        saving_num.setForeground(new Color(-16777216));
        saving_num.setText("Label");
        panel3.add(saving_num, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openSaving_button = new JButton();
        openSaving_button.setBackground(new Color(-1118482));
        openSaving_button.setForeground(new Color(-16777216));
        openSaving_button.setText("+");
        panel3.add(openSaving_button, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(16, -1), null, 0, false));
        delete_saving_button = new JButton();
        delete_saving_button.setBackground(new Color(-1118482));
        delete_saving_button.setForeground(new Color(-16777216));
        delete_saving_button.setText("-");
        panel3.add(delete_saving_button, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(16, 20), null, 0, false));
        final Spacer spacer21 = new Spacer();
        wholePanel.add(spacer21, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setBackground(new Color(-524801));
        wholePanel.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checking_scroll = new JScrollPane();
        checking_scroll.setEnabled(true);
        panel4.add(checking_scroll, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        checking_table = new JTable();
        checking_table.setEnabled(true);
        checking_scroll.setViewportView(checking_table);
        final Spacer spacer22 = new Spacer();
        panel4.add(spacer22, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer23 = new Spacer();
        panel4.add(spacer23, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel5.setBackground(new Color(-524801));
        wholePanel.add(panel5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saving_scroll = new JScrollPane();
        saving_scroll.setBackground(new Color(-524801));
        saving_scroll.setEnabled(true);
        panel5.add(saving_scroll, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        saving_table = new JTable();
        saving_table.setEnabled(true);
        saving_scroll.setViewportView(saving_table);
        final Spacer spacer24 = new Spacer();
        panel5.add(spacer24, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer25 = new Spacer();
        panel5.add(spacer25, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return wholePanel;
    }

}
