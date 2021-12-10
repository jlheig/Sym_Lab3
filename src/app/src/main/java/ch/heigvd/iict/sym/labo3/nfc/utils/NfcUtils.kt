package ch.heigvd.iict.sym.labo3.nfc.utils

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.experimental.and


val TAG = "NfcUtils"

fun setupForegroundDispatch(activity: AppCompatActivity, nfcAdapter: NfcAdapter) {
    val intent: Intent = Intent(
        activity.applicationContext,
        activity.javaClass
    )

    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
    val pendingIntent =
        PendingIntent.getActivity(activity.applicationContext, 0, intent, 0);

    val filters = arrayOfNulls<IntentFilter>(1)
    val techList = arrayOf<Array<String>>()

    filters[0] = IntentFilter()
    filters[0]?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
    filters[0]?.addCategory(Intent.CATEGORY_DEFAULT)

    try {
        filters[0]!!.addDataType("text/plain")
    } catch (e: IntentFilter.MalformedMimeTypeException) {
        Log.e(TAG, "MalformedMimeTypeException", e)
    }

    nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, techList)
}

fun stopForegroundDispatch(activity: AppCompatActivity, nfcAdapter: NfcAdapter) {
    if (nfcAdapter != null) {
        nfcAdapter.disableForegroundDispatch(activity);
    }
}

suspend fun readNfcData(tag: Tag) : String? = withContext(Dispatchers.IO){
        val ndef = Ndef.get(tag);
        if (ndef == null) {
            Log.e(TAG, "NDEF is not supported by this Tag.")
            return@withContext null;
        }

        val ndefMessage = ndef.cachedNdefMessage;
        val records = ndefMessage.records

        val datas = ArrayList<String>()

        for (ndefRecord in records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)
            ) {
                datas.add(readText(ndefRecord))
            }
        }

        datas[0]

}

fun readText(record: NdefRecord): String {
    val payload = record.payload
    val textEncoding = Charsets.UTF_8
    val languageCodeLength: Int = payload[0].and(63).toInt()

    val data =
        String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1, textEncoding)
    Log.i(TAG, "Read data: $data")
    return data
}
