package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.User
import com.example.ui.PadelViewModel
import com.example.ui.theme.*

@Composable
fun RankingScreen(
    viewModel: PadelViewModel,
    currentUser: User
) {
    val users by viewModel.filteredUsers.collectAsStateWithLifecycle()
    val searchQuery by viewModel.rankingSearchQuery.collectAsStateWithLifecycle()
    val levelFilter by viewModel.rankingLevelFilter.collectAsStateWithLifecycle()
    var targetSecurityUser by remember { mutableStateOf<User?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // Header
            RankingHeader()

            // Weekly Highlight Card inspired by Vibrant Palette Design
            WeeklyRankingHighlightCard(users.take(2))

            // Search and Filters Section
            RankingSearchAndFilterCard(
                query = searchQuery,
                onQueryChange = { viewModel.updateRankingSearch(it) },
                selectedFilter = levelFilter,
                onFilterSelected = { viewModel.updateRankingLevelFilter(it) }
            )

            // Players Leaderboard List
            if (users.isEmpty()) {
                EmptyRankingState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(users) { index, player ->
                        val globalRank = index + 1
                        PlayerLeaderboardCard(
                            user = player,
                            rankPosition = globalRank,
                            isMe = player.id == currentUser.id,
                            securePhone = viewModel.getSecurePhoneNumber(player),
                            onPhoneClick = {
                                val currentPhone = viewModel.getSecurePhoneNumber(player)
                                if (currentPhone.contains("•")) {
                                    targetSecurityUser = player
                                }
                            }
                        )
                    }
                }
            }
        }

        if (targetSecurityUser != null) {
            targetSecurityUser?.let { player ->
                SecurityPhoneExplanationDialog(
                    player = player,
                    onDismiss = { targetSecurityUser = null }
                )
            }
        }
    }
}

@Composable
fun RankingHeader() {
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
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(VoltGreen, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🏆", fontSize = 20.sp)
            }
            Column {
                Text(
                    text = "Ranking Club",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = "Clasificación oficial de socios del club",
                    fontSize = 11.sp,
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun WeeklyRankingHighlightCard(topPlayers: List<User>) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = VoltGreen),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RANKING SEMANAL 🔥",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = TextBlack
                )
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "TOP SOCIOS",
                        color = TextBlack,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))

            if (topPlayers.isNotEmpty()) {
                topPlayers.forEachIndexed { idx, player ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${idx + 1}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextBlack,
                                modifier = Modifier.width(20.dp)
                            )
                            Text(
                                text = player.name,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = TextBlack
                            )
                        }
                        Text(
                            text = "${player.rankingPoints} pts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextBlack
                        )
                    }
                    if (idx < topPlayers.size - 1) {
                        Divider(color = Color.Black.copy(alpha = 0.1f), thickness = 0.5.dp)
                    }
                }
            } else {
                Text(
                    text = "No hay datos de clasificación disponibles",
                    fontSize = 12.sp,
                    color = TextBlack.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun RankingSearchAndFilterCard(
    query: String,
    onQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("Todos", "Principiante", "Intermedio", "Avanzado")

    Card(
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Buscar jugador por nombre...", color = TextGray, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Limpiar",
                            tint = TextGray,
                            modifier = Modifier.clickable { onQueryChange("") }
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PadelBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedTextColor = TextBlack,
                    unfocusedTextColor = TextBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("ranking_search_bar"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Filtrar por nivel:",
                color = TextBlack,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                filters.forEach { filter ->
                    val selected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .background(
                                if (selected) PadelBlue else Color(0xFFF1F5F9),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onFilterSelected(filter) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (selected) Color.White else TextGray,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerLeaderboardCard(
    user: User,
    rankPosition: Int,
    isMe: Boolean,
    securePhone: String,
    onPhoneClick: () -> Unit
) {
    val topRankColor = when (rankPosition) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> null
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isMe) VoltGreen.copy(alpha = 0.08f) else SlateSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isMe) 1.5.dp else 1.dp,
                color = if (isMe) VoltGreen else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            // Rank Number or Medal
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = topRankColor ?: Color(0xFFF1F5F9),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (topRankColor != null) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Premio",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = rankPosition.toString(),
                        color = TextGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Player Details block
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isMe) "${user.name} (Yo)" else user.name,
                        color = TextBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                when {
                                    user.level <= 2.5 -> Color(0xFFFEF3C7)
                                    user.level <= 4.0 -> LightPadelBlue
                                    else -> Color(0xFFFEE2E2)
                                },
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = String.format("%.1f", user.level),
                            color = when {
                                user.level <= 2.5 -> Color(0xFFD97706)
                                user.level <= 4.0 -> PadelBlue
                                else -> DangerRed
                            },
                            fontWeight = FontWeight.Black,
                            fontSize = 9.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SwapCalls, contentDescription = null, tint = TextGray, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(user.position, color = TextGray, fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = TextGray, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("V: ${user.matchesWon} - D: ${user.matchesLost}", color = TextGray, fontSize = 11.sp)
                    }
                }

                // Masked phone row
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable { onPhoneClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isMasked = securePhone.contains("•")
                    Icon(
                        imageVector = if (isMasked) Icons.Default.Lock else Icons.Default.Phone,
                        contentDescription = "Contacto",
                        tint = if (isMasked) SecurityAmber else SafeGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isMasked) "$securePhone 🔒" else securePhone,
                        color = if (isMasked) SecurityAmber else SafeGreen,
                        fontSize = 11.sp,
                        fontWeight = if (isMasked) FontWeight.Normal else FontWeight.Bold
                    )
                }
            }

            // Total Points
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${user.rankingPoints}",
                    color = PadelBlue,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "puntos",
                    color = TextGray,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyRankingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Group, contentDescription = null, tint = TextGray, modifier = Modifier.size(54.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Sin resultados de socios",
            color = TextBlack,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(
            text = "Busca otro término o desmarca los filtros de nivel.",
            color = TextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SecurityPhoneExplanationDialog(
    player: User,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = SecurityAmber, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Privacidad Protegida 🔒", color = TextBlack, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "El número telefónico de ${player.name} está bajo protección pasiva de spam.",
                    color = TextBlack,
                    fontSize = 13.sp
                )
                Text(
                    text = "Para cumplir con la RGPD, no se muestran datos de contacto directos en rankings públicos.",
                    color = TextGray,
                    fontSize = 12.sp
                )
                Box(
                    modifier = Modifier
                        .background(LightPadelBlue, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "💡 Cómo desbloquear: Coincide con ${player.name} en una misma pista en el 'Espacio Libre' para habilitar tus canales de contacto directos de partido de forma segura.",
                        color = PadelBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PadelBlue, contentColor = Color.White),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Entendido", fontWeight = FontWeight.Bold)
            }
        }
    )
}
