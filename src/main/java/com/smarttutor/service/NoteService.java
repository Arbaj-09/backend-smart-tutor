package com.smarttutor.service;

import com.smarttutor.entity.Note;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.entity.Teacher;
import com.smarttutor.repository.NoteRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import com.smarttutor.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    private final String UPLOAD_DIR = "uploads/notes/";
    
    public Note uploadNote(String title, String description, MultipartFile file, 
                        Long classId, Long divisionId, Long teacherId) {
        try {
            // Validate entities
            ClassEntity classEntity = classRepository.findById(classId).orElse(null);
            Division division = divisionRepository.findById(divisionId).orElse(null);
            Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
            
            if (classEntity == null || division == null || teacher == null) {
                throw new RuntimeException("Invalid class, division, or teacher");
            }
            
            String fileUrl = "";
            if (file != null && !file.isEmpty()) {
                fileUrl = saveFile(file);
            }
            
            Note note = new Note();
            note.setTitle(title);
            note.setDescription(description);
            note.setFileUrl(fileUrl);
            note.setClassEntity(classEntity);
            note.setDivision(division);
            note.setTeacher(teacher);
            
            return noteRepository.save(note);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }
    
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }
    
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }
    
    public List<Note> getNotesByClassAndDivision(Long classId, Long divisionId) {
        return noteRepository.findByClassAndDivision(classId, divisionId);
    }
    
    public List<Note> getNotesByTeacher(Long teacherId) {
        return noteRepository.findByTeacherId(teacherId);
    }
    
    public Note updateNote(Long id, Note note) {
        Note existingNote = getNoteById(id);
        if (existingNote == null) {
            throw new RuntimeException("Note not found");
        }
        
        existingNote.setTitle(note.getTitle());
        existingNote.setDescription(note.getDescription());
        
        return noteRepository.save(existingNote);
    }
    
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
    
    private String saveFile(MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        
        // Save file
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);
        
        return UPLOAD_DIR + newFilename;
    }
}
