import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
class ChooseWhatToDo extends JFrame implements ActionListener
{
    
    JPanel panel;
    JLabel label;
    JButton withdraw;
    JButton deposit;
    JButton balance_enquery;
    JButton change_pin;
    JButton mini_statement;
    JButton exit;
    Connection c;
    ChooseWhatToDo(Connection c)
    {
        this.c = c;

        withdraw = new JButton("WITHDRAW");
        deposit = new JButton("DEPOSIT");
        balance_enquery = new JButton("BALANCE ENQUIRY");
        change_pin = new JButton("CHANGE PIN");
        exit = new JButton("EXIT");

        withdraw.addActionListener(this);
        deposit.addActionListener(this);
        balance_enquery.addActionListener(this);
        change_pin.addActionListener(this);
        exit.addActionListener(this);

        withdraw.setBounds(270,200,175,40);
        deposit.setBounds(270,300,175,40);
        balance_enquery.setBounds(270,400,175,40);
        change_pin.setBounds(50,200,175,40);
        exit.setBounds(50,300,175,40);

        withdraw.setFocusable(false);
        deposit.setFocusable(false);
        balance_enquery.setFocusable(false);
        change_pin.setFocusable(false);
        exit.setFocusable(false);

        label = new JLabel("Choose Your Transaction");
        label.setFont(new Font("Copperplate Gothic Light",Font.PLAIN,30));
        label.setForeground(Color.WHITE);
        label.setBounds(25,15,500,200);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);
        panel.setBounds(20,20,495,470);
        panel.add(label); 
        panel.add(withdraw);
        panel.add(deposit);
        panel.add(balance_enquery);
        panel.add(change_pin);
        panel.add(exit);

        ImageIcon title_icon = new ImageIcon("icons/ATM.png");
        this.setTitle("ATM Simulator");
        this.setIconImage(title_icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds((LoginPage.w)/2-275,(LoginPage.h)/2-275,550,550);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.black);
        this.setResizable(false);
        this.add(panel);
        this.setLayout(null);

        JButton duplicate_button = new JButton();
        duplicate_button.setFocusable(true);
        duplicate_button.setBounds(1000, 1000, 30, 30);
        this.getRootPane().setDefaultButton(duplicate_button);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==withdraw)
        {
            new Withdraw(c);
            this.dispose();
        }
        else if(e.getSource()==deposit)
        {
            new Deposit(c);
            this.dispose();
        }
        else if(e.getSource()==balance_enquery)
        {
            try
            {
                PreparedStatement ps = c.prepareStatement("select balance from Accounts where account_number = "+LoginPage.accno);
                ResultSet rs = ps.executeQuery();
                rs.next();
                JOptionPane.showMessageDialog(null,"Your balance is INR "+rs.getString("balance"),"Balance",JOptionPane.INFORMATION_MESSAGE);
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        else if(e.getSource()==change_pin)
        {
            new ChangePIN(c);
            this.dispose();
        }
        else if(e.getSource()==exit)
        {
            System.exit(0);
        }
    }
}