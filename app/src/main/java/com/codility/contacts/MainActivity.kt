package com.codility.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.codility.contacts.adapter.MyAdapter
import com.codility.contacts.listener.OnCallListener
import com.codility.contacts.listener.Utility
import com.codility.contacts.model.Contact
import kotlinx.android.synthetic.main.activity_main.*

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity(), OnCallListener<Contact> {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    override fun onCall(contact: Contact) {
        Utility.makeCall(this, contact.number)
    }

    override fun onMessage(contact: Contact) {
        Utility.doMessage(this, contact.number)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadContacts()
    }

    private fun loadContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE), PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val adapter = MyAdapter(getContacts())
            recyclerView.adapter = adapter
            adapter.setListener(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                showToast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun getContacts(): ArrayList<Contact> {
        val contacts = ArrayList<Contact>()
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                    if (cursorPhone.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contacts.add(Contact(name, phoneNumValue))
                        }
                    }
                    cursorPhone.close()
                }
            }
        } else {
            showToast("No contacts available!")
        }
        cursor.close()
        return contacts
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}