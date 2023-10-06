package com.example.wheathermate

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wheathermate.databinding.FragmentRsettingBinding

class RsettingFragment : Fragment() {

    private var _binding: FragmentRsettingBinding? = null // view binding instance.
    private val binding get() = _binding!! // non-null assertion for the view binding.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRsettingBinding.inflate(inflater, container, false)

        // Get the saved switch state from SharedPreferences.
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val savedSwitchState = sharedPreferences.getBoolean("switch1", false)

        // Set the initial switch state.
        binding.switch1.isChecked = savedSwitchState

        // Set a listener for switch1.
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "알림을 보낼게요", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "알림을 껐어요", Toast.LENGTH_SHORT).show()
            }

            // Save the new switch state to SharedPreferences.
            with(sharedPreferences.edit()) {
                putBoolean("switch1", isChecked)
                apply()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}