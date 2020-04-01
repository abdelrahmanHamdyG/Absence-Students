package com.abdelrahmanhamdy2.absencestudents.Activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.GroupsNow
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.UIDS
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.checkNetwork
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.groupPath
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.idPath
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.log
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencestudents.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {


    var firebaseDatabase=FirebaseDatabase.getInstance()
    lateinit var uid:String
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Wait")
        progressDialog.setCancelable(false)

        buttonStartActivity.setOnClickListener {


            val id=editIdStart.text.toString()
            val group=editGroupStart.text.toString()

            if (!checkNetwork(this)){


                showToast(this,"Check You Internet Connection ")

            }else{
                if (id.isEmpty()||group.isEmpty()){


                    showToast(this,"Empty")
                }else{

                    progressDialog.show()
                    CoroutineScope(IO).launch {
                        searchIdAndGroups(id, group)

                    }


                }






            }


        }

    }

    private fun searchIdAndGroups(id:String, group:String){

        val dbRefSearchId=firebaseDatabase.getReference(UIDS).child(id)
        dbRefSearchId.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                showToast(this@StartActivity,p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.value==null){
                    CoroutineScope(Main).launch {
                        showToast(this@StartActivity, "please enter a correct id")
                        progressDialog.dismiss()
                    }
                }else {
                    for (i in p0.children){

                        uid = i.getValue(String::class.java)!!
                    }


                    val dbRefGetGroupName =
                        firebaseDatabase.getReference(uid).child(GroupsNow).child(group)
                    dbRefGetGroupName.addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            log(p0.message)
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.value!=null){


                                CoroutineScope(Main).launch {
                                    editGroupStart.setText("")
                                    editIdStart.setText("")
                                }

                                val i=Intent(this@StartActivity,
                                    MainActivity::class.java)
                                i.putExtra(idPath,uid)
                                i.putExtra(groupPath,group)
                                startActivity(i)


                            }else{

                                CoroutineScope(Main).launch {
                                    showToast(this@StartActivity, "Enter a correct groupName")
                                    progressDialog.dismiss()
                                }

                                }}



                    })
                }
            }


        })


    }

        }




