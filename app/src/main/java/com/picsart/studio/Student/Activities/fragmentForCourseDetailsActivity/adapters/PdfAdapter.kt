package com.picsart.studio.Student.Activities.fragmentForCourseDetailsActivity.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.picsart.studio.Models.PDFModel
import com.picsart.studio.databinding.ItemPdfBinding
import java.io.File
import java.io.FileOutputStream

class PdfAdapter(
    private val context: Context,
    private val pdfList: List<PDFModel?>
) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(val binding: ItemPdfBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val binding = ItemPdfBinding.inflate(LayoutInflater.from(context), parent, false)
        return PdfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdf = pdfList[position]
        holder.binding.pdfTitle.text = pdf?.name
        holder.binding.card.setOnClickListener {
            openUrlInBrowser(pdf?.url.toString())
        }
    }

    override fun getItemCount(): Int = pdfList.size
    private fun openUrlInBrowser(url:String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }
}

