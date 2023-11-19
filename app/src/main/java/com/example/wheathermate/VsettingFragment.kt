package com.example.wheathermate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.wheathermate.databinding.FragmentVsettingBinding
import com.example.wheathermate.databinding.ItemBackgroundImageBinding

class VsettingFragment : Fragment() {
    private var _binding: FragmentVsettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVsettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Clean up any references that can lead to memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class BackgroundImageAdapter(private val items : List<BackgroundImage>) : RecyclerView.Adapter<BackgroundImageAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBackgroundImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateView(parent))
    }

    private fun inflateView(parent: ViewGroup): ItemBackgroundImageBinding {
        return ItemBackgroundImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder : ViewHolder , position:Int){
        setDrawableForImageView(holder, position)

        // Set any additional configuration for item view (such as text, click listeners, etc.)
        // ...
    }

    private fun setDrawableForImageView(holder: ViewHolder, position: Int) {
        val drawable = ContextCompat.getDrawable(holder.itemView.context, items[position].resId)
        holder.binding.imageView.setImageDrawable(drawable)
    }

    override fun getItemCount() = items.size
}