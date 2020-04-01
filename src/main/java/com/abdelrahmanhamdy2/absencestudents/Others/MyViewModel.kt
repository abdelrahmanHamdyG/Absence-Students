package com.abdelrahmanhamdy2.absencestudents.Others

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.booleanPath
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyViewModel(application: Application):AndroidViewModel(application) {


    var trueOrFalse=MutableLiveData<String>()
    private var firebaseDatabase=FirebaseDatabase.getInstance()

    fun isThereSection(uid:String,group:String){

        val databassReference=firebaseDatabase.getReference(uid).child(group+booleanPath)
        databassReference.addValueEventListener(object: ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

            val data=p0.getValue(Boolean::class.java)




                if (data!!){

                    trueOrFalse.value="true"

                }else{
                    trueOrFalse.value="false"

                }
            }
        })

    }

}