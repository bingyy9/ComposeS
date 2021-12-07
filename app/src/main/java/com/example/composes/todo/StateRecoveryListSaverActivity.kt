package com.example.composes.todo

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composes.ui.theme.ComposeSTheme
import kotlinx.parcelize.Parcelize

class StateRecoveryListSaverActivity: ComponentActivity() {

    data class City(val name: String, var contry: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSTheme {
                CityScreen()
            }
        }
    }

    @Composable
    fun CityScreen(){
        val citySaver = listSaver<City, Any> (
            save = { listOf(it.name, it.contry) },
            restore = { City(it[0] as String, it[1] as String) }
        )
        var (city, setCity) = rememberSaveable(stateSaver = citySaver) {
            mutableStateOf(City("Madrid", "Spain"))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ){
            TextButton(
                colors = ButtonDefaults.buttonColors(),
                onClick = { setCity(City("BeJin", "China")) }
            ) {
                Text(text = "Click to change")
            }

            Text(text = "${city.contry} - ${city.name}")
        }
    }
}