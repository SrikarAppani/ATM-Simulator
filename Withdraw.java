import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Withdraw extends JFrame implements ActionListener, FocusListener
{
    JTextField amount_field;
    JPanel panel;
    JButton back_button, enter_button, duplicate_button;
    Connection c;
    Withdraw(Connection c) 
    {
        this.c = c;
        ImageIcon back_icon_img = new ImageIcon("icons/BackButton.jpg");
        ImageIcon back_icon = new ImageIcon(back_icon_img.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
        duplicate_button = new JButton();
        duplicate_button.setFocusable(true);
        duplicate_button.setBounds(1000,1000,10,10);
        back_button = new JButton();
        back_button.setIcon(back_icon);
        back_button.setBounds(10, 10, 30, 30);
        back_button.setFocusable(false);
        back_button.addActionListener(this);

        enter_button = new JButton("ENTER");
        enter_button.setBounds(370, 110, 100, 35);
        enter_button.setFocusable(false);
        enter_button.setBackground(Color.black);
        enter_button.setForeground(Color.white);
        enter_button.addActionListener(this);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);
        panel.setBounds(20, 20, 495, 170);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        panel.add(back_button);
        panel.add(duplicate_button);
        panel.add(enter_button);

        amount_field = new JTextField("Enter the amount to be withdrawn");
        amount_field.setBackground(Color.black);
        amount_field.setCaretColor(Color.white);
        amount_field.setForeground(Color.white);
        amount_field.setBounds(90, 50, 300, 40);
        amount_field.setFont(new Font("Calibri", Font.ITALIC, 15));
        amount_field.setHorizontalAlignment(JLabel.CENTER);
        amount_field.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        amount_field.addFocusListener(this);

        ImageIcon title_icon = new ImageIcon("icons/ATM.png");
        this.setTitle("ATM Simulator");
        this.setIconImage(title_icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds((LoginPage.w)/2-275,(LoginPage.h)/2-275,550,250);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.black);
        this.setResizable(false);
        this.add(panel);
        this.setLayout(null);
        panel.add(amount_field);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == back_button) 
        {
            new ChooseWhatToDo(c);
            this.dispose();
        } 
        else if(e.getSource() == enter_button) 
        {
            String amt = amount_field.getText();
            if (amt.equals("Enter the amount to be withdrawn") || amt.equals("") || amt.equals("0")) 
            {
                System.out.println("Invalid amount");
            } 
            else 
            {
                try 
                {
                    String accno = LoginPage.accno;
                    PreparedStatement ps = c.prepareStatement("select balance, account_type, minimum_balance, over_draft from Accounts where account_number = ?");
                    ps.setString(1, accno);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) 
                    {
                        String actype = rs.getString("account_type");
                        float bal = rs.getFloat("balance");
                        float min = rs.getFloat("minimum_balance");
                        float od = rs.getFloat("over_draft");
                        float amount = Float.parseFloat(amt);
                        int x = 0;
                        if(actype.toLowerCase().equals("savings")) 
                        {
                            if(bal-amount >= min) 
                            {
                                PreparedStatement dec_amt = c.prepareStatement("update Accounts set balance = ? where account_number = ?");
                                dec_amt.setFloat(1, bal - amount);
                                dec_amt.setString(2, accno);
                                x = dec_amt.executeUpdate();
                            } 
                            else 
                            {
                                JOptionPane.showMessageDialog(null, "Insufficient balance", "Withdraw", JOptionPane.WARNING_MESSAGE);
                            }
                        } 
                        else if(actype.toLowerCase().equals("current")) 
                        {
                            if(amount<bal+od) 
                            {
                                PreparedStatement dec_amt = c.prepareStatement("update Accounts set (balance = ?), (over_draft = ?) where account_number = ?");
                                if(bal-amount<=0)
                                {
                                    dec_amt.setFloat(1, 0);
                                    dec_amt.setFloat(2,od - (amount - bal));
                                    dec_amt.setString(3, accno);
                                }
                                else
                                {
                                    dec_amt.setFloat(1, bal-amount);
                                    dec_amt.setFloat(2,od);
                                    dec_amt.setString(3, accno);
                                }
                                x = dec_amt.executeUpdate();
                            } 
                            else 
                            {
                                JOptionPane.showMessageDialog(null, "OverDraft Exceeded", "Withdraw", JOptionPane.WARNING_MESSAGE);
                            }
                        }

                        if(x != 0) 
                        {
                            int y = JOptionPane.showConfirmDialog(null, "Withdraw successful, do you want to display your balance on screen?", "Withdraw", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
                            if(y == 0) 
                            {
                                PreparedStatement ps2 = c.prepareStatement("select balance from Accounts where account_number = ?");
                                ps2.setString(1, accno);
                                ResultSet rs2 = ps2.executeQuery();
                                if(rs2.next()) 
                                {
                                    JOptionPane.showMessageDialog(null, "Your balance is INR " + rs2.getString("balance"), "Balance", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                        this.dispose();
                        new LoginPage(c);
                    }
                } 
                catch (NumberFormatException nfe) 
                {
                    System.out.println("Invalid amount format");
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) 
    {
        if(e.getSource() == amount_field && amount_field.getText().equals("Enter the amount to be withdrawn"))
        {
            amount_field.setText("");
            amount_field.setFont(new Font("Times New Roman",Font.PLAIN,20));
        }
    }

    @Override
    public void focusLost(FocusEvent e) 
    {
        if(e.getSource() == amount_field && amount_field.getText().equals(""))
        {
            amount_field.setFont(new Font("Calibri",Font.ITALIC,15));
            amount_field.setText("Enter the amount to be withdrawn");
        }
    }
}
