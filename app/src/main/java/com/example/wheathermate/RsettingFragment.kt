package com.example.wheathermate

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
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

        setupSwitch(sharedPreferences, binding.switch1, "switch1")
        setupSwitch(sharedPreferences, binding.switch2, "switch2")
        setupSwitch(sharedPreferences, binding.switch3, "switch3")
        setupSwitch(sharedPreferences, binding.switch4, "switch4")
        setupSwitch(sharedPreferences, binding.switch5, "switch5")

        return binding.root
    }

    private fun setupSwitch(sharedPreferences: SharedPreferences, switch: Switch, key: String) {
        switch.isChecked = getSavedSwitchState(sharedPreferences, key)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startAlarm()
            } else {
                stopAlarm()
            }
            saveSwitchState(sharedPreferences, key, isChecked)
        }
    }
    //스위치 초기 상태 및 상태변경 등록

    private fun getSavedSwitchState(sharedPreferences: SharedPreferences, key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
    //설정을 불러와 스위치 상태를 유지 및 저장

    private fun saveSwitchState(sharedPreferences: SharedPreferences, key: String, state: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, state)
            apply()
        }
    }
    //스위치 설정 저장

    private fun startAlarm() {
        Toast.makeText(requireContext(), getString(R.string.send_alarm), Toast.LENGTH_SHORT).show()

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
    }//workmanger 확인 필요

    private fun stopAlarm() {
        Toast.makeText(requireContext(), getString(R.string.do_not_send_alarm), Toast.LENGTH_SHORT).show()
        WorkManager.getInstance(requireContext()).cancelUniqueWork("WeatherCheck")
    }//workmanger 확인 필요

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
    }//알람 시작 초기 지연 시간 계산
}