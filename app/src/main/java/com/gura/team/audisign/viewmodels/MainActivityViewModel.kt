package com.gura.team.audisign.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.gura.team.audisign.helper.TensorFlowHelper

class MainActivityViewModel(application: Application) : ViewModel() {
    private var count: Int = 0

    init {
        val tensorFlowHelper = TensorFlowHelper(application)
        this.count = tensorFlowHelper.getLabels().size
    }

    fun getLabelsCount(): Int {
        return count
    }

}