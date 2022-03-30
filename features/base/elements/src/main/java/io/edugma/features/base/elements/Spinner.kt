package io.edugma.features.base.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> Spinner(name: String = "",stateList: Array<T>, initialState: T, nameMapper: (T) -> String, onChanged: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row(Modifier.fillMaxWidth(0.8f), verticalAlignment = Alignment.CenterVertically) {
        Text(text = name, modifier = Modifier.padding(end = 10.dp))
        Box(Modifier, contentAlignment = Alignment.Center) {
            Row(
                Modifier
                    .padding(16.dp)
                    .clickable {
                        expanded = !expanded
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nameMapper.invoke(initialState),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }) {
                    stateList.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onChanged.invoke(item)
                            },
                            text = {
                                Text(text = nameMapper.invoke(item))
                            }
                        )
                    }
                }
            }
        }
    }
}