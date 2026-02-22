package com.smarttutor.repository;

import com.smarttutor.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    Optional<Division> findByDivisionNameAndClassEntityId(String divisionName, Long classId);
    boolean existsByDivisionNameAndClassEntityId(String divisionName, Long classId);
    
    @Query("SELECT d FROM Division d WHERE d.classEntity.id = :classId")
    List<Division> findByClassId(@Param("classId") Long classId);
    
    List<Division> findByClassEntityId(Long classId);
}
