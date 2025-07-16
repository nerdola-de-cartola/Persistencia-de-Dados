package br.ufg.inf.repository;

import br.ufg.inf.model.Professor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ProfessorRepositoryTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @BeforeEach
    void setUp() {
        // Limpa o banco de dados antes de cada teste
        professorRepository.deleteAll();
    }

    @Test
    void testCriarProfessor() {
        // Arrange
        Professor professor = new Professor("João Silva", "1980-05-15", "Universidade Federal de Goiás");

        // Act
        Professor professorSalvo = professorRepository.save(professor);

        // Assert
        assertNotNull(professorSalvo.getId());
        assertEquals("João Silva", professorSalvo.getNomeCompleto());
        assertEquals("1980-05-15", professorSalvo.getDataDeNascimento());
        assertEquals("Universidade Federal de Goiás", professorSalvo.getInstituicao());
    }

    @Test
    void testLerProfessor() {
        // Arrange
        Professor professor = new Professor("Maria Santos", "1975-08-20", "Universidade de Brasília");
        Professor professorSalvo = professorRepository.save(professor);

        // Act
        Optional<Professor> professorEncontrado = professorRepository.findById(professorSalvo.getId());

        // Assert
        assertTrue(professorEncontrado.isPresent());
        assertEquals("Maria Santos", professorEncontrado.get().getNomeCompleto());
        assertEquals("1975-08-20", professorEncontrado.get().getDataDeNascimento());
        assertEquals("Universidade de Brasília", professorEncontrado.get().getInstituicao());
    }

    @Test
    void testLerTodosProfessores() {
        // Arrange
        Professor professor1 = new Professor("Carlos Pereira", "1985-03-10", "UFG");
        Professor professor2 = new Professor("Ana Costa", "1990-12-05", "UnB");
        Professor professor3 = new Professor("Pedro Oliveira", "1982-07-25", "USP");

        professorRepository.save(professor1);
        professorRepository.save(professor2);
        professorRepository.save(professor3);

        // Act
        List<Professor> professores = professorRepository.findAll();

        // Assert
        assertEquals(3, professores.size());
        assertTrue(professores.stream().anyMatch(p -> p.getNomeCompleto().equals("Carlos Pereira")));
        assertTrue(professores.stream().anyMatch(p -> p.getNomeCompleto().equals("Ana Costa")));
        assertTrue(professores.stream().anyMatch(p -> p.getNomeCompleto().equals("Pedro Oliveira")));
    }

    @Test
    void testAtualizarProfessor() {
        // Arrange
        Professor professor = new Professor("Roberto Lima", "1978-11-30", "UFMG");
        Professor professorSalvo = professorRepository.save(professor);

        // Act
        professorSalvo.setNomeCompleto("Roberto Lima Silva");
        professorSalvo.setInstituicao("Universidade Federal de Minas Gerais");
        Professor professorAtualizado = professorRepository.save(professorSalvo);

        // Assert
        assertEquals(professorSalvo.getId(), professorAtualizado.getId());
        assertEquals("Roberto Lima Silva", professorAtualizado.getNomeCompleto());
        assertEquals("1978-11-30", professorAtualizado.getDataDeNascimento());
        assertEquals("Universidade Federal de Minas Gerais", professorAtualizado.getInstituicao());
    }

    @Test
    void testDeletarProfessor() {
        // Arrange
        Professor professor = new Professor("Fernanda Rocha", "1983-04-18", "UFRJ");
        Professor professorSalvo = professorRepository.save(professor);

        // Act
        professorRepository.deleteById(professorSalvo.getId());

        // Assert
        Optional<Professor> professorEncontrado = professorRepository.findById(professorSalvo.getId());
        assertFalse(professorEncontrado.isPresent());
    }

    @Test
    void testDeletarTodosProfessores() {
        // Arrange
        Professor professor1 = new Professor("Lucas Martins", "1988-06-12", "UFSC");
        Professor professor2 = new Professor("Juliana Neves", "1992-09-03", "UFPR");

        professorRepository.save(professor1);
        professorRepository.save(professor2);

        // Act
        professorRepository.deleteAll();

        // Assert
        List<Professor> professores = professorRepository.findAll();
        assertTrue(professores.isEmpty());
    }

    @Test
    void testContarProfessores() {
        // Arrange
        Professor professor1 = new Professor("Marcos Souza", "1979-02-14", "UFRGS");
        Professor professor2 = new Professor("Carla Mendes", "1987-10-08", "UFBA");

        professorRepository.save(professor1);
        professorRepository.save(professor2);

        // Act
        long quantidade = professorRepository.count();

        // Assert
        assertEquals(2, quantidade);
    }

    @Test
    void testVerificarExistenciaProfessor() {
        // Arrange
        Professor professor = new Professor("Ricardo Alves", "1981-12-22", "UFC");
        Professor professorSalvo = professorRepository.save(professor);

        // Act & Assert
        assertTrue(professorRepository.existsById(professorSalvo.getId()));
        assertFalse(professorRepository.existsById("id-inexistente"));
    }

    @Test
    void testSalvarProfessorComCamposVazios() {
        // Arrange
        Professor professor = new Professor("", "", "");

        // Act
        Professor professorSalvo = professorRepository.save(professor);

        // Assert
        assertNotNull(professorSalvo.getId());
        assertEquals("", professorSalvo.getNomeCompleto());
        assertEquals("", professorSalvo.getDataDeNascimento());
        assertEquals("", professorSalvo.getInstituicao());
    }

    @Test
    void testSalvarProfessorComNomeNulo() {
        // Arrange
        Professor professor = new Professor(null, "1985-01-01", "Universidade Teste");

        // Act
        Professor professorSalvo = professorRepository.save(professor);

        // Assert
        assertNotNull(professorSalvo.getId());
        assertNull(professorSalvo.getNomeCompleto());
        assertEquals("1985-01-01", professorSalvo.getDataDeNascimento());
        assertEquals("Universidade Teste", professorSalvo.getInstituicao());
    }
}
