package br.com.contabil.courses.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.contabil.courses.adapter.ContabilCoursesAdapter;
import br.com.contabil.courses.dto.ContabilCoursesDto;
import br.com.contabil.courses.entity.ContabilCoursesEntity;
import br.com.contabil.courses.exception.InternalServerError;
import br.com.contabil.courses.exception.NotFoundException;
import br.com.contabil.courses.repository.ContabilCoursesRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContabilCoursesService - Testes Unitários")
class ContabilCoursesServiceTest {

	@Mock
	private ContabilCoursesRepository repository;

	@Mock
	private ContabilCoursesAdapter adapter;

	@InjectMocks
	private ContabilCoursesService service;

	private ContabilCoursesDto dto;
	private ContabilCoursesEntity entity;

	@BeforeEach
	void setUp() {
		dto = new ContabilCoursesDto();
		dto.setId(1);
		dto.setTitle("Contabilidade Básica");
		dto.setImageSrc("http://img1.png");

		entity = new ContabilCoursesEntity();
		entity.setId(1);
		entity.setTitle("Contabilidade Básica");
		entity.setImageSrc("http://img1.png");
	}

	// ─────────────────────────────────────────────────────────────────────────
	// findAll
	// ─────────────────────────────────────────────────────────────────────────

	@Nested
	@DisplayName("findAll")
	class FindAll {

		@Test
        @DisplayName("Deve retornar lista de DTOs com sucesso")
        void deveRetornarListaDeDtos() throws Exception {
            when(repository.findAll()).thenReturn(List.of(entity));
            when(adapter.adapterEntityToDto(entity)).thenReturn(dto);

            List<ContabilCoursesDto> result = service.findAll();

            assertThat(result)
                    .hasSize(1)
                    .first()
                    .extracting(ContabilCoursesDto::getTitle)
                    .isEqualTo("Contabilidade Básica");

            verify(adapter, times(1)).adapterEntityToDto(entity);
        }

		@Test
        @DisplayName("Deve lançar NotFoundExceptionException quando não há registros")
        void deveLancarNotFoundException_quandoListaVazia() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            assertThatThrownBy(() -> service.findAll())
                    .isInstanceOf(NotFoundException.class);
        }

		@Test
        @DisplayName("Deve lançar InternalServerError para qualquer outra exceção")
        void deveLancarInternalServerError_quandoExcecaoGenerica() {
            when(repository.findAll()).thenThrow(new RuntimeException("erro inesperado"));

            assertThatThrownBy(() -> service.findAll())
                    .isInstanceOf(InternalServerError.class);
        }
	}

	// ─────────────────────────────────────────────────────────────────────────
	// findById
	// ─────────────────────────────────────────────────────────────────────────

	@Nested
	@DisplayName("findById")
	class FindById {

		@Test
        @DisplayName("Deve retornar DTO quando curso encontrado")
        void deveRetornarDto_quandoEncontrado() throws Exception {
            when(repository.findById(1)).thenReturn(Optional.of(entity));
            when(adapter.adapterEntityToDto(entity)).thenReturn(dto);

            ContabilCoursesDto result = service.findById(1);

            assertThat(result)
                    .extracting(ContabilCoursesDto::getId, ContabilCoursesDto::getTitle)
                    .containsExactly(1, "Contabilidade Básica");
        }

		@Test
        @DisplayName("Deve lançar NotFoundExceptionException quando curso não encontrado")
        void deveLancarNotFoundException_quandoNaoEncontrado() {
            when(repository.findById(999)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(999))
                    .isInstanceOf(NotFoundException.class);
        }

		@Test
        @DisplayName("Deve lançar InternalServerError para qualquer outra exceção")
        void deveLancarInternalServerError_quandoExcecaoGenerica() {
            when(repository.findById(any())).thenThrow(new RuntimeException("erro inesperado"));

            assertThatThrownBy(() -> service.findById(1))
                    .isInstanceOf(InternalServerError.class);
        }
	}

	// ─────────────────────────────────────────────────────────────────────────
	// findByTitle
	// ─────────────────────────────────────────────────────────────────────────

	@Nested
	@DisplayName("findByTitle")
	class FindByTitle {

		@Test
        @DisplayName("Deve retornar DTO quando curso encontrado pelo título")
        void deveRetornarDto_quandoEncontrado() throws Exception {
            when(repository.findByTitle("Contabilidade Básica")).thenReturn(Optional.of(entity));
            when(adapter.adapterEntityToDto(entity)).thenReturn(dto);

            ContabilCoursesDto result = service.findByTitle("Contabilidade Básica");

            assertThat(result)
                    .extracting(ContabilCoursesDto::getId, ContabilCoursesDto::getTitle)
                    .containsExactly(1, "Contabilidade Básica");
        }

		@Test
        @DisplayName("Deve lançar NotFoundExceptionException quando título não encontrado")
        void deveLancarNotFoundException_quandoNaoEncontrado() {
            when(repository.findByTitle("Inexistente")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findByTitle("Inexistente"))
                    .isInstanceOf(NotFoundException.class);
        }

		@Test
        @DisplayName("Deve lançar InternalServerError para qualquer outra exceção")
        void deveLancarInternalServerError_quandoExcecaoGenerica() {
            when(repository.findByTitle(any())).thenThrow(new RuntimeException("erro inesperado"));

            assertThatThrownBy(() -> service.findByTitle("Contabilidade Básica"))
                    .isInstanceOf(InternalServerError.class);
        }
	}
}