package com.abdelrahmanhamdy2.absencestudents.Others

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import java.text.DateFormat
import java.util.*

class Helper {




    companion object{

        const val UIDS="UIDS"
        const val GroupsNow="GroupsNow"
        const val idPath="idPath"
        const val groupPath="groupPath"
        const val SectionKey="SectionKey"
        const val booleanPath="booleanPath"
        const val studentAbsentsPath="StudentAbsentsPath"
        const val studentPresentsPath="StudentPresentsPath"
        const val TheFather="TheFather"



        fun log(text:String){

            Log.i("MyTag",text)
        }
        fun checkNetwork(context: Context):Boolean{

            var connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state== NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(
                        ConnectivityManager
                            .TYPE_MOBILE).state== NetworkInfo.State
                .CONNECTED

        }
        fun showToast(context: Context, text:String){

            Toast.makeText(context,text, Toast.LENGTH_LONG).show()

        }

        fun getTime(): String {
            val calender = Calendar.getInstance()
            return DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calender.time)
        }





    }

}