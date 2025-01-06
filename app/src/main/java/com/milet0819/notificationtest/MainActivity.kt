package com.milet0819.notificationtest

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.milet0819.notificationtest.common.ApiResult
import com.milet0819.notificationtest.common.RetrofitClient
import com.milet0819.notificationtest.common.repo.PostRepository
import com.milet0819.notificationtest.common.repo.UsersRepository
import com.milet0819.notificationtest.common.utils.logger
import com.milet0819.notificationtest.common.utils.toast
import com.milet0819.notificationtest.databinding.ActivityMainBinding
import com.milet0819.notificationtest.hilt.Car
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.LinkedList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: PostViewModel by viewModels()

    @Inject lateinit var car: Car

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

        binding.sEraseMode.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.dv.setEraserMode(isChecked)
//            binding.dv.isEraseMode = isChecked
        }

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
//                highFunc(
//                    onComplete = { println("onComplete") },
//                    onError = "error"
//                )
//                performTask { println("performing a task.") }
//                printType(5)
//                val json = """{"name": "John", "age": 30}"""
//                val user = json.toObject<User>()
//
//                println(user)
            }
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