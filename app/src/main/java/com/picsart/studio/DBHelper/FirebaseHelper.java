package com.picsart.studio.DBHelper;

import android.util.Log;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.picsart.studio.Models.Course;
import com.picsart.studio.Models.Question;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.Models.Quiz_Attempts;
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
    private static final String USER_ENROLLED_IN_COURSE_COLLECTION_NAME = "course_enrolled";
    private FirebaseFirestore firestore;
    private CollectionReference coursesCollection;
    private CollectionReference contentsCollection;
    private CollectionReference usersCollection;
    private CollectionReference quizzesCollection;
    private CollectionReference quizAttemptsCollection;
    private CollectionReference userEnrolledCollection;

    public FirebaseHelper() {
        firestore = FirebaseFirestore.getInstance();
        coursesCollection = firestore.collection(COURSES_COLLECTION_NAME);
        contentsCollection = firestore.collection(CONTENTS_COLLECTION_NAME);
        usersCollection = firestore.collection(USERS_COLLECTION_NAME);
        quizzesCollection = firestore.collection(QUIZ_COLLECTION_NAME);
        quizAttemptsCollection = firestore.collection(QUIZ_ATTEMPT_COLLECTION_NAME);
        userEnrolledCollection = firestore.collection(USER_ENROLLED_IN_COURSE_COLLECTION_NAME);
    }

    //  Function Implemented
    public Task<Void> updateCourse(String teacher_id, String courseId, Course course) {
        String collectionPath = "/users/" + teacher_id + "/course_created/" + courseId;
        DocumentReference courseRef = FirebaseFirestore.getInstance().document(collectionPath);

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

    public Task<DocumentReference> addUser(User user) {
        return usersCollection.add(user);
    }

    public Task<Course> getCourseById(String courseId) {
        String coursesCollectionPath = "courses";
        CollectionReference coursesCollection = firestore.collection(coursesCollectionPath);

        return coursesCollection.document(courseId).get().continueWith(new Continuation<DocumentSnapshot, Course>() {
            @Override
            public Course then(Task<DocumentSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Course course = document.toObject(Course.class);
                        return course;
                    } else {
                        throw new Exception("Course not found.");
                    }
                } else {
                    throw task.getException();
                }
            }
        });
    }

    public Task<List<Course>> getEnrolledCoursesByStudentId(String studentId) {
        // Collection path for course_enrolled
        String courseEnrolledCollectionPath = "course_enrolled";

        List<Task<Course>> courseTasks = new ArrayList<>();

        // Get all documents where student_id matches the provided studentId
        return firestore.collection(courseEnrolledCollectionPath)
                .whereEqualTo("student_id", studentId)
                .get()
                .continueWithTask(task -> {
                    // Handle the result of the initial query
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        // Process the enrollment documents to get the course IDs
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String courseId = document.getString("course_id");
                            if (courseId != null) {
                                // Set the Course ID to the document ID
                                Course course = new Course();
                                course.setId(document.getId());
                                // For each course ID, fetch the course details using getCourseById method
                                Task<Course> c = getCourseById(courseId, course);
                                courseTasks.add(c);
                            }
                        }

                        // Continue with the next task: combining all Course tasks into a single task
                        return Tasks.whenAllSuccess(courseTasks);
                    } else {
                        // Handle the failure case
                        Log.e("Firestore Error", "Error fetching enrolled courses", task.getException());
                        throw task.getException();
                    }
                });
    }

    private Task<Course> getCourseById(String courseId, Course course) {
        String coursesCollectionPath = "courses";

        // Get the document with the provided course ID
        return firestore.collection(coursesCollectionPath).document(courseId).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Convert the document to a Course object
                            Course fetchedCourse = document.toObject(Course.class);
                            // Set the courseId of the fetched course to its documentId
                            fetchedCourse.setId(document.getId());
                            return fetchedCourse;
                        } else {
                            // Handle the case where the course document does not exist
                            Log.d("Firestore Error", "Course document not found.");
                            return null;
                        }
                    } else {
                        // Handle the failure case
                        Log.e("Firestore Error", "Error fetching course", task.getException());
                        throw task.getException();
                    }
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
                        int totalQuizzes = documentSnapshot.getLong("totalQuizzes").intValue();
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
                        int totalQuizzes = document.getLong("totalQuizzes").intValue();
                        String description = document.getString("description");
                        int img = document.getLong("img").intValue();
                        String teacherId = document.getString("Teacher_id");

                        Course enrolledCourse = new Course(courseId, teacherId, courseName, courseCategory, duration, totalQuizzes, description, img);
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
        CollectionReference coursesRef = firestore.collection(COURSES_COLLECTION_NAME);

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
                int totalQuizzes = document.getLong("totalQuizzes").intValue();
                String description = document.getString("description");
                int img = document.getLong("img").intValue();
                String teacherId = document.getString("Teacher_id");

                Course enrolledCourse = new Course(courseId, teacherId, courseName, courseCategory, duration, totalQuizzes, description, img);
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

    public Task<DocumentReference> enrollStudentInCourse(String studentId, String courseId) {
        String courseEnrolledCollectionPath = "course_enrolled";
        CollectionReference courseEnrolledRef = firestore.collection(courseEnrolledCollectionPath);
        Map<String, Object> enrollmentData = new HashMap<>();
        enrollmentData.put("student_id", studentId);
        enrollmentData.put("course_id", courseId);
        return courseEnrolledRef.add(enrollmentData);
    }

    public Task<List<Quiz>> getQuizzesByCourseId(String courseId) {
        CollectionReference quizzesRef = FirebaseFirestore.getInstance().collection("quizzes");
        Query query = quizzesRef.whereEqualTo("course_id", courseId);

        return query.get().continueWith(task -> {
            List<Quiz> quizzes = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    // Manually extract data from the document and map it to Quiz object
                    String quizId = documentSnapshot.getId();
                    String quizName = documentSnapshot.getString("quiz_name");
                    int totalQuestions = documentSnapshot.getLong("total_questions").intValue();
                    String instructorId = documentSnapshot.getString("instructor_id");

                    List<Question> questions = new ArrayList<>();
                    List<Map<String, Object>> questionDataList = (List<Map<String, Object>>) documentSnapshot.get("questions");
                    if (questionDataList != null) {
                        for (Map<String, Object> questionData : questionDataList) {
                            String questionText = (String) questionData.get("question_text");
                            List<String> options = (List<String>) questionData.get("options");
                            Long correctOptionIndexLong = (Long) questionData.get("correct_option_index");
                            int correctOptionIndex = (correctOptionIndexLong != null) ? correctOptionIndexLong.intValue() : -1;

                            Question question = new Question(questionText, options, correctOptionIndex);
                            questions.add(question);
                        }
                    }

                    Quiz quiz = new Quiz(quizId, quizName, totalQuestions, courseId, instructorId, questions);
                    quizzes.add(quiz);
                }
            } else {
                // Handle error if the query fails
            }
            return quizzes;
        });
    }

    public void storeQuizAttempt(String userId, String courseId, String quizId, int score) {
        Map<String, Object> quizAttemptData = new HashMap<>();
        quizAttemptData.put("user_id", userId);
        quizAttemptData.put("course_id", courseId);
        quizAttemptData.put("quiz_id", quizId);
        quizAttemptData.put("score", score);

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

    //  Functions to implement
    public Task<List<Quiz_Attempts>> getQuizAttemptsByUserId(String userId) {
        CollectionReference quizAttemptsRef = firestore.collection("quiz_attempts");

        // Create a query to fetch documents where "user_id" is equal to the given userId
        Query query = quizAttemptsRef.whereEqualTo("user_id", userId);

        // Execute the query asynchronously and convert the Task<QuerySnapshot> to Task<List<Quiz_Attempts>>
        return query.get().continueWith(task -> {
            List<Quiz_Attempts> quizAttemptsList = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        // Convert the document data to your Quiz_Attempts model or read individual fields
                        String courseId = documentSnapshot.getString("course_id");
                        String quizId = documentSnapshot.getString("quiz_id");
                        String score = String.valueOf(documentSnapshot.getLong("score").intValue());

                        Quiz_Attempts quizAttempt = new Quiz_Attempts(courseId, quizId, score, userId);
                        quizAttemptsList.add(quizAttempt);
                    }
                }
            } else {
                // Log the exception
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("Firestore Query Error", "Error: " + exception.getMessage());
                }
            }
            return quizAttemptsList;
        });
    }

    public Task<Void> deleteQuiz(String quizId) {
        // Collection path for quizzes
        String quizzesCollectionPath = "quizzes";

        // Get the document reference for the quiz using the provided quizId
        DocumentReference quizRef = firestore.collection(quizzesCollectionPath).document(quizId);

        // Delete the quiz document using the document reference
        return quizRef.delete();
    }

    public Task<Void> updateQuiz(String quizId, String quiz_name, int Total_question, List<Question> options) {
        String quizzesCollectionPath = QUIZ_COLLECTION_NAME;
        DocumentReference quizRef = firestore.collection(quizzesCollectionPath).document(quizId);
        Map<String, Object> quizUpdates = new HashMap<>();
        quizUpdates.put("quiz_name", quiz_name);
        quizUpdates.put("total_questions", Total_question);
        quizUpdates.put("questions", options);
        return quizRef.update(quizUpdates);
    }

    public Task<List<Quiz_Attempts>> getQuizAttemptsByCourseId(String courseId) {
        CollectionReference quizAttemptsRef = firestore.collection(QUIZ_ATTEMPT_COLLECTION_NAME);

        Query query = quizAttemptsRef.whereEqualTo("course_id", courseId);

        return query.get().continueWith(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<Quiz_Attempts> quizAttemptsList = new ArrayList<>();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String user_id = document.getString("user_id");
                    String quiz_id = document.getString("quiz_id");
                    String score = document.getLong("score").toString();
                    // You can extract other fields from the document as well

                    Quiz_Attempts quizAttempt = new Quiz_Attempts(courseId, quiz_id, score, user_id);
                    quizAttemptsList.add(quizAttempt);
                }
                return quizAttemptsList;
            } else {
                // Handle the case when retrieving quiz attempts fails
                throw task.getException();
            }
        });
    }

    public Task<Quiz> getQuizByID_but_no_Questions(String quizId) {
        CollectionReference quizzesRef = firestore.collection("quizzes");
        DocumentReference quizRef = quizzesRef.document(quizId);

        return quizRef.get().continueWith(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Convert the document data to your Quiz model or read individual fields
                    String courseId = document.getString("course_id");
                    String instructorId = document.getString("instructor_id");
                    String quizName = document.getString("quiz_name");
                    int totalQuestions = document.getLong("total_questions").intValue();
                    List<Question> questions = new ArrayList<>();
                    // Assuming you have the Quiz model constructor
                    Quiz quiz = new Quiz(quizId, quizName, totalQuestions, courseId, instructorId, questions);
                    return quiz;
                } else {
                    // Handle the case when the quiz document does not exist
                    return null;
                }
            } else {
                // Handle the failure case
                throw task.getException();
            }
        });
    }
}
