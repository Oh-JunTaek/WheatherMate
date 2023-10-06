package com.example.wheathermate

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wheathermate.databinding.FragmentRsettingBinding

class RsettingFragment : Fragment() {

    private var _binding: FragmentRsettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRsettingBinding.inflate(inflater, container, false)

        // 각 스위치에 대한 리스너 설정 및 초기 상태 설정 등을 여기서 합니다.
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Switch 1 is on", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Switch 1 is off", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}