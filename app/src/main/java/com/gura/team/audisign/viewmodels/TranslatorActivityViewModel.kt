package com.gura.team.audisign.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gura.team.audisign.helper.TensorFlowHelper
import com.gura.team.audisign.model.PredictionResult

class TranslatorActivityViewModel(application: Application) : ViewModel() {

    private val tensorFlowHelper: TensorFlowHelper = TensorFlowHelper(application)
    val result = MutableLiveData<PredictionResult>()

    fun classify(bitmap: Bitmap) {
        tensorFlowHelper.load(bitmap)
        result.apply {
            value = tensorFlowHelper.predict()
        }
    }
}