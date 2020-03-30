package DBMS;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.Scanner;


public class DB {

    static Statement stmt;
    static Connection conn = null;
    static String name;

    public DB (String name)
    {
        this.name = name;
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("object created");
        }

        catch (Exception ex) {
            // handle the error
            System.out.println("sth went wrong");
            ex.printStackTrace();
        }

        createNewDB();
    }

    private void createNewDB(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=abcd");
            stmt = this.conn.createStatement();
            int result = stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS LIBRARY");

            stmt.close();
            conn.close();

        }

        catch (SQLException ex){
            ex.printStackTrace();
        }

        String createReadersTableStatement = "CREATE TABLE IF NOT EXISTS Readers(ReaderID int(11) NOT NULL AUTO_INCREMENT,"
                + "Name VARCHAR(45) NOT NULL, Surname VARCHAR(45) NOT NULL,"
                + "PESEL VARCHAR(12) NOT NULL UNIQUE, PRIMARY KEY (ReaderID))";

        String createBooksTableStatement = "CREATE TABLE IF NOT EXISTS Books(BookID int(11) NOT NULL AUTO_INCREMENT,"
                + "Title VARCHAR(100) NOT NULL, ReleaseYear int(5) NOT NULL,"
                + "Type VARCHAR(25) NOT NULL, PRIMARY KEY (BookID))";

        // one to many relation
        String createBorrowedTableStatement = "CREATE TABLE IF NOT EXISTS Borrowed("
                + "BookID int(11) NOT NULL, ReaderID int(11) NOT NULL, "
                + "BorrowedDate DATETIME DEFAULT CURRENT_TIMESTAMP, ReturnDate DATE NOT NULL,"
                + "FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE CASCADE,"
                + "FOREIGN KEY (ReaderID) REFERENCES Readers(ReaderID) ON DELETE CASCADE, UNIQUE (BookID))";

        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/LIBRARY?user=root&password=abcd");

            System.out.println("adding tables");
            stmt = conn.createStatement();
            stmt.executeUpdate(createBooksTableStatement);
            stmt = conn.createStatement();
            stmt.executeUpdate(createReadersTableStatement);
            stmt.executeUpdate(createBorrowedTableStatement);
            System.out.println("done");

            stmt.close();
            conn.close();
        }

        catch (SQLException ex){
            ex.printStackTrace();
        }



    }
    
    public static void add_reader(String name, String surname, String pesel)
    {
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/LIBRARY?user=root&password=abcd");
            //stmt = conn.createStatement();

            String addStatement = "INSERT INTO Readers (Name, Surname, PESEL) VALUES (?,?,?)";
            PreparedStatement preparedStmt = conn.prepareStatement(addStatement);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, surname);
            preparedStmt.setString(3, pesel);
            preparedStmt.execute();

            conn.close();

            System.out.println("Added " + name +" "+ surname + " " + pesel);

        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void delete_reader(String name, String surname)
    {
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/LIBRARY?user=root&password=abcd");
            String deleteStatement = "DELETE FROM Readers WHERE ReaderID = ?";
            String searchStatement = "SELECT * FROM Readers WHERE Name LIKE ? AND Surname LIKE ?";
            PreparedStatement preparedStmt = conn.prepareStatement(searchStatement);
            preparedStmt.setString(1,name);
            preparedStmt.setString(2,surname);

            ResultSet rs = preparedStmt.executeQuery();

            // checking whether there is a single person with given name or multiple
            int rsCounter = 0;
            int readerID = 0;
            while(rs.next())
            {
                rsCounter++;
                readerID = rs.getInt("ReaderID");

                if(rsCounter > 1)
                    break;
            }

            if(rsCounter > 0 && rsCounter < 2)
            {
                preparedStmt = conn.prepareStatement(deleteStatement);
                preparedStmt.setInt(1, readerID);
                preparedStmt.execute();

                System.out.println("Succesfully deleted " + name + " " + surname + " from the database.\n");
            }

            else
            {
                rs.beforeFirst();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                while (rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        String columnValue = rs.getString(i);
                        System.out.print( rsmd.getColumnName(i)+ " - " + columnValue + " | ");
                    }

                    System.out.println("");
                }

                System.out.println("Which person would you like to delete? Please type ID in.");
                Scanner inputScanner = new Scanner(System.in);
                String id = inputScanner.nextLine();

                preparedStmt = conn.prepareStatement(deleteStatement);
                preparedStmt.setInt(1, Integer.parseInt(id));
                preparedStmt.execute();

                System.out.println("Succesfully deleted " + name + " " + surname + " from the database.\n");
            }

        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    public static void add_book(String title, int releaseYear, String type)
    {
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/LIBRARY?user=root&password=abcd");
            //stmt = conn.createStatement();

            String addStatement = "INSERT INTO Books (Title, ReleaseYear, Type) VALUES (?,?,?)";
            PreparedStatement preparedStmt = conn.prepareStatement(addStatement);
            preparedStmt.setString(1, title);
            preparedStmt.setInt(2, releaseYear);
            preparedStmt.setString(3, type);
            preparedStmt.execute();

            conn.close();                                                                                               

            System.out.println("Added " + title +" "+ releaseYear + " " + type);

        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void delete_book(String title)
    {
        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/LIBRARY?user=root&password=abcd");
            String deleteStatement = "DELETE FROM Books WHERE BookID = ?";
            String searchStatement = "SELECT * FROM Books WHERE Title LIKE ?";
            PreparedStatement preparedStmt = conn.prepareStatement(searchStatement);
            preparedStmt.setString(1,title);

            ResultSet rs = preparedStmt.executeQuery();

            // checking whether there is a single person with given name or multiple
            int rsCounter = 0;
            int bookID = 0;
            while(rs.next())
            {
                rsCounter++;
                bookID = rs.getInt("BookID");

                if(rsCounter > 1)
                    break;
            }

            if(rsCounter > 0 && rsCounter < 2)
            {
                preparedStmt = conn.prepareStatement(deleteStatement);
                preparedStmt.setInt(1, bookID);
                preparedStmt.execute();

                System.out.println("Successfully deleted \"" + title + "\" from the database.\n");
            }

            else
            {
                rs.beforeFirst();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                while (rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        String columnValue = rs.getString(i);
                        System.out.print( rsmd.getColumnName(i)+ " - " + columnValue + " | ");
                    }

                    System.out.println("");
                }

                System.out.println("Which book would you like to delete? Please type ID in.");
                Scanner inputScanner = new Scanner(System.in);
                String id = inputScanner.nextLine();

                preparedStmt = conn.prepareStatement(deleteStatement);
                preparedStmt.setInt(1, Integer.parseInt(id));
                preparedStmt.execute();

                System.out.println("Successfully deleted \"" + title + "\" from the database.\n");
            }

        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }


    public static void borrow_book(String name, String surname, String title)
    {
        String findPerson = "SELECT * FROM Readers WHERE (Name LIKE ? AND Surname LIKE ? )";
        //String findBook = "SELECT * FROM Books WHERE (Title LIKE ?)";
        String findNotBorrowed = "select * from Books WHERE NOT EXISTS (Select * from Borrowed WHERE Books.BookID = Borrowed.BookID)";

        try
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/LIBRARY?user=root&password=abcd");   
            stmt = conn.createStatement();

            PreparedStatement preparedStmt = conn.prepareStatement(findPerson);
            preparedStmt.setString(1,name);
            preparedStmt.setString(2,surname);
            //System.out.println(findPerson);

            ResultSet rs = preparedStmt.executeQuery();

            //if reader exists then check if book exists and isn't borrowed...
            //add book -> INSERT INTO BORROWED (ReaderID, BookID, ReturnDate) VALUES (?,?, curdate()+21)

            if(rs.first())
            {
                rs.beforeFirst();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                while (rs.next())
                {
                    for (int i = 1; i <= columnsNumber; i++)
                    {
                        String columnValue = rs.getString(i);
                        System.out.print( rsmd.getColumnName(i)+ " - " + columnValue + " | ");
                    }

                    System.out.println("");
                }

                System.out.println("Which person would you like to lend the book? Please type ID in.");
                Scanner inputScanner = new Scanner(System.in);
                String readerID = inputScanner.nextLine();

                //reader is in the DB
                //System.out.println("if1");
                preparedStmt = conn.prepareStatement(findNotBorrowed);
                //preparedStmt.setString(1,title);
                ResultSet rs2 = preparedStmt.executeQuery();

                if(rs2.first())
                {
                    //System.out.println("if2");
                    //found the book
                    // select * from Books WHERE NOT EXISTS (Select * from Borrowed WHERE Books.BookID = Borrowed.BookID); - books not borrowed yet

                    // (select * from Books WHERE NOT EXISTS (Select * from Borrowed WHERE Books.BookID = Borrowed.BookID));
                    rsmd = rs2.getMetaData();
                    columnsNumber = rsmd.getColumnCount();
                    rs2.previous();

                    System.out.println("Which book would you like to lend? Please type the BookID in.");

                    while (rs2.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            if(rs2.getString((2)).equals(title))
                            {
                                if (i > 1)
                                    System.out.print(",  ");

                                String columnValue = rs2.getString(i);
                                System.out.print( rsmd.getColumnName(i)+ " - " + columnValue );
                            }
                        }
                        System.out.println("");
                    }

                    inputScanner = new Scanner(System.in);
                    String bookID = inputScanner.nextLine();

                    String addBook = "INSERT INTO Borrowed (ReaderID, BookID, ReturnDate) VALUES (?,?, curdate()+ INTERVAL 21 DAY)";

                    //creating query
                    preparedStmt = conn.prepareStatement(addBook);
                    preparedStmt.setString(1,readerID);
                    preparedStmt.setString(2, bookID);
                    preparedStmt.executeUpdate();



                    System.out.println("Succesfully lent the book!");
                }

                else
                    System.out.println("There are no books to lend!");

            }

            else
                System.out.println("There's nobody called " + name + " " + surname + " in the datebase.");
        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

    }
}
