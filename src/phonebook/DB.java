package phonebook;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet; 
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB 
    {
    final String URL=  "jdbc:derby:phonebook;create=true";
    final String USERNAME=  "";
    final String PASSWORD=  "";
    
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;
    
    public DB()
        {
        try 
            {
            conn = DriverManager.getConnection(URL);
            System.out.println("A kapcsolat létrejött");
            } 
        catch (SQLException ex) 
            {
            System.out.println("Kapcsolódási hiba: "+ex);
            }
        
        if(conn != null)
            {
            try 
                {
                createStatement = conn.createStatement();
                } 
            catch (SQLException ex) 
                {
                System.out.println("Valami baj van van a createStatament (teherautó) létrehozásakor.");
                System.out.println(""+ex);
                }
            }
        
        try
           {
            dbmd = conn.getMetaData();
            } 
        catch (SQLException ex) 
            {
            System.out.println("Valami baj van a DatabaseMetaData (adatbázis leírása) létrehozásakor..");
            System.out.println(""+ex);
            }
        
        try 
            {
             ResultSet rs = dbmd.getTables(null,"APP", "CONTACTS", null);
            if(!rs.next())
                {
                createStatement.execute("create table contacts(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),lastname varchar(20), firstname varchar(20), email varchar(30))");
                }
            } 
        catch (SQLException ex) 
            {
            System.out.println("Valami baj van az adattáblák létrehozásakor.");
            System.out.println(""+ex);
            }
        }
    
    public ArrayList<Person> getAllContacts()
        {
        String sql = "SELECT * FROM contacts";
        ArrayList<Person> contacs = null;
        
        try 
            {
            ResultSet rs = createStatement.executeQuery(sql);
            contacs = new ArrayList<>();
            
            while(rs.next())
                {
                Person actualPerson = new Person(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"), rs.getString("email"));
                contacs.add(actualPerson); 
                }
            } 
        
        catch (Exception ex) 
            {
            System.out.println("Valami baj van a kontaktok kiolvasásával.");
            System.out.println(""+ex);
            }

        return contacs;
        }
    
    public void addContact(Person person)
        {
        try 
           {
            String sql = "INSERT INTO contacts (lastname, firstname, email) values(?, ?, ?)";
            PreparedStatement preparedStatement  = conn.prepareStatement(sql);
            preparedStatement.setString(1, person.getLastName());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.execute();
            }
        catch (SQLException ex) 
            {
            System.out.println("Valami baj van a kontakt hozzáadásával.");
            System.out.println(""+ex);
            }
        }
    
    public void updateContact(Person person)
        {
        try {
            String sql = "UPDATE contacts SET lastname = ?, firstname = ?, email = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, person.getLastName());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setInt(4, Integer.parseInt(person.getId()));
            preparedStatement.execute();
            } 
        catch (SQLException ex) 
            {
            System.out.println("Valami baj van a kontakt frissítésvel.");
            System.out.println("" + ex);
            }        
        }
    
    public void removeContact(Person person)
        {
        try {
            String sql = "DELETE FROM contacts WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(person.getId()));
            preparedStatement.execute();
            } 
        catch (SQLException ex) 
            {
            System.out.println("Valami baj van a kontakt törlésekor.");
            System.out.println("" + ex);
            }        
        }
    }