import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public class LoginPage extends JFrame implements ActionListener, FocusListener
{
    JLabel label;
    JPanel panel;
    JButton submit;
    JPasswordField password;
    JTextField username;
    Connection c;
    ImageIcon submit_button_image;
    JCheckBox showPasswordCheckbox;
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static int w, h;
    static String accno;
    LoginPage(Connection c)
    {
        w = (int)size.getWidth();
        h = (int)size.getHeight();
        this.c = c;
        ImageIcon icon = new ImageIcon("icons/SubmitButton.png");
        submit_button_image = new ImageIcon(icon.getImage().getScaledInstance(230, 90, java.awt.Image.SCALE_SMOOTH));

        label = new JLabel("Enter Credentials");
        label.setFont(new Font("Elephant",Font.PLAIN,50));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.WHITE);

        panel=new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setBounds(20,20,495,470);
        panel.setOpaque(true);
        
        username = new JTextField();
        username.setBounds(120,150,300,40);
        username.setHorizontalAlignment(JLabel.CENTER);
        username.setFont(new Font("Calibri",Font.PLAIN,15));
        username.setText("Enter your Account Number here");
        username.setForeground(Color.GRAY);
        username.addFocusListener(this);
        
        password = new JPasswordField();
        password.setBounds(120,250,300,40);
        password.setHorizontalAlignment(JLabel.CENTER);
        password.setEchoChar((char) 0);
        password.setFont(new Font("Calibri",Font.PLAIN,15));
        password.setText("Enter your PIN here");
        password.setForeground(Color.GRAY);
        password.addFocusListener(this);

        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.addActionListener(this);
        showPasswordCheckbox.setBounds(200,335,120,20);
        showPasswordCheckbox.setHorizontalAlignment(JLabel.CENTER);
        
        submit = new JButton(submit_button_image);
        submit.setBounds(185,400,150,50);
        submit.setHorizontalAlignment(JLabel.CENTER);
        submit.addActionListener(this);
        
        ImageIcon title_icon = new ImageIcon("icons/ATM.png");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds((LoginPage.w)/2-275,(LoginPage.h)/2-275,550,550);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.black);
        this.setVisible(true);
        this.setTitle("ATM Simulator");
        this.setIconImage(title_icon.getImage());
        this.add(submit);
        this.add(password);
        this.add(username);
        this.add(showPasswordCheckbox);
        this.setResizable(false);
        panel.add(label);
        this.add(panel);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==submit)
        {
            try{
                accno=username.getText();
                PreparedStatement ps =  c.prepareStatement("select account_number from Accounts where account_number = "+accno);
                ResultSet rs = ps.executeQuery();
                if(rs.next())
                {
                    PreparedStatement ps2 =  c.prepareStatement("select pin_number from Accounts where account_number = ?");
                    ps2.setString(1,username.getText());
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    int pin = rs2.getInt(1);
                    if(pin == Integer.parseInt(new String(password.getPassword())))
                    {
                        new ChooseWhatToDo(c);
                        this.dispose();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Entered wrong pin!","Invalid",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Account Number doesn't exist!","Invalid",JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        else if(e.getSource()==showPasswordCheckbox)
        {
            if(showPasswordCheckbox.isSelected())
                password.setEchoChar((char) 0);
            else
                if((new String(password.getPassword()).equals("Enter your PIN here")))
                    password.setEchoChar((char) 0);
                else
                    password.setEchoChar('*');
        }
    }
    @Override
    public void focusGained(FocusEvent e) 
    {
        if(e.getSource()==username && (username.getText().equals("") || username.getText().equals("Enter your Account Number here")))
        {
            username.setFont(new Font("Times New Roman",Font.PLAIN,20));
            username.setText("");
            username.setForeground(Color.BLACK);
        }
        else if(e.getSource()==password && ((new String(password.getPassword()).equals("")) || (new String(password.getPassword()).equals("Enter your PIN here"))))
        {
            password.setFont(new Font("Times New Roman",Font.PLAIN,20));
            password.setText("");
            password.setEchoChar('*');
            password.setForeground(Color.BLACK);
        }
    }
    @Override
    public void focusLost(FocusEvent e) 
    {
        if(e.getSource()==username && username.getText().equals(""))
        {
            username.setFont(new Font("Calibri",Font.PLAIN,15));
            username.setText("Enter your Account Number here");
            username.setForeground(Color.GRAY);
        }
        else if(e.getSource()==password && (new String(password.getPassword()).equals("")))
        {
            password.setFont(new Font("Calibri",Font.PLAIN,15));
            password.setText("Enter your PIN here");
            password.setEchoChar((char) 0);
            password.setForeground(Color.GRAY);
        }
    }
}
