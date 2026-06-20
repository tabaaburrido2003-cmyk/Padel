package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.MatchInvitation
import com.example.data.User
import com.example.ui.PadelViewModel
import com.example.ui.theme.*

@Composable
fun MatchFeedScreen(
    viewModel: PadelViewModel,
    currentUser: User
) {
    val invitations by viewModel.filteredInvitations.collectAsStateWithLifecycle()
    val levelFilter by viewModel.feedLevelFilter.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // App Bar (Top Nav)
            MatchFeedHeader()

            // User Quick Stats Card (tu nivel)
            UserQuickStatsCard(currentUser)

            // Section Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Falta uno?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextBlack
                )
                Box(
                    modifier = Modifier
                        .background(LightPadelBlue, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${invitations.size} partidos activos",
                        color = PadelBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            // Filtering Chips Row
            LevelFilterChipsSection(
                selectedFilter = levelFilter,
                onFilterSelected = { viewModel.updateFeedLevelFilter(it) }
            )

            // List of Open Matches
            if (invitations.isEmpty()) {
                EmptyMatchFeedState { showCreateDialog = true }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(invitations, key = { it.id }) { invitation ->
                        MatchInvitationCard(
                            invitation = invitation,
                            currentUser = currentUser,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }

        // Floating Action Button to post new game (VoltGreen Accent)
        FloatingActionButton(
            onClick = { showCreateDialog = true },
            containerColor = VoltGreen,
            contentColor = TextBlack,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .testTag("create_match_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Publicar Partido",
                modifier = Modifier.size(28.dp)
            )
        }

        if (showCreateDialog) {
            CreateMatchInvitationDialog(
                viewModel = viewModel,
                onDismiss = { showCreateDialog = false }
            )
        }
    }
}

@Composable
fun MatchFeedHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Logo Accent Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(VoltGreen, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎾", fontSize = 20.sp)
            }
            Column {
                Text(
                    text = "PadelMatch",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = "Conecta y compite localmente",
                    fontSize = 11.sp,
                    color = TextGray
                )
            }
        }
        
        // Secure indicator badge
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(PadelBlue, CircleShape)
                )
                Text("SECURE", color = PadelBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun UserQuickStatsCard(currentUser: User) {
    val categoryLabel = when {
        currentUser.level <= 2.5 -> "BRONCE"
        currentUser.level <= 4.0 -> "PLATA"
        currentUser.level <= 5.5 -> "ORO"
        else -> "PLATINO"
    }
    
    val levelSubLabel = when {
        currentUser.level <= 2.5 -> "NIVEL I"
        currentUser.level <= 4.0 -> "NIVEL II"
        currentUser.level <= 5.5 -> "NIVEL II"
        else -> "PRO"
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SlateCard),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "MI CATEGORÍA",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$categoryLabel $levelSubLabel",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = String.format("%.1f", currentUser.level),
                            color = VoltGreen,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Rating Gral.",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 10.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Win state info block
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Partidos", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                            Text("${currentUser.matchesPlayed}", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.Default.SportsTennis, contentDescription = null, tint = VoltGreen, modifier = Modifier.size(16.dp))
                    }
                    
                    // Ranking block
                    Row(
                        modifier = Modifier
                            .weight(1.1f)
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Puntos", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                            Text("${currentUser.rankingPoints}", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = VoltGreen, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LevelFilterChipsSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("Todos", "Principiante", "Intermedio", "Avanzado", "Profesional")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filters.forEach { filter ->
                val selected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) VoltGreen else Color.White,
                            RoundedCornerShape(12.dp)
                        )
                        .border(
                            1.dp,
                            if (selected) VoltGreen else Color(0xFFE2E8F0),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onFilterSelected(filter) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (selected) TextBlack else TextGray,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMatchFeedState(onCreateClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SportsTennis,
            contentDescription = null,
            tint = TextGray.copy(alpha = 0.4f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No hay partidos en esta categoría",
            color = TextBlack,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "La lista está vacía actualmente. Puedes crear un partido tú mismo o de lo contrario esperar a que otro miembro del club publique uno.",
            color = TextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onCreateClick,
            colors = ButtonDefaults.buttonColors(containerColor = SlateCard, contentColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(44.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Publicar Partido Libre", fontSize = 13.sp)
        }
    }
}

@Composable
fun MatchInvitationCard(
    invitation: MatchInvitation,
    currentUser: User,
    viewModel: PadelViewModel
) {
    val joinedPlayerIds = invitation.getPlayerIdsList()
    val isCreator = invitation.creatorId == currentUser.id
    val isJoined = joinedPlayerIds.contains(currentUser.id)
    val slotsFilled = joinedPlayerIds.size
    val slotsLeft = invitation.playersNeeded - slotsFilled

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isJoined) VoltGreen else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Club name & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFFEDD5), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = invitation.clubName,
                            color = TextBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Por: ${invitation.creatorName} (Nvl ${invitation.creatorLevel})",
                            color = TextGray,
                            fontSize = 11.sp
                        )
                    }
                }

                // Match Status Flag
                Box(
                    modifier = Modifier
                        .background(
                            if (slotsLeft <= 0) Color(0xFFEFF6FF) else Color(0xFFFFF7ED),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (slotsLeft <= 0) "Completo" else "Faltan $slotsLeft",
                        color = if (slotsLeft <= 0) PadelBlue else Color(0xFFEA580C),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Game details Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("FECHA Y HORA", color = TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("${invitation.date} • ${invitation.time}", color = TextBlack, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("NIVEL MÍNIMO", color = TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("${invitation.levelRequired}", color = PadelBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (invitation.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"${invitation.notes}\"",
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = TextGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Players list header
            Text(
                text = "Jugadores inscritos (${slotsFilled}/${invitation.playersNeeded}):",
                color = TextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Creador
            JoinedPlayerItem(
                name = "${invitation.creatorName} (Creador)",
                level = invitation.creatorLevel,
                phoneSecureInfo = "Creador",
                isMe = isCreator,
                showSecureLabel = true
            )

            // Other Players
            val joinedNames = invitation.getPlayerNamesList()
            val joinedIds = invitation.getPlayerIdsList()
            joinedIds.forEachIndexed { idx, playerId ->
                val name = if (idx < joinedNames.size) joinedNames[idx] else "Jugador"
                val isMe = playerId == currentUser.id

                Spacer(modifier = Modifier.height(4.dp))
                JoinedPlayerItem(
                    name = name,
                    level = null,
                    phoneSecureInfo = "Registrado",
                    isMe = isMe,
                    showSecureLabel = false
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Privacy notice inline block
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF3C7).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = SecurityAmber,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val inMatch = isCreator || isJoined
                Text(
                    text = if (inMatch) {
                        "🔓 Pista compartida: Teléfonos de contacto habilitados."
                    } else {
                        "🔒 Contacto protegido: Apúntate para comunicarte."
                    },
                    color = if (inMatch) PadelBlue else SecurityAmber,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isCreator) {
                    OutlinedButton(
                        onClick = { viewModel.deleteInvitation(invitation) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                        border = borderStroke(DangerRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancelar Partido", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    val full = invitation.isFull
                    Button(
                        onClick = { viewModel.toggleJoinMatch(invitation) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isJoined) DangerRed.copy(alpha = 0.1f) else SlateCard,
                            contentColor = if (isJoined) DangerRed else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (isJoined) 1.dp else 0.dp,
                                color = if (isJoined) DangerRed else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        val labelText = if (isJoined) "Salir del Partido" else if (full) "Pista Llena" else "Unirse al Partido"
                        val labelIcon = if (isJoined) Icons.Default.ExitToApp else Icons.Default.CheckCircle

                        Icon(labelIcon, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(labelText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

fun borderStroke(color: Color): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(1.dp, color)
}

@Composable
fun JoinedPlayerItem(
    name: String,
    level: Double?,
    phoneSecureInfo: String,
    isMe: Boolean,
    showSecureLabel: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5F9).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = if (isMe) PadelBlue else TextGray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isMe) "$name (Yo)" else name,
                color = TextBlack,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (level != null) {
                Box(
                    modifier = Modifier
                        .background(LightPadelBlue, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("Nvl $level", color = PadelBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (showSecureLabel) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Protección",
                    tint = SecurityAmber,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun CreateMatchInvitationDialog(
    viewModel: PadelViewModel,
    onDismiss: () -> Unit
) {
    var clubName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Hoy") }
    var time by remember { mutableStateOf("18:30") }
    var playersNeeded by remember { mutableStateOf(1) }
    var levelRequired by remember { mutableStateOf(3.0) }
    var notes by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Publicar Nuevo Partido 🎾",
                color = TextBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "Introduce los detalles para buscar compañeros.",
                    fontSize = 12.sp,
                    color = TextGray
                )

                OutlinedTextField(
                    value = clubName,
                    onValueChange = { clubName = it },
                    label = { Text("Club de Pádel") },
                    placeholder = { Text("Ej. Padel Central, Club Miramar") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextBlack,
                        focusedBorderColor = PadelBlue,
                        focusedLabelColor = PadelBlue
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Fecha") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextBlack,
                            focusedBorderColor = PadelBlue,
                            focusedLabelColor = PadelBlue
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Hora") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextBlack,
                            focusedBorderColor = PadelBlue,
                            focusedLabelColor = PadelBlue
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Text("Jugadores que te FALTAN:", color = TextBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1, 2, 3).forEach { num ->
                        val selected = playersNeeded == num
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (selected) PadelBlue else Color(0xFFF1F5F9),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { playersNeeded = num }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Faltan $num",
                                color = if (selected) Color.White else TextGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nivel requerido:", color = TextBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .background(VoltGreen, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = String.format("%.1f+", levelRequired),
                            color = TextBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                Slider(
                    value = levelRequired.toFloat(),
                    onValueChange = { levelRequired = kotlin.math.round(it * 10) / 10.0 },
                    valueRange = 1.0f..7.0f,
                    steps = 59,
                    colors = SliderDefaults.colors(
                        thumbColor = PadelBlue,
                        activeTrackColor = PadelBlue,
                        inactiveTrackColor = Color(0xFFE2E8F0)
                    )
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Comentarios") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextBlack,
                        focusedBorderColor = PadelBlue,
                        focusedLabelColor = PadelBlue
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                if (errorMsg != null) {
                    Text(text = errorMsg ?: "", color = DangerRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.createInvitation(
                        clubName = clubName,
                        date = date,
                        time = time,
                        playersNeeded = playersNeeded,
                        levelRequired = levelRequired,
                        notes = notes,
                        onSuccess = { onDismiss() },
                        onError = { errorMsg = it }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = PadelBlue, contentColor = Color.White),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Publicar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextGray)
            }
        }
    )
}
