package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Repositorio;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.List;

import view.AppView;

public class Student implements Initializable {
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
    view.Student selectedStudent;
    boolean isUpdating = false;
    boolean isCreating = false;

    private static Repositorio<model.Student, Integer> StudentRepo = 
        new Repositorio<model.Student, Integer>(AppController.getDatabase(), model.Student.class);
        
    public Student() {}

    private void clearSelection() {
        tabela.getSelectionModel().clearSelection();
        selectedStudent = null;
    }

    private void updateButtons() {
        if (selectedStudent == null) {
            atualizarButton.setDisable(true);
            deletarButton.setDisable(true);
        } else {
            atualizarButton.setDisable(false);
            deletarButton.setDisable(false);
        }

        if (isCreating || isUpdating) {
            cancelarButton.setDisable(false);
            salvarButton.setDisable(false);    
            adicionarButton.setDisable(true);
        } else {
            cancelarButton.setDisable(true);
            salvarButton.setDisable(true);
            adicionarButton.setDisable(false);
        }
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
        isCreating = false;
        isUpdating = false;
        clearSelection();
        desabilitarCampos(true);
        updateButtons();
        limparCampos();
        // tabela.getSelectionModel().select(-1);        
    }
    
    @FXML
    public void onSalvarButtonAction() {
        try {
            model.Student Student = new model.Student();            
            Student.setRegistration(Integer.parseInt(matriculaField.getText()));
            Student.setFullName(nomeCompletoField.getText());
            Student.setBirthday(sdf.parse(dataDeNascimentoField.getText()));
            view.Student studentView;
            
            if (isUpdating) {
                Student.setId(Integer.parseInt(idField.getText()));
                StudentRepo.update(Student); 
                studentView = modelToView(Student);
                tabela.getItems().set(tabela.getItems().indexOf(selectedStudent), studentView);
            } else {
                model.Student savedStudent = StudentRepo.create(Student); 
                studentView = modelToView(savedStudent);
                tabela.getItems().add(studentView);
            }


            tabela.getSelectionModel().select(studentView);
            selectedStudent = studentView;
            isCreating = false;
            isUpdating = false;
            desabilitarCampos(true);
            updateButtons();            
        }
        catch(Exception e) {
            new Alert(AlertType.ERROR, "Erro ao salvar: "+e.getMessage()).show();
        }
    }    
    
    @FXML
    public void onAdicionarButtonAction() {
        isCreating = true;
        clearSelection();
        desabilitarCampos(false);
        updateButtons();
        limparCampos();
    }

    @FXML
    public void onAtualizarButtonAction() {
        try {
            isUpdating = true;
            desabilitarCampos(false);
            updateButtons();
        }
        catch(Exception e) {
            new Alert(AlertType.ERROR, "Erro ao atualizar: "+e.getMessage()).show();
        }
    }

    @FXML
    public void onDeletarButtonAction() {
        try {
            var student = new model.Student();
            int id = Integer.parseInt(idField.getText());
            student.setId(id);
            //student.setBirthday(sdf.parse(dataDeNascimentoField.getText()));
            //student.setFullName(nomeCompletoField.getText());
            //student.setRegistration(Integer.parseInt(matriculaField.getText()));
            StudentRepo.delete(student);
            tabela.getItems().remove(selectedStudent);
            clearSelection();
            limparCampos();
            updateButtons();
        }
        catch(Exception e) {
            new Alert(AlertType.ERROR, "Erro ao deletar: "+e.getMessage()).show();
        }
    }

    @FXML
    private void handleStudentSelected(view.Student newSelection) {
        selectedStudent = newSelection;

        if (newSelection != null) {
            idField.setText(Integer.toString(newSelection.getId()));
            dataDeNascimentoField.setText(newSelection.getDataDeNascimento());
            nomeCompletoField.setText(newSelection.getNomeCompleto());
            matriculaField.setText(Integer.toString(newSelection.getMatricula()));
            updateButtons();
        }
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
    
    private view.Student modelToView(model.Student Student) {
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
        List<model.Student> listaFromDatabase = StudentRepo.loadAll();
        for(model.Student Student: listaFromDatabase) {
            lista.add(modelToView(Student));
        }
        return lista;
    }
}