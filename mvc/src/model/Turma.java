package model;


import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;

@DatabaseTable(tableName = "turma")
public class Turma {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.STRING)
    private String codigo;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Disciplina disciplina;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Teacher teacher;

    // Este campo é apenas informativo e não deve ser persistido
    @DatabaseField(persisted = false)
    private int alunosMatriculados;

    @DatabaseField(dataType = DataType.INTEGER)
    private int vagasDisponiveis;

    // GET/SET ID
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // GET/SET Código
    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // GET/SET Disciplina
    public Disciplina getDisciplina() {
        return this.disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    // GET/SET Alunos Matriculados
    public int getAlunosMatriculados() {
        return this.alunosMatriculados;
    }

    public void setAlunosMatriculados(int alunosMatriculados) {
        this.alunosMatriculados = alunosMatriculados;
    }

    // GET/SET Vagas Disponíveis
    public int getVagasDisponiveis() {
        return this.vagasDisponiveis;
    }

    public void setVagasDisponiveis(int vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public boolean possoMatricular() {
        return alunosMatriculados < vagasDisponiveis;
    }

    @Override
    public String toString() {
        return "Turma [id=" + id + ", codigo=" + codigo + ", disciplina=" + disciplina + ", teacher=" + teacher
                + ", alunosMatriculados=" + alunosMatriculados + ", vagasDisponiveis=" + vagasDisponiveis + "]";
    }
}