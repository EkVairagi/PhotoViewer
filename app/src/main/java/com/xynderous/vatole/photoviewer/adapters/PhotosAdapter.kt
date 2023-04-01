package com.xynderous.vatole.photoviewer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.xynderous.vatole.photoviewer.R
import com.xynderous.vatole.photoviewer.databinding.PhotosLayoutBinding
import com.xynderous.vatole.photoviewer.model.PhotoModel

class PhotosAdapter(val onPhotoSelected: (photo: PhotoModel, position: Int) -> Unit) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    private val data:ArrayList<PhotoModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            PhotosLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position],position)
    }

    fun updateItems(photosList: List<PhotoModel>){
        data.clear()
        data.addAll(photosList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size ?: 0
    inner class ViewHolder(private val itemBinding: PhotosLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(photoModel: PhotoModel,position: Int){

                itemBinding.apply {

                    imgPhoto.load(photoModel.urls?.thumb){
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