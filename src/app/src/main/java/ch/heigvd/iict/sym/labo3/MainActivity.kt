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
import android.os.PersistableBundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts


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
    }
}
