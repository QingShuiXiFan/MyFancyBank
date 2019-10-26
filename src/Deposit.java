import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

public class Deposit extends JFrame {
    public static int FORM_WIDTH = 450;
    public static int FORM_Height = 250;

    private JPanel wholePanel;
    private JComboBox currency_comboBox;
    private JLabel currency_label;
    private JTextField amount_textField;
    private JLabel toUSD_label;
    private JButton backButton;
    private JButton enterButton;
    private JComboBox choose_account_comboBox;
    private JLabel alarm_panel;
    private String amount_textField_placeholder = "max 1,000,000";

    public Deposit(Service_customer service_customer_Handle, Customer customer) {
        this.setSize(FORM_WIDTH, FORM_Height);
        this.setLocationRelativeTo(null);
        this.setTitle("Profiteer Bank - Deposit");
        this.setContentPane(wholePanel);

        //initialize combobox for currency
        initializeCurrencyComboBox();
        initializeAccountComboBox(customer);

        amount_textField.setText(amount_textField_placeholder);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
                service_customer_Handle.setVisible(true);
            }
        });
        currency_comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String currency = currency_comboBox.getSelectedItem().toString();
                currency_label.setText(Services.CURRENCY_SYMBOL_MAP.get(currency));

                String amount = amount_textField.getText();
                if (!amount.equals("") && !amount.equals(amount_textField_placeholder)) {
                    if (Integer.parseInt(amount) == 0) {
                        // if first digit is 0
                        amount_textField.setText("");
                    }
                    String USD = formatDouble(Integer.parseInt(amount) / Services.CURRENCY_MAP.get(currency));
                    toUSD_label.setText("USD " + USD);
                } else {
                    toUSD_label.setText("USD 0.00");
                }
            }
        });
        amount_textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (amount_textField.getText().isEmpty()) {
                    amount_textField.setForeground(Color.GRAY);
                    amount_textField.setText(amount_textField_placeholder);
                }
            }
        });
        amount_textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (amount_textField.getText().equals(amount_textField_placeholder)) {
                    amount_textField.setForeground(Color.BLACK);
                    amount_textField.setText("");
                }
            }
        });
        amount_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
                    if (amount_textField.getText().length() == 7) {
                        // limit amount in 1,000,000
                        e.consume();
                    }
                } else {
                    e.consume(); // filter invalid input
                }
            }
        });
        amount_textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String amount = amount_textField.getText();
                String currency = currency_comboBox.getSelectedItem().toString();
                if (!amount.equals("") && !amount.equals(amount_textField_placeholder)) {
                    if (Integer.parseInt(amount) == 0) {
                        // if first digit is 0
                        amount_textField.setText("");
                    }
                    String USD = formatDouble(Integer.parseInt(amount) / Services.CURRENCY_MAP.get(currency));
                    toUSD_label.setText("USD " + USD);
                } else {
                    toUSD_label.setText("USD 0.00");
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
        enterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String currency = currency_comboBox.getSelectedItem().toString();
                Double exchangeRate = Services.CURRENCY_MAP.get(currency);
                int serviceFee = Services.SERVICE_FEE_MAP.get("deposit");
                String account = choose_account_comboBox.getSelectedItem().toString();
                String amount = amount_textField.getText();
                String amountInUSD = toUSD_label.getText();

                if (checkInputInformation()) {
                    String information = "Account: " + account + "\n" + "Amount: " + currency + " " + amount + " " + amountInUSD + " \n";
                    String text = "Exchange rate:  1 : " + exchangeRate + "\n Service fee: $" + serviceFee;
                    int result = JOptionPane.showConfirmDialog(null,
                            information + text,
                            "Deposit Confirm",
                            JOptionPane.YES_NO_OPTION);

                    if (result == 0) {//YES
                        String accountNum = choose_account_comboBox.getSelectedItem().toString().split(" ")[0];
                        float amountInFloat = Float.parseFloat(amountInUSD.split(" ")[1]);
                        System.out.println(accountNum);

                        // add balance for selected account
                        customer.getAccounts().get(customer.findAccount(accountNum)).addBalance(amountInFloat);
                        customer.getAccounts().get(customer.findAccount(accountNum)).settleServiceFee("deposit");
                        // settle balance for customer
                        customer.settleBalacne();

                        //add record
                        Record record = new Record(customer, accountNum, "deposit", amountInFloat);
                        Services.RECORD_MAP.put(record, customer);

                        String balance = customer.getAccounts().get(customer.findAccount(accountNum)).getBalanceInString();
                        JOptionPane.showMessageDialog(null,
                                "Congratulations, " + customer.getName().getFirstName() + "\nAccount num: " + accountNum + "\nBalance: $" + balance);
                        closeFrame();
                        service_customer_Handle.freshBalance();
                        service_customer_Handle.setVisible(true);
                    }
                }
            }
        });
    }

    public void closeFrame() {
        this.dispose();
    }

    public String formatDouble(double s) {
        DecimalFormat fmt = new DecimalFormat("##0.00");
        return fmt.format(s);
    }

    private void initializeCurrencyComboBox() {
        Iterator iter = Services.CURRENCY_MAP.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            currency_comboBox.addItem(entry.getKey());
        }
        currency_comboBox.setSelectedItem("USD");
    }

    private void initializeAccountComboBox(Customer customer) {
        if (customer.getAccounts().size() == 0) {
            choose_account_comboBox.addItem("No account available.");
        }
        for (int i = 0; i < customer.getAccounts().size(); i++) {
            String accountNum = customer.getAccounts().get(i).getAccountNumber();
            String accountType = customer.getAccounts().get(i).printAcountType();
            String balance = "$ " + customer.getAccounts().get(i).getBalance();
            choose_account_comboBox.addItem(accountNum + " " + accountType + " " + balance);
        }
    }

    private boolean checkInputInformation() {
        String amount = amount_textField.getText();
        if (choose_account_comboBox.getSelectedItem().toString().equals("No account available.")) {
            alarm_panel.setText("You do not have an account. Please open an account first.");
            return false;
        } else if (amount.equals("") || amount.equals(amount_textField_placeholder)) {
            alarm_panel.setText("Type in deposit amount.");
            return false;
        }
        return true;
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
        wholePanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        wholePanel.setBackground(new Color(-524801));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-524801));
        wholePanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setBackground(new Color(-524801));
        Font label1Font = this.$$$getFont$$$(null, -1, 22, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-16777216));
        label1.setText("Deposit");
        panel1.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-524801));
        wholePanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setBackground(new Color(-524801));
        label2.setForeground(new Color(-16777216));
        label2.setText("Choose currency:");
        panel2.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currency_comboBox = new JComboBox();
        currency_comboBox.setBackground(new Color(-1118482));
        panel2.add(currency_comboBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setBackground(new Color(-524801));
        wholePanel.add(panel3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setBackground(new Color(-524801));
        label3.setForeground(new Color(-16777216));
        label3.setText("Amount:");
        panel3.add(label3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel3.add(spacer5, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        currency_label = new JLabel();
        currency_label.setBackground(new Color(-524801));
        currency_label.setForeground(new Color(-16777216));
        currency_label.setText("$");
        panel3.add(currency_label, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel3.add(spacer6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        amount_textField = new JTextField();
        amount_textField.setBackground(new Color(-1118482));
        amount_textField.setForeground(new Color(-4473925));
        amount_textField.setText("");
        panel3.add(amount_textField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        toUSD_label = new JLabel();
        toUSD_label.setBackground(new Color(-524801));
        toUSD_label.setForeground(new Color(-16777216));
        toUSD_label.setText("USD 0.00");
        panel3.add(toUSD_label, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setBackground(new Color(-524801));
        wholePanel.add(panel4, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setBackground(new Color(-1118482));
        backButton.setForeground(new Color(-16777216));
        backButton.setText("Back");
        panel4.add(backButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enterButton = new JButton();
        enterButton.setBackground(new Color(-1118482));
        enterButton.setForeground(new Color(-16777216));
        enterButton.setText("Enter");
        panel4.add(enterButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel4.add(spacer7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel4.add(spacer8, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel5.setBackground(new Color(-524801));
        wholePanel.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setBackground(new Color(-524801));
        label4.setForeground(new Color(-16777216));
        label4.setText("Choose account:");
        panel5.add(label4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel5.add(spacer9, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel5.add(spacer10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        choose_account_comboBox = new JComboBox();
        choose_account_comboBox.setBackground(new Color(-1118482));
        panel5.add(choose_account_comboBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel6.setBackground(new Color(-524801));
        wholePanel.add(panel6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        alarm_panel = new JLabel();
        alarm_panel.setForeground(new Color(-1174508));
        alarm_panel.setText("");
        panel6.add(alarm_panel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel6.add(spacer11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel6.add(spacer12, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
