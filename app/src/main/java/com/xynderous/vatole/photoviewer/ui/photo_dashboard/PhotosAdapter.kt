package com.xynderous.vatole.photoviewer.ui.photo_dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.xynderous.vatole.photoviewer.R
import com.xynderous.vatole.photoviewer.data.model.DomainPhotoModel
import com.xynderous.vatole.photoviewer.databinding.PhotosLayoutBinding

class PhotosAdapter(val onPhotoSelected: (photo: DomainPhotoModel, position: Int) -> Unit) :
    RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<DomainPhotoModel>() {
        override fun areItemsTheSame(oldItem: DomainPhotoModel, newItem: DomainPhotoModel): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: DomainPhotoModel, newItem: DomainPhotoModel): Boolean {
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
        fun bind(photoModel: DomainPhotoModel, position: Int) {
            itemBinding.apply {
                imgPhoto.load(photoModel.urls?.thumb) {
                    placeholder(R.color.white)
                    crossfade(true)
                }
                txtCaption.text = photoModel.user?.name
                cardPhoto.setOnClickListener {
                    onPhotoSelected(photoModel, position)
                }
            }
        }
    }
}