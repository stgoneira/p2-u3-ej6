package cl.stgoneira.android.iplavisos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    IplAvisosUI()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun IplAvisosUI() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TITULO
        Text(
            "IplAvisos",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 70.dp)
        )

        // BOTONES
        Column {
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { /*TODO*/ }
            ) {
                Text("Buscar Avisos")
            }
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { /*TODO*/ }
            ) {
                Text("Publicar")
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