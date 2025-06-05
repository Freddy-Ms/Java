# Java

# WebApp

### 1.Testy
![Zrzut ekranu 2025-06-03 131541](https://github.com/user-attachments/assets/fe158740-41d2-4c36-a154-91ba9b36a1e3)

### 2.Model bazy danych
![Zrzut ekranu 2025-06-03 134945](https://github.com/user-attachments/assets/e7b63a8f-ea78-4d09-b0e8-6e640234ffed)
```

/**
 * Model bazy danych do systemu zarządzania kursami online.
 */

enum CourseLevel {
    BEGINNER, INTERMEDIATE, ADVANCED
}

enum Gender {
    MALE, FEMALE, OTHER
}

entity Instructor {
    firstName String
    lastName String
    email String
    bio String
    hireDate Instant
    rating Float
}

entity Course {
    title String required
    description String
    startDate LocalDate
    endDate LocalDate
    level CourseLevel
    price Float
}

entity Student {
    firstName String
    lastName String
    birthDate LocalDate
    gender Gender
    email String
    registrationDate Instant
}

entity StudentProfile {
    bio String
    linkedinUrl String
    githubUrl String
    profilePictureUrl String
}

// Relacje
relationship OneToMany {
    Instructor to Course{instructor}
}

relationship ManyToMany {
    Course{student(firstName)} to Student{course}
}

relationship OneToOne {
    Student{profile} to StudentProfile
}

// Paginacja
paginate Course, Instructor, Student, StudentProfile with pagination

// DTO i serwisy
dto * with mapstruct
service all with serviceImpl
angularSuffix * with App
```
### 3. Wyglądy encji
![Zrzut ekranu 2025-06-03 135139](https://github.com/user-attachments/assets/fac830e3-94c8-4754-b475-2f7aa7d21b84)
![Zrzut ekranu 2025-06-03 135253](https://github.com/user-attachments/assets/cade004c-f6d1-4d55-be3d-c73726fcc78f)
![Zrzut ekranu 2025-06-03 135151](https://github.com/user-attachments/assets/a306f30a-86a8-4d4a-87f8-e6630631a080)
![Zrzut ekranu 2025-06-03 135302](https://github.com/user-attachments/assets/02087b42-e123-4bcf-8d3f-4b7849cdde81)
![Zrzut ekranu 2025-06-03 135258](https://github.com/user-attachments/assets/1426f872-5fc4-45f9-a593-4728643585c3)
