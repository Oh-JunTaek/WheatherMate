package com.example.wheathermate

import android.content.Context
import java.util.concurrent.TimeUnit
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wheathermate.databinding.FragmentRsettingBinding
import java.util.Calendar

class RsettingFragment : Fragment() {

    private var _binding: FragmentRsettingBinding? = null // view binding instance.
    private val binding get() = _binding!! // non-null assertion for the view binding.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRsettingBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val savedSwitchState1 = sharedPreferences.getBoolean("switch1", false)
        binding.switch1.isChecked = savedSwitchState1

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val dailyRequest = PeriodicWorkRequestBuilder<WeatherCheckWorker>(24, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                    .build()

                WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                    "WeatherCheck",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    dailyRequest)

                Toast.makeText(requireContext(), getString(R.string.send_alarm), Toast.LENGTH_SHORT).show()
            } else {
                WorkManager.getInstance(requireContext()).cancelUniqueWork("WeatherCheck")
                Toast.makeText(requireContext(), getString(R.string.do_not_send_alarm), Toast.LENGTH_SHORT).show()
            }

            with(sharedPreferences.edit()) {
                putBoolean("switch1", isChecked)
                apply()
            }
        }

        // For switch2...
        val savedSwitchState2 = sharedPreferences.getBoolean("switch2", false)

        // Set the initial switch state for switch2.
        binding.switch2.isChecked = savedSwitchState2

        // Set a listener for switch2.
        binding.switch2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), getString(R.string.send_alarm), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.do_not_send_alarm), Toast.LENGTH_SHORT).show()
            }

            // Save the new switch state to SharedPreferences.
            with(sharedPreferences.edit()) {
                putBoolean("switch2", isChecked)
                apply()
            }
        }


        val savedSwitchState3 = sharedPreferences.getBoolean("switch3", false)

        binding.switch3.isChecked = savedSwitchState3
        binding.switch3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), getString(R.string.send_alarm), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.do_not_send_alarm), Toast.LENGTH_SHORT).show()
            }
            with(sharedPreferences.edit()) {
                putBoolean("switch3", isChecked)
                apply()
            }
        }

        val savedSwitchState4 = sharedPreferences.getBoolean("switch4", false)

        binding.switch4.isChecked = savedSwitchState4
        binding.switch4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), getString(R.string.send_alarm), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.do_not_send_alarm), Toast.LENGTH_SHORT).show()
            }
            with(sharedPreferences.edit()) {
                putBoolean("switch4", isChecked)
                apply()
            }
        }

        val savedSwitchState5 = sharedPreferences.getBoolean("switch5", false)

        binding.switch5.isChecked = savedSwitchState5
        binding.switch5.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), getString(R.string.send_alarm), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.do_not_send_alarm), Toast.LENGTH_SHORT).show()
            }
            with(sharedPreferences.edit()) {
                putBoolean("switch5", isChecked)
                apply()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
    private fun calculateInitialDelay(): Long {
        val now = Calendar.getInstance()
        val nextFourPM = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 16)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (now.after(nextFourPM)) {
            nextFourPM.add(Calendar.DATE, 1)
        }

        return nextFourPM.timeInMillis - now.timeInMillis
    }
}