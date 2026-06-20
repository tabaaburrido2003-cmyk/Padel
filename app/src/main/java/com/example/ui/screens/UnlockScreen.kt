package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.User
import com.example.ui.PadelViewModel
import com.example.ui.theme.*

@Composable
fun UnlockScreen(
    viewModel: PadelViewModel,
    currentUser: User?,
    onUnlocked: () -> Unit
) {
    if (currentUser == null) {
        RegisterView(viewModel)
    } else {
        PinUnlockView(viewModel, currentUser, onUnlocked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(viewModel: PadelViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var level by remember { mutableStateOf(3.0) }
    var position by remember { mutableStateOf("Ambos") }
    var pin by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo Header
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(VoltGreen, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎾", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "PadelMatch",
                fontSize = 28.sp,
                color = TextBlack,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Únete a la plataforma de pádel más moderna. Encuentra partidos libres y sube en el ranking de tu club.",
                fontSize = 13.sp,
                color = TextGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Security Box Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightPadelBlue, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = PadelBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Seguridad Local Protegida",
                        color = TextBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tus datos filiados y teléfono quedan protegidos con tu PIN. Nadie verá tu número a menos que compartan pista contigo.",
                        color = TextGray,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Registration Card
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Crear Cuenta de Jugador",
                        color = TextBlack,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre Completo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PadelBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PadelBlue,
                            unfocusedLabelColor = TextGray,
                            focusedTextColor = TextBlack,
                            unfocusedTextColor = TextBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_name_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PadelBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PadelBlue,
                            unfocusedLabelColor = TextGray,
                            focusedTextColor = TextBlack,
                            unfocusedTextColor = TextBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_email_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Número de Teléfono") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PadelBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PadelBlue,
                            unfocusedLabelColor = TextGray,
                            focusedTextColor = TextBlack,
                            unfocusedTextColor = TextBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_phone_input"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Padel Selection Level Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nivel de Pádel (1.0 - 7.0)",
                            color = TextBlack,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .background(PadelBlue, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = String.format("%.1f", level),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Slider(
                        value = level.toFloat(),
                        onValueChange = { level = kotlin.math.round(it * 10) / 10.0 },
                        valueRange = 1.0f..7.0f,
                        steps = 59,
                        colors = SliderDefaults.colors(
                            thumbColor = PadelBlue,
                            activeTrackColor = PadelBlue,
                            inactiveTrackColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Categoría: " + when {
                            level <= 2.5 -> "Principiante (1.0 - 2.5)"
                            level <= 4.0 -> "Intermedio (2.6 - 4.0)"
                            level <= 5.5 -> "Avanzado (4.1 - 5.5)"
                            else -> "Profesional (5.6 - 7.0)"
                        },
                        fontSize = 12.sp,
                        color = PadelBlue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Position Selector
                    Text(
                        text = "Lado preferido en la pista:",
                        color = TextBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Drive", "Revés", "Ambos").forEach { pos ->
                            val selected = position == pos
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selected) PadelBlue else Color(0xFFF1F5F9),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { position = pos }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                  Text(
                                    text = pos,
                                    color = if (selected) Color.White else TextGray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // PIN Code Setup Box
                    Text(
                        text = "Código PIN de Acceso (4 dígitos)",
                        color = TextBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    OutlinedTextField(
                        value = pin,
                        onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) pin = it },
                        label = { Text("Crear PIN de Seguridad") },
                        placeholder = { Text("Introduce 4 números") },
                        leadingIcon = { Icon(Icons.Default.LockOpen, contentDescription = null, tint = TextGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PadelBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedLabelColor = PadelBlue,
                            unfocusedLabelColor = TextGray,
                            focusedTextColor = TextBlack,
                            unfocusedTextColor = TextBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_pin_input"),
                        singleLine = true
                    )
                }
            }

            // Error Display
            if (errorMsg != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMsg ?: "",
                    color = DangerRed,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button inspired by Vibrant Palette Design
            Button(
                onClick = {
                    viewModel.registerNewUser(
                        name = name,
                        email = email,
                        phone = phone,
                        level = level,
                        position = position,
                        pin = pin,
                        onSuccess = { errorMsg = null },
                        onError = { errorMsg = it }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SlateCard,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("register_submit_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Registrarme y Acceder", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PinUnlockView(
    viewModel: PadelViewModel,
    currentUser: User,
    onUnlocked: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val errorMsg by viewModel.verificationError.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(VoltGreen, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎾", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PadelMatch",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = TextBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¡Hola de nuevo, ${currentUser.name}!",
                color = TextBlack,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Ingresa tu PIN de 4 dígitos para desbloquear tus estadísticas y partidos.",
                color = TextGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // PIN Dot Indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..4) {
                    val filled = pin.length >= i
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = if (filled) PadelBlue else Color(0xFFE2E8F0),
                                shape = CircleShape
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (filled) PadelBlue else Color(0xFFCBD5E1),
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMsg != null) {
                Text(
                    text = errorMsg ?: "",
                    color = DangerRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Virtual Keyboard Grid
            Column(
                modifier = Modifier.widthIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (col in 1..3) {
                            val num = row * 3 + col
                            NumButton(
                                text = num.toString(),
                                onClick = { if (pin.length < 4) pin += num.toString() }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .clickable { pin = "" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Borrar", color = DangerRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    NumButton(
                        text = "0",
                        onClick = { if (pin.length < 4) pin += "0" }
                    )

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(PadelBlue, CircleShape)
                            .clickable {
                                if (pin.length == 4) {
                                    val success = viewModel.verifyPinAndUnlock(pin)
                                    if (success) {
                                        onUnlocked()
                                    } else {
                                        pin = ""
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Validar", tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Cerrar sesión / Registrar otra cuenta",
                color = TextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { viewModel.logout() }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun NumButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(Color.White, CircleShape)
            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextBlack,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
