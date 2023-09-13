package com.example.bookshelf.App.View.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.bookshelf.App.Domain.PageSplitter
import com.example.bookshelf.App.ViewModel.ProfileViewModel
import com.example.bookshelf.R
import com.example.bookshelf.databinding.ActivityProfileBinding
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val PICK_FILE_REQUEST_CODE = 101
    private lateinit var binding: ActivityProfileBinding
    private val vm: ProfileViewModel by viewModels()

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Обработка выбранного изображения
                Log.d("Yatoro", uri.toString())
                binding.roundedImageView.setImageURI(uri)
                // Преобразуем изображение в массив байтов
                vm.putImage(getImageByteArray(uri))
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.setMainImage()
        vm.setMainBook()

        binding.btBookmarks.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, BookmarksActivity::class.java))
        }
        binding.btAddBooks.setOnClickListener {
            openFilePicker()
        }
        binding.btSettings.setOnClickListener {

        }
        binding.btLogOut.setOnClickListener {
            vm.logOut()
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.roundedImageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }


        vm.image.observe(this) {
            binding.roundedImageView.setImageBitmap(it)
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
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type =
            "application/*" // Можете использовать "application/*" для всех файлов или "*/*" для всех типов
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val file = File(cacheDir, "temp_fb2.fb2")
                    val outputStream = FileOutputStream(file)

                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()

                    val fb2 = FictionBook(file)
                    val pageSplitter = PageSplitter(
                        binding.viewPager.width,
                        (binding.viewPager.height * 0.91).toInt(),
                        1f,
                        0
                    )
                    val textPaint = TextPaint()
                    textPaint.textSize = resources.getDimension(R.dimen.book_font_size)
                    vm.parseFb2(fb2,pageSplitter,textPaint)
                    file.delete()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getImageByteArray(imageUri: Uri): ByteArray {
        val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}