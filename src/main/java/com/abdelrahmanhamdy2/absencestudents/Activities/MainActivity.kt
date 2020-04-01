package com.abdelrahmanhamdy2.absencestudents.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.GroupsNow
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.SectionKey
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.TheFather
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.getTime
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.groupPath
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.idPath
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.log
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.showToast
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.studentAbsentsPath
import com.abdelrahmanhamdy2.absencestudents.Others.Helper.Companion.studentPresentsPath
import com.abdelrahmanhamdy2.absencestudents.Others.ModelData
import com.abdelrahmanhamdy2.absencestudents.Others.MyViewModel
import com.abdelrahmanhamdy2.absencestudents.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_layout.view.*

class MainActivity : AppCompatActivity() {


    lateinit var alertDialog: AlertDialog
    private lateinit var viewModel: MyViewModel
    lateinit var data2: ModelData
    var firebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressShare = ProgressDialog(this)
        progressShare.setCancelable(false)
        progressShare.setTitle("Please wait")
        progressShare.show()

        val uid = intent.extras!!.getString(idPath)!!
        val group = intent.extras!!.getString(groupPath)!!
        val readShared = getSharedPreferences(SectionKey, Context.MODE_PRIVATE)
        val string = readShared.getString("$group${getTime()}", "null")
        if (string != "null") {

            progressShare.dismiss()

            startActivity(
                Intent(this@MainActivity, YouAddedActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

        } else {
            progressShare.dismiss()
            alertDialog = AlertDialog.Builder(this).create()
            val layoutInflater =
                LayoutInflater.from(this).inflate(R.layout.alert_layout, null, false)
            alertDialog.setView(layoutInflater)

            viewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
            viewModel.isThereSection(uid, group)


            buttonAddStudent.setOnClickListener {

                val id2 = editID.text.toString()

                val dbRef = firebaseDatabase.getReference(uid).child(GroupsNow).child(group).child(
                    TheFather
                ).child(studentAbsentsPath)



                dbRef.orderByChild("idOfStudent").equalTo(id2)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onCancelled(p0: DatabaseError) {


                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.value == null) {

                                showToast(this@MainActivity, "Enter a valid ID")

                            } else {

                                log(p0.value.toString())
                                for (i2 in p0.children) {

                                    ////////////////////////////////////////////////////
                                    data2 = i2.getValue(ModelData::class.java)!!
                                    log("${data2.nameOfStudent}")
                                    log("${data2.idOfStudent}")

                                }    ///////////////////////////
                                alertDialog.setTitle("is your name ${data2.nameOfStudent} ")
                                alertDialog.show()
                                ////////////////////////////////////////////////////

                                layoutInflater.yes.setOnClickListener {
                                    alertDialog.dismiss()
                                    val dbRefToPre =
                                        firebaseDatabase.getReference(uid).child(GroupsNow)
                                            .child(group).child(TheFather)
                                            .child(studentPresentsPath).push()
                                    dbRefToPre.setValue(
                                        ModelData(
                                            data2.idOfStudent!!,
                                            dbRefToPre.key!!,
                                            data2.nameOfStudent!!
                                        )
                                    ).addOnCompleteListener {

                                        val dbRefFromAbs = firebaseDatabase.getReference(uid).child(
                                            GroupsNow
                                        ).child(group).child(TheFather).child(
                                            studentAbsentsPath
                                        ).child(data2.keyOfStudent!!)
                                        dbRefFromAbs.removeValue().addOnCompleteListener {
                                            val editor = getSharedPreferences(
                                                SectionKey,
                                                Context.MODE_PRIVATE
                                            ).edit()
                                            editor.putString(
                                                "$group${getTime()}",
                                                "$group${getTime()}"
                                            )
                                            editor.apply()

                                            showToast(
                                                this@MainActivity,
                                                "You added Your Self successfully "
                                            )
                                            startActivity(
                                                Intent(
                                                    this@MainActivity,
                                                    YouAddedActivity::class.java
                                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            )

                                        }

                                    }


                                }
                                layoutInflater.no.setOnClickListener {
                                    alertDialog.dismiss()

                                }


                            }

                        }
                    })


            }

            viewModel.trueOrFalse.observe(this, Observer {

                if (it == "true") {

                    buttonAddStudent.isEnabled = true
                    buttonAddStudent.text = "Add Student"

                } else {

                    buttonAddStudent.isEnabled = false
                    buttonAddStudent.text = "No Section Now"


                }


            })


        }
    }
}