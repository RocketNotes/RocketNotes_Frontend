package com.upc.rocketnotes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(){
    val robotoFontFamily = FontFamily(Font(R.font.robotoblackitalic))
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF888888), fontSize = 20.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, fontFamily = robotoFontFamily)) {
                        append("\uD83D\uDDD2\uFE0F ROCKET")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF1EC089), fontSize = 20.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, fontFamily = robotoFontFamily)) {
                        append("NOTES")
                    }
                }
            )
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon",
                    tint =  Color(0xFF888888)
                )
            }
        },
        // Aquí agregas la sombra con elevación
        modifier = Modifier.shadow(8.dp)
    )

}