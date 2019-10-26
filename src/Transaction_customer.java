import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Transaction_customer extends JFrame {
    public static int FORM_WIDTH = 600;
    public static int FORM_Height = 400;
    private static int count = 0;

    private JPanel wholePanel;
    private JButton backButton;
    private JLabel name_label;
    private JTable transaction_table;
    private JComboBox choose_account_comboBox;
    private JScrollPane transaction_scroll;

    public Transaction_customer(Service_customer service_customer_Handle, Customer customer) {
        this.setSize(FORM_WIDTH, FORM_Height);
        this.setLocationRelativeTo(null);
        this.setTitle("Profiteer Bank - Transactions");
        this.setContentPane(wholePanel);

        // set first name
        name_label.setText(customer.getName().getFirstName());
        initializeAccountComboBox(customer);
        refreshTables(customer);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
                service_customer_Handle.setVisible(true);
            }
        });
        choose_account_comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (count++ % 2 == 0) {
                    refreshTables(customer);
                    customer.settleBalacne();
                    service_customer_Handle.freshBalance();
                }
            }
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                closeFrame();
                service_customer_Handle.setVisible(true);
            }
        });
    }

    public void closeFrame() {
        this.dispose();
    }

    private void initializeAccountComboBox(Customer customer) {
        if (customer.getAccounts().size() == 0) {
            choose_account_comboBox.addItem("No account available.");
        } else {
            choose_account_comboBox.addItem("view all");
            for (int i = 0; i < customer.getAccounts().size(); i++) {
                String accountNum = customer.getAccounts().get(i).getAccountNumber();
                String accountType = customer.getAccounts().get(i).printAcountType();
                String balance = "$ " + customer.getAccounts().get(i).getBalance();
                choose_account_comboBox.addItem(accountNum + " " + accountType + " " + balance);
            }
            choose_account_comboBox.setSelectedItem("view all");
        }
    }

    private void refreshTables(Customer customer) {
        String accountNum = choose_account_comboBox.getSelectedItem().toString();
        if (accountNum.equals("view all")) {
            accountNum = "";
        } else if (accountNum.equals("No account available.")) {
            return;
        } else {
            accountNum = accountNum.split(" ")[0];
            //settle service fee
            customer.getAccounts().get(customer.findAccount(accountNum)).settleServiceFee("view");
            //add record
            String note = "view transaction history.";
            Record record = new Record(customer, accountNum, "view", 0, note);
            Services.RECORD_MAP.put(record, customer);
        }
        String[] columnNames = {"Trans#", "DateTime", "Account#", "Action", "Amount/$", "Note"};
        DefaultTableModel model = (DefaultTableModel) transaction_table.getModel();
        model.setDataVector(initializeData(customer, accountNum), columnNames);
        transaction_scroll = new JScrollPane(new JTable(model));
    }

    public Object[][] initializeData(Customer customer, String accountNum) {
        Object[][] data;

        // return all records
        if (accountNum.equals("")) {
            // get this customer's record
            ArrayList<Record> records = Tool.getOneCustomerRecord(customer);
            data = new Object[records.size()][6];
            int count = 0;
            for (int i = 0; i < data.length; i++) {
                data[count][0] = records.get(i).getTransactionCode(); // transcation code
                data[count][1] = records.get(i).getLocalDateTime(); // date time
                data[count][2] = records.get(i).getAccountNum(); // account number
                data[count][3] = records.get(i).getAction(); // action
                data[count][4] = records.get(i).getSum(); // sum involved
                data[count][5] = records.get(i).getNote(); // note
                count++;
            }
        } else {// return records for one account
            // get this account's record
            ArrayList<Record> records = Tool.getOneAccountRecord(accountNum);
            int size = Tool.getRecordNumForAccount(accountNum);
            data = new Object[size][6];
            int count = 0;
            for (int i = 0; i < data.length; i++) {
                data[count][0] = records.get(i).getTransactionCode(); // transcation code
                data[count][1] = records.get(i).getLocalDateTime(); // date time
                data[count][2] = records.get(i).getAccountNum(); // account number
                data[count][3] = records.get(i).getAction(); // action
                data[count][4] = records.get(i).getSum(); // sum involved
                data[count][5] = records.get(i).getNote(); // note
                count++;
            }
        }
        return data;
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
        wholePanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        wholePanel.setBackground(new Color(-524801));
        final Spacer spacer1 = new Spacer();
        wholePanel.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-524801));
        wholePanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 22, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16777216));
        label1.setText("Transactions");
        panel1.add(label1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        name_label = new JLabel();
        name_label.setForeground(new Color(-16777216));
        name_label.setText("name");
        panel1.add(name_label, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setBackground(new Color(-1118482));
        backButton.setForeground(new Color(-16777216));
        backButton.setText("Back");
        panel1.add(backButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transaction_scroll = new JScrollPane();
        transaction_scroll.setBackground(new Color(-524801));
        wholePanel.add(transaction_scroll, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        transaction_table = new JTable();
        transaction_scroll.setViewportView(transaction_table);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-524801));
        wholePanel.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setForeground(new Color(-16777216));
        label2.setText("Choose account:");
        panel2.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel2.add(spacer5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        choose_account_comboBox = new JComboBox();
        choose_account_comboBox.setBackground(new Color(-1118482));
        choose_account_comboBox.setForeground(new Color(-16777216));
        panel2.add(choose_account_comboBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return wholePanel;
    }

}
