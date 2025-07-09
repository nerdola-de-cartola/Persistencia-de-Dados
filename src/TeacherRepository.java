import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class TeacherRepository {
    private static Dao<Teacher, Integer> dao;
    private List<Teacher> loadedTeachers;
    private Teacher loadedTeacher;

    public TeacherRepository(Database database) {
        TeacherRepository.setDatabase(database);
        loadedTeachers = new ArrayList<>();
    }

    public static void setDatabase(Database database) {
        try {
            dao = DaoManager.createDao(database.getConnection(), Teacher.class);
            TableUtils.createTableIfNotExists(database.getConnection(), Teacher.class);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public Teacher create(Teacher teacher) {
        try {
            int rows = dao.create(teacher);
            if (rows == 0) {
                throw new SQLException("Error: object not saved");
            }
            this.loadedTeacher = teacher;
            this.loadedTeachers.add(teacher);
        } catch (SQLException e) {
            System.out.println("Create error: " + e.getMessage());
        }
        return teacher;
    }

    public void update(Teacher teacher) {
        try {
            dao.update(teacher);
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
        }
    }

    public void delete(Teacher teacher) {
        try {
            dao.delete(teacher);
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
        }
    }

    public Teacher loadFromId(int id) {
        try {
            this.loadedTeacher = dao.queryForId(id);
            if (this.loadedTeacher != null) {
                this.loadedTeachers.add(this.loadedTeacher);
            }
        } catch (SQLException e) {
            System.out.println("Load from ID error: " + e.getMessage());
        }
        return this.loadedTeacher;
    }

    public List<Teacher> loadAll() {
        try {
            this.loadedTeachers = dao.queryForAll();
            if (!this.loadedTeachers.isEmpty()) {
                this.loadedTeacher = this.loadedTeachers.get(0);
            }
        } catch (SQLException e) {
            System.out.println("Load all error: " + e.getMessage());
        }
        return this.loadedTeachers;
    }

    // Getters and setters can be added here if needed

    public static void main(String[] args) {
        // Inicializa o banco de dados
        Database db = new Database("test.db");
        TeacherRepository repo = new TeacherRepository(db);

        System.out.println("=== CREATE ===");
        Teacher teacher = new Teacher("João da Silva", "Física", 12);
        repo.create(teacher);
        System.out.println("Criado: " + teacher);

        System.out.println("\n=== READ (by ID) ===");
        Teacher loaded = repo.loadFromId(teacher.getId());
        if (loaded != null) {
            System.out.println("Carregado por ID: " + loaded);
        } else {
            System.out.println("Professor não encontrado.");
        }

        System.out.println("\n=== UPDATE ===");
        loaded.setYearsOfExperience(15);
        repo.update(loaded);
        Teacher updated = repo.loadFromId(loaded.getId());
        System.out.println("Atualizado: " + updated);

        System.out.println("\n=== READ ALL ===");
        List<Teacher> allTeachers = repo.loadAll();
        for (Teacher t : allTeachers) {
            System.out.println(t);
        }

        System.out.println("\n=== DELETE ===");
        repo.delete(updated);
        Teacher afterDelete = repo.loadFromId(updated.getId());
        if (afterDelete == null) {
            System.out.println("Professor removido com sucesso.");
        } else {
            System.out.println("Falha ao remover professor.");
        }

        System.out.println("\n=== FINAL LIST ===");
        List<Teacher> finalList = repo.loadAll();
        for (Teacher t : finalList) {
            System.out.println(t);
        }
    }

}
