package DBMS;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.*;

public class Controller {

    private String pressedButtonId = "";
    private  DB dataBaseManager;

    Controller(){
        this.dataBaseManager = new DB("test");
    }

    private static HBox addHbox(Button[] buttons)
    {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15,12,15,12));
        hbox.setSpacing(15);
        hbox.setMinWidth(100);

        //adding buttons to hbox in for each loop
        for (Button btn:buttons)
        {
            btn.setId(btn.getText());
            //btn.setMaxWidth(Double.MAX_VALUE);
            hbox.getChildren().add(btn);
        }
        return hbox;
    }

    private void addStyleSheet(Scene scene){
        URL url = this.getClass().getResource("styles.css");
        if (url == null)
        {
            System.out.println("Resource not found. Aborting.");
            System.exit(-1);
        }
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);
    }

    public void setLoginScene(Stage stage){
        //creating layout of the form
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(25,25,25,25));

        Text sceneTitle = new Text("Welcome!");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle,0,0,2,1); //0,0 - row and column #, 2,1 - sets column span to 2 and row span to 1

        //login form
        Label userName = new Label("Username:");
        Label password = new Label("Password:");

        TextField userTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        //column / row
        grid.add(userName,0,1);
        grid.add(userTextField,1,1);
        grid.add(password,0,2);
        grid.add(passwordField,1,2);

        //creating sign-in button
        Button signInBtn = new Button("Sign in");
        HBox btnHBox = new HBox(10); //10px spacing
        btnHBox.setAlignment(Pos.BOTTOM_RIGHT);
        btnHBox.getChildren().add(signInBtn);
        grid.add(btnHBox, 1,4);

        final Text logInActionTarget = new Text();
        grid.add(logInActionTarget,1,6);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        this.addStyleSheet(scene);

