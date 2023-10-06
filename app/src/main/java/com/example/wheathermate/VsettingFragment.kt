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
    private var _binding: FragmentVsettingBinding? = null // 뷰바인딩 객체 선언.
    private val binding get() = _binding!! // non-null 타입으로 가져오기 위한 프로퍼티.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVsettingBinding.inflate(inflater, container, false) // 인플레이트.
        return binding.root // 바인딩 클래스의 root를 반환.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 메모리 누수 방지를 위해 onDestroy에서 null로 설정.
    }
}

class BackgroundImageAdapter(private val items : List<BackgroundImage>) : RecyclerView.Adapter<BackgroundImageAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBackgroundImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBackgroundImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder , position:Int){
        val drawable = ContextCompat.getDrawable(holder.itemView.context, items[position].resId)
        holder.binding.imageView.setImageDrawable(drawable)

        //아이템 뷰에 대한 설정(텍스트나 클릭 리스너 등록)
        // ...

    }

    override fun getItemCount() = items.size

}