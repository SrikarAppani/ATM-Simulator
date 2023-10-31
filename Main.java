import java.sql.Connection;
import java.sql.DriverManager;

public class Main 
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM","root","Srikar@26");
            new LoginPage(c);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
