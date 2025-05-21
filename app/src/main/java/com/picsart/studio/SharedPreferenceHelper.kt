package com.picsart.studio

import android.content.Context
import android.content.SharedPreferences
import com.picsart.studio.Models.User

object SharedPreferenceHelper {

    private const val STUDENT_PREFS = "student_data"
    private const val TEACHER_PREFS = "teacher_data"

    fun saveStudentData(context: Context, student: User, id: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(STUDENT_PREFS, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("id", id)
            putString("name", student.name)
            putString("img", student.img)
            putString("badge", student.badge)
            putString("dob", student.dob)
            apply()
        }
    }

    fun getStudentData(context: Context): User? {
        val prefs: SharedPreferences = context.getSharedPreferences(STUDENT_PREFS, Context.MODE_PRIVATE)
        val id = prefs.getString("id", null) ?: return null
        val name = prefs.getString("name", null)
        val img = prefs.getString("img", null)
        val badge = prefs.getString("badge", null)
        val dob = prefs.getString("dob", null)
        if (name != null && img != null && badge != null && dob != null){
            return User(id, name ?: "", img ?: "", badge ?: "", dob ?: "", "", "student")
        }
        return null
    }

    fun saveTeacherData(context: Context, teacher: User, id: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(TEACHER_PREFS, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("id", id)
            putString("name", teacher.name)
            putString("img", teacher.img)
            putString("badge", teacher.badge)
            putString("dob", teacher.dob)
            apply()
        }
    }

    fun getTeacherData(context: Context): User? {
        val prefs: SharedPreferences = context.getSharedPreferences(TEACHER_PREFS, Context.MODE_PRIVATE)
        val id = prefs.getString("id", null) ?: return null
        val name = prefs.getString("name", null)
        val img = prefs.getString("img", null)
        val badge = prefs.getString("badge", null)
        val dob = prefs.getString("dob", null)
        if (name != null && img != null && badge != null && dob != null){
            return User(id, name ?: "", img ?: "", badge ?: "", dob ?: "", "", "teacher")
        }
        return null
    }
}
