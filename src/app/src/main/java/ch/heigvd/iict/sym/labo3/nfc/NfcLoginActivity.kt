package ch.heigvd.iict.sym.labo3.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ch.heigvd.iict.sym.labo3.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.experimental.and
import ch.heigvd.iict.sym.labo3.nfc.utils.*
import kotlinx.coroutines.channels.Channel


class NfcLoginActivity : AppCompatActivity() {
    private val TAG = "NfcLoginActivity"
    private val MIME_TEXT_PLAIN = "text/plain"
    val dataPass = Channel<String>()
    private var nfcValid : Boolean = false;

    private lateinit var email : TextView
    private lateinit var password : TextView
    private lateinit var validate : Button
    private lateinit var nfcAdapter: NfcAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_login)
        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);
        validate = findViewById(R.id.main_validate)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);



        if (nfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this,"NFC is disabled.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"NFC is enabled.", Toast.LENGTH_SHORT).show();
        }


        validate.setOnClickListener(){
            if(email.text?.toString() != "hello" ||
               password.text?.toString() != "world")
            {
                Toast.makeText(this, R.string.incorrect_credentials, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if(!nfcValid){
                Toast.makeText(this, R.string.incorrect_nfc_factor, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.i(TAG, "Login successful")
            val intent = Intent(this, NfcSecurityActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                Log.i(TAG, "Found NFC device!")
                val type = intent.type
                if (MIME_TEXT_PLAIN.equals(type)) {
                    lifecycleScope.launch {

                    }

                    val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)

                    lifecycleScope.launch {
                        if (tag != null) {
                            withContext(Dispatchers.IO){
                               val factor = readNfcData(tag)
                                validateNFCFactor(factor)
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
    
    private suspend fun validateNFCFactor(factor: String?){
        nfcValid = factor == "test"
        Log.i(TAG, "NFC factor received is $nfcValid")
    }

}