package com.codility.contacts.listener

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast

object Utility {

    fun makeCall(context: Context, phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:".plus(phoneNumber))
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        startActivity(context, callIntent, null)
        Toast.makeText(context, "Calling...", Toast.LENGTH_SHORT).show()
    }

    fun doMessage(context: Context, phoneNumber: String) {
        val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:".plus(phoneNumber)))
        startActivity(context, smsIntent, null)
    }
}