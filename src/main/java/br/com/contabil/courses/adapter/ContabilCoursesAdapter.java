package br.com.contabil.courses.adapter;

import org.springframework.stereotype.Component;

import br.com.contabil.courses.dto.ContabilCoursesDto;
import br.com.contabil.courses.entity.ContabilCoursesEntity;

@Component
public class ContabilCoursesAdapter {

	public ContabilCoursesEntity adapterDtoToEntity(ContabilCoursesDto dto, int id) {
		
		ContabilCoursesEntity entity = new ContabilCoursesEntity();
		
		entity.setId(id);
		entity.setTitle(dto.getTitle());
		entity.setImageSrc(dto.getImageSrc());
		
		return entity;
	}
	
	public ContabilCoursesDto adapterEntityToDto(ContabilCoursesEntity entity) {
		
		ContabilCoursesDto dto = new ContabilCoursesDto();
		
		dto.setId(entity.getId());
		dto.setTitle(entity.getTitle());
		dto.setImageSrc(entity.getImageSrc());
		
		return dto;
	}
}
