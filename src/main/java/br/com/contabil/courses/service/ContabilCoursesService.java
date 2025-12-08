package br.com.contabil.courses.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.contabil.courses.adapter.ContabilCoursesAdapter;
import br.com.contabil.courses.dto.ContabilCoursesDto;
import br.com.contabil.courses.entity.ContabilCoursesEntity;
import br.com.contabil.courses.exception.NotFoundExceptionException;
import br.com.contabil.courses.exception.internalServerError;
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
		} catch (NotFoundExceptionException e) {
			throw new NotFoundExceptionException(e.getMessage());
		} catch (Exception e) {
			throw new internalServerError(e.getMessage());
		}
		return dtos;
	}

	public ContabilCoursesDto findById(int id) throws Exception {
		ContabilCoursesDto dto;
		try {
			ContabilCoursesEntity entity = repository.findById(id)
					.orElseThrow(() -> new NotFoundExceptionException("Nenhum registro encontrado."));
			dto = adapter.adapterEntityToDto(entity);
		} catch (NotFoundExceptionException e) {
			throw new NotFoundExceptionException(e.getMessage());
		} catch (Exception e) {
			throw new internalServerError(e.getMessage());
		}
		return dto;
	}

	public ContabilCoursesDto findByTitle(String title) throws Exception {
		ContabilCoursesDto dto;

		try {
			ContabilCoursesEntity entity = repository.findByTitle(title)
					.orElseThrow(() -> new NotFoundExceptionException("Nenhum registro encontrado."));
			dto = adapter.adapterEntityToDto(entity);
		} catch (NotFoundExceptionException e) {
			throw new NotFoundExceptionException(e.getMessage());
		} catch (Exception e) {
			throw new internalServerError(e.getMessage());
		}

		return dto;
	}

	private static void notFoundChecker(int paramForCheck) throws NotFoundExceptionException {
		if (paramForCheck == 0) {
			throw new NotFoundExceptionException("Nenhum registro encontrado.");
		}
	}
}
