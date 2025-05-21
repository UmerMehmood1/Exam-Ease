package com.picsart.studio.Instructor.Activities.fragmentForCourseDetailsActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.picsart.studio.DBHelper.FirebaseHelper
import com.picsart.studio.Instructor.Activities.fragmentForCourseDetailsActivity.bottomSheets.AddPdfToCourseBottomSheet
import com.picsart.studio.Models.PDFModel
import com.picsart.studio.Student.Activities.fragmentForCourseDetailsActivity.adapters.PdfAdapter
import com.picsart.studio.databinding.FragmentInstructorPastPaperBinding

class InstructorPastPaperFragment(private val course_id: String) : Fragment() {
    private lateinit var binding: FragmentInstructorPastPaperBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstructorPastPaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseHelper().getPDFsByCourseId(course_id).addOnCompleteListener { l: Task<List<PDFModel?>> ->
            if (l.isSuccessful) {
                context?.let {
                    binding.pastPaperRV.adapter = PdfAdapter(it,l.result)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to fetch quizzes", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        context?.let { context->
            binding.addPdf.setOnClickListener {
                AddPdfToCourseBottomSheet(context, course_id){
                    FirebaseHelper().addPDFToCourse(course_id, it)
                    Toast.makeText(context, "Video Added. Please Re-open course to view updated content.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}