package model;

import java.sql.SQLException;

public class TurmaRepositorio extends Repositorio<model.Turma, Integer> {
    private static final Repositorio<model.Matricula, Integer> matriculaRepo = model.Repositorios.MATRICULA;


    public TurmaRepositorio(Database database, Class<Turma> entityClass) {
        super(database, entityClass);
    }

    public int contarAlunosMatriculadosNaTurma(Turma turma) throws SQLException {
        return (int) matriculaRepo.getDao()
            .queryBuilder()
            .where()
            .eq("turma_id", turma.getId())
            .countOf();
    }
}
