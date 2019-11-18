package com.sa.vocalizesdk

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sdk.vocalize.Vocalize
import com.sdk.vocalize.VocalizeInitializeListener
import com.sdk.vocalize.VocalizeListener

class HomeMainActivity : AppCompatActivity(), VocalizeInitializeListener {

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPhonePermission()
        }

    }

    private fun getPhonePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                )
            ) {

            }

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                101
            )
        } else {
            Vocalize.initialize(this@HomeMainActivity)
            Vocalize.setInitializeListener(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101) {
            if (grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                Vocalize.initialize(this@HomeMainActivity)
                Vocalize.setInitializeListener(this)
            } else {
                getPhonePermission()
            }

            return
        }

    }

    override fun onInit() {
        // Get the action and data from the intent to handle it.
        val action: String? = intent?.action
        when (action) {
            // When the action is triggered by a deep-link, Intent.Action_VIEW will be used
            Intent.ACTION_VIEW -> callMethod(intent)

            // Otherwise start the app as you would normally do.
            else -> {

            }
        }
    }

    override fun onError(error: String) {
        Log.e("Error=====>> ", error)
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun callMethod(intent: Intent?) {
        Vocalize.searchKeyword(intent?.data, object : VocalizeListener {

            override fun onSuccess() {

            }

            override fun onError(error: String) {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Vocalize.reset()
    }
}