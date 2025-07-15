package model;

public class Repositorios {
    public static Database database = new Database("app.sqlite");
    public static final Repositorio<Student, Integer> ESTUDANTE =
        new Repositorio<>(database, Student.class);
    public static final Repositorio<Teacher, Integer> PROFESSOR =
        new Repositorio<>(database, Teacher.class);
    public static final Repositorio<Matricula, Integer> MATRICULA =
        new Repositorio<>(database, Matricula.class);        
    public static final Repositorio<Disciplina, Integer> DISCIPLINA =
        new Repositorio<>(database, Disciplina.class);   
    public static final TurmaRepositorio TURMA =
        new TurmaRepositorio(database, Turma.class);
}