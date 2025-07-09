package teacher;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;

import controller.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import java.util.ArrayList;

public class TeacherRepository {
    private static Dao<Teacher, Integer> dao;
    private List<Teacher> loadedTeachers;
    private Teacher loadedTeacher;
    private TeacherJsonSerializer jsonSerializer;
    private TeacherXmlSerializer xmSerializer;

    public TeacherRepository(Database database) {
        TeacherRepository.setDatabase(database);
        loadedTeachers = new ArrayList<>();
        jsonSerializer = new TeacherJsonSerializer();
        try {
            xmSerializer = new TeacherXmlSerializer();
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public String dumpData(String formato) {
        List<Teacher> teachers = loadAll();

        try {
            if (formato.equalsIgnoreCase("json")) {
                return jsonSerializer.toJson(teachers);
            } else if (formato.equalsIgnoreCase("xml")) {
                return xmSerializer.toXml(teachers);
            }
        } catch (Exception e) {
            System.err.println("Erro ao exportar dados: " + e.getMessage());
        }

        return "";
    }

    // Salva os dados serializados em um arquivo
    public boolean dumpFile(String formato, File arquivo) {
        String data = dumpData(formato);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write(data);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
            return false;
        }
    }

    // Cria um professor a partir de um JSON
    public Teacher createFromJSON(String json) {
        try {
            Teacher teacher = jsonSerializer.fromJson(json)[0];
            return create(teacher);
        } catch (Exception e) {
            System.err.println("Erro ao criar a partir do JSON: " + e.getMessage());
            return null;
        }
    }

    // Cria um professor a partir de um XML
    public Teacher createFromXML(String xml) {
        try {
            Teacher teacher = xmSerializer.fromXml(xml)[0];
            return create(teacher);
        } catch (Exception e) {
            System.err.println("Erro ao criar a partir do XML: " + e.getMessage());
            return null;
        }
    }

    // Importa vários professores de uma string no formato JSON ou XML
    public int importData(String formato, String data) {
        int count = 0;

        try {
            if (formato.equalsIgnoreCase("json")) {
                Teacher[] teachers = jsonSerializer.fromJson(data);
                for (Teacher t : teachers) {
                    create(t);
                    count++;
                }
            } else if (formato.equalsIgnoreCase("xml")) {
                Teacher[] teachers = xmSerializer.fromXml(data);
                for (Teacher t : teachers) {
                    create(t);
                    count++;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao importar dados: " + e.getMessage());
        }

        return count;
    }

    // Importa professores a partir de um arquivo serializado
    public int importFile(String formato, File arquivo) {
        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            return importData(formato, sb.toString());
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return 0;
        }
    }

    public static void testCrud(String[] args) {
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

    public static void main(String[] args) throws Exception {
        Database db = new Database("test.db");
        TeacherRepository repo = new TeacherRepository(db);

        // Criação inicial
        Teacher t1 = new Teacher("Ana Paula", "História", 11);
        Teacher t2 = new Teacher("Carlos Mendes", "Geografia", 9);
        repo.create(t1);
        repo.create(t2);

        // Exportar para JSON
        String json = repo.dumpData("json");
        System.out.println("JSON:\n" + json);

        // Importar do JSON
        int count = repo.importData("json", json);
        System.out.println("Importados do JSON: " + count);

        // Exportar para arquivo XML
        File xmlFile = new File("teachers.xml");
        repo.dumpFile("xml", xmlFile);

        // Importar de arquivo XML
        int xmlCount = repo.importFile("xml", xmlFile);
        System.out.println("Importados do XML: " + xmlCount);
    }
}
