package br.com.contabil.courses.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContabilCoursesDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Min(message = "Id inválido.", value = 1)
	private int id;
	
	@NotBlank(message = "insira um título válido.")
	private String title;
	
	@NotBlank(message = "insira um imageSrc válido.")
	private String imageSrc;
}
