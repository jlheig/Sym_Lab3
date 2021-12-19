package ch.heigvd.iict.sym.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.os.PersistableBundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import ch.heigvd.iict.sym.labo3.ibeacon.IbeaconActivity
import ch.heigvd.iict.sym.labo3.nfc.NfcLoginActivity


class MainActivity : AppCompatActivity() {

    private lateinit var nfcBtn : Button
    private lateinit var barcodeBtn : Button
    private lateinit var ibeaconBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nfcBtn = findViewById(R.id.main_nfcButton)
        barcodeBtn   = findViewById(R.id.main_codeBarreButton)
        ibeaconBtn   = findViewById(R.id.main_iBeaconButton)

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
