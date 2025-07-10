package view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Teacher {    
    private SimpleIntegerProperty id;
    private SimpleStringProperty nomeCompleto;
    // private SimpleStringProperty dataDeNascimento;
    private SimpleStringProperty disciplina;
    private SimpleIntegerProperty anosDeExperiencia;
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Teacher(int id, String nomeCompleto, String disciplina, int anosDeExperiencia) {
        this.id = new SimpleIntegerProperty(id);
        this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
        // this.dataDeNascimento = new SimpleStringProperty(sdf.format(dataDeNascimento));
        this.disciplina = new SimpleStringProperty(disciplina);
        this.anosDeExperiencia = new SimpleIntegerProperty(anosDeExperiencia);        
    }
    
    public int getId() {
        return this.id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNomeCompleto() {
        return this.nomeCompleto.get();
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto.set(nomeCompleto);
    }

    // public String getDataDeNascimento() {
    //     return this.dataDeNascimento.get();
    // }

    // public void setDataDeNascimento(String dataDeNascimento) {
    //     this.dataDeNascimento.set(dataDeNascimento);
    // }

    public String getDisciplina() {
        return this.disciplina.get();
    }

    public void setDisciplina(String disciplina) {
        this.disciplina.set(disciplina);
    }

    public int getAnosDeExperiencia() {
        return this.anosDeExperiencia.get();
    }

    public void setAnosDeExperiencia(int anos) {
        this.anosDeExperiencia.set(anos);
    }
}
