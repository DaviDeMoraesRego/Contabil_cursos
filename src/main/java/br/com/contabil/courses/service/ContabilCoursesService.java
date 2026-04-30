package br.com.contabil.courses.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.contabil.courses.adapter.ContabilCoursesAdapter;
import br.com.contabil.courses.dto.ContabilCoursesDto;
import br.com.contabil.courses.entity.ContabilCoursesEntity;
import br.com.contabil.courses.exception.NotFoundException;
import br.com.contabil.courses.exception.InternalServerError;
import br.com.contabil.courses.repository.ContabilCoursesRepository;

@Service
public class ContabilCoursesService {

	@Autowired
	ContabilCoursesRepository repository;

	@Autowired
	ContabilCoursesAdapter adapter;

	public List<ContabilCoursesDto> findAll() throws Exception {
		List<ContabilCoursesDto> dtos = new ArrayList<>();
		try {
			List<ContabilCoursesEntity> entities = repository.findAll();
			notFoundChecker(entities.size());
			entities.forEach(entity -> dtos.add(adapter.adapterEntityToDto(entity)));
		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
		return dtos;
	}

	public ContabilCoursesDto findById(int id) throws Exception {
		ContabilCoursesDto dto;
		try {
			ContabilCoursesEntity entity = repository.findById(id)
					.orElseThrow(() -> new NotFoundException("Nenhum registro encontrado."));
			dto = adapter.adapterEntityToDto(entity);
		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
		return dto;
	}

	public ContabilCoursesDto findByTitle(String title) throws Exception {
		ContabilCoursesDto dto;

		try {
			ContabilCoursesEntity entity = repository.findByTitle(title)
					.orElseThrow(() -> new NotFoundException("Nenhum registro encontrado."));
			dto = adapter.adapterEntityToDto(entity);
		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}

		return dto;
	}

	private static void notFoundChecker(int paramForCheck) throws NotFoundException {
		if (paramForCheck == 0) {
			throw new NotFoundException("Nenhum registro encontrado.");
		}
	}
}
