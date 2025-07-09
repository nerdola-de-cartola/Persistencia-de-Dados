package student;

import javax.xml.bind.*;
import java.io.StringWriter;
import java.io.StringReader;

public class StudentXmlSerializer
{
    private JAXBContext context;

    public StudentXmlSerializer() throws JAXBException {
        context = JAXBContext.newInstance(Student.class);
    }

    public String toXml(Student student) throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(student, sw);
        return sw.toString();
    }

    public Student fromXml(String xml) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        return (Student) unmarshaller.unmarshal(sr);
    }

    public static void main(String[] args) throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setFullName("Maria Souza");
        student.setRegistration(123);
        student.setBirthday(new java.util.Date());

        StudentXmlSerializer serializer = new StudentXmlSerializer();
        String xml = serializer.toXml(student);
        System.out.println("XML gerado:\n" + xml);

        // Desserializar
        Student loaded = serializer.fromXml(xml);
        System.out.println("Nome do estudante desserializado: " + loaded.getFullName());
    }
}