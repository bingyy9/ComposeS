package com.example.composes.breakout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composes.R
import com.example.composes.breakout.repo.BroadcastType

@Composable
fun BroadcastView (
    broadcastSessions: List<BroadcastType>,
    broadcastGroups: List<BroadcastType>,
    onSend: (session: BroadcastType, group: BroadcastType, message: String) -> Unit,
    onCancel: () -> Unit
) {
    val (selectedSessionIndex, setSelectedSessionIndex) = rememberSaveable { mutableStateOf(0)}
    val (selectedGroupIndex, setGroupIndex) = rememberSaveable { mutableStateOf(0)}
    val (text, setText) = rememberSaveable { mutableStateOf("")}

    Column (Modifier.verticalScroll(rememberScrollState())){
        BroadcastTitleRow(onCancel = onCancel)
        BroadcastTo(
            broadcastSessions = broadcastSessions,
            broadcastGroups = broadcastGroups,
            text = text,
            onTextChange = setText,
            selectedSessionIndex = selectedSessionIndex,
            onSessionIndexChange = setSelectedSessionIndex,
            selectedGroupIndex = selectedGroupIndex,
            onGroupIndexChange = setGroupIndex
        )

        Spacer(modifier = Modifier.height(20.dp))

        BroadcastBottomButtonRow(
            { onSend(broadcastSessions[selectedSessionIndex], broadcastGroups[selectedGroupIndex], text) } ,
            onCancel
        )
    }
}

@Composable
fun BroadcastTitleRow(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ){
        Text(
            text = stringResource(id = R.string.broadcast_message),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = stringResource(id = R.string.cd_done),
            modifier = Modifier
                .clickable { onCancel() }
                .padding(7.dp),
        )
    }
}

@Composable
fun BroadcastTo(
    broadcastSessions: List<BroadcastType>,
    broadcastGroups: List<BroadcastType>,
    text: String,
    onTextChange: (String) -> Unit,
    selectedSessionIndex: Int,
    onSessionIndexChange: (Int) -> Unit,
    selectedGroupIndex: Int,
    onGroupIndexChange: (Int) -> Unit,
){

    Column (modifier = Modifier.padding(start = 32.dp, end = 32.dp)){
        Text(
            text = stringResource(id = R.string.broadcast_to),
        )

        Spacer(modifier = Modifier.height(13.dp))


        //TODO: tablet/phone >> yingchun
        Row(modifier = Modifier
            .fillMaxWidth()) {
            broadcastDropDownMenu(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                menuItems = broadcastSessions,
                selectedIndex = selectedSessionIndex,
                onIndexChange = onSessionIndexChange
            )
            broadcastDropDownMenu(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                menuItems = broadcastGroups,
                selectedIndex = selectedGroupIndex,
                onIndexChange = onGroupIndexChange
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BroadcastInputText(
            text,
            onTextChange,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BroadcastInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {} //带默认值
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column() {
        TextField(
            value = text,
            onValueChange = {
                if (it.length <= 4000) onTextChange(it)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.bo_color_1),
                focusedIndicatorColor =  Color.Transparent,
                unfocusedIndicatorColor =  Color.Transparent
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(190.dp),
            maxLines = 10,
            placeholder = { Text(stringResource(id = R.string.BO_BREAKOUT_SESSION_BROADCAST_MESSAGE_HINT)) },
            shape = RoundedCornerShape(8.dp),
            //配置软键盘
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onImeAction()
                keyboardController?.hide() //隐藏键盘
            })
        )

        //counter message
        Text(
            text = "${text.length} / 4000",
            textAlign = TextAlign.End,
            color = Blue,
            style = MaterialTheme.typography.caption, //use the caption text style
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun BroadcastBottomButtonRow(
    onSend: () -> Unit,
    onCancel: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 2.dp)
    ){
        Text(
            text = stringResource(id = R.string.broadcast_history),
            color = colorResource(id = R.color.bo_color_2),
            fontSize = 15.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp)
        )

        Button(
            onClick = { onSend() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White,
                disabledBackgroundColor = Color.Black,
                disabledContentColor = Color.Black
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.send),
                fontSize = 16.sp,
                modifier = Modifier.background(Color.Black),
            )
        }

        Button(
            onClick = { onCancel() },
            Modifier.padding(start = 6.dp, end = 20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                disabledBackgroundColor = Color.White,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                fontSize = 16.sp,
            )
        }
    }
}