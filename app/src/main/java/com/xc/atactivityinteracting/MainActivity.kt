package com.xc.atactivityinteracting

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger

val PICK_CONTACT_REQUEST = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonCall.setOnClickListener({
            view ->
            val number = Uri.parse("tel:5551234");
            val callIntent = Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent)
        })

        buttonViewWeb.setOnClickListener({
            view ->
            val webpage = Uri.parse("http://www.android.com")
            startActivity(Intent(Intent.ACTION_VIEW, webpage))
        })

        buttonSendEmail.setOnClickListener({
            view ->
            val intent = Intent(Intent.ACTION_SEND)
            // The intent does not have a URI, so declare the "text/plain" MIME type
            intent.data = Uri.parse("mailto:")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("xiechao06@gmail.com")) // recipients
            intent.putExtra(Intent.EXTRA_SUBJECT, "Email subject")
            intent.putExtra(Intent.EXTRA_TEXT, "Email message text")
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
            // You can also attach multiple items by passing an ArrayList of Uris
            if (intent.resolveActivity(packageManager) != null) {
                val chooser = Intent.createChooser(intent, title);
                startActivity(chooser);
            }
        })

        buttonPickContact.setOnClickListener({
            view ->
            val pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE // Show user only contacts w/ phone numbers
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST)
        })

        buttonInvokeFirstApp.setOnClickListener({
            view ->
            val intent = Intent(Intent.ACTION_SEND, Uri.parse("androidtraining://"))
            intent.putExtra("EXTRA_MESSAGE", "HELLO from activity interacting")
            Log.v("MainActivity", intent.data.toString())
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                val contactUri = data!!.data;
                // We only need the NUMBER column, because there will be only one row in the result
                val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER);

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                val cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null)
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                val column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                val number = cursor.getString(column);
                Toast.makeText(this, "You just choose $number", Toast.LENGTH_SHORT).show()

            }

        }

    }
}
