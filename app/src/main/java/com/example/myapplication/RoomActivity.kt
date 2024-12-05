package com.example.myapplication

import RoomDto
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.service.RoomService
import com.example.myapplication.ui.theme.MyApplicationTheme

class RoomActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Retrieve the room parameter from the Intent
        val param = intent.getStringExtra(AUTOMAcorp.ROOM_PARAM)
        val room = RoomService.findByNameOrId(param)
        Log.d("RoomActivity", "Room name passed to Intent: $room")
        Log.d("RoomActivity", "Room param: $param")
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (room != null) {
                        RoomDetail(room, Modifier.padding(innerPadding))
                    } else {
                        NoRoom(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

// Step 1: NoRoom Composable
@Composable
fun NoRoom(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.act_room_none),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

// Step 2-4: RoomDetail Composable
@Composable
fun RoomDetail(room: RoomDto, modifier: Modifier = Modifier) {
    var roomState by remember { mutableStateOf(room) }

    Column(modifier = modifier.padding(16.dp)) {
        // Room Name
        OutlinedTextField(
            value = roomState.name,
            onValueChange = { roomState = roomState.copy(name = it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.act_room_name)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current Temperature (Read-Only)
        Text(
            text = stringResource(R.string.act_room_current_temp, roomState.currentTemperature!!),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Target Temperature (Slider)
        Slider(
            value = roomState.targetTemperature?.toFloat() ?: 18.0f,
            onValueChange = { roomState = roomState.copy(targetTemperature = it.toDouble()) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            valueRange = 10f..28f
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display Target Temperature Value
        Text(
            text = stringResource(
                R.string.act_room_target_temp,
                roomState.currentTemperature ?: 0.0
            ),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Step 5: String Resources
/*
In your res/values/strings.xml file, add the following strings:

<string name="act_room_none">No room found</string>
<string name="act_room_name">Room Name</string>
<string name="act_room_current_temp">Current Temperature: %1$s°C</string>
<string name="act_room_target_temp">Target Temperature: %1$s°C</string>
*/

// Step 6: RoomDto Class


// RoomService Mock Implementation
object RoomService {
    fun findByNameOrId(param: String?): RoomDto? {
        return when (param) {
            "Room123" -> RoomDto(name = "Room123", currentTemperature = 22.5, targetTemperature = 24.0)
            "Room" -> RoomDto(name = "Room", currentTemperature = 21.0, targetTemperature = 23.0)
            else -> null
        }
    }
}


@Composable
fun NoRoomPreview() {
    MyApplicationTheme {
        NoRoom()
    }
}

@Preview(showBackground = true)
@Composable
fun RoomDetailPreview() {
    MyApplicationTheme {
        RoomDetail(
            RoomDto(name = "Sample Room", currentTemperature = 22.5, targetTemperature = 24.0)
        )
    }
}
