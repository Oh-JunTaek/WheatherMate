import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wheathermate.BuildConfig
import com.example.wheathermate.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(HomeActivity.WeatherApiService::class.java)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val call = service.getWeatherData("Seoul", BuildConfig.WEATHER_API_KEY, "metric").execute()
                if (call.isSuccessful) {
                    val weatherData = call.body()
                    if (weatherData != null && weatherData.weather[0].main == "Rain") {
                        // 비가 오는 경우 알림 스케줄링
                        scheduleRainNotification()
                    }
                } else {
                    // 에러 메시지 출력
                    println("Error: ${call.errorBody()?.string()}")
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private fun scheduleRainNotification() {
        // AlarmManager를 사용하여 알림 스케줄링하는 코드 작성 필요.
    }
}