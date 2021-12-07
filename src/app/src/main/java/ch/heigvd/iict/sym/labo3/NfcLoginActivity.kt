package ch.heigvd.iict.sym.labo3

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.experimental.and


class NfcLoginActivity : AppCompatActivity() {
    private val TAG = "NfcLoginActivity"
    private val MIME_TEXT_PLAIN = "text/plain"

    private var nfcValid : Boolean = false;

    private lateinit var email : TextView
    private lateinit var password : TextView
    private lateinit var nfcAdapter: NfcAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_login)
        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this,"NFC is disabled.", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"NFC is enabled.", Toast.LENGTH_LONG).show();
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            if(NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                Log.i(TAG, "Found NFC device!")
                val type = intent.type
                if (MIME_TEXT_PLAIN.equals(type)) {
                    val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                    lifecycleScope.launch {
                        if (tag != null) {
                            readNfcData(tag)
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
        setupForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        stopForegroundDispatch()
    }

    private fun setupForegroundDispatch() {
        val intent: Intent = Intent(
            this.applicationContext,
            this.javaClass
        )

        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
        val pendingIntent =
            PendingIntent.getActivity(this.applicationContext, 0, intent, 0);

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        filters[0]?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0]?.addCategory(Intent.CATEGORY_DEFAULT)

        try {
            filters[0]!!.addDataType("text/plain")
        } catch (e: MalformedMimeTypeException) {
            Log.e(TAG, "MalformedMimeTypeException", e)
        }

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    private fun stopForegroundDispatch(){
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private suspend fun readNfcData(tag : Tag){
        return withContext(Dispatchers.IO){
             var ndef = Ndef.get(tag);
            if (ndef == null) {
                Log.e(TAG, "NDEF is not supported by this Tag.")
                return@withContext;
            }

            var ndefMessage = ndef.cachedNdefMessage;
            var records = ndefMessage.records

            for(ndefRecord in records){
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                    Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT))
                {
                    readText(ndefRecord)
                }
            }
        }
    }

    private fun readText(record: NdefRecord){
        val payload = record.payload
        val textEncoding = Charsets.UTF_8
        val languageCodeLength: Int = payload[0].and(63).toInt()
        val data = String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1, textEncoding)
        Log.i(TAG, "Read data: $data")
    }
}