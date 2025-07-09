package student;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StudentJsonSerializer
{
    private Gson gson;
    
    public StudentJsonSerializer() {
        // Configurando para formatar datas e pretty printing
        gson = new GsonBuilder()
                   .setDateFormat("yyyy-MM-dd")
                   .setPrettyPrinting()
                   .create();
    }
    
    public String toJson(Student student) {
        return gson.toJson(student);
    }
    
    public Student fromJson(String json) {
        return gson.fromJson(json, Student.class);
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setId(1);
        student.setFullName("Maria Souza");
        student.setRegistration(123);
        student.setBirthday(new java.util.Date());
        
        StudentJsonSerializer serializer = new StudentJsonSerializer();
        String json = serializer.toJson(student);
        System.out.println("JSON gerado:\n" + json);
        
        // Desserializar
        Student loaded = serializer.fromJson(json);
        System.out.println("Nome do estudante desserializado: " + loaded.getFullName());
    }
}