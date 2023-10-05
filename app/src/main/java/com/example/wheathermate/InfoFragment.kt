import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wheathermate.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 앱 버전 가져오기
        try {
            val pInfo = activity?.packageManager?.getPackageInfo(activity?.packageName!!, 0)
            val version = pInfo?.versionName

            binding.tvVersionInfo.text = "App Version : $version"

        } catch (e : PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        // 이메일과 계좌정보 세팅
        val infoTitle = SpannableString("개발자 메일\n")
        infoTitle.setSpan(StyleSpan(Typeface.BOLD), 0, infoTitle.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val infoEmail = SpannableString("wns5388@naver.com")
        binding.tvInfo.text = SpannableStringBuilder().append(infoTitle).append(infoEmail)

        val accountTitle = SpannableString("후원계좌\n")
        accountTitle.setSpan(StyleSpan(Typeface.BOLD), 0, accountTitle.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val accountNumber = SpannableString("카카오뱅크 3333-09-4728124")
        binding.tvAccountInfo.text = SpannableStringBuilder().append(accountTitle).append(accountNumber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}//1