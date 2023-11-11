package com.gura.team.audisign.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import com.gura.team.audisign.R
import com.gura.team.audisign.adapter.CategoryAdapter
import com.gura.team.audisign.helper.WindowHelper
import com.gura.team.audisign.viewmodels.AppViewModelFactory
import com.gura.team.audisign.viewmodels.CategoryActivityViewModel

class CategoryActivity : AppCompatActivity() {

    private lateinit var windowHelper: WindowHelper
    private lateinit var gridView: GridView
    private lateinit var factory: AppViewModelFactory
    private lateinit var viewModel: CategoryActivityViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var closeImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        factory = AppViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[CategoryActivityViewModel::class.java]

        windowHelper = WindowHelper(this, window)
        windowHelper.statusBarColor = R.color.white
        windowHelper.publish()

        gridView = findViewById(R.id.gridView)
        closeImageButton = findViewById(R.id.closeImageButton)

        closeImageButton.setOnClickListener {
            finish()
        }

        viewModel.loadCategory()

        viewModel.category.observe(this) {
            categoryAdapter = CategoryAdapter(it, this)
            gridView.adapter = categoryAdapter
            gridView.setOnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, LessonActivity::class.java)
                intent.putExtra("category", position)
                startActivity(intent)
            }
        }

    }
}