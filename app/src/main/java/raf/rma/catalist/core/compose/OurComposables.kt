package raf.rma.catalist.core.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import raf.rma.catalist.core.theme.CatalistTheme
import raf.rma.catalist.core.theme.Orange

@Composable
fun AppIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    query: String,
    activated: Boolean
) {
    var text by remember { mutableStateOf(query) }
    var active by remember { mutableStateOf(activated) }
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        value = text,
        onValueChange = { newText ->
            text = newText
            active = true
            onQueryChange(newText)
        },
        placeholder = { Text(text = "Search") },
        textStyle = TextStyle(color = Color.Black),
        leadingIcon = { AppIconButton(imageVector = Icons.Default.Search, onClick = { }) },
        trailingIcon = {
            if(active){
                AppIconButton(
                    imageVector = Icons.Default.Clear,
                    onClick = {
                        if(text.isNotEmpty()) {
                            text = ""
                            onQueryChange(text)
                        }
                        else{
                            active = false
                            onCloseClicked()
                            focusManager.clearFocus()
                        }
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            unfocusedIndicatorColor = Orange,
            focusedContainerColor = Color.White,
            focusedIndicatorColor = Orange,
        ),
    )
}

@Composable
fun DetailsWidget(
    number: Int,
    trait: String
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .border(4.dp, Orange, CircleShape)
                .padding(1.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Center),
//                style = MaterialTheme.typography.bodyLarge,
                text = number.toString(),
                style = TextStyle(
                    fontSize = 30.sp,
//                    color = Orange
                )
            )
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 3.dp),
//            .align(Alignment.Center),
//                style = MaterialTheme.typography.bodyLarge,
            text = trait,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}