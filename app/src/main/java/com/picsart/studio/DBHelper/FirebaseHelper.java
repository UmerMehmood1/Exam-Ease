package com.picsart.studio.DBHelper;

import android.util.Log;
import java.util.concurrent.CompletableFuture;
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
import com.picsart.studio.Models.PDFModel;
import com.picsart.studio.Models.Question;
import com.picsart.studio.Models.Quiz;
import com.picsart.studio.Models.Quiz_Attempts;
import com.picsart.studio.Models.User;
import com.picsart.studio.Models.Video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FirebaseHelper {
    private static final String COURSES_COLLECTION_NAME = "courses";
    private static final String CONTENTS_COLLECTION_NAME = "contents";
    private static final String USERS_COLLECTION_NAME = "users";
    private static final String QUIZ_COLLECTION_NAME = "quizzes";
    private static final String QUIZ_ATTEMPT_COLLECTION_NAME = "quiz_attempts";
    private static final String USER_ENROLLED_IN_COURSE_COLLECTION_NAME = "course_enrolled";
    private static final String VIDEOS_COLLECTION_NAME = "videos";
    private static final String PDFS_COLLECTION_NAME = "pdfs";
    private CollectionReference pdfsCollection;
    private CollectionReference videosCollection;
    private final FirebaseFirestore firestore;
    private final CollectionReference coursesCollection;
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
        videosCollection = firestore.collection(VIDEOS_COLLECTION_NAME);
        pdfsCollection = firestore.collection(PDFS_COLLECTION_NAME);

    }
    public Task<DocumentReference> addPDFToCourse(String courseId, PDFModel pdfModel) {
        Map<String, Object> pdfData = new HashMap<>();
        pdfData.put("name", pdfModel.getName());
        pdfData.put("url", pdfModel.getUrl());
        pdfData.put("course_id", courseId);
        return pdfsCollection.add(pdfData);
    }
    public Task<List<PDFModel>> getPDFsByCourseId(String courseId) {
        return pdfsCollection.whereEqualTo("course_id", courseId)
                .get()
                .continueWith(task -> {
                    List<PDFModel> pdfs = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            String name = doc.getString("name");
                            String url = doc.getString("url");
                            String course_id = doc.getString("course_id");
                            pdfs.add(new PDFModel(name, url, course_id));
                        }
                    }
                    return pdfs;
                });
    }

    public Task<DocumentReference> addVideoToCourse(String courseId, String title, String description, String url) {
        Map<String, Object> videoData = new HashMap<>();
        videoData.put("course_id", courseId);
        videoData.put("title", title);
        videoData.put("description", description);
        videoData.put("url", url);

        return videosCollection.add(videoData);
    }
    public Task<List<Video>> getVideosByCourseId(String courseId) {
        return videosCollection.whereEqualTo("course_id", courseId)
                .get()
                .continueWith(task -> {
                    List<Video> videos = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Video video = document.toObject(Video.class);
                            videos.add(video);
                        }
                    }
                    return videos;
                });
    }

    public Task<User> getUserByID(String user_id) {
        DocumentReference userRef = usersCollection.document(user_id);
        return userRef.get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            return user;
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                });
    }
    public Task<Void> updateCourse(String courseId, Course course) {
        String collectionPath = COURSES_COLLECTION_NAME+"/"+courseId;
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
                return null;
            }
        });
    }
    public Task<DocumentReference> addUser(User user) {
        return usersCollection.add(user);
    }
    public Task<List<Course>> getEnrolledCoursesByStudentId(String studentId) {
        String courseEnrolledCollectionPath = "course_enrolled";

        return firestore.collection(courseEnrolledCollectionPath)
                .whereEqualTo("student_id", studentId)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult(); // Get the QuerySnapshot
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments(); // Get all documents

                        List<Task<Course>> courseTasks = new ArrayList<>();
                        for (DocumentSnapshot document : documents) {
                            String courseId = document.getString("course_id");
                            if (courseId != null) {
                                courseTasks.add(getCourseById(courseId));
                            }
                        }
                        // Use Tasks.whenAllSuccess to wait for all course tasks to complete
                        return Tasks.whenAllSuccess(courseTasks);
                    } else {
                        Log.e("Firestore Error", "Error fetching enrolled courses", task.getException());
                        throw task.getException();
                    }
                });
    }
    private Task<Course> getCourseById(String courseId) {
        String coursesCollectionPath = COURSES_COLLECTION_NAME;
        return firestore.collection(coursesCollectionPath).document(courseId).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Course fetchedCourse = document.toObject(Course.class);
                            fetchedCourse.setId(document.getId());
                            return fetchedCourse;
                        } else {
                            Log.d("Firestore Error", "Course document not found.");
                            return null;
                        }
                    } else {
                        Log.e("Firestore Error", "Error fetching course", task.getException());
                        throw task.getException();
                    }
                });
    }
    public Task<List<Course>> getCoursesByTeacherId(String teacherId) {
        CollectionReference coursesRef = firestore.collection("courses");
        Query query = coursesRef.whereEqualTo("teacher_id", teacherId);
        return query.get().continueWith(task -> {
            List<Course> courses = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
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
        TaskCompletionSource<List<Course>> taskCompletionSource = new TaskCompletionSource<>();
        coursesEnrolledRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<Course> courses = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
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
                    taskCompletionSource.setResult(courses);
                } else {
                    taskCompletionSource.setResult(new ArrayList<>());
                }
            } else {
                Exception e = task.getException();
                if (e != null) {
                    taskCompletionSource.setException(e);
                }
            }
        });
        return taskCompletionSource.getTask();
    }
    public Task<List<Course>> findCoursesByName(String searchTerm) {
        CollectionReference coursesRef = firestore.collection(COURSES_COLLECTION_NAME);
        Query query = coursesRef.whereEqualTo("name", searchTerm);
        return query.get().continueWith(task -> {
            List<Course> courses = new ArrayList<>();
            QuerySnapshot querySnapshot = task.getResult();
            for (DocumentSnapshot document : querySnapshot) {
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
    public Task<String> addCourse(Course course) {
        DocumentReference docRef = coursesCollection.document(); // Generate document reference
        String generatedId = docRef.getId();
        course.setId(generatedId); // Set ID in course model

        // Return a task that completes with the document ID
        return docRef.set(course)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        task.getException(); // Propagate error
                    }
                    return Tasks.forResult(generatedId); // Return the document ID
                });
    }


    public Task<DocumentReference> enrollStudentInCourse(String studentId, String courseId) {
        String courseEnrolledCollectionPath = "course_enrolled";
        CollectionReference courseEnrolledRef = firestore.collection(courseEnrolledCollectionPath);

        Query query = courseEnrolledRef
                .whereEqualTo("student_id", studentId)
                .whereEqualTo("course_id", courseId);

        return query.get().continueWithTask(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                return Tasks.forResult(null);
            } else {
                Map<String, Object> enrollmentData = new HashMap<>();
                enrollmentData.put("student_id", studentId);
                enrollmentData.put("course_id", courseId);
                return courseEnrolledRef.add(enrollmentData);
            }
        });
    }
    public Task<List<Quiz>> getQuizzesByCourseId(String courseId) {
        CollectionReference quizzesRef = FirebaseFirestore.getInstance().collection("quizzes");
        Query query = quizzesRef.whereEqualTo("course_id", courseId);
        return query.get().continueWith(task -> {
            List<Quiz> quizzes = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                    String quizId = documentSnapshot.getId();
                    String quizName = documentSnapshot.getString("quiz_name");
                    int totalQuestions = documentSnapshot.getLong("total_questions").intValue();
                    String instructorId = documentSnapshot.getString("instructor_id");
                    List<Question> questions = new ArrayList<>();
                    List<Map<String, Object>> questionDataList = (List<Map<String, Object>>) documentSnapshot.get("questions");
                    if (questionDataList != null) {
                        for (Map<String, Object> questionData : questionDataList) {
                            String question_Text = (String) questionData.get("question_text");
                            List<String> options = (List<String>) questionData.get("options");
                            Long correctOptionIndexLong = (Long) questionData.get("correct_option_index");
                            int correctOptionIndex = (correctOptionIndexLong != null) ? correctOptionIndexLong.intValue() : -1;

                            Question question = new Question(question_Text, options, correctOptionIndex);
                            questions.add(question);
                        }
                    }
                    Quiz quiz = new Quiz(quizId, quizName, totalQuestions, courseId, instructorId, questions);
                    quizzes.add(quiz);
                }
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
        List<Map<String, Object>> questionsData = new ArrayList<>();
        for (Question question : questions) {
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("question_text", question.getQuestionText());
            questionData.put("options", question.getOptions());
            questionData.put("correct_option_index", question.getCorrectOptionIndex());
            questionsData.add(questionData);
            Log.d("Bug Detector", "Options are: "+question.getOptions().get(0)+", "+question.getOptions().get(1)+", "+question.getOptions().get(2)+", "+question.getOptions().get(3)+", ");
        }
        quizData.put("questions", questionsData); // Add the list of questions to the quizData
        quizzesCollection.add(quizData);
    }
    public Task<List<Quiz_Attempts>> getQuizAttemptsByUserId(String userId, String course_id) {
        CollectionReference quizAttemptsRef = firestore.collection("quiz_attempts");
        Query query = quizAttemptsRef.whereEqualTo("user_id", userId).whereEqualTo("course_id", course_id);
        return query.get().continueWith(task -> {
            List<Quiz_Attempts> quizAttemptsList = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        String quizId = documentSnapshot.getString("quiz_id");
                        String score = String.valueOf(documentSnapshot.getLong("score").intValue());
                        Quiz_Attempts quizAttempt = new Quiz_Attempts(course_id, quizId, score, userId);
                        quizAttemptsList.add(quizAttempt);
                    }
                }
            }
            return quizAttemptsList;
        });
    }
    public Task<Void> deleteQuiz(String quizId) {
        String quizzesCollectionPath = "quizzes";
        DocumentReference quizRef = firestore.collection(quizzesCollectionPath).document(quizId);
        return quizRef.delete();
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
                    Quiz_Attempts quizAttempt = new Quiz_Attempts(courseId, quiz_id, score, user_id);
                    quizAttemptsList.add(quizAttempt);
                }
                return quizAttemptsList;
            } else {
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
                    String courseId = document.getString("course_id");
                    String instructorId = document.getString("instructor_id");
                    String quizName = document.getString("quiz_name");
                    int totalQuestions = document.getLong("total_questions").intValue();
                    List<Question> questions = new ArrayList<>();
                    Quiz quiz = new Quiz(quizId, quizName, totalQuestions, courseId, instructorId, questions);
                    return quiz;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        });
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
}
