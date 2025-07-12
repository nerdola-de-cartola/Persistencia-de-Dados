package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import model.Repositorio;
import model.TurmaRepositorio;
import model.Disciplina;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import view.Turma;

import java.util.List;

public class TurmaController extends AbstractCrudController<model.Turma, Turma, Integer> implements Initializable {

    @FXML
    private TableView<Turma> tabela;

    @FXML
    private TableColumn<Turma, Integer> idCol;
    @FXML
    private TableColumn<Turma, String> codigoCol;
    @FXML
    private TableColumn<Turma, String> disciplinaCol;
    @FXML
    private TableColumn<Turma, String> teacherCol;
    @FXML
    private TableColumn<Turma, Integer> alunosMatriculadosCol;
    @FXML
    private TableColumn<Turma, Integer> vagasDisponiveisCol;

    @FXML
    private TextField idField;
    @FXML
    private TextField codigoField;
    @FXML
    private ComboBox<Disciplina> disciplinaComboBox;
    @FXML
    private ComboBox<model.Teacher> teacherComboBox;
    @FXML
    private Label alunosMatriculadosLabel;
    @FXML
    private TextField vagasDisponiveisField;

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

    private static final TurmaRepositorio turmaRepo = model.Repositorios.TURMA;
    private static final Repositorio<model.Disciplina, Integer> disciplinaRepo = model.Repositorios.DISCIPLINA;
    private static final Repositorio<model.Teacher, Integer> teacherRepo = model.Repositorios.PROFESSOR;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar colunas
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        codigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        disciplinaCol.setCellValueFactory(new PropertyValueFactory<>("disciplinaCodigo"));
        teacherCol.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        alunosMatriculadosCol.setCellValueFactory(new PropertyValueFactory<>("alunosMatriculados"));
        vagasDisponiveisCol.setCellValueFactory(new PropertyValueFactory<>("vagasDisponiveis"));

        // Carregar disciplinas no ComboBox
        List<Disciplina> disciplinas = disciplinaRepo.loadAll();
        
        disciplinaComboBox.setItems(FXCollections.observableArrayList(disciplinas));
        disciplinaComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Disciplina d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? "" : d.getCodigo());
            }
        });
        disciplinaComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Disciplina d, boolean empty) {
                super.updateItem(d, empty);
                setText(empty || d == null ? "" : d.getCodigo());
            }
        });

        List<model.Teacher> teachers = teacherRepo.loadAll();
        teacherComboBox.setItems(FXCollections.observableArrayList(teachers));
        teacherComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(model.Teacher t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? "" : t.getName());
            }
        });
        teacherComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(model.Teacher t, boolean empty) {
                super.updateItem(t, empty);
                setText(empty || t == null ? "" : t.getName());
            }
        });

        super.initialize();
    }

    @Override
    protected Repositorio<model.Turma, Integer> getRepositorio() {
        return turmaRepo;
    }

    @Override
    protected Turma modelToView(model.Turma t) {
        return new Turma(
            t.getId(),
            t.getCodigo(),
            t.getDisciplina() != null ? t.getDisciplina().getId() : 0,
            t.getDisciplina() != null ? t.getDisciplina().getCodigo() : "",
            t.getVagasDisponiveis(),
            t.getAlunosMatriculados(),
            t.getTeacher().getName()
        );
    }

    @Override
    protected model.Turma viewToModel() {
        model.Turma t = new model.Turma();
        t.setCodigo(codigoField.getText());
        t.setDisciplina(disciplinaComboBox.getValue());
        t.setTeacher(teacherComboBox.getValue());

        // Parsing seguro
        String vagasText = vagasDisponiveisField.getText();
        int vagas = 0;
        if (vagasText != null && !vagasText.isBlank()) {
            try {
                vagas = Integer.parseInt(vagasText);
            } catch (NumberFormatException ex) {
                vagas = 0;
            }
        }
        t.setVagasDisponiveis(vagas);

        //alunosMatriculados é derivado
        try {
            System.out.println("oi");
            int a = turmaRepo.contarAlunosMatriculadosNaTurma(t);
            System.out.println("tchal");
            t.setAlunosMatriculados(a);
        } catch (SQLException e) {
            System.out.println(e);
            return t;
        }

        return t;
    }

    // @Override
    protected void preencherCampos(Turma turma) {
        idField.setText(String.valueOf(turma.getId()));
        codigoField.setText(turma.getCodigo());
        alunosMatriculadosLabel.setText(String.valueOf(turma.getAlunosMatriculados()));
        vagasDisponiveisField.setText(String.valueOf(turma.getVagasDisponiveis()));

        // Selecionar disciplina por ID
        Disciplina d = disciplinaRepo.loadFromId(turma.getDisciplinaId());
        disciplinaComboBox.setValue(d);

        List<model.Teacher> teachers = teacherRepo.loadAll();
        for (model.Teacher t : teachers) {
            if (t.getName().equals(turma.getTeacherName())) {
                teacherComboBox.setValue(t);
                break;
            }
        }
    }

    @Override
    protected void limparCampos() {
        idField.clear();
        codigoField.clear();
        alunosMatriculadosLabel.setText("");
        vagasDisponiveisField.clear();
        disciplinaComboBox.setValue(null);
        teacherComboBox.setValue(null);
    }

    @Override
    protected void desabilitarCampos(boolean desabilitado) {
        codigoField.setDisable(desabilitado);
        disciplinaComboBox.setDisable(desabilitado);
        teacherComboBox.setDisable(desabilitado);
        vagasDisponiveisField.setDisable(desabilitado);
        // Label alunosMatriculados é só leitura
    }

    @Override
    protected void desabilitarBotoes(boolean adicionar, boolean atualizar, boolean deletar, boolean cancelar, boolean salvar) {
        adicionarButton.setDisable(adicionar);
        atualizarButton.setDisable(atualizar);
        deletarButton.setDisable(deletar);
        cancelarButton.setDisable(cancelar);
        salvarButton.setDisable(salvar);
    }

    @Override
    protected TableView<Turma> getTabela() {
        return tabela;
    }

    @Override
    protected Integer getIdFromViewModel(Turma viewModel) {
        return viewModel.getId();
    }

    @Override
    protected void setIdOnEntity(model.Turma entidade, Integer id) {
        entidade.setId(id);
    }

    @FXML
    public void onAdicionar() {
        super.onAdicionar();
    }

    @FXML
    public void onSalvar() {
        super.onSalvar();
    }

    @FXML
    public void onAtualizar() {
        super.onAtualizar();
    }

    @FXML
    public void onDeletar() {
        super.onDeletar();
    }

    @FXML
    public void onCancelar() {
        super.onCancelar();
    }
}