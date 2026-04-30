package br.com.contabil.courses.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.contabil.courses.dto.ContabilCoursesDto;
import br.com.contabil.courses.exception.InternalServerError;
import br.com.contabil.courses.exception.NotFoundException;
import br.com.contabil.courses.service.ContabilCoursesService;

@WebMvcTest(controllers = { ContabilCoursesController.class, RestExceptionHandler.class })
@DisplayName("ContabilCoursesController - Testes de Integração")
class ContabilCoursesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContabilCoursesService service;

    private ContabilCoursesDto dto;

    private static final String BASE_URL = "/contabil-courses/v1";

    @BeforeEach
    void setUp() {
        dto = new ContabilCoursesDto();
        dto.setId(1);
        dto.setTitle("Contabilidade Básica");
        dto.setImageSrc("http://img1.png");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /contabil-courses/v1
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /contabil-courses/v1")
    class FindAll {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 e lista de cursos com sucesso")
        void deveRetornar200_quandoHouverCursos() throws Exception {
            when(service.findAll()).thenReturn(List.of(dto));

            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].id").value(1))
                    .andExpect(jsonPath("$.data[0].title").value("Contabilidade Básica"))
                    .andExpect(jsonPath("$.errors").doesNotExist());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 quando não há cursos")
        void deveRetornar404_quandoSemCursos() throws Exception {
            when(service.findAll()).thenThrow(new NotFoundException("Nenhum registro encontrado."));

            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Nenhum registro encontrado."));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 500 quando service lança InternalServerError")
        void deveRetornar500_quandoErroInterno() throws Exception {
            when(service.findAll()).thenThrow(new InternalServerError("erro interno"));

            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errors").value("erro interno"));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /contabil-courses/v1/{id}/
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /contabil-courses/v1/{id}/")
    class FindById {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 e curso encontrado pelo id")
        void deveRetornar200_quandoEncontrado() throws Exception {
            when(service.findById(1)).thenReturn(dto);

            mockMvc.perform(get(BASE_URL + "/1/"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.title").value("Contabilidade Básica"))
                    .andExpect(jsonPath("$.errors").doesNotExist());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 quando curso não encontrado")
        void deveRetornar404_quandoNaoEncontrado() throws Exception {
            when(service.findById(999))
                    .thenThrow(new NotFoundException("Nenhum registro encontrado."));

            mockMvc.perform(get(BASE_URL + "/999/"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Nenhum registro encontrado."));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 500 quando service lança InternalServerError")
        void deveRetornar500_quandoErroInterno() throws Exception {
            when(service.findById(anyInt())).thenThrow(new InternalServerError("erro interno"));

            mockMvc.perform(get(BASE_URL + "/1/"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errors").value("erro interno"));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /contabil-courses/v1/title/{title}/
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /contabil-courses/v1/title/{title}/")
    class FindByTitle {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 e curso encontrado pelo título")
        void deveRetornar200_quandoEncontrado() throws Exception {
            when(service.findByTitle("Contabilidade Básica")).thenReturn(dto);

            mockMvc.perform(get(BASE_URL + "/title/Contabilidade Básica/"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.title").value("Contabilidade Básica"))
                    .andExpect(jsonPath("$.errors").doesNotExist());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 404 quando título não encontrado")
        void deveRetornar404_quandoNaoEncontrado() throws Exception {
            when(service.findByTitle("Inexistente"))
                    .thenThrow(new NotFoundException("Nenhum registro encontrado."));

            mockMvc.perform(get(BASE_URL + "/title/Inexistente/"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Nenhum registro encontrado."));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 500 quando service lança InternalServerError")
        void deveRetornar500_quandoErroInterno() throws Exception {
            when(service.findByTitle(any())).thenThrow(new InternalServerError("erro interno"));

            mockMvc.perform(get(BASE_URL + "/title/Contabilidade Básica/"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errors").value("erro interno"));
        }
    }
}