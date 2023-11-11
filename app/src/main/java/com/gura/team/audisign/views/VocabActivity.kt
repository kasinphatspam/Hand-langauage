package com.gura.team.audisign.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import com.gura.team.audisign.R
import com.gura.team.audisign.adapter.CategoryAdapter
import com.gura.team.audisign.adapter.LabelAdapter
import com.gura.team.audisign.helper.TensorFlowHelper
import com.gura.team.audisign.helper.WindowHelper

class VocabActivity : AppCompatActivity() {

    private lateinit var tensorFlowHelper: TensorFlowHelper
    private lateinit var gridView: GridView
    private lateinit var label: ArrayList<String>
    private lateinit var closeImageButton: ImageButton
    private lateinit var labelAdapter: LabelAdapter
    private lateinit var windowHelper: WindowHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocab)

        windowHelper = WindowHelper(this, window)
        windowHelper.statusBarColor = R.color.white
        windowHelper.publish()

        tensorFlowHelper = TensorFlowHelper(this)
        label = tensorFlowHelper.getLabels() as ArrayList<String>

        gridView = findViewById(R.id.gridView)
        closeImageButton = findViewById(R.id.closeImageButton)

        closeImageButton.setOnClickListener {
            finish()
        }

        labelAdapter = LabelAdapter(label, this)
        gridView.adapter = labelAdapter
    }
}