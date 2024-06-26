package com.murvetarslan.neseyiyakala

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.murvetarslan.neseyiyakala.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var skor = 0

    //Görselleri tutacağımız liste
    var imageArray = ArrayList<ImageView>()

    var runnable = Runnable { }
    var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Listeye görselleri ekliyoruz
        imageArray.add(binding.imageView1)
        imageArray.add(binding.imageView2)
        imageArray.add(binding.imageView3)
        imageArray.add(binding.imageView4)
        imageArray.add(binding.imageView5)
        imageArray.add(binding.imageView6)
        imageArray.add(binding.imageView7)
        imageArray.add(binding.imageView8)
        imageArray.add(binding.imageView9)

        hideImages()

        //Zamanı geriye saymak için CountDownTimer
        object : CountDownTimer(15500,1000){
            override fun onTick(millisUntilFinished: Long) { //Her bir(1000) saniyede ne olacak
                binding.timeText.text = "Time: ${millisUntilFinished/1000}"
            }

            override fun onFinish() { // Zamanlayıcı bittiğinde ne olacak
                binding.timeText.text = "Kalan Zaman: 0"
                handler.removeCallbacks(runnable)  //runnable durmalı.Çünkü durmazsa görsel gezmeye devam eder.

                for(image in imageArray){
                    image.visibility = View.INVISIBLE //sonunda da kalan görseli de kaldıralım ki tıklanmasın
                }

                //Oyun bittiğinde alert mesajı ile tekrar oynamak isteyip istemediğini soracağız
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Oyun Bitti")
                alert.setMessage("Tekrar Oynamak İster Misin?")

                alert.setPositiveButton("Evet",DialogInterface.OnClickListener { dialog, which ->
                    //Evet e bastığı için oyunu baştan başlatıyoruz
                    val intentFromMain = intent
                    finish() //Önceki haline dönmesini istemiyorum.Bir sonraki açılmadan güncel olan kapansın istiyorum
                    startActivity(intentFromMain)
                })

                alert.setNegativeButton("Hayır",DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this@MainActivity,"Oyun Bitti!",Toast.LENGTH_LONG).show()
                })
                alert.show()
            }

        }.start()
    }

    //Neşenin üstüne tıklayınca skor arttırma işlemi
    fun skorArttir(view : View){
        skor = skor + 1
        binding.skorText.text = "Skor: ${skor}"
    }

    //Neşeleri Saklama İşlemi
    fun hideImages(){
        runnable = object : Runnable {
            override fun run() { //Devamlı çalıştırmak istediklerimizi buraya ekliyoruz
                // View.GONE,  View.INVISIBLE (Görünmez) vs View.VISIBLE (Görünür)
                for(image in imageArray){
                    image.visibility = View.INVISIBLE
                }
                val randomIndex = Random.nextInt(9) // 0 ile 8 arasında rastgele bir indeks seçer
                imageArray[randomIndex].visibility = View.VISIBLE

                //Kaç saniyede bir çalıştırmak için
                handler.postDelayed(runnable,500)
            }
        }
        handler.post(runnable) //runnable ı başlatıyoruz
    }
}