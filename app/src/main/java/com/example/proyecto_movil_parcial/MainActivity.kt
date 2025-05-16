package com.example.proyecto_movil_parcial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Configurar Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Obtener el usuario actual
        val auth = Firebase.auth
        val user = auth.currentUser
        val userName = user?.displayName ?: "Usuario"

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        userName = if (user != null) "Hola, ${userName}!" else "Loading..",
                        onLogoutClick = { signOutAndStartSignInActivity() }
                    )
                }
            }
        }
    }

    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Optional: Update UI or show a message to the user
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

@Composable
fun MainScreen(userName: String, onLogoutClick: () -> Unit) {
    // Estados para controlar qué pantalla se muestra
    var currentScreen by remember { mutableStateOf("home") }

    // Colores personalizados basados en la imagen
    val beigeHeader = Color(0xFFD0B49F)
    val beigeLightBox = Color(0xFFEEDFD0)
    val beigeButton = Color(0xFFD0B49F)
    val greenButton = Color(0xFF71A194)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header con el saludo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(beigeHeader)
                .padding(16.dp)
        ) {
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        // Contenido principal basado en la pantalla actual
        when (currentScreen) {
            "home" -> HomeScreen(beigeButton, beigeLightBox, greenButton)
            "dictionary" -> DictionaryScreen()
            "challenges" -> ChallengesScreen()
            "profile" -> ProfileScreen(onLogoutClick)
        }

        // Barra de navegación inferior
        NavigationBar(
            containerColor = Color.White
        ) {
            // Icono Inicio
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Inicio"
                    )
                },
                label = { Text("Inicio", fontSize = 12.sp) },
                selected = currentScreen == "home",
                onClick = { currentScreen = "home" }
            )

            // Icono Mi diccionario
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Mi diccionario"
                    )
                },
                label = { Text("Mi diccionario", fontSize = 12.sp) },
                selected = currentScreen == "dictionary",
                onClick = { currentScreen = "dictionary" }
            )

            // Icono Desafíos
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Desafíos"
                    )
                },
                label = { Text("Desafíos", fontSize = 12.sp) },
                selected = currentScreen == "challenges",
                onClick = { currentScreen = "challenges" }
            )

            // Icono Cuenta/Perfil
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Cuenta"
                    )
                },
                label = { Text("Cuenta", fontSize = 12.sp) },
                selected = currentScreen == "profile",
                onClick = { currentScreen = "profile" }
            )
        }
    }
}

@Composable
fun HomeScreen(beigeButton: Color, beigeLightBox: Color, greenButton: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título "Tus palabras en frase"
        Text(
            text = "Tus palabras en frase",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Caja con mensaje de no palabras
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(beigeLightBox)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no tienes palabras nuevas buscadas",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón Mi Diccionario
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(beigeButton)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Icono de libro
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Diccionario",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mi Diccionario",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            // Botón Nueva palabra
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(greenButton)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Icono de documento con +
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Nueva palabra",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nueva palabra",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun DictionaryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pantalla del Diccionario",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Aquí se mostrarán tus palabras guardadas",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ChallengesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pantalla de Desafíos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Aquí se mostrarán tus desafíos",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ProfileScreen(onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.usuario),
            contentDescription = "Perfil",
            modifier = Modifier.size(120.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Información de tu perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Botón de cerrar sesión
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(8.dp)
        ) {
            Text(
                text = "Cerrar Sesión",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen(
            userName = "Hola, Vanesa!",
            onLogoutClick = {}
        )
    }
}