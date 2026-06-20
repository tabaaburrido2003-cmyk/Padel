package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.data.User
import com.example.ui.PadelViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: PadelViewModel,
    currentUser: User
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var winRate = 0.0
    if (currentUser.matchesPlayed > 0) {
        winRate = (currentUser.matchesWon.toDouble() / currentUser.matchesPlayed.toDouble()) * 100
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Upper Profile Header card
        ProfileMainInfoCard(currentUser)

        // Statistics Block
        ProfileStatsSection(currentUser, winRate)

        // Simulated MATCH LOGGER section
        SimulateMatchLoggerCard(currentUser, viewModel, context, coroutineScope)

        // Security Options Block
        SecuritySettingsCard(viewModel, currentUser, context)
    }
}

@Composable
fun ProfileMainInfoCard(currentUser: User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlateSurface)
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circle Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(VoltGreen, CircleShape)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SlateCard, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsTennis,
                        contentDescription = "Padel Ball",
                        tint = VoltGreen,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentUser.name,
                fontSize = 22.sp,
                color = TextBlack,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = currentUser.email,
                color = TextGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Badges Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Level Badge
                Box(
                    modifier = Modifier
                        .background(PadelBlue, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Nivel ${currentUser.level} (${currentUser.levelLabel})",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                // Preferred Court side badge
                Box(
                    modifier = Modifier
                        .background(SlateSurface, RoundedCornerShape(20.dp))
                        .border(1.dp, PadelBlue.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Lado: ${currentUser.position}",
                        color = PadelBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Score Display in Sleek Dark Theme style of the Quick Stats card
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.widthIn(max = 240.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${currentUser.rankingPoints}",
                        color = VoltGreen,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "PUNTOS CLASIFICATORIOS",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileStatsSection(currentUser: User, winRate: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Estadísticas de Pista",
            color = TextBlack,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatsBlock(
                title = "Jugados",
                value = currentUser.matchesPlayed.toString(),
                icon = Icons.Default.SportsTennis,
                modifier = Modifier.weight(1f)
            )
            StatsBlock(
                title = "Victorias",
                value = currentUser.matchesWon.toString(),
                icon = Icons.Default.TrendingUp,
                modifier = Modifier.weight(1f),
                color = PadelBlue
            )
            StatsBlock(
                title = "Eficacia",
                value = String.format("%.0f%%", winRate),
                icon = Icons.Default.EmojiEvents,
                modifier = Modifier.weight(1.1f),
                color = SecurityAmber
            )
        }
    }
}

@Composable
fun StatsBlock(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    val finalIconAndValueColor = if (color == Color.Unspecified) TextBlack else color

    Card(
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (color == Color.Unspecified) TextGray else finalIconAndValueColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = finalIconAndValueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = title,
                color = TextGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SimulateMatchLoggerCard(
    currentUser: User,
    viewModel: PadelViewModel,
    context: android.content.Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope
) {
    var isWinSelection by remember { mutableStateOf(true) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Simulador de Resultados rápido 🎮",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )
            Text(
                text = "Registrar partidos de simulación rápida en local para actualizar y probar la reactividad del ranking.",
                fontSize = 11.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // WON Button Selection
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isWinSelection) VoltGreen.copy(alpha = 0.2f) else Color(0xFFF1F5F9),
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (isWinSelection) VoltGreen else Color.Transparent,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { isWinSelection = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Victoria (+25 pts)", color = TextBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // LOST Button Selection
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (!isWinSelection) DangerRed.copy(alpha = 0.1f) else Color(0xFFF1F5F9),
                            RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            if (!isWinSelection) DangerRed else Color.Transparent,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { isWinSelection = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Derrota (-15 pts)", color = if (!isWinSelection) DangerRed else TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val ptsDiff = if (isWinSelection) 25 else -15
                    val newPoints = (currentUser.rankingPoints + ptsDiff).coerceAtLeast(0)
                    val newWon = if (isWinSelection) currentUser.matchesWon + 1 else currentUser.matchesWon
                    val newLost = if (!isWinSelection) currentUser.matchesLost + 1 else currentUser.matchesLost
                    
                    val updatedUser = currentUser.copy(
                        matchesPlayed = currentUser.matchesPlayed + 1,
                        matchesWon = newWon,
                        matchesLost = newLost,
                        rankingPoints = newPoints
                    )

                    coroutineScope.launch {
                        viewModel.updateUser(updatedUser)
                        Toast.makeText(context, "Resultado registrado localmente. ¡Tus puntos se actualizaron!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SlateCard, contentColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text("Registrar Marcador", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun SecuritySettingsCard(
    viewModel: PadelViewModel,
    currentUser: User,
    context: android.content.Context
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Seguridad del Dispositivo & Privacidad",
                fontWeight = FontWeight.Bold,
                color = TextBlack,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // SHA-256 Info line
            SecurityInfoRow(
                icon = Icons.Default.Lock,
                title = "Algoritmo de PIN",
                value = "SHA-256 + Salt"
            )
            Divider(color = Color(0xFFF1F5F9), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // RGPD Compliant info line
            SecurityInfoRow(
                icon = Icons.Default.Shield,
                title = "Privacidad de Datos",
                value = "RGPD Local Activo"
            )
            Divider(color = Color(0xFFF1F5F9), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

            // DB Storage info
            SecurityInfoRow(
                icon = Icons.Default.SdCard,
                title = "Base de datos",
                value = "Room SQLite Encriptada Local"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Lock screen check test button
                OutlinedButton(
                    onClick = {
                        viewModel.lockApp()
                        Toast.makeText(context, "Pantalla bloqueada con PIN de seguridad", Toast.LENGTH_SHORT).show()
                    },
                    border = borderStroke(SecurityAmber),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1.1f)
                ) {
                    Icon(Icons.Default.LockClock, contentDescription = null, tint = SecurityAmber, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Bloquear PIN", color = SecurityAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Log out
                Button(
                    onClick = {
                        viewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.1f), contentColor = DangerRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, DangerRed, RoundedCornerShape(10.dp))
                ) {
                    Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Salir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SecurityInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = PadelBlue, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, color = TextBlack, fontSize = 13.sp)
        }
        Text(text = value, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
