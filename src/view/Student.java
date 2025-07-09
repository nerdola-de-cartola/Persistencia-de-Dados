package view;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {    
    private SimpleIntegerProperty id;
    private SimpleStringProperty nomeCompleto;
    private SimpleStringProperty dataDeNascimento;
    private SimpleIntegerProperty matricula;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    public Student(int id, String nomeCompleto, Date dataDeNascimento, int matricula) {
        this.id = new SimpleIntegerProperty(id);
        this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
        this.dataDeNascimento = new SimpleStringProperty(sdf.format(dataDeNascimento));
        this.matricula = new SimpleIntegerProperty(matricula);        
    }
    
    public int getId(){
        return this.id.get();
    }

    public void setId(int id){
        this.id.set(id);
    }

    public String getNomeCompleto(){
        return this.nomeCompleto.get();
    }

    public void setNomeCompleto(String nomeCompleto){
        this.nomeCompleto.set(nomeCompleto);
    }

    public String getDataDeNascimento(){
        return this.dataDeNascimento.get();
    }

    public void setDataDeNascimento(String dataDeNascimento){
        this.dataDeNascimento.set(dataDeNascimento);
    }

    public int getMatricula(){
        return this.matricula.get();
    }

    public void setMatricula(int matricula){
        this.matricula.set(matricula);
    }
}