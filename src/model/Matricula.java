package model;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;

@DatabaseTable(tableName="matricula")
public class Matricula {
    
    @DatabaseField(generatedId = true)
    private int id;
   
    @DatabaseField(columnName = "estudante_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Student estudante;    
    
    @DatabaseField(columnName = "turma_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Turma turma;
    
    @DatabaseField(dataType=DataType.ENUM_STRING)
    private StatusMatricula status;

    public Matricula() {}

    public Matricula(int id, String nomeEstudante, int idEstudante, Turma turma, StatusMatricula status) {
        var estudante =  new Student();
        estudante.setId(idEstudante);
        estudante.setFullName(nomeEstudante);

        this.id = id;
        this.estudante = estudante;
        this.turma = turma;
        this.status = status;

        // if (status == StatusMatricula.CONFIRMADA) {
        //     if (!turma.possoMatricular()) {
        //         this.status = StatusMatricula.SOLICITADA;
        //     }
        // }
    }

    /**GET Method Propertie id*/
    public int getId(){
        return this.id;
    }//end method getId

    /**SET Method Propertie id*/
    public void setId(int id){
        this.id = id;
    }//end method setId

    /**GET Method Propertie estudante*/
    public Student getEstudante(){
        return this.estudante;
    }//end method getEstudante

    /**SET Method Propertie estudante*/
    public void setEstudante(Student estudante){
        this.estudante = estudante;
    }//end method setEstudante

    /**GET Method Propertie turma*/
    public Turma getTurma(){
        return this.turma;
    }//end method getTurma

    /**SET Method Propertie turma*/
    public void setTurma(Turma turma){
        this.turma = turma;
    }//end method setTurma
    
    /**GET Method Property status*/
    public StatusMatricula getStatus() {
        return this.status;
    }
    
    /**SET Method Property status*/
    public void setStatus(StatusMatricula status) {
        this.status = status;
    }

    public String getEstudanteNome() {
        return this.estudante.getFullName();
    }

    public String getTurmaCodigo() {
        return this.turma.getCodigo();
    }
//End GetterSetterExtension Source Code


}//End class