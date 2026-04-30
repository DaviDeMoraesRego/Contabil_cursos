package br.com.contabil.courses.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.contabil.courses.entity.ContabilCoursesEntity;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("ContabilCoursesRepository - Testes de Integração")
class ContabilCoursesRepositoryTest {

	@Autowired
	private ContabilCoursesRepository repository;

	// ─────────────────────────────────────────────────────────────────────────
	// Test Data Builder
	// ─────────────────────────────────────────────────────────────────────────

	private ContabilCoursesEntity buildCourse(int id, String title, String imageSrc) {
		ContabilCoursesEntity e = new ContabilCoursesEntity();
		e.setId(id);
		e.setTitle(title);
		e.setImageSrc(imageSrc);
		return e;
	}

	@BeforeEach
	void setUp() {
		repository.deleteAll();
		repository.saveAll(List.of(buildCourse(1, "Contabilidade Básica", "http://img1.png"),
				buildCourse(2, "Contabilidade Avançada", "http://img2.png"),
				buildCourse(3, "Auditoria", "http://img3.png")));
	}

	// ─────────────────────────────────────────────────────────────────────────
	// findAll (herdado do JpaRepository)
	// ─────────────────────────────────────────────────────────────────────────

	@Nested
	@DisplayName("findAll")
	class FindAll {

		@Test
		@DisplayName("Deve retornar todos os cursos cadastrados")
		void deveRetornarTodosOsCursos() {
			assertThat(repository.findAll()).hasSize(3);
		}

		@Test
		@DisplayName("Deve retornar lista vazia quando não há cursos")
		void deveRetornarListaVazia_quandoSemCursos() {
			repository.deleteAll();

			assertThat(repository.findAll()).isEmpty();
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// findById (herdado do JpaRepository)
	// ─────────────────────────────────────────────────────────────────────────

	@Nested
	@DisplayName("findById")
	class FindById {

		@Test
		@DisplayName("Deve retornar o curso correto pelo id")
		void deveRetornarCurso_quandoEncontrado() {
			Optional<ContabilCoursesEntity> result = repository.findById(1);

			assertThat(result).isPresent().hasValueSatisfying(c -> {
				assertThat(c.getId()).isEqualTo(1);
				assertThat(c.getTitle()).isEqualTo("Contabilidade Básica");
				assertThat(c.getImageSrc()).isEqualTo("http://img1.png");
			});
		}

		@Test
		@DisplayName("Deve retornar Optional vazio para id inexistente")
		void deveRetornarVazio_quandoNaoEncontrado() {
			assertThat(repository.findById(999)).isEmpty();
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// findByTitle
	// ─────────────────────────────────────────────────────────────────────────

	@Nested
	@DisplayName("findByTitle")
	class FindByTitle {

		@Test
		@DisplayName("Deve retornar o curso correto pelo título")
		void deveRetornarCurso_quandoEncontrado() {
			Optional<ContabilCoursesEntity> result = repository.findByTitle("Auditoria");

			assertThat(result).isPresent().hasValueSatisfying(c -> {
				assertThat(c.getId()).isEqualTo(3);
				assertThat(c.getTitle()).isEqualTo("Auditoria");
			});
		}

		@Test
		@DisplayName("Deve retornar Optional vazio para título inexistente")
		void deveRetornarVazio_quandoTituloNaoEncontrado() {
			assertThat(repository.findByTitle("Título Inexistente")).isEmpty();
		}

		@Test
		@DisplayName("Deve ser case-sensitive na busca por título")
		void deveSercaseSensitive() {
			assertThat(repository.findByTitle("auditoria")).isEmpty();
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Callbacks JPA
	// ─────────────────────────────────────────────────────────────────────────

	@Nested
	@DisplayName("Callbacks JPA")
	class CallbacksJpa {

		@Test
		@DisplayName("Deve preencher dthr_create e dthr_update automaticamente ao persistir")
		void devePreencherDatasAoPersistir() {
			ContabilCoursesEntity novo = buildCourse(99, "Novo Curso", "http://novo.png");

			ContabilCoursesEntity salvo = repository.save(novo);

			assertThat(salvo.getDthr_create()).isNotNull();
			assertThat(salvo.getDthr_update()).isNotNull();
		}

		@Test
		@DisplayName("Deve atualizar dthr_update ao fazer update sem alterar dthr_create")
		void deveAtualizarDthrUpdate_semAlterarDthrCreate() {
			ContabilCoursesEntity entity = repository.findById(1).orElseThrow();
			java.util.Date dthrCreateOriginal = entity.getDthr_create();

			entity.setTitle("Título Atualizado");
			entity.setDthr_update(new java.util.Date(dthrCreateOriginal.getTime() + 1000));
			ContabilCoursesEntity atualizado = repository.saveAndFlush(entity);

			assertThat(atualizado.getDthr_create()).isEqualTo(dthrCreateOriginal);
			assertThat(atualizado.getDthr_update()).isAfter(dthrCreateOriginal);
		}
	}
}