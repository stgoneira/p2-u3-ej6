package cl.stgoneira.android.iplavisos.ui

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CrearAvisoViewModel:ViewModel() {
    var titulo = mutableStateOf("")
    var descripcion = mutableStateOf("")
    var precio = mutableStateOf("0")
    var imageUri = mutableStateOf<Uri?>(null)
}