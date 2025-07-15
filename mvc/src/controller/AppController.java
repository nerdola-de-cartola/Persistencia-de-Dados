package controller;

import javafx.fxml.Initializable;
import model.Database;

import java.net.URL;
import java.util.ResourceBundle;
import view.AppView;

public class AppController implements Initializable {    
    AppView appView;

    private static model.Database database = new Database("app.sqlite");
        
    public AppController() {
        this.appView = new AppView();
    }

    public static model.Database getDatabase() {
        return AppController.database;
    }

    public static void main(String[] args) throws Exception {
        AppController appController = new AppController();
        appController.appView.run(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}