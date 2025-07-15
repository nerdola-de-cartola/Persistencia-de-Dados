package model;


import javax.xml.bind.*;
import java.io.StringWriter;
import java.io.StringReader;

public class XmlSerializer<T> {
    private final JAXBContext context;
    private final Class<T> type;

    public XmlSerializer(Class<T> type) throws JAXBException {
        this.type = type;
        this.context = JAXBContext.newInstance(type);
    }

    public String toXml(T object) throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(object, sw);
        return sw.toString();
    }

    public T fromXml(String xml) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        return type.cast(unmarshaller.unmarshal(sr));
    }

    public static void main(String[] args) throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setFullName("Maria Souza");
        student.setRegistration(123);
        student.setBirthday(new java.util.Date());

        XmlSerializer<Student> serializer = new XmlSerializer<>(Student.class);
        String xml = serializer.toXml(student);
        System.out.println("XML gerado:\n" + xml);

        // Desserializar
        Student loaded = serializer.fromXml(xml);
        System.out.println("Nome do estudante desserializado: " + loaded.getFullName());
    }
}
