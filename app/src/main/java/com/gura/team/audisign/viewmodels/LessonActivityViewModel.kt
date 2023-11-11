package com.gura.team.audisign.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gura.team.audisign.model.Vocab

class LessonActivityViewModel : ViewModel() {

    private var categoryId: Int = 0
    private val database = Firebase.database
    private lateinit var reference: DatabaseReference

    var vocab = MutableLiveData<ArrayList<Vocab>>()
    var none = MutableLiveData<Boolean>()
    private var vocabArrayList = ArrayList<Vocab>()

    fun setCategoryId(id: Int) {
        this.categoryId = id
    }

    fun load() {
        val reference = database.getReference("1")
            .child("category")
            .child(categoryId.toString())

        reference.child("vocab")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.childrenCount <= 0) {
                        none.apply {
                            value = true
                        }
                    }
                    snapshot.children.forEach {
                        val data = it.getValue(Vocab::class.java)
                        vocabArrayList.add(data!!)
                    }

                    vocab.apply {
                        value = vocabArrayList
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })

        this.reference = reference
    }
}