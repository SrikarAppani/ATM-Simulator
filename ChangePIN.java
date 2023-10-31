import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class ChangePIN extends JFrame implements ActionListener, FocusListener
{
    Connection c;
    JPasswordField new_pin_field, confirm_pin_field;
    JPanel panel;
    JButton submit_button, back_button, duplicate_button;
    ChangePIN(Connection c)
    {
        this.c = c;

        ImageIcon back_icon_img = new ImageIcon("icons/BackButton.jpg");
        ImageIcon back_icon = new ImageIcon(back_icon_img.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
        duplicate_button = new JButton();
        duplicate_button.setFocusable(true);
        duplicate_button.setBounds(1000, 1000, 30, 30);
        back_button = new JButton();
        back_button.setIcon(back_icon);
        back_button.setBounds(10, 10, 30, 30);
        back_button.setFocusable(true);
        back_button.addActionListener(this);

        new_pin_field=new JPasswordField("Enter new PIN");
        confirm_pin_field=new JPasswordField("Confirm new PIN");

        new_pin_field.setEchoChar((char)0);
        confirm_pin_field.setEchoChar((char)0);

        new_pin_field.addFocusListener(this);
        confirm_pin_field.addFocusListener(this);

        new_pin_field.setBounds(110, 60, 300, 40);
        confirm_pin_field.setBounds(110, 110, 300, 40);

        new_pin_field.setBackground(Color.black);
        confirm_pin_field.setBackground(Color.black);

        new_pin_field.setForeground(Color.white);
        confirm_pin_field.setForeground(Color.white);
        
        new_pin_field.setHorizontalAlignment(JLabel.CENTER);
        confirm_pin_field.setHorizontalAlignment(JLabel.CENTER);

        new_pin_field.setCaretColor(Color.white);
        confirm_pin_field.setCaretColor(Color.white);

        new_pin_field.setFont(new Font("Calibri",Font.ITALIC,15));
        confirm_pin_field.setFont(new Font("Calibri",Font.ITALIC,15));

        submit_button = new JButton("SUBMIT");
        submit_button.addActionListener(this);
        submit_button.setFocusable(false);
        submit_button.setBackground(Color.black);
        submit_button.setForeground(Color.white);
        submit_button.setBounds(370,200,100,40);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);
        panel.setBounds(20,20,495,270);
        panel.add(duplicate_button);
        panel.add(back_button);
        panel.add(new_pin_field);
        panel.add(confirm_pin_field);
        panel.add(submit_button);

        ImageIcon title_icon = new ImageIcon("icons/ATM.png");
        this.setTitle("ATM Simulator");
        this.setIconImage(title_icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds((LoginPage.w)/2-275,(LoginPage.h)/2-275,550,350);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.black);
        this.setResizable(false);
        this.add(panel);
        this.setLayout(null);
    }


    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == back_button) 
        {
            new ChooseWhatToDo(c);
            this.dispose();
        } 
        else if (e.getSource() == submit_button) 
        {
            try 
            {
                String newPIN = new String(new_pin_field.getPassword());
                String confirmPIN = new String(confirm_pin_field.getPassword());
                if(newPIN.equals(confirmPIN)) 
                {
                    PreparedStatement ps = c.prepareStatement("update Accounts set pin_number = ? where account_number = ?");
                    ps.setString(1, newPIN);
                    ps.setString(2, LoginPage.accno);
                    ps.executeUpdate();

                    String[] responses = {"Ok","Exit"};
                    int y = JOptionPane.showOptionDialog(null, "PIN changed successfully!", "Success", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, responses, 0);

                    if(y == 0) 
                    {
                        new LoginPage(c);
                        this.dispose();
                    } 
                    else 
                    {
                        System.exit(0);
                    }
                } 
                else 
                {
                    JOptionPane.showMessageDialog(null, "PINs do not match, re-enter the PIN", "No Match", JOptionPane.ERROR_MESSAGE);
                }
            } 
            catch(NumberFormatException nfe) 
            {
                JOptionPane.showMessageDialog(null, "Invalid PIN entered", "Invalid", JOptionPane.ERROR_MESSAGE);
            } 
            catch(Exception ex) 
            {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) 
    {
        if(e.getSource() == new_pin_field || e.getSource() == confirm_pin_field) 
        {
            JPasswordField field = (JPasswordField) e.getSource();
            if(new String(field.getPassword()).equals("Enter new PIN") || new String(field.getPassword()).equals("Confirm new PIN")) 
            {
                field.setText("");
                field.setEchoChar('*');
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) 
    {
        if(e.getSource() == new_pin_field || e.getSource() == confirm_pin_field) 
        {
            JPasswordField field = (JPasswordField) e.getSource();
            if(field.getPassword().length == 0) 
            {
                field.setText("Enter new PIN");
                field.setEchoChar((char) 0);
            }
        }
    }
}