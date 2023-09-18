import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wheathermate.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 워크 매니저로 작업 요청을 스케줄링합니다.
        val workRequest = OneTimeWorkRequestBuilder<WeatherWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)

        // 여기서 코루틴을 시작할 수 있습니다.
        scope.launch {
            // ...
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()  // 액티비티가 파괴될 때 코루틴도 취소합니다.
    }
}