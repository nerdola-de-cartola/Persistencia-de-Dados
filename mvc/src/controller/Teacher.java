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
import java.util.ResourceBundle;
import java.util.List;

import view.AppView;

public class Teacher implements Initializable {
    @FXML
    private TableView<view.Teacher> tabela;
    @FXML
    private TableColumn<view.Teacher, Integer> idCol;
    @FXML
    private TableColumn<view.Teacher, String> nameCol;
    @FXML
    private TableColumn<view.Teacher, String> subjectCol;
    @FXML
    private TableColumn<view.Teacher, Integer> yearsOfExperienceCol;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextField yearsOfExperienceField;

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
    view.Teacher selectedTeacher;
    boolean isUpdating = false;
    boolean isCreating = false;

    private static Repositorio<model.Teacher, Integer> teacherRepo =
        new Repositorio<model.Teacher, Integer>(AppController.getDatabase(), model.Teacher.class);

    public Teacher() {}

    private void clearSelection() {
        tabela.getSelectionModel().clearSelection();
        selectedTeacher = null;
    }

    private void updateButtons() {
        if (selectedTeacher == null) {
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
        nameField.setDisable(desabilitado);
        subjectField.setDisable(desabilitado);
        yearsOfExperienceField.setDisable(desabilitado);
    }

    private void limparCampos() {
        idField.setText("");
        nameField.setText("");
        subjectField.setText("");
        yearsOfExperienceField.setText("");
    }

    @FXML
    public void onCancelarButtonAction() {
        isCreating = false;
        isUpdating = false;
        clearSelection();
        desabilitarCampos(true);
        updateButtons();
        limparCampos();
    }

    @FXML
    public void onSalvarButtonAction() {
        try {
            model.Teacher teacher = new model.Teacher();
            teacher.setName(nameField.getText());
            teacher.setSubject(subjectField.getText());
            teacher.setYearsOfExperience(Integer.parseInt(yearsOfExperienceField.getText()));

            view.Teacher teacherView;

            if (isUpdating) {
                teacher.setId(Integer.parseInt(idField.getText()));
                teacherRepo.update(teacher);
                teacherView = modelToView(teacher);
                tabela.getItems().set(tabela.getItems().indexOf(selectedTeacher), teacherView);
            } else {
                model.Teacher savedTeacher = teacherRepo.create(teacher);
                teacherView = modelToView(savedTeacher);
                tabela.getItems().add(teacherView);
            }

            tabela.getSelectionModel().select(teacherView);
            selectedTeacher = teacherView;
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
            var teacher = new model.Teacher();
            int id = Integer.parseInt(idField.getText());
            teacher.setId(id);
            teacherRepo.delete(teacher);
            tabela.getItems().remove(selectedTeacher);
            clearSelection();
            limparCampos();
            updateButtons();
        }
        catch(Exception e) {
            new Alert(AlertType.ERROR, "Erro ao deletar: "+e.getMessage()).show();
        }
    }

    @FXML
    private void handleTeacherSelected(view.Teacher newSelection) {
        selectedTeacher = newSelection;

        if (newSelection != null) {
            idField.setText(Integer.toString(newSelection.getId()));
            nameField.setText(newSelection.getNomeCompleto());
            subjectField.setText(newSelection.getDisciplina());
            yearsOfExperienceField.setText(Integer.toString(newSelection.getAnosDeExperiencia()));
            updateButtons();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("disciplina"));
        yearsOfExperienceCol.setCellValueFactory(new PropertyValueFactory<>("anosDeExperiencia"));

        tabela.setItems(loadAllTeachers());
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, oldSelection, newSelection) -> {
                handleTeacherSelected(newSelection);
            });
    }

    private view.Teacher modelToView(model.Teacher teacher) {
        return new view.Teacher(
            teacher.getId(),
            teacher.getName(),
            teacher.getSubject(),
            teacher.getYearsOfExperience()
        );
    }

    private ObservableList<view.Teacher> loadAllTeachers() {
        ObservableList<view.Teacher> lista = FXCollections.observableArrayList();
        List<model.Teacher> listaFromDatabase = teacherRepo.loadAll();
        for (model.Teacher teacher : listaFromDatabase) {
            lista.add(modelToView(teacher));
        }
        return lista;
    }
}
