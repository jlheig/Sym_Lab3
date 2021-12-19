package ch.heigvd.iict.sym.labo3

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.PersistableBundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest


class MainActivity : AppCompatActivity() {

    private lateinit var nfcBtn : Button
    private lateinit var barcodeBtn : Button
    private lateinit var ibeaconBtn : Button

    companion object {
        private const val CAMERA_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nfcBtn = findViewById(R.id.main_nfcButton)
        barcodeBtn   = findViewById(R.id.main_codeBarreButton)
        ibeaconBtn   = findViewById(R.id.main_iBeaconButton)

        barcodeBtn.setOnClickListener {
            checkPermissions(Manifest.permission.CAMERA, CAMERA_PERMISSION)
        }
    }


    //Next two functions are from GeeksForGeeks, their tutorial was really useful
    //SRC : https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/

    /**
     * This function checks if the permission we are asking for is authorised or not on this device,
     * if we are authorised we start the activity, other wise we ask for the permission
     */
    private fun checkPermissions(permission: String, permissionCode: Int){
        if(ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), permissionCode);
        }
        else{
            startActivity(Intent(this, CodeBarreActivity::class.java))
        }
    }

    /**
     * This function requests the permission we need if we never asked for it yet, it starts the activity if its accepted
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                startActivity(Intent(this, CodeBarreActivity::class.java))
            }
        }
    }
}
