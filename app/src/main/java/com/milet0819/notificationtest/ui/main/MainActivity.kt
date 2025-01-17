package com.milet0819.notificationtest.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.milet0819.notificationtest.R
import com.milet0819.notificationtest.common.ApiResult
import com.milet0819.notificationtest.common.utils.buildIntent
import com.milet0819.notificationtest.common.utils.logger
import com.milet0819.notificationtest.common.utils.registerForActivityResult
import com.milet0819.notificationtest.common.utils.startActivity
import com.milet0819.notificationtest.common.utils.toast
import com.milet0819.notificationtest.databinding.ActivityMainBinding
import com.milet0819.notificationtest.hilt.Car
import com.milet0819.notificationtest.ui.drawing.DrawingActivity
import com.milet0819.notificationtest.ui.lottery.LotteryActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: PostViewModel by viewModels()

    @Inject lateinit var car: Car

    val drawingLauncher = registerForActivityResult {
        logger(it)
        toast("Start DrawingActivity")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)

        initState()
        initObserver()

        binding.button.setOnClickListener {
            lifecycleScope.launch {
                viewModel.fetchPost()
            }
        }

        binding.button2.setOnClickListener {
            lifecycleScope.launch {
                viewModel.fetchUsers()
            }
        }

        binding.button3.setOnClickListener {
            lifecycleScope.launch {
                viewModel.fetchPost()
                viewModel.fetchUsers()
            }
        }

        binding.button4.setOnClickListener {
            lifecycleScope.launch {
//                viewModel.fetchPost2()
                toast(R.string.app_name)
            }
        }

        binding.button5.setOnClickListener {
            lifecycleScope.launch {

            }
        }

        binding.btDrawingActivity.setOnClickListener {

//            startActivity<DrawingActivity>()

//            startActivity<DrawingActivity>(
//                DrawingActivity.CODE to 88,
//                DrawingActivity.NAME to "oscar"
//            ) test

            drawingLauncher.launch(buildIntent<DrawingActivity>())
        }

        binding.btLotteryActivity.setOnClickListener {
            startActivity<LotteryActivity>()
        }

    }

    /**
     * launch 가 세분화 되어있는 이유.
     * lifecycleScope는 하나의 코루틴을 생성한다.
     * 'collect'는 suspending func이기 때문에, 첫 번째 'collect'호출이 완료되지 않으면 두번째 'collect' 호출로 진행되지 않는다.
     */
    private fun initState() = lifecycleScope.launch {

        launch {
            viewModel.postState.collect { postState ->
                when (postState) {
                    is ApiResult.Init -> { }
                    is ApiResult.Loading -> { }
                    is ApiResult.Success -> {
                        binding.tvResult.text = postState.data.toString()
                    }
                    is ApiResult.Error -> {
                        binding.tvResult.text = postState.exception.message
                    }
                }
            }
        }

        launch {
            viewModel.usersState.collect { usersState ->
                when (usersState) {
                    is ApiResult.Init -> {
                        val text = "Init usersState"
//                        binding.tvResult.text = text
                        println(text)
                    }
                    is ApiResult.Loading -> {
                        val text = "... Loading usersState"
                        binding.tvResult.text = text
                        println(text)
                    }
                    is ApiResult.Success -> {
                        binding.tvResult.text = usersState.data.toString()
                    }
                    is ApiResult.Error -> {
                        binding.tvResult.text = usersState.exception.message
                        Log.e("postState", usersState.exception.message.toString())
                    }
                }
            }
        }

    }

    private fun initObserver() {
        viewModel.postData.observe(this) {
            lifecycleScope.launch {
                binding.tvResult.text = it.toString()
            }
        }
    }

    private fun highFunc(
        onComplete: () -> Unit,
        onError: String
    ) {

        onComplete()
        println(onError)
    }

    private fun performTask(operation: () -> Unit) {
        println("Starting operation...")
        operation()
        println("Operation completed.")
    }

    private inline fun <reified T> printType(value: T) {
        println("Type of T : ${T::class}")

        if (value is Int) {
            println("is Int")
        }
    }

    data class User(val name: String, val age: Int)

    private inline fun <reified T> String.toObject(): T {
        val gson = com.google.gson.Gson()
        return gson.fromJson(this, T::class.java)
    }

}


//@RequiresApi(Build.VERSION_CODES.O)
//private fun createNotification() {
//    val channel = NotificationChannel("2", "name", NotificationManager.IMPORTANCE_HIGH)
//
//    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    notificationManager.createNotificationChannel(channel)
//
//    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//    var builder = NotificationCompat.Builder(this, "2")
//        .setSmallIcon(R.mipmap.ic_launcher)
//        .setContentTitle("가나다라마바사아자차카타파하|가나다라마바사아자차카타파하|가나다라마바사아자차카타파하|가나다라마바사아자차카타파하")
//        .setContentText("가나다라마바사아자차카타파하|가나다라마바사아자차카타파하|가나다라마바사아자차카타파하|가나다라마바사아자차카타파하")
//        .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources, R.drawable.parc)))
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setFullScreenIntent(pendingIntent, true)
//        .build()
//
//    if (ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.POST_NOTIFICATIONS
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        // TODO: Consider calling
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//        return
//    }
//    NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), builder)
//}