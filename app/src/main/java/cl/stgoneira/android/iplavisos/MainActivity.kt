package cl.stgoneira.android.iplavisos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.stgoneira.android.iplavisos.ui.theme.IplavisosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IplavisosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IplAvisosApp()
                }
            }
        }
    }
}

@Composable
fun IplAvisosApp(
    navController: NavHostController = rememberNavController()
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
            CrearAvisoUI()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CrearAvisoUI() {
    var titulo by remember{  mutableStateOf("") }
    var descripcion by remember{  mutableStateOf("") }
    var precio by remember{  mutableStateOf("0") }
    val scrollState = rememberScrollState()

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
            value = titulo,
            onValueChange = {titulo = it},
            label = {Text(stringResource(R.string.titulo))},
        )

        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = descripcion,
            onValueChange = {descripcion = it},
            label={Text(stringResource(R.string.descripcion))},
            minLines = 6,
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = precio,
            onValueChange = {precio = it},
            label={Text(stringResource(R.string.precio))},
            prefix = {Text("$")}
        )

        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                rememberVectorPainter(image = Icons.Outlined.AddAPhoto),
                contentDescription = stringResource(R.string.agregar_foto)
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.LightGray))

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