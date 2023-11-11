package com.gura.team.audisign.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.gura.team.audisign.R
import com.gura.team.audisign.helper.WindowHelper
import com.gura.team.audisign.model.Vocab
import com.gura.team.audisign.viewmodels.AppViewModelFactory
import com.gura.team.audisign.viewmodels.LessonActivityViewModel
import com.gura.team.audisign.viewmodels.LessonFragmentViewModel
import com.gura.team.audisign.views.fragment.LessonFragment

class LessonActivity : AppCompatActivity(), LessonFragment.ButtonClickListener {

    private lateinit var windowHelper: WindowHelper
    private lateinit var factory: AppViewModelFactory
    private lateinit var viewModel: LessonActivityViewModel
    private lateinit var fragmentViewModel: LessonFragmentViewModel
    private var categoryId: Int = 0
    private var currentVocab: Int = 0
    private lateinit var vocabArrayList: ArrayList<Vocab>
    private lateinit var statusTextView: TextView
    private lateinit var closeImageButton: ImageButton
    private lateinit var comingSoonConstraintLayout: ConstraintLayout

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        categoryId = intent.getIntExtra("category", 0)

        windowHelper = WindowHelper(this, window)
        windowHelper.statusBarColor = R.color.white
        windowHelper.publish()

        statusTextView = findViewById(R.id.statusTextView)
        comingSoonConstraintLayout = findViewById(R.id.comingSoonConstraintLayout)
        closeImageButton = findViewById(R.id.closeImageButton)

        closeImageButton.setOnClickListener {
            finish()
        }

        factory = AppViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[LessonActivityViewModel::class.java]
        fragmentViewModel = ViewModelProvider(this, factory)[LessonFragmentViewModel::class.java]

        viewModel.setCategoryId(categoryId)
        viewModel.load()

        viewModel.vocab.observe(this) {
            vocabArrayList = it
            if(vocabArrayList.size > 0){
                statusTextView.text = "คำศัพท์ (${currentVocab + 1 }/${vocabArrayList.size})"
                val fragment = LessonFragment()
                fragment.newInstance(it[currentVocab], currentVocab + 1, vocabArrayList.size)
                replaceFragment(fragment)
            }
        }

        viewModel.none.observe(this) {
            if(it){
                comingSoonConstraintLayout.visibility = View.VISIBLE
                statusTextView.text = "คำศัพท์ (0/0)"
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment, fragment)
            setReorderingAllowed(true)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun next() {
        if((currentVocab+1) == vocabArrayList.size){
            finish()
        }else{
            currentVocab += 1
            statusTextView.text = "คำศัพท์ (${currentVocab + 1 }/${vocabArrayList.size})"
            val fragment = LessonFragment()
            fragment.newInstance(vocabArrayList[currentVocab], currentVocab+ 1, vocabArrayList.size)
            replaceFragment(fragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun previous() {
        if((currentVocab) <= 0){
            finish()
        }else{
            currentVocab -= 1
            statusTextView.text = "คำศัพท์ (${currentVocab + 1 }/${vocabArrayList.size})"
            val fragment = LessonFragment()
            fragment.newInstance(vocabArrayList[currentVocab], currentVocab +1 , vocabArrayList.size)
            replaceFragment(fragment)
        }
    }

    override fun onClick(i: Int) {
        if(i==1){
            next()
        }else if ( i== 0) {
            previous()
        }
    }
}