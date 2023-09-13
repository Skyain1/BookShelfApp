package com.example.bookshelf.App.View.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bookshelf.App.Model.Item.Library
import com.example.bookshelf.App.Model.Item.WeekStat
import com.example.bookshelf.App.Utilities.CharSequenceAdapter
import com.example.bookshelf.App.Utilities.PreferenceManager
import com.example.bookshelf.App.ViewModel.LoginViewModel
import com.example.bookshelf.App.ViewModel.MainViewModel
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.setMainImage()
        vm.setWeekStat()
        vm.setMainBook()

        vm.weekStat.observe(this) {
            setStat(it)
        }
        vm.image.observe(this) {
            binding.roundedImageView.setImageBitmap(it)
        }

        vm.bokk.observe(this) {
            setBook(it.Skin)
            binding.pb.setProgressPercentage(
                ((((it.ProgressPage ?: 50) + 1).toDouble() / it.AllCount.toDouble()) * 100),
                true
            )
            binding.proc.text =
                ((((it.ProgressPage) + 1).toDouble() / it.AllCount.toDouble()) * 100).toInt()
                    .toString() + "%"

            if ((it.ProgressPage / 60) == 0) {
                binding.timer.text = (it.ProgressPage).toString() + " minutes left"
            } else {
                binding.timer.text = (it.ProgressPage / 60).toString() + " hours left"
            }
            val name = it.Name
            binding.roundedImageView2.setOnClickListener {
                val intent = Intent(this@MainActivity, ReaderActivity::class.java)
                intent.putExtra("ClickedBook",name )
                startActivity(intent)
            }
        }

        vm.library.observe(this){
            if (it.books.size == 1) {
                binding.booksCount.text = it.books.size.toString() + " book"
            } else {
                binding.booksCount.text = it.books.size.toString() + " books"
            }
            var booksSucess = 0
            var alltimer = 0
            for (bok in it.books) {
                alltimer += bok.ProgressPage
                if (bok.ProgressPage == bok.AllCount) {
                    booksSucess++
                }
            }
            if (booksSucess == 1) {
                binding.BookSucess.text = booksSucess.toString() + " book"
            } else {
                binding.BookSucess.text = booksSucess.toString() + " books"
            }

            if ((alltimer / 60) == 0) {
                binding.allTimer.text = alltimer.toString() + " m"
            } else {
                binding.allTimer.text = (alltimer / 60).toString() + " h"
            }
        }


        binding.btBookmarks.setOnClickListener {
            startActivity(Intent(this@MainActivity, BookmarksActivity::class.java))
        }
        binding.btBookmarksBig.setOnClickListener {
            startActivity(Intent(this@MainActivity, BookmarksActivity::class.java))
        }
        binding.BtMore.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        binding.toProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
             overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        binding.BtBookShelf.setOnClickListener {
            startActivity(Intent(this@MainActivity, BookShelfActivity::class.java))
        }
    }

    private fun setBook(image : String) {
        val byteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        val imbm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        binding.roundedImageView2.setImageBitmap(imbm)
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_and_scale)
        binding.roundedImageView2.startAnimation(animation)
    }

    private fun setStat(weekStat: WeekStat) {
            val total =
                (weekStat.Mo + weekStat.Tu + weekStat.We + weekStat.Th + weekStat.Fr + weekStat.Sa + weekStat.Su).toDouble()
            binding.pbMo.setProgressPercentage((((weekStat.Mo).toDouble()) / total) * 100)
            binding.pbTu.setProgressPercentage((((weekStat.Tu).toDouble()) / total) * 100)
            binding.pbWe.setProgressPercentage((((weekStat.We).toDouble()) / total) * 100)
            binding.pbTh.setProgressPercentage((((weekStat.Th).toDouble()) / total) * 100)
            binding.pbFr.setProgressPercentage((((weekStat.Fr).toDouble()) / total) * 100)
            binding.pbSa.setProgressPercentage((((weekStat.Sa).toDouble()) / total) * 100)
            binding.pbSu.setProgressPercentage((((weekStat.Su).toDouble()) / total) * 100)
    }
}