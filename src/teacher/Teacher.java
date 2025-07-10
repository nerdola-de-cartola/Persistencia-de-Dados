package teacher;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "teacher")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "teachers")
public class Teacher {

    // Primary key with auto-generated ID
    @DatabaseField(generatedId = true)
    private int id;

    // Name of the teacher, cannot be null
    @DatabaseField(canBeNull = false)
    private String name;

    // Subject taught by the teacher
    @DatabaseField
    private String subject;

    // Number of years of experience
    @DatabaseField(columnName = "years_of_experience")
    private int yearsOfExperience;

    // ORMLite requires a no-argument constructor
    public Teacher() {}

    public Teacher(String name, String subject, int yearsOfExperience) {
        this.name = name;
        this.subject = subject;
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int years) {
        this.yearsOfExperience = years;
    }

    @Override
    public String toString() {
        return "Teacher{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", subject='" + subject + '\'' +
               ", yearsOfExperience=" + yearsOfExperience +
               '}';
    }

    public void setId(int id) {
        this.id = id;
    }
}
