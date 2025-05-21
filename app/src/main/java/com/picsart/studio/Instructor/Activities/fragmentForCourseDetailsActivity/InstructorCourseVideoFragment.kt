package com.picsart.studio.Student.Activities.fragmentForCourseDetailsActivity

import AddVideoToCourseBottomSheet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.picsart.studio.DBHelper.FirebaseHelper
import com.picsart.studio.Models.Video
import com.picsart.studio.Student.Activities.fragmentForCourseDetailsActivity.adapters.CourseVideoAdapter
import com.picsart.studio.Student.Activities.fragmentForCourseDetailsActivity.adapters.InstructorCourseVideoAdapter
import com.picsart.studio.databinding.FragmentCourseVideoBinding
import com.picsart.studio.databinding.InstructorCourseVideoFragmentBinding

class InstructorCourseVideoFragment(private val course_id:String) : Fragment() {
    private lateinit var binding: InstructorCourseVideoFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InstructorCourseVideoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseHelper().getVideosByCourseId(course_id).addOnCompleteListener { l: Task<List<Video?>> ->
            if (l.isSuccessful) {
                context?.let {
                    binding.videosRv.adapter = InstructorCourseVideoAdapter(it, l.result)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to fetch quizzes", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.addVideo.setOnClickListener {
            context?.let {
                AddVideoToCourseBottomSheet(it, course_id){ video->
                    FirebaseHelper().addVideoToCourse(course_id, video.title, video.description, video.url)
                    Toast.makeText(it, "Video Added. Please Re-open course to view updated content.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}