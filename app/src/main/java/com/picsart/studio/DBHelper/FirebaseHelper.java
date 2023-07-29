package com.picsart.studio.DBHelper;

import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.picsart.studio.Models.Course;
import com.picsart.studio.Models.Question;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.Models.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    // Collection names
    private static final String COURSES_COLLECTION_NAME = "courses";
    private static final String CONTENTS_COLLECTION_NAME = "contents";
    private static final String USERS_COLLECTION_NAME = "users";
    private static final String QUIZ_COLLECTION_NAME = "quizzes";
    private static final String QUIZ_ATTEMPT_COLLECTION_NAME = "quiz_attempts";
    private FirebaseFirestore firestore;
    private CollectionReference coursesCollection;
    private CollectionReference contentsCollection;
    private CollectionReference usersCollection;
    private CollectionReference quizzesCollection;
    private CollectionReference quizAttemptsCollection;
    public FirebaseHelper() {
        firestore = FirebaseFirestore.getInstance();
        coursesCollection = firestore.collection(COURSES_COLLECTION_NAME);
        contentsCollection = firestore.collection(CONTENTS_COLLECTION_NAME);
        usersCollection = firestore.collection(USERS_COLLECTION_NAME);
        quizzesCollection = firestore.collection(QUIZ_COLLECTION_NAME);
        quizAttemptsCollection = firestore.collection(QUIZ_ATTEMPT_COLLECTION_NAME);
    }