//        setting background image
//        URL url = this.getClass().getResource("img.jpeg");
//        Image img = new Image (url.toExternalForm());
//        BackgroundImage bgImg = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
//                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,false,false,true,false));
//
//        grid.setBackground(new Background(bgImg));


        //adding event handler
        signInBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                logInActionTarget.setFill(Color.FIREBRICK);
                logInActionTarget.setText("Loggin in");
                setManagerScene(stage);
            }
        });
    }

    public void setManagerScene(Stage stage)
    {
        GridPane managerGrid = new GridPane();
        managerGrid.getStyleClass().add("grid-pane");
        Text welcomeText = new Text("Library management system:");

        managerGrid.add(welcomeText, 0,0,2,1);

        //utility buttons
        Button newReaderBtn = new Button("Add Reader");
        Button newBookBtn = new Button("Add Book");
        Button deleteReaderBtn = new Button("Delete Reader");
        Button deleteBookBtn = new Button("Delete Book");
        Button lendBookBtn = new Button("Lend Book");
        Button debugBtn = new Button("Debug");

        managerGrid.add(addHbox(new Button[]{newReaderBtn, deleteReaderBtn}), 0,2);
        managerGrid.add(addHbox(new Button[] {newBookBtn, deleteBookBtn}),0,3);
        managerGrid.add(addHbox(new Button[] {lendBookBtn, debugBtn}),0,4);

        //adding action listeners
        newReaderBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("add reader");
                pressedButtonId = newReaderBtn.getId();
                addingScene(stage);

            }
        });

        deleteReaderBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pressedButtonId = deleteReaderBtn.getId();
                deleteScene(stage);
                System.out.println("delete reader");
            }
        });

        newBookBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("add book");
                pressedButtonId = newBookBtn.getId();
                addingScene(stage);
            }
        });

        deleteBookBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pressedButtonId = deleteBookBtn.getId();
                deleteScene(stage);
                System.out.println("delete book");
            }
        });

        lendBookBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("lend book");
            }
        });

        debugBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Search");
                pressedButtonId = debugBtn.getId();
                debugScene(stage);
            }
        });


        Scene scene = new Scene(managerGrid);
        this.addStyleSheet(scene);
        System.out.println(managerGrid.getStyleClass());

        stage.setScene(scene);
    }

    private Scene addingScene(Stage stage){
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");

        Button submitBtn = new Button("Submit");
        HBox box = new HBox();
        box.getChildren().add(submitBtn);
        box.setAlignment(Pos.BOTTOM_RIGHT);

        Button cancelBtn = new Button("Cancel");
        HBox box2 = new HBox();
        box2.getChildren().add(cancelBtn);
        box2.setAlignment(Pos.BOTTOM_LEFT);

        if (pressedButtonId.equals("Add Reader"))
        {
            Text txt = new Text("Please fill in the from in order to add new reader.");

            //creating form
            Label readerName = new Label("Reader's name:");
            Label readerSurname = new Label("Reader's surname:");
            Label readerPesel = new Label("Reader's PESEL number:");

            TextField name = new TextField();
            TextField surname = new TextField();
            TextField pesel = new TextField();

            grid.add(txt,0,0,3,1);
            grid.add(readerName,0,1);
            grid.add(name,1,1);
            grid.add(readerSurname,0,2);
            grid.add(surname,1,2);
            grid.add(readerPesel,0,3);
            grid.add(pesel,1,3);

            int blockCounter = 0;

            //textfields validators
            Label wrongName = new Label();
            name.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!name.getText().matches("^[\\p{Lu}]\\p{Ll}+$"))
                    {
                        wrongName.setText("Name should consists of letters only and start with capital letter.");
                        wrongName.setTextFill(Color.FIREBRICK);

                        if(grid.getChildren().contains(wrongName))
                            grid.add(wrongName,3,1);

                        System.out.println("incorrect name");
                    }

                    else
                        wrongName.setText("");
                }

            });

            Label wrongSurname = new Label();
            surname.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!surname.getText().matches("^[\\p{Lu}]\\p{Ll}+$"))
                    {
                        wrongSurname.setText("Surname should consists of letters only and start with capital letter.");
                        wrongSurname.setTextFill(Color.FIREBRICK);

                        if(grid.getChildren().contains(wrongSurname))
                            grid.add(wrongSurname,3,2);

                        System.out.println("incorrect surname");
                    }

                    else
                        wrongSurname.setText("");
                }

            });

            Label wrongPESEL = new Label();
            pesel.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    System.out.println("leng = " + pesel.getText().length());
                    if(!(pesel.getText().matches("^[0-9]*$") && (pesel.getText().length() == 11)))
                    {
                        wrongPESEL.setVisible(true);
                        wrongPESEL.setText("PESEL consists of 11 digits");
                        wrongPESEL.setTextFill(Color.FIREBRICK);

                        if(!grid.getChildren().contains(wrongPESEL))
                            grid.add(wrongPESEL,3,3);

                        System.out.println("incorrect pesel");
                    }

                    else {
                        wrongPESEL.setText("");
                        wrongPESEL.setVisible(false);
                        System.out.println("elseS");
                    }
                }

            });

            submitBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    if(wrongPESEL.getText().equals("") && pesel.getText().length() > 0
                        && wrongName.getText().equals("") && name.getText().length() > 0
                        && wrongSurname.getText().equals("") && surname.getText().length() > 0)
                    {
                        String nameFromTxt = name.getText();
                        String surnameFromTxt = surname.getText();
                        String peselFormTxt = pesel.getText();

                        dataBaseManager.add_reader(nameFromTxt, surnameFromTxt, peselFormTxt);

                        System.out.println(nameFromTxt + " " + surnameFromTxt + " " + peselFormTxt);

                    }

                    else
                    {
                        System.out.println("Wrong input values.");
                    }

                }
            });

            System.out.println("add reader scene");
        }

        else if(pressedButtonId.equals("Add Book"))
        {
            Text txt = new Text("Please fill in the from in order to add new book.");
            //creating form
            Label bookTitle = new Label("Book title:");
            Label bookReleaseYear = new Label("Release year:");
            Label bookType = new Label("Book type:");

            TextField title = new TextField();
            TextField releaseYear = new TextField();
            TextField type = new TextField();

            grid.add(txt,0,0,3,1);
            grid.add(bookTitle,0,1);
            grid.add(title,1,1);
            grid.add(bookReleaseYear,0,2);
            grid.add(releaseYear,1,2);
            grid.add(bookType,0,3);
            grid.add(type,1,3);

            //textfields validators
            Label wrongTitle = new Label();
            title.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(title.getText().matches("^[\\p{Ll}]$") || title.getText().length()==0)
                    {
                        wrongTitle.setText("Book name should start with capital letter.");
                        wrongTitle.setTextFill(Color.FIREBRICK);
                        if(!grid.getChildren().contains(wrongTitle))
                            grid.add(wrongTitle,3,1);

                        System.out.println("incorrect title");
                    }

                    else
                        wrongTitle.setText("");
                }

            });

            Label wrongReleaseYear = new Label();
            releaseYear.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!(releaseYear.getText().matches("^[0-9]+$") && releaseYear.getText().length() == 4))
                    {
                        wrongReleaseYear.setText("Year consits of 4 digits.");
                        wrongReleaseYear.setTextFill(Color.FIREBRICK);

                        if(!grid.getChildren().contains(wrongReleaseYear))
                            grid.add(wrongReleaseYear,3,2);
                        System.out.println("incorrect release year");
                    }

                    else
                        wrongReleaseYear.setText("");
                }

            });

            Label wrongType = new Label();
            type.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!(type.getText().matches("^[\\p{Lu}]\\p{Ll}+$")))
                    {
                        wrongType.setVisible(true);
                        wrongType.setText("Type should consist of letters");
                        wrongType.setTextFill(Color.FIREBRICK);
                        if(!grid.getChildren().contains(wrongType))
                            grid.add(wrongType,3,3);
                        System.out.println("incorrect type");
                    }

                    else
                    {
                        wrongType.setText("");
                        wrongType.setVisible(false);
                        System.out.println("elseS");
                    }
                }

            });

            submitBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(wrongReleaseYear.getText().equals("") && releaseYear.getText().length() > 0
                    && wrongTitle.getText().equals("") && title.getText().length() > 0
                    && wrongType.getText().equals("") && type.getText().length() > 0)
                    {
                        String titleFromTxt = title.getText();
                        String releaseYearFromTxt = releaseYear.getText();
                        String typeFormTxt = type.getText();

                        dataBaseManager.add_book(titleFromTxt, Integer.parseInt(releaseYearFromTxt), typeFormTxt);

                        System.out.println(titleFromTxt + " " + releaseYearFromTxt + " " + typeFormTxt);

                    }

                    else
                    {
                        System.out.println("incorrect values.");
                    }


                }
            });

            System.out.println("add book scene");
        }

        else
            return null;

        //adding buttons to the grid
        grid.add(box,1,6);
        grid.add(box2,0,6);

        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setManagerScene(stage);
            }
        });

        System.out.println(grid.getStyleClass());

        Scene scene = new Scene(grid);
        //remember to add the stylesheet...
        this.addStyleSheet(scene);
        stage.setScene(scene);
        return scene;
    }

    private void deleteScene(Stage stage)
    {
        Text deleteTxt = new Text();
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");

        Button cancelButton = new Button("Cancel");
        Button deleteButton = new Button("Delete");

        HBox cancelBox = new HBox();
        cancelBox.getChildren().add(cancelButton);
        cancelBox.setAlignment(Pos.BOTTOM_LEFT);
        HBox deleteBox = new HBox();
        deleteBox.getChildren().add(deleteButton);
        deleteBox.setAlignment(Pos.BOTTOM_RIGHT);

        grid.add(cancelBox,0,4);
        grid.add(deleteBox, 1,4);

        grid.add(deleteTxt,0,0,2,1);
        if(pressedButtonId.equals("Delete Reader"))
        {
            deleteTxt.setText("Please fill in the from in order to delete reader.");
            System.out.println("del reader scene");

            //creating from
            Label name = new Label("Reader name:");
            Label surname = new Label("Reader surname:");

            TextField nameTxt = new TextField();
            TextField surnameTxt = new TextField();

            grid.add(name,0,1);
            grid.add(nameTxt,1,1);

            grid.add(surname, 0,2);
            grid.add(surnameTxt,1,2);

            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("Deleting " + nameTxt.getText() + " " + surnameTxt.getText() + " from the database");
                }
            });

        }

        else if(pressedButtonId.equals("Delete Book"))
        {
            deleteTxt.setText("Please fill in the from in order to delete book.");
            System.out.println("del book scene");

            //creating from
            Label title = new Label("Book title:");

            TextField titleTxt = new TextField();

            grid.add(title,0,1);
            grid.add(titleTxt,1,1);

            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("Deleting '" + titleTxt.getText() + "' from the database");
                }
            });
        }

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setManagerScene(stage);
            }
        });

        Scene scene = new Scene(grid);
        this.addStyleSheet(scene);
        stage.setScene(scene);
    }

    private TableView resultListToTableView(List<HashMap<String,Object>> list){
        TableView tableView = new TableView();
        tableView.setPlaceholder(new Label("No rows to display"));

        Vector<TableColumn<String,Object>> columns = new Vector<TableColumn<String, Object>>();

        //creating columns
        int i = 1;
        for(Map.Entry<String, Object> entry : list.get(0).entrySet()){
            columns.add(new TableColumn<String, Object>(entry.getKey()));
            columns.lastElement().setCellValueFactory(new PropertyValueFactory<>("val" + i));
            i++;
        }

        tableView.getColumns().addAll(columns);

        //inserting raws of data WIP
        for (Map<String, Object> map : list){
            ArrayList<Object> row = new ArrayList<Object>();
            for (Map.Entry<String, Object> entry : map.entrySet()){

                row.add(entry.getValue());

                System.out.println("[" + entry.getKey() + ", " + entry.getValue() + "]");
            }

            tableView.getItems().add(new TableValues(row));
        }



        return tableView;
    }

    private void debugScene (Stage stage){
        List<HashMap<String,Object>> readersList= dataBaseManager.findPerson("Mietek", "Mietek");
        System.out.println(readersList);
        TableView tableView = resultListToTableView(readersList);

        VBox vBox = new VBox(tableView);

        Scene scene = new Scene(vBox);

        stage.setScene(scene);


    }

}
