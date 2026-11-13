package com.darien.vpweek7.soal1.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darien.vpweek7.soal1.model.Card
import com.darien.vpweek7.soal1.ui.widget.PriceBox
import com.darien.vpweek7.soal1.ui.widget.SpecRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailSheet(
    card: Card,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF141923),
        shape = RectangleShape,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF4A1E22), Color(0xFF141923))),
                )
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0x66F2F4F7)),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1A1D24)),
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color(0xFFF2F4F7))
                }
            }

            RemoteImage(
                url = card.imageLargeUrl,
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(0.72f)
                    .clip(RoundedCornerShape(14.dp)),
            )

            Text(
                card.name,
                color = Color(0xFFF2F4F7),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                card.setName,
                color = Color(0xFF8B93A3),
                fontSize = 13.sp,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PriceBox(
                    label = "Market Price",
                    price = card.marketPrice,
                    valueColor = Color(0xFF3DD68C),
                    borderColor = Color(0x803DD68C),
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    modifier = Modifier.weight(1f),
                )
                PriceBox(
                    label = "Lowest Price",
                    price = card.lowestPrice,
                    valueColor = Color(0xFFF5C451),
                    borderColor = Color(0x80F5C451),
                    icon = Icons.Filled.LocalOffer,
                    modifier = Modifier.weight(1f),
                )
            }

            val sections = listOf(
                "Market Activity" to listOf(
                    "Active Listings" to card.listings?.toString(),
                    "Verified Sellers" to card.sellers?.toString(),
                    "Primary Printing" to card.primaryPrinting,
                ),
                "Card Specifications" to listOf(
                    "Card Number" to card.cardNumber,
                    "Rarity" to card.rarity,
                    "Type" to card.pokemonType,
                    "Illustrator" to card.artist,
                ),
            )

            sections.forEach { (title, rows) ->
                Text(
                    title,
                    color = Color(0xFFF2F4F7),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1A1D24))
                        .border(1.dp, Color(0xFF2A2F3A), RoundedCornerShape(14.dp)),
                ) {
                    rows.forEachIndexed { index, (label, value) ->
                        SpecRow(label = label, value = value)
                        if (index < rows.lastIndex) {
                            HorizontalDivider(color = Color(0xFF2A2F3A))
                        }
                    }
                }
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5483B)),
            ) {
                Text("Done", color = Color(0xFFF2F4F7), fontWeight = FontWeight.Bold)
            }
        }
    }
}