package teacher;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TeacherJsonSerializer {
    private Gson gson;

    public TeacherJsonSerializer() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    public String toJson(Teacher teacher) {
        return gson.toJson(teacher);
    }

    public String toJson(List<Teacher> teachers) {
        return gson.toJson(teachers);
    }

    public Teacher[] fromJson(String json) {
        Teacher[] teachers = gson.fromJson(json, Teacher[].class);
        return teachers;
    }   

    public static void main(String[] args) {
        Teacher teacher = new Teacher("Maria Souza", "Biologia", 8);

        TeacherJsonSerializer serializer = new TeacherJsonSerializer();
        String json = serializer.toJson(teacher);
        System.out.println("JSON gerado:\n" + json);

        // Desserializar
        Teacher loaded = serializer.fromJson(json)[0];
        System.out.println("Nome do professor desserializado: " + loaded.getName());
    }
}
