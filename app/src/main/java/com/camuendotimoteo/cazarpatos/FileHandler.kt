package com.camuendotimoteo.cazarpatos

interface FileHandler {

    fun saveInformation (datosAGrabar:Pair<String,String>)
    fun readInformation ():Pair<String, String>
}