package com.picsart.studio.DBHelper

import android.os.Handler
import android.os.Looper
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.picsart.studio.Models.Course
import com.picsart.studio.Models.Question
import com.picsart.studio.Models.Quiz
import com.picsart.studio.Models.Quiz_Attempts
import com.picsart.studio.Models.User


class DummyDataGenerator {
    private val firestore = FirebaseFirestore.getInstance()

    fun generateDummyData() {
        val firebaseHelper = FirebaseHelper()


// 1. Create Teacher and Student Users
        val teacher = User("Mr. Teacher", "img1", "Gold", "1985-06-15", "teach123", "teacher")
        val student = User("Student One", "img2", "Silver", "2000-03-10", "stud123", "student")


// Add users to Firestore
        firebaseHelper.addUser(teacher).addOnSuccessListener { teacherRef: DocumentReference ->
            val teacherId = teacherRef.id
            teacher.id = teacherId
            firebaseHelper.addUser(student)
                .addOnSuccessListener { studentRef: DocumentReference ->
                    val studentId = studentRef.id
                    student.id = studentId

                    // 2. Create Course by Teacher
                    val course =
                        Course(
                            "Intro to Java",
                            "Programming",
                            "10h",
                            1,
                            "Java basics course",
                            1,
                            teacherId
                        )
                    firebaseHelper.addCourse(course)
                        .addOnSuccessListener { courseId: String ->
                            // 3. Add Video to Course
                            firebaseHelper.addVideoToCourse(
                                courseId,
                                "Java Basics Video",
                                "Introduction to Java",
                                "https://video.example.com/java"
                            )

                            // 4. Add Quiz to Course with Questions
                            val options: MutableList<String> =
                                ArrayList()
                            options.add("int")
                            options.add("float")
                            options.add("char")
                            options.add("double")

                            val questions: MutableList<Question> =
                                ArrayList()
                            questions.add(
                                Question(
                                    "What is a valid data type in Java?",
                                    options,
                                    0
                                )
                            )

                            firebaseHelper.addQuiz(
                                "Java Quiz 1",
                                questions.size,
                                courseId,
                                teacherId,
                                questions
                            )

                            // 5. Wait a little before storing quiz attempt (you can retrieve quiz ID properly in production)
                            Handler(Looper.getMainLooper()).postDelayed({
                                firebaseHelper.getQuizzesByCourseId(courseId)
                                    .addOnSuccessListener { quizzes: List<Quiz> ->
                                        if (!quizzes.isEmpty()) {
                                            val quiz = quizzes[0]
                                            // 6. Student attempts quiz
                                            firebaseHelper.storeQuizAttempt(
                                                studentId,
                                                courseId,
                                                quiz.quizId,
                                                90
                                            )
                                        }
                                    }
                            }, 3000)

                            // 7. Enroll Student in Course
                            firebaseHelper.enrollStudentInCourse(studentId, courseId)
                        }
                }
        }

    }

}
