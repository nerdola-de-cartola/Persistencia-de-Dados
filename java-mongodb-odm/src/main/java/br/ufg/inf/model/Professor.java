package br.ufg.inf.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "professor")
public class Professor {

    @Id
    private String id;
    private String nomeCompleto;
    private String dataDeNascimento;
    private String instituicao;

    public Professor() {}

    public Professor(String nomeCompleto, String dataDeNascimento, String instituicao) {
        this.nomeCompleto = nomeCompleto;
        this.dataDeNascimento = dataDeNascimento;
        this.instituicao = instituicao;
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

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }
}
