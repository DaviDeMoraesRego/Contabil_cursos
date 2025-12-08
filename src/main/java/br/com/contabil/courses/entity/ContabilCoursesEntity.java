package br.com.contabil.courses.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "tab_courses", schema = "contabil")
public class ContabilCoursesEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "image_src", nullable = false)
	private String imageSrc;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "dthr_create", nullable = false)
	private Date dthr_create;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "dthr_update", nullable = false)
	private Date dthr_update;

	@PrePersist
	public void prePersist() {
		Date date = new Date();
		this.dthr_create = date;
		this.dthr_update = date;
	}

	@PreUpdate
	public void preupdate() {
		this.dthr_update = new Date();
	}
}
