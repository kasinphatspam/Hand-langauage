package com.gura.team.audisign.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gura.team.audisign.model.Category

class CategoryActivityViewModel: ViewModel() {

    var category = MutableLiveData<ArrayList<Category>>()
    private var categoryArrayList = ArrayList<Category>()

    private val database = Firebase.database
    private val reference = database.getReference("1").child("category")

    fun loadCategory() {
        reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount <= 0) {
                    return
                }
                snapshot.children.forEach {
                    val data = it.getValue(Category::class.java)
                    categoryArrayList.add(data!!)
                }
                category.apply {
                    value = categoryArrayList
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}