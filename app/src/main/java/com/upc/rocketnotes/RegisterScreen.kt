package com.upc.rocketnotes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upc.rocketnotes.R
import com.upc.rocketnotes.ui.theme.RocketNotesTheme
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController


@Composable
fun RegisterScreen(navController: NavHostController) {
    // Estado para almacenar los valores de los campos
    var user by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    val robotoFontFamily = FontFamily(Font(R.font.robotoblackitalic))
    var selectedRole by remember { mutableStateOf("Administrador") }

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
                .size(width = 184.dp, height = 148.dp)
                .padding(bottom = 32.dp),
            contentScale = ContentScale.Fit
        )
        // Texto: Registrarse
        Text(
            text = "Registrarse",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFontFamily,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 45.dp, bottom = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de selección de rol
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 45.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RoleButton(
                text = "Administrador",
                isSelected = selectedRole == "Administrador",
                onClick = { selectedRole = "Administrador" }
            )

            RoleButton(
                text = "Profesor",
                isSelected = selectedRole == "Profesor",
                onClick = { selectedRole = "Profesor" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        // Cuadro de texto: Confirma tu contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirma tu contraseña *") },
            modifier = Modifier
                .width(290.dp)
                .height(60.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón: Registrarse
        Button(
            onClick = { /* Acción para registrarse */ },
            modifier = Modifier
                .width(290.dp)
                .height(60.dp)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1EC089)),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                "Registrarse",
                fontFamily = robotoFontFamily,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón: ¿Ya tienes una cuenta? Ingresa aquí
        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .width(290.dp)
                .height(60.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                "¿Ya tienes una cuenta? Ingresa aquí",
                fontFamily = robotoFontFamily,
                fontSize = 16.sp,
                color = Color(0xFF1EC089),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RoleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(50.dp)
            .padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF1EC089) else Color.White,
            contentColor = if (isSelected) Color.White else Color(0xFF1EC089)
        ),
        shape = RoundedCornerShape(8.dp),

    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RocketNotesTheme {

    }
}