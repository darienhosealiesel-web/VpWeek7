package com.darien.vpweek7.soal1.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import java.util.Locale

fun formatPrice(price: Double?): String =
    price?.let { String.format(Locale.US, "\$%.2f", it) } ?: "—"

@Composable
fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    if (url == null) {
        Box(
            modifier = modifier.background(Color(0xFF2A2F3A)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.Image,
                contentDescription = null,
                tint = Color(0xFF8B93A3),
            )
        }
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = modifier.background(Color(0xFF2A2F3A)),
        )
    }
}

@Composable
fun LoadingBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Color(0xFFE5483B))
    }
}

@Composable
fun ErrorBox(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF1A1D24)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.WifiOff,
                contentDescription = null,
                tint = Color(0xFFE5483B),
                modifier = Modifier.size(28.dp),
            )
        }
        Text(
            "Connection Failed",
            color = Color(0xFFE5483B),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            message,
            color = Color(0xFF8B93A3),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5483B)),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = null,
                    tint = Color(0xFFF2F4F7),
                    modifier = Modifier.padding(end = 6.dp),
                )
                Text("Try Again", color = Color(0xFFF2F4F7))
            }
        }
    }
}

@Composable
fun PriceBox(
    label: String,
    price: Double?,
    valueColor: Color,
    borderColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1F29))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(valueColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = valueColor,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(label, color = Color(0xFF8B93A3), fontSize = 12.sp)
        }
        Text(
            formatPrice(price),
            color = valueColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun SpecRow(label: String, value: String?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, color = Color(0xFF8B93A3), fontSize = 14.sp)
        Text(
            value ?: "—",
            color = Color(0xFFF2F4F7),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(color = Color(0xFFF2F4F7), fontSize = 15.sp),
        placeholder = { Text("Search Pokemon cards", color = Color(0xFF8B93A3)) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color(0xFFE5483B))
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Clear", tint = Color(0xFF8B93A3))
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFFF2F4F7),
            unfocusedTextColor = Color(0xFFF2F4F7),
            focusedContainerColor = Color(0xFF1A1D24),
            unfocusedContainerColor = Color(0xFF1A1D24),
            focusedBorderColor = Color(0xFF2A2F3A),
            unfocusedBorderColor = Color(0xFF2A2F3A),
            cursorColor = Color(0xFFE5483B),
        ),
    )
}

@Composable
fun FilterChipRow(
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(options) { option ->
            AssistChip(
                onClick = { onSelect(option) },
                label = {
                    Text(option, color = Color(0xFFF2F4F7), fontSize = 13.sp)
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFF1A1D24),
                    labelColor = Color(0xFFF2F4F7),
                ),
                border = AssistChipDefaults.assistChipBorder(
                    enabled = true,
                    borderColor = Color(0xFF2A2F3A),
                ),
            )
        }
    }
}