//  Function Implemented
    public Task<Void> updateCourse(String teacher_id,String courseId, Course course) {
        String collectionPath = "/users/"+teacher_id+"/course_created/" + courseId;
        DocumentReference courseRef = FirebaseFirestore.getInstance().document(collectionPath );

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", course.getName());
        updates.put("category", course.getCategory());
        updates.put("description", course.getDescription());
        updates.put("duration", course.getDuration());
        updates.put("img", course.getImg());
        updates.put("totalQuizzes", course.getTotalQuizzes());

        return courseRef.update(updates);
    }
    public Task<List<DocumentSnapshot>> getUsersByNameAndPassword(String name, String password) {
        CollectionReference usersCollection = firestore.collection("users");

        Query query = usersCollection
                .whereEqualTo("name", name)
                .whereEqualTo("password", password);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                return task.getResult().getDocuments();
            } else {
                // Handle error if the query fails
                return null;
            }
        });
    }
    public Task<DocumentSnapshot> getUserById(String userId) {
        // Get the reference to the document for the specified user ID
        DocumentReference userRef = firestore.collection("users").document(userId);
        // Fetch the document and return the Task<DocumentSnapshot>
        return userRef.get();
    }
    public Task<DocumentSnapshot> getCourseById(String courseId) {
        DocumentReference courseRef = firestore.collection("courses").document(courseId);
        return courseRef.get();
    }
    public Task<DocumentReference> addUser(User user) {
        return usersCollection.add(user);
    }
    public Task<List<Course>> getCoursesForStudent(String studentId) {
        String collectionPath = "/users/" + studentId + "/enrolled_in";
        CollectionReference enrolledCoursesRef = FirebaseFirestore.getInstance().collection(collectionPath);

        return enrolledCoursesRef.get().continueWith(task -> {
            List<Course> courses = new ArrayList<>();

            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String courseId = document.getId();
                        String courseName = document.getString("name");
                        String courseCategory = document.getString("category");
                        String description = document.getString("description");
                        String duration = document.getString("duration");
                        int img = document.getLong("img").intValue();
                        String teacher_id = document.getString("teacher_id");
                        String t_quiz = document.getString("totalQuizzes");

                        Course course = new Course(courseId, teacher_id, courseName, courseCategory, duration, Integer.valueOf(t_quiz), description, img);
                        courses.add(course);
                    }
                }
            }

            return courses;
        });
    }

    public Task<List<Course>> getCoursesByTeacherId(String teacherId) {
        // Reference to the "courses" collection
        CollectionReference coursesRef = firestore.collection("courses");

        // Create a query to fetch documents where "teacher_id" is equal to the given teacherId
        Query query = coursesRef.whereEqualTo("teacher_id", teacherId);

        // Execute the query asynchronously and convert the Task<QuerySnapshot> to Task<List<Course>>
        return query.get().continueWith(task -> {
            List<Course> courses = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        // Extract the data from the document and create a Course object
                        String courseId = documentSnapshot.getId();
                        String category = documentSnapshot.getString("category");
                        String description = documentSnapshot.getString("description");
                        String duration = documentSnapshot.getString("duration");
                        String name = documentSnapshot.getString("name");
                        int img = documentSnapshot.getLong("img").intValue();
                        int totalQuizzes = Integer.parseInt(documentSnapshot.getString("totalQuizzes"));
                        courses.add(new Course(courseId, teacherId, name, category, duration, totalQuizzes, description, img));
                    }
                }
            } else {
                // Log the exception
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("Firestore Query Error", "Error: " + exception.getMessage());
                }
            }
            return courses;
        });
    }
    public Task<List<Course>> getAllCoursesAvailable() {
        String collectionPath = COURSES_COLLECTION_NAME;
        CollectionReference coursesEnrolledRef = firestore.collection(collectionPath);

        // Create a TaskCompletionSource to handle the asynchronous result
        TaskCompletionSource<List<Course>> taskCompletionSource = new TaskCompletionSource<>();

        coursesEnrolledRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<Course> courses = new ArrayList<>();

                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // Convert the document data to your Course model or read individual fields
                        String courseId = document.getId();
                        String courseName = document.getString("name");
                        String courseCategory = document.getString("category");
                        String duration = document.getString("duration");
                        String totalQuizzes = document.getString("totalQuizzes");
                        String description = document.getString("description");
                        int img = document.getLong("img").intValue();
                        String teacherId = document.getString("Teacher_id");

                        Course enrolledCourse = new Course(courseId, teacherId, courseName, courseCategory, duration, Integer.valueOf(totalQuizzes), description, img);
                        courses.add(enrolledCourse);
                    }

                    // Resolve the task with the list of courses
                    taskCompletionSource.setResult(courses);
                } else {
                    // Resolve the task with an empty list if there are no courses
                    taskCompletionSource.setResult(new ArrayList<>());
                }
            } else {
                // Handle any errors that may occur during retrieval
                Exception e = task.getException();
                if (e != null) {
                    taskCompletionSource.setException(e);
                }
            }
        });

        return taskCompletionSource.getTask();
    }
    public Task<List<Course>> findCoursesByName(String searchTerm) {
        // Get a reference to the "courses_enrolled" collection
        CollectionReference coursesRef = firestore.collection("courses_enrolled");

        // Create a query to find courses where the name contains the given search term
        Query query = coursesRef.whereEqualTo("name", searchTerm);

        // Execute the query and return the Task to handle the result
        return query.get().continueWith(task -> {
            List<Course> courses = new ArrayList<>();
            QuerySnapshot querySnapshot = task.getResult();

            // Iterate through the documents in the QuerySnapshot
            for (DocumentSnapshot document : querySnapshot) {
                // Convert the document data to your Course model or read individual fields
                String courseId = document.getId();
                String courseName = document.getString("name");
                String courseCategory = document.getString("category");
                String duration = document.getString("duration");
                String totalQuizzes = document.getString("totalQuizzes");
                String description = document.getString("description");
                int img = document.getLong("img").intValue();
                String teacherId = document.getString("Teacher_id");

                Course enrolledCourse = new Course(courseId, teacherId, courseName, courseCategory, duration, Integer.valueOf(totalQuizzes), description, img);
                courses.add(enrolledCourse);
            }

            return courses;
        });
    }
    public Task<DocumentReference> addCourse(Course course) {
        String collectionPath = COURSES_COLLECTION_NAME;
        CollectionReference coursesCreatedRef = firestore.collection(collectionPath);
        return coursesCreatedRef.add(course);
    }

    public Task<Void> enrollStudentInCourse(String studentId, String courseId) {
        String courseCollectionPath = "/courses/" + courseId + "/StudentEnrolled";
        CollectionReference courseEnrolledRef = firestore.collection(courseCollectionPath);

        String userCollectionPath = "/users/" + studentId + "/enrolled_in";
        CollectionReference userEnrolledRef = firestore.collection(userCollectionPath);

        Task<DocumentSnapshot> studentTask = getUserById(studentId);
        Task<DocumentSnapshot> courseTask = getCourseById(courseId);

        return Tasks.whenAllSuccess(studentTask, courseTask)
                .continueWithTask(task -> {
                    DocumentSnapshot studentSnapshot = studentTask.getResult();
                    DocumentSnapshot courseSnapshot = courseTask.getResult();

                    if (studentSnapshot.exists() && courseSnapshot.exists()) {
                        WriteBatch batch = firestore.batch();
                        batch.set(courseEnrolledRef.document(studentId), studentSnapshot.getData());
                        batch.set(userEnrolledRef.document(courseId), courseSnapshot.getData());
                        return batch.commit();
                    } else {
                        throw new Exception("Student or Course not found.");
                    }
                });
    }
    private void addQuizReferenceToCourse(String courseId, DocumentReference quizRef) {
        coursesCollection.document(courseId)
                .update("quizzes", FieldValue.arrayUnion(quizRef))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Quiz collection from Firebase:", "Quiz reference added to the course document");
                        } else {
                            Log.e("Quiz collection from Firebase:", "Error updating course document", task.getException());
                        }
                    }
                });
    }
    public Task<List<Quiz>> getCourseQuizzes(String courseId) {
        final TaskCompletionSource<List<Quiz>> taskCompletionSource = new TaskCompletionSource<>();

        coursesCollection.document(courseId)
                .collection("quizzes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Quiz> quizzes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Quiz quiz = document.toObject(Quiz.class);
                            quizzes.add(quiz);
                        }
                        taskCompletionSource.setResult(quizzes);
                    } else {
                        taskCompletionSource.setException(task.getException());
                    }
                });

        return taskCompletionSource.getTask();
    }
    //  Functions to implement

    public void storeQuizAttempt(String userId, String courseId, String quizId, int score) {
        Map<String, Object> quizAttemptData = new HashMap<>();
        quizAttemptData.put("user_id", userId);
        quizAttemptData.put("course_id", courseId);
        quizAttemptData.put("quiz_id", quizId);
        quizAttemptData.put("score", score);
        // Add other quiz attempt data as needed (e.g., timestamp)

        quizAttemptsCollection.add(quizAttemptData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Log.d("Quiz collection from Firebase:", "Quiz attempt stored with ID: " + task.getResult().getId());
                        } else {
                            Log.e("Quiz collection from Firebase:", "Error storing quiz attempt", task.getException());
                        }
                    }
                });
    }

    public Task<Void> deleteCourse(String teacher_id, String courseId) {
        String courseCollectionPath = "/courses/" + courseId + "/quizzes";
        String crs_quiz = "/users/" + teacher_id + "/course_created/" + courseId + "/quizzes";
        DocumentReference courseRef = FirebaseFirestore.getInstance().document(courseCollectionPath);
        courseRef.delete();
        DocumentReference courseRef1 = FirebaseFirestore.getInstance().document(crs_quiz);
        return courseRef1.delete();
}

    public void addQuiz(String Quiz_name, int Total_questions, String courseId, String instructorId, List<Question> questions) {
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("course_id", courseId);
        quizData.put("instructor_id", instructorId);
        quizData.put("quiz_name", Quiz_name);
        quizData.put("total_questions", Total_questions);
        // Add other quiz-related data as needed (e.g., questions)

        // Convert the list of Question objects to a list of Map<String, Object>
        List<Map<String, Object>> questionsData = new ArrayList<>();
        for (Question question : questions) {
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("question_text", question.getQuestionText());
            questionData.put("options", question.getOptions());
            questionData.put("correct_option_index", question.getCorrectOptionIndex());
            questionsData.add(questionData);
        }

        quizData.put("questions", questionsData); // Add the list of questions to the quizData

        quizzesCollection.add(quizData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference quizRef = task.getResult();
                        } else {
                        }
                    }
                });
    }


    public void getUserQuizAttempts(String userId, OnCompleteListener<QuerySnapshot> listener) {
        quizAttemptsCollection.whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(listener);
    }

    public Task<List<Quiz>> getQuizzesByCourseAndInstructorId(String courseId, String instructorId) {
        // Reference to the "quizzes" collection
        CollectionReference quizzesRef = firestore.collection("quizzes");

        // Create a query to fetch documents where "course_id" is equal to the given courseId
        // and "instructor_id" is equal to the given instructorId
        Query query = quizzesRef
                .whereEqualTo("course_id", courseId)
                .whereEqualTo("instructor_id", instructorId);

        // Execute the query asynchronously and convert the Task<QuerySnapshot> to Task<List<Quiz>>
        return query.get().continueWith(task -> {
            List<Quiz> quizzes = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        String quizId = documentSnapshot.getId();
                        String quizName = documentSnapshot.getString("quiz_name");
                        int totalQuestions = documentSnapshot.getLong("total_questions").intValue();
                        List<Question> questions = documentSnapshot.toObject(Quiz.class).getQuestions();
                        quizzes.add(new Quiz(quizId, quizName, totalQuestions, courseId, instructorId, questions));
                    }
                }
            } else {
                // Log the exception
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("Firestore Query Error", "Error: " + exception.getMessage());
                }
            }
            return quizzes;
        });
    }

}
