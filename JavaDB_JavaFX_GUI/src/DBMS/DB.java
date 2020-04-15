package DBMS;
import javax.xml.transform.Result;
import java.io.StringBufferInputStream;
import java.sql.*;
import java.util.*;


public class DB {

    private static Statement stmt;
    private static Connection conn = null;
    private static String name;
    private static String url;

    public DB (String name)
    {
        this.name = name;
        this.url = "jdbc:mysql://localhost/Library?user=root&password=abcd&characterEncoding=utf8";
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver");
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
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=abcd&characterEncoding=utf8");
        Statement stmt = con.createStatement() ){
            System.out.println("creating new DB");
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS Library CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci");
        }

        catch (SQLException ex){
            ex.printStackTrace();
        }

        String createReadersTableStatement = "CREATE TABLE IF NOT EXISTS Readers(ReaderID int(11) NOT NULL AUTO_INCREMENT,"
                + "Name VARCHAR(45) NOT NULL, Surname VARCHAR(45) NOT NULL,"
                + "PESEL VARCHAR(12) NOT NULL UNIQUE, PRIMARY KEY (ReaderID))";

//        --------------------------------------
        String createBooksTableStatement = "CREATE TABLE IF NOT EXISTS Books(BookID int(11) NOT NULL AUTO_INCREMENT,"
                + "Title VARCHAR(100) NOT NULL, "
                + "Author VARCHAR(100) NOT NULL,"
                + "ReleaseYear int(5) NOT NULL,"
                + "Type VARCHAR(25) NOT NULL, PRIMARY KEY (BookID))";
        //--------------------------------------

        // one to many relation
        String createBorrowedTableStatement = "CREATE TABLE IF NOT EXISTS Borrowed("
                + "BookID int(11) NOT NULL, ReaderID int(11) NOT NULL, "
                + "BorrowedDate DATETIME DEFAULT CURRENT_TIMESTAMP, ReturnDate DATE NOT NULL,"
                + "FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE CASCADE,"
                + "FOREIGN KEY (ReaderID) REFERENCES Readers(ReaderID) ON DELETE CASCADE, UNIQUE (BookID))";

        try{
            conn = DriverManager.getConnection(url);

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
            conn = DriverManager.getConnection(url);
            //stmt = conn.createStatement();

            String addStatement = "INSERT INTO Readers (Name, Surname, PESEL) VALUES (?,?,?)";
            System.out.println(name + " " + surname + " " + pesel);
            PreparedStatement preparedStmt = conn.prepareStatement(addStatement);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, surname);
            preparedStmt.setString(3, pesel);
            preparedStmt.execute();

            conn.close();
            preparedStmt.close();

            System.out.println("Added " + name +" "+ surname + " " + pesel);

        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void delete_reader(int id){
        String deleteStatement = "DELETE FROM Readers WHERE ReaderID = ?";

        try (Connection con = DriverManager.getConnection(url);
            PreparedStatement prepStmt = con.prepareStatement(deleteStatement)){
                prepStmt.setInt(1, id);
                prepStmt.execute();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void delete_reader(String name, String surname)
    {
        String deleteStatement = "DELETE FROM Readers WHERE (Name LIKE ? AND Surname LIKE ?)";
        try(Connection con = DriverManager.getConnection(url);
            PreparedStatement preparedStmt = con.prepareStatement(deleteStatement)) {

            preparedStmt.setString(1,name);
            preparedStmt.setString(2,surname);
            preparedStmt.execute();
            System.out.println(preparedStmt);
                System.out.println("Succesfully deleted " + name + " " + surname + " from the database.\n");
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void add_book(String title, String author, int releaseYear, String type)
    {
        try
        {
            conn = DriverManager.getConnection(url);
            //stmt = conn.createStatement();

            String addStatement = "INSERT INTO Books (Title, Author, ReleaseYear, Type) VALUES (?,?,?,?)";
            PreparedStatement preparedStmt = conn.prepareStatement(addStatement);
            preparedStmt.setString(1, title);
            preparedStmt.setString(2, author);
            preparedStmt.setInt(3, releaseYear);
            preparedStmt.setString(4, type);
            preparedStmt.execute();

            conn.close();                                                                                               
            preparedStmt.close();

            System.out.println("Added " + title +" "+ releaseYear + " " + type);

        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void delete_book(int id){
        String deleteStatement = "DELETE FROM Books WHERE BookID = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement prepStmt = con.prepareStatement(deleteStatement)){
            prepStmt.setInt(1, id);
            prepStmt.execute();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void delete_book(String title)
    {
        String deleteStatement = "DELETE FROM Books WHERE Title LIKE ?";
        try(Connection con = DriverManager.getConnection(url);
            PreparedStatement preparedStmt = con.prepareStatement(deleteStatement)) {

            preparedStmt.setString(1,title);
            preparedStmt.execute();
            System.out.println(preparedStmt);
            System.out.println("Succesfully deleted " + title);
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void borrowBook(int readerID, int bookID){
        String borrowStatement = "INSERT INTO Borrowed (ReaderID, BookID, ReturnDate) VALUES (?, ?, CURDATE()+ INTERVAL 21 DAY)";

        try(Connection con = DriverManager.getConnection(url);
        PreparedStatement preparedStatement = con.prepareStatement(borrowStatement)){
            preparedStatement.setInt(1, readerID);
            preparedStatement.setInt(2, bookID);

            preparedStatement.execute();
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
            conn = DriverManager.getConnection(url);
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

            conn.close();
            preparedStmt.close();
            rs.close();
        }

        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    public List <LinkedHashMap<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        List <LinkedHashMap<String, Object>> list = new ArrayList <LinkedHashMap<String, Object>>();
        while (rs.next()){
            LinkedHashMap <String, Object> row = new LinkedHashMap <String, Object>(columns);

            for (int i = 1; i <= columns; i++){
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }

    public List<LinkedHashMap<String,Object>> findPerson(String name, String surname){
        String sql = "SELECT * FROM Readers WHERE (Name LIKE ? AND Surname LIKE ?)";
        List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

        try( Connection con = DriverManager.getConnection(url);
         PreparedStatement prepstmt = con.prepareStatement(sql)){
            prepstmt.setString(1, name);
            prepstmt.setString(2,surname);

            try(ResultSet rs = prepstmt.executeQuery()) {
                list = resultSetToArrayList(rs);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public List<LinkedHashMap<String,Object>> findBook(String title){
        String sql = "SELECT Books.BookID, Title, Author, ReleaseYear, Type FROM Books LEFT JOIN Borrowed ON Books.BookID = Borrowed.BookID WHERE Borrowed.BorrowedDate IS NULL AND Title LIKE ?";
        List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();

        //GET RID OF BOOKS THAT ARE BORROWED
        
        try( Connection con = DriverManager.getConnection(url);
             PreparedStatement prepstmtAllBooks = con.prepareStatement(sql)){

            prepstmtAllBooks.setString(1, title);

            try(ResultSet rs = prepstmtAllBooks.executeQuery()) {
                list = resultSetToArrayList(rs);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        System.out.println("LISTA = " + list.toString());
        return list;

    }
}
