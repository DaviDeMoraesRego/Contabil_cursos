package br.com.contabil.courses.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.contabil.courses.entity.ContabilCoursesEntity;

@Repository
public interface ContabilCoursesRepository extends JpaRepository<ContabilCoursesEntity, Integer>{

	@Query("SELECT c FROM ContabilCoursesEntity c WHERE c.title = :title")
	Optional<ContabilCoursesEntity> findByTitle(@Param("title") String title);

}
