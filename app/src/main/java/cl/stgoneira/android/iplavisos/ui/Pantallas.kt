package cl.stgoneira.android.iplavisos.ui

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.stgoneira.android.iplavisos.R
import coil.compose.AsyncImage
import java.io.File


@Composable
fun IplAvisosApp(
    navController: NavHostController = rememberNavController(),
    crearAvisoViewModel: CrearAvisoViewModel = viewModel()
) {
    NavHost(
        navController = navController
        , startDestination = "inicio"
    ) {

        composable("inicio") {
            InicioUI(
                goToAvisos = { navController.navigate("avisos") },
                goToCrearAviso = { navController.navigate("crearAviso") }
            )
        }

        composable("avisos") {
            AvisosUI()
        }

        composable("crearAviso") {
            CrearAvisoUI(
                goToPreview = { navController.navigate("fotoPreview")},
                crearAvisoViewModel = crearAvisoViewModel
            )
        }

        composable("fotoPreview") {
            FotoPreviewUI(
                goToCrearAviso = { navController.navigate("crearAviso") },
                crearAvisoViewModel = crearAvisoViewModel
            )
        }
    }
}

@Composable
fun FotoPreviewUI(
    goToCrearAviso:()->Unit = {},
    crearAvisoViewModel: CrearAvisoViewModel
) {
    val contexto = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    var cameraController: LifecycleCameraController? by remember { mutableStateOf(null) }

    AndroidView(
        factory = {
            cameraController = LifecycleCameraController(contexto).apply {
                bindToLifecycle( lifeCycleOwner )
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            }
            PreviewView(contexto).apply {
                controller = cameraController
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                setOnClickListener {
                    val fileName = "img_${System.currentTimeMillis()}.jpg"
                    val file = File(contexto.filesDir, fileName)
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                    val onImageSaveCallback = object : OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            Log.v("IplAvisos", outputFileResults.savedUri.toString())
                            crearAvisoViewModel.imageUri.value = outputFileResults.savedUri
                            goToCrearAviso()
                        }
                        override fun onError(exception: ImageCaptureException) {
                            exception.message?.let { it1 -> Log.e("IplAvisos.errors", it1) }
                        }
                    }
                    cameraController!!.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(contexto),
                        onImageSaveCallback
                    )
                }
            }
        },
        onRelease = {
            cameraController?.unbind()
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showSystemUi = true)
@Composable
fun CrearAvisoUI(
    goToPreview:() -> Unit = {},
    crearAvisoViewModel: CrearAvisoViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    val lanzadorPermisos = rememberLauncherForActivityResult( ActivityResultContracts.RequestPermission() ) { permisoConcedido ->
        if( permisoConcedido ) {
            goToPreview()
        } else {
            // mostrar mensaje
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("Crear Aviso", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(30.dp))
        TextField(
            value = crearAvisoViewModel.titulo.value,
            onValueChange = {crearAvisoViewModel.titulo.value = it},
            label = { Text(stringResource(R.string.titulo)) },
        )

        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = crearAvisoViewModel.descripcion.value,
            onValueChange = {crearAvisoViewModel.descripcion.value = it},
            label={ Text(stringResource(R.string.descripcion)) },
            minLines = 6,
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = crearAvisoViewModel.precio.value,
            onValueChange = {crearAvisoViewModel.precio.value = it},
            label={ Text(stringResource(R.string.precio)) },
            prefix = { Text("$") }
        )

        IconButton(
            onClick = {
                lanzadorPermisos.launch(Manifest.permission.CAMERA)
            }
        ) {
            Icon(
                rememberVectorPainter(image = Icons.Outlined.AddAPhoto),
                contentDescription = stringResource(R.string.agregar_foto)
            )
        }

        crearAvisoViewModel.imageUri.let {
            AsyncImage(model = it.value, contentDescription = null)
        }

        if( crearAvisoViewModel.imageUri.value == null) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.LightGray))
        }

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(stringResource(R.string.publicar))
        }
    }
}

@Composable
fun AvisosUI() {
    Text("TODO: Listado de Avisos")
}

//@Preview(showSystemUi = true)
@Composable
fun InicioUI(
    goToAvisos:()->Unit = {},
    goToCrearAviso:()->Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TITULO
        Text(
            stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 70.dp)
        )

        // BOTONES
        Column {
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { goToAvisos() }
            ) {
                Text(stringResource(id = R.string.btn_buscar_avisos))
            }
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { goToCrearAviso() }
            ) {
                Text(stringResource(id = R.string.btn_crear_aviso))
            }
        }

        // FOOTER
        Text(
            "Todos los derechos reservados 2024",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.inversePrimary
        )
    }
}