package ch.heigvd.iict.sym.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
import ch.heigvd.iict.sym.labo3.ibeacon.IbeaconActivity
import ch.heigvd.iict.sym.labo3.nfc.NfcLoginActivity


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

    private fun checkPermissions(permission: String, permissionCode: Int){
        if(ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), permissionCode);
        }
        else{
            startActivity(Intent(this, CodeBarreActivity::class.java))
        }
    }

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
            
        ibeaconBtn.setOnClickListener{
            val intent = Intent(this, IbeaconActivity::class.java)
            startActivity(intent)
        }
        
        nfcBtn.setOnClickListener {
            val intent = Intent(this, NfcLoginActivity::class.java)
            startActivity(intent)
        }
    }
}
