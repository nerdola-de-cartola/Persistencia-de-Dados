package br.ufg.inf.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "estudante")
public class Estudante {

    @Id
    private String id;
    private String nomeCompleto;
    private String dataDeNascimento;
    private int matricula;

    public Estudante() {}

    public Estudante(String nomeCompleto, String dataDeNascimento, int matricula) {
        this.nomeCompleto = nomeCompleto;
        this.dataDeNascimento = dataDeNascimento;
        this.matricula = matricula;
    }

    public String getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
}