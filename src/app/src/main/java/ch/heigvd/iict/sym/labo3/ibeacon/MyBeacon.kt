package ch.heigvd.iict.sym.labo3.ibeacon

import org.altbeacon.beacon.Identifier

data class MyBeacon(private var rssi : Int, private val major : Identifier, private val minor : Identifier) {

    override fun toString(): String {
        return "rssi : ${rssi} | major : ${major} | minor : ${minor}"
    }
}
