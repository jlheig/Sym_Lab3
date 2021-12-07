package ch.heigvd.iict.sym.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent


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

        nfcBtn.setOnClickListener {
            val intent = Intent(this, NfcLoginActivity::class.java)
            startActivity(intent)
        }
    }
}
