import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.picsart.studio.Models.Video
import com.picsart.studio.databinding.BottomSheetVideoInputBinding

class AddVideoToCourseBottomSheet(
    context: Context,
    private val courseId: String,
    private val onSave: (Video) -> Unit
) : BottomSheetDialog(context) {

    private val binding = BottomSheetVideoInputBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {
            if (validateInputs()) {
                val video = Video(binding.editTextTitle.text.toString().trim(), binding.editTextDescription.text.toString().trim(), binding.editTextUrl.text.toString().trim(), courseId)
                onSave(video)
                dismiss()
            }
        }
        show()
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val title = binding.editTextTitle.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()
        val url = binding.editTextUrl.text.toString().trim()

        // Title
        if (title.isEmpty()) {
            binding.editTextTitle.error = "Title is required"
            isValid = false
        } else {
            binding.editTextTitle.error = null
        }

        // Description
        if (description.isEmpty()) {
            binding.editTextDescription.error = "Description is required"
            isValid = false
        } else {
            binding.editTextDescription.error = null
        }

        // URL
        if (url.isEmpty()) {
            binding.editTextUrl.error = "URL is required"
            isValid = false
        } else {
            binding.editTextUrl.error = null
        }

        return isValid
    }
}
