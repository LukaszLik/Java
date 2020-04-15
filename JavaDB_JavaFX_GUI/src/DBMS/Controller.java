package DBMS;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.Buffer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.*;

public class Controller {

    private String pressedButtonId = "";
    private  DB dataBaseManager;

    Controller(){
        this.dataBaseManager = new DB("test");
    }

    public String capitalizeString(String str)
    {                                                   //non letters at the begining
        if(str == null || str.length()==1 || str.matches("^\\P{L}$"))
            return str;

        return str.substring(0,1).toUpperCase() + str.substring(1);
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
                lendScene(stage);
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
            Label wrongName = new Label("");
            name.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!name.getText().matches("^[\\p{L}]+$"))
                    {
                        wrongName.setVisible(true);
                        wrongName.setText("Name should consist of letters.");
                        wrongName.setTextFill(Color.FIREBRICK);

                        if(!grid.getChildren().contains(wrongName))
                            grid.add(wrongName,3,1);

                        System.out.println("incorrect name");
                    }

                    else {
                        wrongName.setText("");
                        wrongName.setVisible(false);
                    }
                }

            });

            Label wrongSurname = new Label();
            surname.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!surname.getText().matches("^[\\p{L}]+$"))
                    {
                        wrongSurname.setText("Surname should consists of letters.");
                        wrongSurname.setTextFill(Color.FIREBRICK);

                        if(!grid.getChildren().contains(wrongSurname))
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
                        String nameFromTxt = capitalizeString(name.getText());
                        String surnameFromTxt = capitalizeString(surname.getText());
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
            Label bookAuthor = new Label("Author:");
            Label bookReleaseYear = new Label("Release year:");
            Label bookType = new Label("Book type:");

            TextField title = new TextField();
            TextField author = new TextField();
            TextField releaseYear = new TextField();
            TextField type = new TextField();

            grid.add(txt,0,0,3,1);
            grid.add(bookTitle,0,1);
            grid.add(title,1,1);
            grid.add(bookAuthor,0,2);
            grid.add(author,1,2);
            grid.add(bookReleaseYear,0,3);
            grid.add(releaseYear,1,3);
            grid.add(bookType,0,4);
            grid.add(type,1,4);

            //textfields validators
            Label wrongTitle = new Label();
            title.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!title.getText().matches("^[\\p{L}]+$") || title.getText().length()==0)
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

            Label wrongAuthor = new Label();
            author.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!author.getText().matches("^[\\p{L}\\s,-]+$") || author.getText().length() == 0) {
                        wrongAuthor.setText("Should consist of letters, '-' or ',' if the are multiple authors");
                        wrongAuthor.setTextFill(Color.FIREBRICK);

                        if(!grid.getChildren().contains(wrongAuthor))
                            grid.add(wrongAuthor,3,2);

                        System.out.println("incorrect author name");
                    }
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
                            grid.add(wrongReleaseYear,3,3);
                        System.out.println("incorrect release year");
                    }

                    else
                        wrongReleaseYear.setText("");
                }

            });

            Label wrongType = new Label();
            type.focusedProperty().addListener((arg0, oldValue, newValue) -> {
                if(!newValue){
                    if(!(type.getText().matches("^[\\p{L}]+$")))
                    {
                        wrongType.setVisible(true);
                        wrongType.setText("Type should consist of letters");
                        wrongType.setTextFill(Color.FIREBRICK);
                        if(!grid.getChildren().contains(wrongType))
                            grid.add(wrongType,3,4);
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
                    && wrongType.getText().equals("") && type.getText().length() > 0
                    && wrongAuthor.getText().equals("") && author.getText().length() > 0)
                    {
                        String titleFromTxt = capitalizeString(title.getText());
                        String releaseYearFromTxt = releaseYear.getText();
                        String typeFormTxt = capitalizeString(type.getText());
                        String authorFromTxt = capitalizeString(author.getText());

                        dataBaseManager.add_book(titleFromTxt, authorFromTxt, Integer.parseInt(releaseYearFromTxt), typeFormTxt);

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

        grid.add(cancelBox,0,5);
        grid.add(deleteBox, 1,5);

        grid.add(deleteTxt,0,0,2,1);
        if(pressedButtonId.equals("Delete Reader"))
        {
            deleteTxt.setText("Please fill in the from in order to delete reader.");
            System.out.println("del reader scene");

            //creating from
            Label name = new Label("Reader name:");
            Label surname = new Label("Reader surname:");
            Label systemStatus = new Label("");
            systemStatus.setTextFill(Color.GREEN);

            TextField nameTxt = new TextField();
            TextField surnameTxt = new TextField();

            grid.add(name,0,1);
            grid.add(nameTxt,1,1);

            grid.add(surname, 0,2);
            grid.add(surnameTxt,1,2);

            grid.add(systemStatus,0,3,2,1);

            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("Deleting " + nameTxt.getText() + " " + surnameTxt.getText() + " from the database");
                    List<LinkedHashMap<String, Object>> list = dataBaseManager.findPerson(nameTxt.getText(), surnameTxt.getText());
                    deleteButton.setText("Search another person");
                    systemStatus.setText("");

                    if (list.size() > 1) {
                        TableView tableView = resultListToTableView(list);

                        VBox vBox = new VBox(tableView);
                        grid.add(vBox, 0, 4,2,1);

                        HBox removeBox = new HBox();
                        Button removeButton = new Button("Remove");
                        removeBox.getChildren().add(removeButton);
                        removeBox.setAlignment(Pos.BOTTOM_RIGHT);


                        vBox.getChildren().add(removeBox);

                        //choosing row [by clicking it] to delte
                        tableView.setRowFactory(new Callback<TableView, TableRow>() {
                            @Override
                            public TableRow call(TableView tableView) {
                                final TableRow<TableValues> row = new TableRow();
                                final ContextMenu contextMenu = new ContextMenu();
                                final MenuItem removeMenuItem = new MenuItem("Remove");

                                ObservableList selectedCells = tableView.getSelectionModel().getSelectedCells();

                                final String[] removeVal = {""};
                                selectedCells.addListener(new ListChangeListener() {
                                    @Override
                                    public void onChanged(Change change) {
                                        TablePosition tablePos = (TablePosition) selectedCells.get(0);
                                        TableValues tbval = (TableValues) tableView.getItems().get(tablePos.getRow());

                                        removeVal[0] = tbval.getVal1();

                                        removeButton.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                System.out.println("Removing " + tbval.getString() + " from the database");
                                                systemStatus.setText("Succesfully removed " + tbval.getString() + " from the database");
                                                tableView.getItems().remove(tablePos.getRow());
                                                dataBaseManager.delete_reader(Integer.parseInt(tbval.getVal1()));
                                            }
                                        });

                                    }
                                });

                                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        TableValues obj = row.getItem();
                                        System.out.println("row = " + obj.getVal1());
                                        tableView.getItems().remove(row.getItem());
                                        systemStatus.setText("Succesfully removed " + obj.getString() + " from the database");
                                        dataBaseManager.delete_reader(Integer.parseInt(obj.getVal1()));
                                    }
                                });

                                contextMenu.getItems().add(removeMenuItem);
                                row.contextMenuProperty().bind(
                                        Bindings.when(row.emptyProperty())
                                                .then((ContextMenu)null)
                                                .otherwise(contextMenu)
                                );

                                return  row;
                            }

                        });

                        Object val = tableView.getSelectionModel().getSelectedItem();
                    }

                    else if (list.size() == 1){
                        systemStatus.setText("Succesfully removed " + nameTxt.getText() + " " + surnameTxt.getText() + " from the database");
                        dataBaseManager.delete_reader(nameTxt.getText(), surnameTxt.getText());
                    }

                    else {
                        systemStatus.setText("There's no such reader in the database");
                        systemStatus.setTextFill(Color.FIREBRICK);

                        System.out.println("There's no such reader in the database");
                    }

                }});

        }

        else if(pressedButtonId.equals("Delete Book"))
        {
            deleteTxt.setText("Please fill in the from in order to delete book.");
            System.out.println("del book scene");

            //creating from
            Label title = new Label("Book title:");
            Label systemStatus = new Label("");
            TextField titleTxt = new TextField();

            grid.add(title,0,1);
            grid.add(titleTxt,1,1);
            grid.add(systemStatus,0,2,1,2);

            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("Deleting '" + titleTxt.getText() + "' from the database");

                    List<LinkedHashMap<String, Object>> list = dataBaseManager.findBook(titleTxt.getText());
                    deleteButton.setText("Search another book");
                    systemStatus.setText("");
                    systemStatus.setTextFill(Color.GREEN);

                    if (list.size() > 1) {
                        TableView tableView = resultListToTableView(list);

                        VBox vBox = new VBox(tableView);
                        grid.add(vBox, 0, 4,2,1);

                        HBox removeBox = new HBox();
                        Button removeButton = new Button("Remove");
                        removeBox.getChildren().add(removeButton);
                        removeBox.setAlignment(Pos.BOTTOM_RIGHT);

                        vBox.getChildren().add(removeBox);

                        //choosing whole row
                        tableView.setRowFactory(new Callback<TableView, TableRow>() {
                            @Override
                            public TableRow call(TableView tableView) {
                                final TableRow<TableValues> row = new TableRow();
                                final ContextMenu contextMenu = new ContextMenu();
                                final MenuItem removeMenuItem = new MenuItem("Remove");

                                ObservableList selectedCells = tableView.getSelectionModel().getSelectedCells();

                                final String[] removeVal = {""};
                                selectedCells.addListener(new ListChangeListener() {
                                    @Override
                                    public void onChanged(Change change) {
                                        TablePosition tablePos = (TablePosition) selectedCells.get(0);
                                        TableValues tbval = (TableValues) tableView.getItems().get(tablePos.getRow());

                                        removeVal[0] = tbval.getVal1();

                                        removeButton.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                System.out.println("Removing " + tbval.getString() + " from the database");
                                                systemStatus.setText("Succesfully removed " + tbval.getString() + " from the database");
                                                tableView.getItems().remove(tablePos.getRow());
                                                dataBaseManager.delete_book(Integer.parseInt(tbval.getVal1()));
                                            }
                                        });

                                    }
                                });

                                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        TableValues obj = row.getItem();
                                        System.out.println("row = " + obj.getVal1());
                                        tableView.getItems().remove(row.getItem());
                                        systemStatus.setText("Succesfully removed " + obj.getString() + " from the database");
                                        dataBaseManager.delete_book(Integer.parseInt(obj.getVal1()));
                                    }
                                });

                                contextMenu.getItems().add(removeMenuItem);
                                row.contextMenuProperty().bind(
                                        Bindings.when(row.emptyProperty())
                                                .then((ContextMenu)null)
                                                .otherwise(contextMenu)
                                );

                                return  row;
                            }

                        });

                        Object val = tableView.getSelectionModel().getSelectedItem();
                    }

                    else if (list.size() == 1){
                        systemStatus.setText("Succesfully removed " + titleTxt.getText() + " from the database");
                        dataBaseManager.delete_book(titleTxt.getText());
                    }

                    else {
                        systemStatus.setText("There's no such book in the database");
                        systemStatus.setTextFill(Color.FIREBRICK);

                        System.out.println("There's no such book in the database");
                    }

                }});

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

    private TableView resultListToTableView(List<LinkedHashMap<String,Object>> list){
        TableView tableView = new TableView();
        tableView.setPlaceholder(new Label("No rows to display"));

        Vector<TableColumn<String,Object>> columns = new Vector<TableColumn<String, Object>>();
        if(list.size() > 0)
            {
            //creating columns
            int i = 1;
            for(Map.Entry<String, Object> entry : list.get(0).entrySet()){
                columns.add(new TableColumn<String, Object>(entry.getKey()));
                columns.lastElement().setCellValueFactory(new PropertyValueFactory<>("val" + i));
                i++;
            }

            tableView.getColumns().addAll(columns);

            //inserting rows of data
            for (Map<String, Object> map : list){
                ArrayList<Object> row = new ArrayList<Object>();
                for (Map.Entry<String, Object> entry : map.entrySet()){

                    row.add(entry.getValue());

                    System.out.println("[" + entry.getKey() + ", " + entry.getValue() + "]");
                }

                tableView.getItems().add(new TableValues(row));
            }
        }

        return tableView;
    }

    private void lendScene (Stage stage){
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");

        Label readerName = new Label("Reader name:");
        Label readerSurname = new Label("Reader surname:");
        Label bookTitle = new Label("Book title:");

        TextField readerNameTxt = new TextField();
        TextField readerSurnameTxt = new TextField();
        TextField bookTitleTxt = new TextField();

        Button cancelButton = new Button("Cancel");
        Button lendButton = new Button("Lend");

        grid.addRow(0, readerName, readerNameTxt);
        grid.addRow(1, readerSurname, readerSurnameTxt);
        grid.addRow(2, bookTitle, bookTitleTxt);

        HBox cancelBox = new HBox();
        cancelBox.setAlignment(Pos.BOTTOM_LEFT);
        cancelBox.getChildren().add(cancelButton);

        HBox lendBox = new HBox();
        lendBox.setAlignment(Pos.BOTTOM_RIGHT);
        lendBox.getChildren().add(lendButton);

        grid.addRow(3, cancelBox, lendBox);

        // should I validate those txt fields?

        //-----------------------------------

        lendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lendSceneChoice(stage, readerNameTxt.getText(), readerSurnameTxt.getText(), bookTitleTxt.getText());
            }
        });

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

    private void lendSceneChoice (Stage stage, String name, String surname, String title){
        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");

        Button cancelButton = new Button("Cancel");
        Button lendButton = new Button("Lend");

        HBox cancelBox = new HBox();
        cancelBox.setAlignment(Pos.BOTTOM_LEFT);
        cancelBox.getChildren().add(cancelButton);

        HBox lendBox = new HBox();
        lendBox.setAlignment(Pos.BOTTOM_RIGHT);
        lendBox.getChildren().add(lendButton);

        grid.addRow(5, cancelBox, lendBox);

        //check whether reader and book exist or not
        List <LinkedHashMap<String, Object>> peopleList = dataBaseManager.findPerson(name, surname);
        List <LinkedHashMap<String, Object>> booksList = dataBaseManager.findBook(title);
        final String[] borrowValBooks = {""};
        final String[] borrowValPeople = {""};
        final String[] bookRowToDelete = {""};

        TableView personView = resultListToTableView(peopleList);
        TableView booksView = resultListToTableView(booksList);

        if((peopleList.size() > 1 || booksList.size() > 1) && (peopleList.size() != 0 && booksList.size() != 0)){
            if (peopleList.size() > 1) {
                VBox personBox = new VBox();
                personBox.getChildren().add(personView);
                grid.add(personBox,0,0,2,1);
            }

            else{
                Label personLabel = new Label("Lending book to: " + capitalizeString(name) + " " + capitalizeString(surname) +".");
                grid.add(personLabel,0,0,2,1);

                TableValues tbval = (TableValues) personView.getItems().get(0);
                borrowValPeople[0] = tbval.getVal1();

                System.out.println(borrowValPeople[0]);
            }

            if (booksList.size() > 1) {
                VBox booksBox = new VBox();
                booksBox.getChildren().add(booksView);
                grid.add(booksBox,0,2,2,1);
            }

            else {
                Label bookLabel = new Label("Lending: " + capitalizeString(title) + ".");
                grid.add(bookLabel, 0, 2, 2, 1);

                TableValues tbval = (TableValues) booksView.getItems().get(0);
                borrowValBooks[0] = tbval.getVal1();

                System.out.println(borrowValBooks[0]);
            }

            ObservableList selectedCellsPeople = personView.getSelectionModel().getSelectedCells();
            ObservableList selectedCellsBooks = booksView.getSelectionModel().getSelectedCells();

            selectedCellsPeople.addListener(new ListChangeListener() {
                @Override
                public void onChanged(Change change) {
                    TablePosition tablePos = (TablePosition) selectedCellsPeople.get(0);
                    TableValues tbval = (TableValues) personView.getItems().get(tablePos.getRow());

                    borrowValPeople[0] = tbval.getVal1();

                    System.out.println("Choose " + tbval.getString() + " from the database");
                }
            });

            selectedCellsBooks.addListener(new ListChangeListener() {
                @Override
                public void onChanged(Change change) {
                    TablePosition tablePos = (TablePosition) selectedCellsBooks.get(0);
                    TableValues tbval = (TableValues) booksView.getItems().get(tablePos.getRow());

                    borrowValBooks[0] = tbval.getVal1();
                    bookRowToDelete[0] = String.valueOf(tablePos.getRow());

                    System.out.println("Choose " + tbval.getString() + " id =" + bookRowToDelete[0] + " from the database");
                }
            });
        }

//        else if (peopleList.size() == 1 && booksList.size() > 1){
//            // ADD LABEL SHOWING PERSON YOU GONNA LEND BOOK TO
//            System.out.println("one person multiple books");
//
//
//        }

        else if (peopleList.size() == 1 && booksList.size() == 1){
            //LEND THE BOOK
            System.out.println("one person one book [readerID:" + peopleList.get(0).get("ReaderID") + ", bookID:" + booksList.get(0).get("BookID") + "]");

            //probably very unwise way to parse Object to int...
            dataBaseManager.borrowBook(Integer.parseInt(peopleList.get(0).get("ReaderID").toString()), Integer.parseInt(booksList.get(0).get("BookID").toString()));
            //--------------------------------------------------
        }

        else{
            //THERE WAS NO MATCHING PERSON OR BOOK
            System.out.println("no matches");
            if(peopleList.size() == 0) {
                Label labelNoPerson = new Label("No such person in the database");
                labelNoPerson.setTextFill(Color.FIREBRICK);
                grid.add(labelNoPerson,0,0,2,1);
            }

            if (booksList.size() == 0){
                Label labelNoBook = new Label("No such book in the database");
                labelNoBook.setTextFill(Color.FIREBRICK);
                grid.add(labelNoBook, 0,2,2,1);
            }

            cancelButton.setText("Back");
            lendButton.setVisible(false);
        }

        lendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Lending " + title + " to " + name + " " + surname +".");

                System.out.println(borrowValBooks[0] + " " + borrowValPeople[0]);

                dataBaseManager.borrowBook(Integer.parseInt(borrowValPeople[0]), Integer.parseInt(borrowValBooks[0]));

                if(booksList.size() > 1)
                    booksView.getItems().remove(Integer.parseInt(bookRowToDelete[0]));

//                lendScene(stage);
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lendScene(stage);
            }
        });

        Scene scene = new Scene(grid);
        this.addStyleSheet(scene);
        stage.setScene(scene);

    }

    private void debugScene (Stage stage){
        List<LinkedHashMap<String,Object>> readersList= dataBaseManager.findPerson("Test", "Test");
        System.out.println(readersList);
        TableView tableView = resultListToTableView(readersList);
        Button btn = new Button("Check");
        // it lets you choose one cell
                        tableView.getSelectionModel().setCellSelectionEnabled(true);
                        ObservableList selectedCells = tableView.getSelectionModel().getSelectedCells();
                        selectedCells.addListener(new ListChangeListener() {
                            @Override
                            public void onChanged(Change change) {
                                TablePosition tablePos = (TablePosition) selectedCells.get(0);
                                Object valx = tablePos.getTableColumn().getCellData(tablePos.getRow());
                                Object valy = tablePos.getRow();
                                TableValues tbval = (TableValues) tableView.getItems().get(tablePos.getRow());
                                System.out.println("selected = " + valx + " " + valy);
                                System.out.println("Object = " + tbval.getString());

                                System.out.println("row nbr = " + tablePos.getRow());
                            }
                        });

//        ObservableList selectedCells = tableView.getSelectionModel().;
//        TableCell cell = new TableCell();
//        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
//                if(tableView.getSelectionModel().getSelectedItem() != null){
//                    Object xd = tableView.getItems().get(cell.getIndex());
//                    TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
//                    ObservableList selectedCells = selectionModel.getSelectedCells();
//                    Object[] arr = selectedCells.toArray();
//                    TablePosition tablePos = (TablePosition) selectedCells.get(0);
////                    ObservableValue valx = tablePos.get;
//
//                    System.out.println("HALO: " + selectedCells);
//                    System.out.println("kurwa " + xd);
//                }
//            }
//        });

        //choosing whole row
        tableView.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView tableView) {
                final TableRow<TableValues> row = new TableRow();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem("Remove");

                tableView.getSelectionModel().setCellSelectionEnabled(true);

                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        TableValues obj = row.getItem();
                        System.out.println("row = " + obj.getVal1());
                        tableView.getItems().remove(row.getItem());
                    }
                });

                contextMenu.getItems().add(removeMenuItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu)
                );


                return  row;
            }


        });


        VBox vBox = new VBox(tableView);
        vBox.getChildren().add(btn);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
    }

}
