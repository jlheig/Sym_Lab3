package ch.heigvd.iict.sym.labo3

import android.app.PendingIntent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.util.Log


class NfcLoginActivity : AppCompatActivity() {
    private val TAG = "NfcLoginActivity"
    private lateinit var email : TextView
    private lateinit var password : TextView
    private lateinit var nfcAdapter: NfcAdapter
    private var nfcValid : Boolean = false;
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
            if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.action)) {
                Log.i(TAG, "Found NFC device!")
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

    suspend fun readNfcData(){

    }
}