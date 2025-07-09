package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.List;

import view.AppView;

public class AppController implements Initializable {
    @FXML
    private TableView<view.Student> tabela;
    @FXML
    private TableColumn<view.Student, Integer> idCol;
    @FXML
    private TableColumn<view.Student, String> nomeCompletoCol;
    @FXML
    private TableColumn<view.Student, String> dataDeNascimentoCol;
    @FXML
    private TableColumn<view.Student, Integer> matriculaCol;
    @FXML
    private TextField idField;
    @FXML
    private TextField nomeCompletoField;
    @FXML
    private TextField dataDeNascimentoField;
    @FXML
    private TextField matriculaField;
    @FXML
    private Button adicionarButton;
    @FXML
    private Button atualizarButton;
    @FXML
    private Button deletarButton;    
    @FXML
    private Button cancelarButton;    
    @FXML
    private Button salvarButton;
    
    AppView appView;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private static controller.Database database = new controller.Database("app.sqlite");
    private static student.StudentRepository StudentRepo = 
        new student.StudentRepository(database);
        
    public AppController() {
        this.appView = new AppView();
    }
    
    private void desabilitarBotoes(boolean adicionar, boolean atualizar, boolean deletar, boolean cancelar, boolean salvar) {
        adicionarButton.setDisable(adicionar);
        atualizarButton.setDisable(atualizar);
        deletarButton.setDisable(deletar);
        cancelarButton.setDisable(cancelar);
        salvarButton.setDisable(salvar);        
    }
    
    private void desabilitarCampos(boolean desabilitado) {
        nomeCompletoField.setDisable(desabilitado);
        matriculaField.setDisable(desabilitado);
        dataDeNascimentoField.setDisable(desabilitado);
    }
    
    private void limparCampos() {
        idField.setText("");
        dataDeNascimentoField.setText("");
        nomeCompletoField.setText("");
        matriculaField.setText("");        
    }
    
    @FXML
    public void onCancelarButtonAction() {
        desabilitarCampos(true);
        desabilitarBotoes(false,true,true,true,true);
        limparCampos();
        tabela.getSelectionModel().select(-1);        
    }
    
    @FXML
    public void onSalvarButtonAction() {
        try {
            student.Student Student = new student.Student();            
            Student.setRegistration(Integer.parseInt(matriculaField.getText()));
            Student.setFullName(nomeCompletoField.getText());
            Student.setBirthday(sdf.parse(dataDeNascimentoField.getText()));            
            student.Student Student_salvo = StudentRepo.create(Student); 
            view.Student StudentView = modelToView(Student_salvo);
            tabela.getItems().add(StudentView);
            tabela.getSelectionModel().select(StudentView);    
            desabilitarCampos(true);
            desabilitarBotoes(false,true,true,true,true);            
        }
        catch(Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar: "+e.getMessage()).show();
        }
    }    
    
    @FXML
    public void onAdicionarButtonAction() {
        tabela.getSelectionModel().select(-1);
        desabilitarCampos(false);
        desabilitarBotoes(true,true,true,false,false);
        limparCampos();
    }

    @FXML
    private void handleStudentSelected(view.Student newSelection) {
        if (newSelection != null)
            idField.setText(Integer.toString(newSelection.getId()));
            dataDeNascimentoField.setText(newSelection.getDataDeNascimento());
            nomeCompletoField.setText(newSelection.getNomeCompleto());
            matriculaField.setText(Integer.toString(newSelection.getMatricula()));
            desabilitarBotoes(false,false,false,true,true);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        nomeCompletoCol.setCellValueFactory(
                new PropertyValueFactory<>("nomeCompleto"));
        dataDeNascimentoCol.setCellValueFactory(
                new PropertyValueFactory<>("dataDeNascimento"));
        matriculaCol.setCellValueFactory(
                new PropertyValueFactory<>("matricula"));
        tabela.setItems(loadAllStudents());
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, oldSelection, newSelection) -> {
                handleStudentSelected(newSelection);
            });
    }
    
    private view.Student modelToView(student.Student Student) {
        return new view.Student(
            Student.getId(),
            Student.getFullName(),
            Student.getBirthday(),
            Student.getRegistration()
        );
    }
    
    private ObservableList<view.Student> loadAllStudents() {
        ObservableList<view.Student> lista = 
            FXCollections.observableArrayList();
        List<student.Student> listaFromDatabase = StudentRepo.loadAll();
        for(student.Student Student: listaFromDatabase) {
            lista.add(modelToView(Student));
        }
        return lista;
    }

    public static void main(String[] args) throws Exception {
        AppController appController = new AppController();
        appController.appView.run(args);
    }
}