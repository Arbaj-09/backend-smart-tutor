# SmartTutor Backend

Smart Learning, Attendance & Assessment Platform Backend

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (Session-based authentication)
- **Spring Data JPA** (Hibernate)
- **MySQL** Database
- **Firebase Admin SDK** (Push notifications)
- **Maven** Build Tool

## Database Configuration

- **Host**: localhost
- **Port**: 3308
- **Database**: SmartTutor
- **Username**: root
- **Password**: root

## Project Structure

```
src/main/java/com/smarttutor/
├── SmartTutorApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   └── FirebaseConfig.java
├── controller/
│   ├── AuthController.java
│   ├── HodController.java
│   ├── TeacherController.java
│   ├── StudentController.java
│   ├── AttendanceController.java
│   ├── QuizController.java
│   ├── NoteController.java
│   ├── NotificationController.java
│   └── ActivityLogController.java
├── service/
│   ├── AuthService.java
│   ├── HodService.java
│   ├── TeacherService.java
│   ├── StudentService.java
│   ├── AttendanceService.java
│   ├── QuizService.java
│   ├── NoteService.java
│   ├── NotificationService.java
│   └── ActivityLogService.java
├── repository/
│   ├── HodRepository.java
│   ├── ClassRepository.java
│   ├── DivisionRepository.java
│   ├── TeacherRepository.java
│   ├── StudentRepository.java
│   ├── AttendanceRepository.java
│   ├── QuizRepository.java
│   ├── QuestionRepository.java
│   ├── QuizAttemptRepository.java
│   ├── NoteRepository.java
│   ├── NotificationRepository.java
│   ├── FcmTokenRepository.java
│   └── ActivityLogRepository.java
├── entity/
│   ├── Hod.java
│   ├── ClassEntity.java
│   ├── Division.java
│   ├── Teacher.java
│   ├── Student.java
│   ├── Attendance.java
│   ├── Note.java
│   ├── Quiz.java
│   ├── Question.java
│   ├── QuizAttempt.java
│   ├── Notification.java
│   ├── FcmToken.java
│   └── ActivityLog.java
├── dto/
│   ├── LoginRequestDTO.java
│   ├── LoginResponseDTO.java
│   ├── StudentDTO.java
│   ├── AttendanceDTO.java
│   ├── QuizResultDTO.java
│   └── DashboardStatsDTO.java
├── enums/
│   ├── Role.java
│   ├── AttendanceStatus.java
│   ├── DifficultyLevel.java
│   └── QuizStatus.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── AccessDeniedException.java
└── util/
    ├── DateUtil.java
    └── SecurityUtil.java
```

## Setup Instructions

### Prerequisites

1. **Java 17** or higher
2. **Maven** 3.6 or higher
3. **MySQL** Server running on port 3308

### Database Setup

1. Create MySQL database named `SmartTutor`
2. Update `application.properties` with your database credentials if different

### Firebase Setup

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Generate a service account key
3. Replace the content of `src/main/resources/firebase-service-account.json` with your service account JSON

### Running the Application

1. Navigate to the backend directory:
   ```bash
   cd Smart_Tutor/backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8082`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/current-user` - Get current user info

### HOD Endpoints
- `GET /api/hod/dashboard` - HOD dashboard stats
- `POST /api/hod/classes` - Create new class
- `GET /api/hod/classes` - Get all classes
- `POST /api/hod/divisions` - Create new division
- `GET /api/hod/divisions` - Get all divisions
- `POST /api/hod/teachers` - Create new teacher
- `GET /api/hod/teachers` - Get all teachers
- `PUT /api/hod/teachers/{id}` - Update teacher
- `DELETE /api/hod/teachers/{id}` - Delete teacher
- `GET /api/hod/students` - Get all students
- `GET /api/hod/activity-logs` - Get activity logs

### Teacher Endpoints
- `GET /api/teacher/dashboard` - Teacher dashboard stats
- `GET /api/teacher/students` - Get assigned students
- `POST /api/teacher/students` - Add new student
- `PUT /api/teacher/students/{id}` - Update student
- `DELETE /api/teacher/students/{id}` - Delete student

### Student Endpoints
- `GET /api/student/dashboard` - Student dashboard stats
- `GET /api/student/notes` - Get notes
- `GET /api/student/quizzes` - Get available quizzes

### Common Endpoints
- `GET /api/attendance` - Get attendance records
- `POST /api/attendance/mark` - Mark attendance
- `GET /api/notes` - Get notes
- `POST /api/notes/upload` - Upload notes
- `GET /api/quiz` - Get quizzes
- `POST /api/quiz/create` - Create quiz
- `POST /api/quiz/attempt/{id}` - Attempt quiz
- `GET /api/results` - Get quiz results
- `GET /api/notifications` - Get notifications
- `POST /api/notifications/fcm/token` - Save FCM token

## Security

- **Authentication**: Session-based authentication
- **Authorization**: Role-based access control (HOD, Teacher, Student)
- **Password Encryption**: BCrypt
- **CORS**: Configured for frontend integration

## Features Implemented

1. **Multi-role Authentication System**
2. **Academic Structure Management** (Classes, Divisions)
3. **User Management** (HOD, Teachers, Students)
4. **Attendance Management**
5. **Notes Management**
6. **Quiz & Assessment System**
7. **Firebase Push Notifications**
8. **Activity Logging**
9. **Dashboard Analytics**
10. **Role-based Access Control**

## Default Credentials

After running the application for the first time, you'll need to create HOD account manually through the database or use the API endpoints.

## Development

The application uses Spring Boot's development tools with hot reload enabled during development.
