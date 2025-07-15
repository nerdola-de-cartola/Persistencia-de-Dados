package model;

import java.sql.SQLException;
import java.util.List;

public class TurmaRepositorio extends Repositorio<model.Turma, Integer> {
    private static final Repositorio<model.Matricula, Integer> matriculaRepo = model.Repositorios.MATRICULA;


    public TurmaRepositorio(Database database, Class<Turma> entityClass) {
        super(database, entityClass);
    }

    public Turma loadFromId(Integer id) {
        try {
            this.loadedEntity = getDao().queryForId(id);
            this.loadedEntity.setAlunosMatriculados(contarAlunosMatriculadosNaTurma(loadedEntity));
            if (this.loadedEntity != null)
                this.loadedEntities.add(this.loadedEntity);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedEntity;
    }

    public List<Turma> loadAll() {
        try {
            this.loadedEntities = getDao().queryForAll();

            for (model.Turma turma : loadedEntities) {
                turma.setAlunosMatriculados(contarAlunosMatriculadosNaTurma(turma));
            }

            if (!this.loadedEntities.isEmpty())
                this.loadedEntity = this.loadedEntities.get(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return this.loadedEntities;
    }


    public int contarAlunosMatriculadosNaTurma(Turma turma) throws SQLException {
        return (int) matriculaRepo.getDao()
            .queryBuilder()
            .where()
            .eq("turma_id", turma.getId())
            .countOf();
    }
}
