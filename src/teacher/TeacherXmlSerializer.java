package teacher;

import javax.xml.bind.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;

public class TeacherXmlSerializer {
    private JAXBContext context;

    public TeacherXmlSerializer() throws JAXBException {
        context = JAXBContext.newInstance(Teacher.class);
    }

    public String toXml(Teacher teacher) throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(teacher, sw);
        return sw.toString();
    }

    public String toXml(List<Teacher> teachers) throws JAXBException {
        StringBuilder sb = new StringBuilder();
        for (Teacher t : teachers) {
            sb.append(toXml(t)).append("\n");
        }
        return sb.toString();
    }

    public Teacher[] fromXml(String xml) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        String[] entries = xml.split("(?=<\\?xml)"); // quebra por cada XML individual
        List<Teacher> teachers = new ArrayList<>();

        for (String e : entries) {
            if (e.trim().isEmpty()) continue;
            StringReader sr = new StringReader(e.trim());
            Teacher t =  (Teacher) unmarshaller.unmarshal(sr);
            teachers.add(t);
        }

        return (Teacher[]) teachers.toArray(new Teacher[0]);
    }

    public static void main(String[] args) throws Exception {
        Teacher teacher = new Teacher("Maria Souza", "Química", 10);

        TeacherXmlSerializer serializer = new TeacherXmlSerializer();
        String xml = serializer.toXml(teacher);
        System.out.println("XML gerado:\n" + xml);

        // Desserializar
        Teacher loaded = serializer.fromXml(xml)[0];
        System.out.println("Nome do professor desserializado: " + loaded.getName());
    }
}
