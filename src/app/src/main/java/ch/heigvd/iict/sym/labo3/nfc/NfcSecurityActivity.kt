package ch.heigvd.iict.sym.labo3.nfc

import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ch.heigvd.iict.sym.labo3.R
import kotlinx.coroutines.channels.Channel

class NfcSecurityActivity : AppCompatActivity() {
    private val AUTHENTICATE_MAX = 10
    val dataPass = Channel<String>()

    private var authTime = 10
    private lateinit var maxSecurity : Button
    private lateinit var midSecurity : Button
    private lateinit var minSecurity : Button
    private lateinit var nfcAdapter: NfcAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_security)
        maxSecurity = findViewById(R.id.max_security_btn)
        midSecurity = findViewById(R.id.mid_security_btn)
        minSecurity = findViewById(R.id.min_security_btn)

    }

}