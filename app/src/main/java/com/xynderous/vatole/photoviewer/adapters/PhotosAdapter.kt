package com.xynderous.vatole.photoviewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.xynderous.vatole.photoviewer.R
import com.xynderous.vatole.photoviewer.databinding.PhotosLayoutBinding
import com.xynderous.vatole.photoviewer.model.PhotoModel

class PhotosAdapter(val onPhotoSelected: (photo: PhotoModel, position: Int) -> Unit) :
    RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<PhotoModel>() {
        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PhotosLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int = differ.currentList.size
    inner class ViewHolder(private val itemBinding: PhotosLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(photoModel: PhotoModel, position: Int) {
            itemBinding.apply {
                imgPhoto.load(photoModel.urls?.thumb) {
                    placeholder(R.color.white)
                    crossfade(true)
                }
                cardPhoto.setOnClickListener {
                    onPhotoSelected(photoModel, position)
                }
            }
        }
    }
}