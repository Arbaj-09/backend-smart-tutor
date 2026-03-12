package com.smarttutor.controller;

import com.smarttutor.entity.Notes;
import com.smarttutor.entity.Teacher;
import com.smarttutor.dto.response.NotesResponseDTO;
import com.smarttutor.service.NotesService;
import com.smarttutor.service.ActivityLogService;
import com.smarttutor.service.NotificationService;
import com.smarttutor.mapper.NotesMapper;
import com.smarttutor.repository.NotesRepository;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotesController {
    
    @Autowired
    private NotesService notesService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private NotesRepository notesRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadNotes(
            @RequestParam("title") String title,
            @RequestParam("subject") String subject,
            @RequestParam("description") String description,
            @RequestParam("classId") Long classId,
            @RequestParam("divisionId") Long divisionId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("teacherId") Long teacherId) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is required");
            }
            
            // Validate file type
            String contentType = file.getContentType();
            if (!isValidFileType(contentType, file.getOriginalFilename())) {
                return ResponseEntity.badRequest().body("Invalid file type. Allowed types: PDF, JPG, JPEG, PNG, DOC, DOCX");
            }
            
            // Create request object
            NotesService.NotesRequest request = new NotesService.NotesRequest();
            request.setTitle(title);
            request.setSubject(subject);
            request.setDescription(description);
            request.setClassId(classId);
            request.setDivisionId(divisionId);
            
            // Upload notes
            Notes notes = notesService.uploadNotes(request, file, teacherId);
            
            // Get teacher details for notification
            Teacher teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            
            // Send notifications to students and HOD with details
            notificationService.sendNoteUploadNotification(classId, divisionId, title, teacher.getName(), subject);
            
            // Log activity
            activityLogService.logActivity(
                Role.TEACHER,
                teacherId,
                "UPLOAD_NOTES",
                "Uploaded notes: " + title + " for " + subject
            );
            
            return ResponseEntity.ok(Map.of(
                "message", "Notes uploaded successfully",
                "notesId", notes.getId(),
                "fileName", notes.getFileName()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error uploading notes: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<NotesResponseDTO>> getAllNotes() {
        try {
            List<Notes> notes = notesRepository.findAllActiveNotes();
            List<NotesResponseDTO> dtos = NotesMapper.toDTOList(notes);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/class/{classId}/division/{divisionId}")
    public ResponseEntity<List<NotesResponseDTO>> getNotesByClassAndDivision(
            @PathVariable Long classId,
            @PathVariable Long divisionId) {
        try {
            List<Notes> notes = notesService.getNotesByClassAndDivision(classId, divisionId);
            List<NotesResponseDTO> dtos = NotesMapper.toDTOList(notes);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<NotesResponseDTO>> getNotesByStudent(@PathVariable Long studentId) {
        try {
            List<Notes> notes = notesService.getNotesByStudent(studentId);
            List<NotesResponseDTO> dtos = NotesMapper.toDTOList(notes);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{notesId}")
    public ResponseEntity<NotesResponseDTO> getNotesById(@PathVariable Long notesId) {
        try {
            Notes notes = notesService.getNotesById(notesId);
            NotesResponseDTO dto = NotesMapper.toDTO(notes);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{notesId}")
    public ResponseEntity<?> deleteNotes(@PathVariable Long notesId, @RequestParam Long teacherId) {
        try {
            notesService.deleteNotes(notesId, teacherId);
            
            return ResponseEntity.ok(Map.of("message", "Notes deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting notes: " + e.getMessage());
        }
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<NotesResponseDTO>> getNotesByTeacher(@PathVariable Long teacherId) {
        try {
            List<Notes> notes = notesRepository.findByTeacherId(teacherId);
            List<NotesResponseDTO> dtos = NotesMapper.toDTOList(notes);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    private boolean isValidFileType(String contentType, String fileName) {
        if (contentType == null && fileName == null) return false;
        
        // Check by content type
        if (contentType != null) {
            return contentType.equals("application/pdf") ||
                   contentType.equals("image/jpeg") ||
                   contentType.equals("image/png") ||
                   contentType.equals("application/msword") ||
                   contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        }
        
        // Check by file extension
        if (fileName != null) {
            String extension = fileName.toLowerCase();
            return extension.endsWith(".pdf") ||
                   extension.endsWith(".jpg") ||
                   extension.endsWith(".jpeg") ||
                   extension.endsWith(".png") ||
                   extension.endsWith(".doc") ||
                   extension.endsWith(".docx");
        }
        
        return false;
    }
}
