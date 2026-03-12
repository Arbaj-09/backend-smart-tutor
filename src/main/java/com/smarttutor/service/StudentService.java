package com.smarttutor.service;

import com.smarttutor.dto.StudentDTO;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.entity.Teacher;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.TeacherClassDivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private TeacherClassDivisionRepository teacherClassDivisionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Student createStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Student with email " + studentDTO.getEmail() + " already exists");
        }
        
        // Validate class and division
        ClassEntity classEntity = classRepository.findById(studentDTO.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", studentDTO.getClassId()));
        
        Division division = divisionRepository.findById(studentDTO.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division", "id", studentDTO.getDivisionId()));
        
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setRollNo(studentDTO.getRollNo());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        student.setPhone(studentDTO.getPhone());
        student.setClassEntity(classEntity);
        student.setDivision(division);
        // Don't set individual teacher - student can be seen by multiple teachers
        
        return studentRepository.save(student);
    }
    
    public Student createStudentForTeacher(StudentDTO studentDTO, Long teacherId) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Student with email " + studentDTO.getEmail() + " already exists");
        }
        
        // Validate class and division
        ClassEntity classEntity = classRepository.findById(studentDTO.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", studentDTO.getClassId()));
        
        Division division = divisionRepository.findById(studentDTO.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division", "id", studentDTO.getDivisionId()));
        
        // Get the specific teacher
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setRollNo(studentDTO.getRollNo());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        student.setPhone(studentDTO.getPhone());
        student.setClassEntity(classEntity);
        student.setDivision(division);
        student.setTeacher(teacher); // Explicitly assign to this teacher
        
        return studentRepository.save(student);
    }
    
    public Student getStudentById(Long id) {
        return studentRepository.findByIdWithEagerLoading(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAllWithEagerLoading();
    }
    
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        // Use eager loading to get the student
        Student existingStudent = studentRepository.findByIdWithEagerLoading(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        existingStudent.setName(studentDTO.getName());
        existingStudent.setRollNo(studentDTO.getRollNo());
        existingStudent.setActive(studentDTO.getActive() != null ? studentDTO.getActive() : true);
        
        if (studentDTO.getEmail() != null && !studentDTO.getEmail().equals(existingStudent.getEmail())) {
            if (studentRepository.existsByEmail(studentDTO.getEmail())) {
                throw new IllegalArgumentException("Student with email " + studentDTO.getEmail() + " already exists");
            }
            existingStudent.setEmail(studentDTO.getEmail());
        }
        
        // Update class and division if provided
        if (studentDTO.getClassId() != null) {
            ClassEntity classEntity = classRepository.findById(studentDTO.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class", "id", studentDTO.getClassId()));
            existingStudent.setClassEntity(classEntity);
        }
        
        if (studentDTO.getDivisionId() != null) {
            Division division = divisionRepository.findById(studentDTO.getDivisionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Division", "id", studentDTO.getDivisionId()));
            existingStudent.setDivision(division);
            
            // Update teacher assignment
            Teacher teacher = teacherClassDivisionRepository
                    .findByClassIdAndDivisionIdAndTeacherActive(studentDTO.getClassId(), studentDTO.getDivisionId(), true)
                    .stream()
                    .map(assignment -> assignment.getTeacher())
                    .findFirst()
                    .orElse(null);
            existingStudent.setTeacher(teacher);
        }
        
        // Update password if provided
        if (studentDTO.getPassword() != null && !studentDTO.getPassword().trim().isEmpty()) {
            existingStudent.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        }
        
        return studentRepository.save(existingStudent);
    }
    
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
    
    public List<Student> getStudentsByTeacher(Long teacherId) {
        return studentRepository.findByTeacherId(teacherId);
    }
    
    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findByClassEntityId(classId);
    }
    
    public List<Student> getStudentsByClassAndDivision(Long classId, Long divisionId) {
        return studentRepository.findByClassEntityIdAndDivisionId(classId, divisionId);
    }
}
