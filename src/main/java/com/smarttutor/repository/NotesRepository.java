package com.smarttutor.repository;

import com.smarttutor.entity.Notes;
import com.smarttutor.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {
    
    @Query("SELECT n FROM Notes n WHERE n.classEntity.id = :classId AND n.division.id = :divisionId AND n.active = :active ORDER BY n.uploadedAt DESC")
    List<Notes> findByClassIdAndDivisionIdAndActiveOrderByUploadedAtDesc(@Param("classId") Long classId, @Param("divisionId") Long divisionId, @Param("active") Boolean active);
    
    List<Notes> findByTeacherAndActiveOrderByUploadedAtDesc(Teacher teacher, Boolean active);
    
    @Query("SELECT n FROM Notes n WHERE n.classEntity.id = :classId AND n.active = :active ORDER BY n.uploadedAt DESC")
    List<Notes> findByClassIdAndActiveOrderByUploadedAtDesc(@Param("classId") Long classId, @Param("active") Boolean active);
    
    @Query("SELECT n FROM Notes n WHERE n.division.id = :divisionId AND n.active = :active ORDER BY n.uploadedAt DESC")
    List<Notes> findByDivisionIdAndActiveOrderByUploadedAtDesc(@Param("divisionId") Long divisionId, @Param("active") Boolean active);
    
    @Query("SELECT n FROM Notes n LEFT JOIN FETCH n.teacher LEFT JOIN FETCH n.classEntity LEFT JOIN FETCH n.division WHERE n.active = true ORDER BY n.uploadedAt DESC")
    List<Notes> findAllActiveNotes();
    
    @Query("SELECT n FROM Notes n LEFT JOIN FETCH n.teacher LEFT JOIN FETCH n.classEntity LEFT JOIN FETCH n.division WHERE n.teacher.id = :teacherId AND n.active = true ORDER BY n.uploadedAt DESC")
    List<Notes> findActiveNotesByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT n FROM Notes n WHERE n.teacher.id = :teacherId")
    List<Notes> findByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT n FROM Notes n LEFT JOIN FETCH n.teacher LEFT JOIN FETCH n.classEntity LEFT JOIN FETCH n.division WHERE n.classEntity.id = :classId AND n.division.id = :divisionId AND n.active = true ORDER BY n.uploadedAt DESC")
    List<Notes> findActiveNotesByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    Optional<Notes> findByIdAndActive(Long id, Boolean active);
    
    void deleteByIdAndTeacher(Long id, Teacher teacher);
}
