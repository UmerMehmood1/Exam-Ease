package com.picsart.studio.Instructor.Activities.fragmentForCourseDetailsActivity.bottomSheets

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.picsart.studio.Models.PDFModel
import com.picsart.studio.databinding.BottomSheetPdfInputBinding

class AddPdfToCourseBottomSheet(
    context: Context,
    private val courseId: String,
    private val onSave: (PDFModel) -> Unit
) : BottomSheetDialog(context) {

    private val binding = BottomSheetPdfInputBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {
            if (validateInputs()) {
                val pdf = PDFModel(
                    name = binding.editTextName.text.toString().trim(),
                    url = binding.editTextUrl.text.toString().trim(),
                    course_id = courseId
                )
                onSave(pdf)
                dismiss()
            }
        }
        show()
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val name = binding.editTextName.text.toString().trim()
        val url = binding.editTextUrl.text.toString().trim()

        if (name.isEmpty()) {
            binding.layoutPdfName.error = "Name is required"
            isValid = false
        } else {
            binding.layoutPdfName.error = null
        }

        if (url.isEmpty()) {
            binding.layoutPdfUrl.error = "URL is required"
            isValid = false
        } else {
            binding.layoutPdfUrl.error = null
        }

        return isValid
    }
}
