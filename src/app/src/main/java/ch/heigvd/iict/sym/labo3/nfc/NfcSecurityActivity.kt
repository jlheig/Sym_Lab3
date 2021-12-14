package ch.heigvd.iict.sym.labo3.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import ch.heigvd.iict.sym.labo3.R
import ch.heigvd.iict.sym.labo3.nfc.utils.correctFactor
import ch.heigvd.iict.sym.labo3.nfc.utils.readNfcData
import ch.heigvd.iict.sym.labo3.nfc.utils.setupForegroundDispatch
import ch.heigvd.iict.sym.labo3.nfc.utils.stopForegroundDispatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NfcSecurityActivity : AppCompatActivity() {
    private val TAG = "NfcSecurityActivity"
    private val MIME_TEXT_PLAIN = "text/plain"

    private val AUTHENTICATE_MAX = 10
    private val AUTHENTICATE_MID = 6
    private val AUTHENTICATE_MIN = 3

    private var authTime = AUTHENTICATE_MAX
    private lateinit var maxSecurity: Button
    private lateinit var midSecurity: Button
    private lateinit var minSecurity: Button
    private lateinit var nfcAdapter: NfcAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_security)
        maxSecurity = findViewById(R.id.max_security_btn)
        midSecurity = findViewById(R.id.mid_security_btn)
        minSecurity = findViewById(R.id.min_security_btn)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                decreaseSecurity()
            }
        }

        maxSecurity.setOnClickListener {
            checkAuth(AUTHENTICATE_MAX)
        }

        midSecurity.setOnClickListener {
            checkAuth(AUTHENTICATE_MID)
        }

        minSecurity.setOnClickListener() {
            checkAuth(AUTHENTICATE_MIN)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                Log.i(TAG, "Found NFC device!")
                val type = intent.type
                if (MIME_TEXT_PLAIN.equals(type)) {

                    val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

                    lifecycleScope.launch {
                        if (tag != null) {
                            withContext(Dispatchers.IO) {
                                val factor = readNfcData(tag)
                                resetAuth(factor)
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Wrong mime type: $type")
                }

            }

        }
    }

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch(this, nfcAdapter)
    }

    override fun onPause() {
        super.onPause()
        stopForegroundDispatch(this, nfcAdapter)
    }

    private fun checkAuth(minAuthRequired: Int) {
        var status = "Auth is "
        if (authTime >= minAuthRequired) {
            status += "valid"
        } else {
            status += "invalid"
        }
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
    }


    private fun decreaseSecurity() {
        while (true) {
            Thread.sleep(4000)
            authTime--
        }
    }

    private fun resetAuth(factor: String?) {
        if (correctFactor(factor)) {
            authTime = AUTHENTICATE_MAX
        }
    }


}