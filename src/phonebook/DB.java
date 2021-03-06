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
    
    private Dialogus dialog = new Dialogus();
    
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;
    
    public DB()
        {
        try 
            {
            conn = DriverManager.getConnection(URL);
            } 
        catch (SQLException ex) 
            {
            dialog.kivetel(ex, "Kapcsolódási hiba!");
            }
        
        if(conn != null)
            {
            try 
                {
                createStatement = conn.createStatement();
                } 
            catch (SQLException ex) 
                {
                dialog.kivetel(ex, "Valami baj van van a createStatament létrehozásakor.");
                }
            }
        
        try
           {
            dbmd = conn.getMetaData();
            } 
        catch (SQLException ex) 
            {
            dialog.kivetel(ex, "Valami baj van a DatabaseMetaData (adatbázis leírása) létrehozásakor..");
            }
        
        try 
            {
             ResultSet rs = dbmd.getTables(null,"APP", "CONTACTS", null);
            if(!rs.next())
                {
                String sql = "create table contacts(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),lastname varchar(20), firstname varchar(20), email varchar(30))";
                boolean valasz = dialog.confirm("Adatbázis nélkül nem használható az alkalmazás", "Ok: létrehozás, Cancel: kilépés!");
                
                if(valasz)
                    createStatement.execute(sql);
                else
                    System.exit(0);
                }
            } 
        catch (SQLException ex) 
            {
            dialog.kivetel(ex, "Valami baj van az adattáblák létrehozásakor.");
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
            dialog.kivetel(ex, "Valami baj van a kontaktok kiolvasásával.");
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
            dialog.kivetel(ex, "Valami baj van a kontakt hozzáadásával.");
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
            dialog.kivetel(ex, "Valami baj van a kontakt frissítésvel.");
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
            dialog.kivetel(ex, "Valami baj van a kontakt törlésekor.");
            }        
        }
    }