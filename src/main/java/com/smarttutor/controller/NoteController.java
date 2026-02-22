package com.smarttutor.controller;

import com.smarttutor.entity.Note;
import com.smarttutor.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @PostMapping("/upload")
    public ResponseEntity<Note> uploadNote(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            @RequestParam("classId") Long classId,
            @RequestParam("divisionId") Long divisionId,
            @RequestParam("teacherId") Long teacherId) {
        try {
            Note uploadedNote = noteService.uploadNote(title, description, file, classId, divisionId, teacherId);
            return ResponseEntity.ok(uploadedNote);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {
        try {
            Note createdNote = noteService.createNote(note);
            return ResponseEntity.ok(createdNote);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        try {
            Note note = noteService.getNoteById(id);
            return ResponseEntity.ok(note);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/class/{classId}/division/{divisionId}")
    public ResponseEntity<List<Note>> getNotesByClassAndDivision(
            @PathVariable Long classId, @PathVariable Long divisionId) {
        List<Note> notes = noteService.getNotesByClassAndDivision(classId, divisionId);
        return ResponseEntity.ok(notes);
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Note>> getNotesByTeacher(@PathVariable Long teacherId) {
        List<Note> notes = noteService.getNotesByTeacher(teacherId);
        return ResponseEntity.ok(notes);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @Valid @RequestBody Note note) {
        try {
            Note updatedNote = noteService.updateNote(id, note);
            return ResponseEntity.ok(updatedNote);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
