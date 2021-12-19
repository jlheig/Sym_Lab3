package ch.heigvd.iict.sym.labo3.ibeacon

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import ch.heigvd.iict.sym.labo3.R
import org.altbeacon.beacon.*


private const val PERMISSION_REQUEST_FINE_LOCATION = 1

class IbeaconActivity : AppCompatActivity() {

    lateinit var list: ListView;
    lateinit var beaconAdapter : ArrayAdapter<MyBeacon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ibeacon)

        list = findViewById(R.id.ibeacon_list)
        beaconAdapter = ArrayAdapter<MyBeacon>(this, android.R.layout.simple_list_item_1)

        checkLocationPermission()

        val beaconManager = BeaconManager.getInstanceForApplication(this)
        val region = Region("beacons-region", null, null, null)

        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        )

        beaconManager.getRegionViewModel(region).rangedBeacons.observe(this, rangingObserver)
        beaconManager.startRangingBeacons(region)

        list.adapter = beaconAdapter
    }

    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        if (beacons.isEmpty()) {
            Toast.makeText(this, "no beacon", Toast.LENGTH_SHORT).show()
        }
        beaconAdapter.clear()
        for (beacon in beacons) {
            beacon.rssi
            beaconAdapter.add(
                MyBeacon(
                    beacon.rssi,
                    beacon.id2,
                    beacon.id3
                )
            )
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_FINE_LOCATION
            )
        }
    }
}