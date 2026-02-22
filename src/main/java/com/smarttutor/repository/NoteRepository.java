package com.smarttutor.repository;

import com.smarttutor.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    @Query("SELECT n FROM Note n WHERE n.classEntity.id = :classId AND n.division.id = :divisionId")
    List<Note> findByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT n FROM Note n WHERE n.teacher.id = :teacherId")
    List<Note> findByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT n FROM Note n WHERE n.classEntity.id = :classId AND n.division.id = :divisionId AND n.teacher.id = :teacherId")
    List<Note> findByClassDivisionAndTeacher(@Param("classId") Long classId, @Param("divisionId") Long divisionId, @Param("teacherId") Long teacherId);
    
    List<Note> findByClassEntityId(Long classId);
    List<Note> findByDivisionId(Long divisionId);
}
