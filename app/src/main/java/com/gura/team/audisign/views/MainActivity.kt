package com.gura.team.audisign.views

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.gura.team.audisign.R
import com.gura.team.audisign.helper.WindowHelper
import com.gura.team.audisign.viewmodels.AppViewModelFactory
import com.gura.team.audisign.viewmodels.MainActivityViewModel
import com.gura.team.audisign.viewmodels.TranslatorActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var factory: AppViewModelFactory
    private lateinit var viewVocabImageButton: ImageButton
    private lateinit var translateCardView: CardView
    private lateinit var learningCardView: CardView
    private lateinit var translateImageButton: ImageButton
    private lateinit var learningImageButton: ImageButton
    private lateinit var textView: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val windowHelper = WindowHelper(this, window)
        windowHelper.statusBarColor = R.color.white
        windowHelper.publish()

        factory = AppViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]

        viewVocabImageButton = findViewById(R.id.viewSupportVocabImageButton)
        translateCardView = findViewById(R.id.translateCardView)
        translateImageButton = findViewById(R.id.translateImageButton)
        learningCardView = findViewById(R.id.learningCardView)
        learningImageButton = findViewById(R.id.learnImageButton)
        textView = findViewById(R.id.textView)

        textView.text = "ขณะนี้รองรับคำศัพท์ทั้งหมด ${viewModel.getLabelsCount()} คำ"

        translateCardView.setOnClickListener {
            val intent = Intent(this, TranslatorActivity::class.java)
            startActivity(intent)
        }

        translateImageButton.setOnClickListener {
            val intent = Intent(this, TranslatorActivity::class.java)
            startActivity(intent)
        }

        learningCardView.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        learningImageButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        viewVocabImageButton.setOnClickListener {
            val intent = Intent(this, VocabActivity::class.java)
            startActivity(intent)
        }
    }
}