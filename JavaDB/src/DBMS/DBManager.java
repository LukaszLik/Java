package DBMS;

import java.util.Scanner;

public class DBManager{

    public static void main (String[] args)
    {
        System.out.println("DB start");
        DB dateBaseManager = new DB("test");

        //managing datebase
        while(true)
        {
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("Type 1 to add new reader to the datebase"
                    + "\nType 2 to add new book"
                    + "\nType 3 to lend a book"
                    + "\nType 4 to delete reader from database"
                    + "\nType 5 to delete book from database"
                    + "\nType q to exit");

            String inputData = inputScanner.nextLine();

            if(inputData.equals("1"))
            {
                System.out.println("New reader's name:");
                String name = inputScanner.nextLine();
                System.out.println("New reader's surname:");
                String surname = inputScanner.nextLine();

                System.out.println("New reader's PESEL:");
                String pesel = inputScanner.nextLine();

                DB.add_reader(name, surname, pesel);
            }

            if(inputData.equals("2"))
            {
                System.out.println("New book's title:");
                String title = inputScanner.nextLine();
                System.out.println("New book's release year:");
                String releaseYear = inputScanner.nextLine();

                System.out.println("New book's type:");
                String type = inputScanner.nextLine();

                DB.add_book(title, Integer.parseInt(releaseYear), type);

            }

            if (inputData.equals("3"))
            {
                System.out.println("Reader's name:");
                String name = inputScanner.nextLine();
                System.out.println("Reader's surname:");
                String surname = inputScanner.nextLine();
                System.out.println("New book's title:");
                String title = inputScanner.nextLine();

                DB.borrow_book(name, surname, title);

            }

            if (inputData.equals("4"))
            {
                System.out.println("Reader's name:");
                String name = inputScanner.nextLine();
                System.out.println("Reader's surname:");
                String surname = inputScanner.nextLine();

                DB.delete_reader(name, surname);

            }

            if (inputData.equals("5"))
            {
                System.out.println("Book's title:");
                String title = inputScanner.nextLine();

                DB.delete_book(title);
            }

            if(inputData.equals("q"))
                break;



        }



    }

}
