package com.smarttutor.repository;

import com.smarttutor.entity.TeacherClassDivision;
import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherClassDivisionRepository extends JpaRepository<TeacherClassDivision, Long> {
    
    List<TeacherClassDivision> findByTeacher(Teacher teacher);
    
    List<TeacherClassDivision> findByTeacherId(Long teacherId);
    
    List<TeacherClassDivision> findByClassEntity(ClassEntity classEntity);
    
    @Query("SELECT tcd FROM TeacherClassDivision tcd WHERE tcd.classEntity.id = :classId")
    List<TeacherClassDivision> findByClassId(@Param("classId") Long classId);
    
    @Query("SELECT tcd FROM TeacherClassDivision tcd WHERE tcd.division.id = :divisionId")
    List<TeacherClassDivision> findByDivisionId(@Param("divisionId") Long divisionId);
    
    List<TeacherClassDivision> findByTeacherAndClassEntity(Teacher teacher, ClassEntity classEntity);
    
    @Query("SELECT tcd FROM TeacherClassDivision tcd WHERE tcd.teacher.id = :teacherId AND tcd.classEntity.id = :classId")
    List<TeacherClassDivision> findByTeacherIdAndClassId(@Param("teacherId") Long teacherId, @Param("classId") Long classId);
    
    Optional<TeacherClassDivision> findByTeacherAndClassEntityAndDivision(Teacher teacher, ClassEntity classEntity, Division division);
    
    @Query("SELECT tcd FROM TeacherClassDivision tcd WHERE tcd.teacher.id = :teacherId AND tcd.classEntity.id = :classId AND tcd.division.id = :divisionId")
    Optional<TeacherClassDivision> findByTeacherIdAndClassIdAndDivisionId(@Param("teacherId") Long teacherId, @Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT DISTINCT tcd.classEntity FROM TeacherClassDivision tcd WHERE tcd.teacher.id = :teacherId")
    List<ClassEntity> findClassesByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT DISTINCT tcd.division FROM TeacherClassDivision tcd WHERE tcd.teacher.id = :teacherId AND tcd.classEntity.id = :classId")
    List<Division> findDivisionsByTeacherIdAndClassId(@Param("teacherId") Long teacherId, @Param("classId") Long classId);
    
    @Query("SELECT tcd FROM TeacherClassDivision tcd WHERE tcd.teacher.id = :teacherId AND tcd.classEntity.id = :classId AND tcd.division.id = :divisionId")
    Optional<TeacherClassDivision> findByTeacherAndClassAndDivision(@Param("teacherId") Long teacherId, @Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT tcd FROM TeacherClassDivision tcd WHERE tcd.classEntity.id = :classId AND tcd.division.id = :divisionId AND tcd.teacher.active = :active")
    List<TeacherClassDivision> findByClassIdAndDivisionIdAndTeacherActive(@Param("classId") Long classId, @Param("divisionId") Long divisionId, @Param("active") Boolean active);
    
    @Query("SELECT t FROM Teacher t WHERE t.id IN (SELECT tcd.teacher.id FROM TeacherClassDivision tcd WHERE tcd.classEntity.id = :classId AND tcd.division.id = :divisionId AND tcd.teacher.active = :active)")
    List<Teacher> findTeachersByClassIdAndDivisionIdAndTeacherActive(@Param("classId") Long classId, @Param("divisionId") Long divisionId, @Param("active") Boolean active);
    
    void deleteByTeacherAndClassEntityAndDivision(Teacher teacher, ClassEntity classEntity, Division division);
    
    @Query("DELETE FROM TeacherClassDivision tcd WHERE tcd.teacher.id = :teacherId AND tcd.classEntity.id = :classId AND tcd.division.id = :divisionId")
    void deleteByTeacherIdAndClassIdAndDivisionId(@Param("teacherId") Long teacherId, @Param("classId") Long classId, @Param("divisionId") Long divisionId);
}
