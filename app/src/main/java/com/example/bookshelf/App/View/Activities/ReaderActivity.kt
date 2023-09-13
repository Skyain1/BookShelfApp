package com.example.bookshelf.App.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.bookshelf.App.Model.Item.Page
import com.example.bookshelf.App.View.Adapters.PagerAdapter
import com.example.bookshelf.App.ViewModel.ReaderViewModel
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ActivityReaderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReaderBinding
    private val vm: ReaderViewModel by viewModels()

    private var bookCount = 0
    private var currentPosition = 0
    private lateinit var curName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        curName = intent.getStringExtra("ClickedBook").toString()
        vm.setBook(curName)

        binding.back.setOnClickListener {
            startActivity(Intent(this@ReaderActivity, MainActivity::class.java))
        }
        binding.leftSide.setOnClickListener {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }
        binding.rightSide.setOnClickListener {
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
        }

        vm.bokk.observe(this) {
            bookCount = it.AllCount
            currentPosition = it.ProgressPage

            binding.viewPager.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val pages = ArrayList<Page>()
                    for (chap in it.Chapters) {
                        for ((c, pag) in chap.Pages.withIndex()) {
                            if (c == 0) {
                                pages.add(Page(pag, 2, chap.Title))
                            } else {
                                pages.add(Page(pag))
                            }
                        }
                    }
                    binding.viewPager.adapter = PagerAdapter(pages)
                    binding.viewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

            binding.numberPages.text = "$currentPosition / $bookCount"
            binding.pb.setProgressPercentage(((currentPosition / bookCount)) * 100.toDouble(), true)
            binding.viewPager.postDelayed({
                binding.viewPager.setCurrentItem(it.ProgressPage - 1, false)
            }, 100)

            val chapterInto = intent.getIntExtra("Chapter", -1)
            if (chapterInto != -1) {
                binding.viewPager.postDelayed({
                    binding.viewPager.setCurrentItem(chapterInto - 1, false)
                }, 100)
            }
            Log.d("Yatoro", currentPosition.toString())
            vm.setMarks(currentPosition)

            binding.toBookMark.setOnClickListener {
                vm.checkBookMark(currentPosition)
            }
        }

        vm.ActiveBookmark.observe(this) {
            if (it) {
                binding.toBookMark.speed = 1.0f
                binding.toBookMark.playAnimation()
            } else {
                binding.toBookMark.speed = -1.0f
                Log.d("Yatoro", "Убавляет")
                binding.toBookMark.playAnimation()
            }
        }

        vm.SwipeBookMark.observe(this) {
            if (it) {
                binding.toBookMark.speed = 1.0f
                binding.toBookMark.playAnimation()
            } else {
                binding.toBookMark.progress = 0.1f
                binding.toBookMark.pauseAnimation()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                currentPosition = position
                vm.swipeView(currentPosition < position, currentPosition)
                binding.numberPages.text = "${currentPosition + 1} / $bookCount"
                binding.pb.setProgressPercentage(
                    (((currentPosition + 1).toDouble() / bookCount.toDouble()) * 100),
                    true
                )
            }
        })

        binding.BtChapters.setOnClickListener {
            val intent = Intent(this@ReaderActivity, ChaptersActivity::class.java)
            intent.putExtra("ChapetrsBook", curName)
            intent.putExtra("ChaptersPage", currentPosition)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        vm.stop(currentPosition)
    }
}