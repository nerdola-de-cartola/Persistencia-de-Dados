package model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer<T> {
    private final Gson gson;
    private final Class<T> type;

    public JsonSerializer(Class<T> type) {
        this.type = type;
        this.gson = new GsonBuilder()
                       .setDateFormat("yyyy-MM-dd")
                       .setPrettyPrinting()
                       .create();
    }

    public String toJson(T obj) {
        return gson.toJson(obj);
    }

    public T fromJson(String json) {
        return gson.fromJson(json, type);
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setId(1);
        student.setFullName("Maria Souza");
        student.setRegistration(123);
        student.setBirthday(new java.util.Date());

        JsonSerializer<Student> serializer = new JsonSerializer<>(Student.class);
        String json = serializer.toJson(student);
        System.out.println("JSON gerado:\n" + json);

        // Desserializar
        Student loaded = serializer.fromJson(json);
        System.out.println("Nome do estudante desserializado: " + loaded.getFullName());
    }
}
