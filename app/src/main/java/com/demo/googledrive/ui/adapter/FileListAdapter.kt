package com.demo.googledrive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.utils.mimetype.toFileType
import com.demo.drive.data.GlideApp
import com.demo.googledrive.R
import com.demo.googledrive.databinding.ListItemFileBinding
import com.demo.googledrive.utils.getIcon
import com.demo.googledrive.utils.toHumanReadableSize

class FileListAdapter :
    PagingDataAdapter<DriveFile, FileListAdapter.ViewHolder>(FilesDiffCallback) {

    private var onItemClick: (driveFile: DriveFile) -> Unit = {}
    private var onMenuItemClick: (driveFile: DriveFile, itemId: Int) -> Unit = { _, _ -> }

    inner class ViewHolder(private val binding: ListItemFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DriveFile) {
            binding.textViewName.text = item.name

            val size = item.size.toHumanReadableSize(binding.textViewSize.context)
            binding.textViewSize.isVisible = !size.isNullOrEmpty()
            binding.textViewSize.text = size

            if (item.thumbnailLink != null) {
                GlideApp.with(binding.imageViewPreview.context)
                    .load(item.thumbnailLink)
                    .into(binding.imageViewPreview)
            } else {
                binding.imageViewPreview.setImageResource(item.mimeType.toFileType().getIcon())
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
            binding.imageViewMore.setOnClickListener {
                val popup = PopupMenu(it.context, it)
                popup.menuInflater.inflate(R.menu.drive_file_options, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    onMenuItemClick(item, menuItem.itemId)
                    true
                }
                popup.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun onItemClick(onItemClick: (driveFile: DriveFile) -> Unit) {
        this.onItemClick = onItemClick
    }

    fun onMenuItemClick(onMenuItemClick: (driveFile: DriveFile, itemId: Int) -> Unit) {
        this.onMenuItemClick = onMenuItemClick
    }
}

object FilesDiffCallback : DiffUtil.ItemCallback<DriveFile>() {
    override fun areItemsTheSame(oldItem: DriveFile, newItem: DriveFile) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DriveFile, newItem: DriveFile) =
        oldItem == newItem
}