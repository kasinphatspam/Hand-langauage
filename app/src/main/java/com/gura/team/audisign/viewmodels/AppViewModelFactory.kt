package com.gura.team.audisign.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModelFactory(val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(application) as T
        } else if (modelClass.isAssignableFrom(TranslatorActivityViewModel::class.java)) {
            return TranslatorActivityViewModel(application) as T
        } else if (modelClass.isAssignableFrom(CategoryActivityViewModel::class.java)) {
            return CategoryActivityViewModel() as T
        } else if (modelClass.isAssignableFrom(LessonActivityViewModel::class.java)) {
            return LessonActivityViewModel() as T
        } else if (modelClass.isAssignableFrom(LessonFragmentViewModel::class.java)) {
            return LessonFragmentViewModel() as T
        }

        throw IllegalArgumentException("UnknownViewModel")
    }
}