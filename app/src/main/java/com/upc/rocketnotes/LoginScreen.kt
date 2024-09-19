package com.upc.rocketnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upc.rocketnotes.ui.theme.RocketNotesTheme
import androidx.compose.ui.text.font.Font
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    // Estado para almacenar los valores del correo y la contraseña
    var user by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val robotoFontFamily = FontFamily(Font(R.font.robotoblackitalic))

    // Estructura de la interfaz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFFFFF)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF888888), fontSize = 36.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, fontFamily = robotoFontFamily)) {
                    append("\uD83D\uDDD2\uFE0F ROCKET")
                }
                withStyle(style = SpanStyle(color = Color(0xFF1EC089), fontSize = 36.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, fontFamily = robotoFontFamily)) {
                    append("NOTES")
                }
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y = (-50).dp)
        )

        // Imagen (sustituir por un recurso de imagen)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(width = 244.dp, height = 208.dp)
                .padding(bottom = 32.dp),
            contentScale = ContentScale.Fit
        )
        // Texto: Iniciar Sesión
        Text(
            text = "Iniciar Sesión",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFontFamily,  // Aplicar la fuente Roboto
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 45.dp, bottom = 16.dp), // Ajustar espacio entre el logo y el cuadro de texto
        )
        // Cuadro de texto: Usuario
        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Usuario *") },
            modifier = Modifier
                .width(290.dp)
                .height(60.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cuadro de texto: Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña *") },
            modifier = Modifier
                .width(290.dp)
                .height(60.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Texto: Olvidaste tu contraseña?
        Text(
            text = "¿Olvidaste tu contraseña?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,  // Texto en negrita
            color = Color.Black,
            textDecoration = TextDecoration.Underline, // Subrayado
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 45.dp, bottom = 16.dp),
            textAlign = TextAlign.Start
        )

        // Botón: Ingresar
        Button(
            onClick = { /* Acción para ingresar */ },
            modifier = Modifier
                .width(290.dp)
                .height(60.dp)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1EC089)),
            shape = RoundedCornerShape(5.dp) // Esquinas ligeramente redondeadas
        ) {
            Text(
                "Ingresar",
                fontFamily = robotoFontFamily,  // Aplicar la fuente Roboto
                fontSize = 18.sp
            )
        }

        // Botón: Registrarse
        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .width(290.dp)
                .height(60.dp)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1EC089)),
            shape = RoundedCornerShape(5.dp) // Esquinas ligeramente redondeadas
        ) {
            Text(
                "Registrarse",
                fontFamily = robotoFontFamily,  // Aplicar la fuente Roboto
                fontSize = 18.sp
            )
        }
    }
}
