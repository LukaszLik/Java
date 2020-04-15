package DBMS;

import javafx.application.Application;
import javafx.stage.Stage;

public class DBManager extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //login scene
        Controller controller = new Controller();

        stage.setTitle("JavaFX tester");

        stage.setX(300);
        stage.setY(150);

        stage.setWidth(1024);
        stage.setHeight(768);

        controller.setManagerScene(stage);

        stage.show();
    }

    public static void main (String[] args)
    {
        Application.launch(args);
    }

}
