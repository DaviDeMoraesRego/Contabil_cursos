package br.com.contabil.courses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.contabil.courses.dto.ContabilCoursesDto;
import br.com.contabil.courses.dto.ResponseDto;
import br.com.contabil.courses.service.ContabilCoursesService;

@Controller
@RestController
@RequestMapping("/contabil-courses/v1")
public class ContabilCoursesController {

	@Autowired
	ContabilCoursesService service;
	
	@GetMapping
	public ResponseEntity<ResponseDto<List<ContabilCoursesDto>>> findAll() throws Exception {
			return ResponseEntity
			.ok(new ResponseDto<List<ContabilCoursesDto>>(service.findAll(), null));
	}
	
	@GetMapping("/{id}/")
	public ResponseEntity<ResponseDto<ContabilCoursesDto>> findById(@PathVariable("id") int id) throws Exception{
		return ResponseEntity
			.ok(new ResponseDto<ContabilCoursesDto>(service.findById(id), null));
	}
	
	@GetMapping("/title/{title}/")
	public ResponseEntity<ResponseDto<ContabilCoursesDto>> findByTitle(@PathVariable("title") String title) throws Exception{
		return ResponseEntity
			.ok(new ResponseDto<ContabilCoursesDto>(service.findByTitle(title), null));
	}
}
