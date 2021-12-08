package com.example.composes.breakout.assign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composes.R
import com.example.composes.breakout.BlackBgButton
import com.example.composes.breakout.WhiteBgButton
import com.example.composes.breakout.repo.AssignType
import java.lang.NumberFormatException

@Composable
fun BreakoutSessionAssignConfigComponent(
    assignType: AssignType,
    onAssignTypeSelected: (AssignType) -> Unit,
    waitingAssignUserCount: Int,
    onCreateBreakoutSession: (Int, AssignType, Boolean) -> Unit,
    showDetailAssign: () -> Unit,
    onCancel: () -> Unit,
) {
    val (sessionNum, setSessionNum) = rememberSaveable { mutableStateOf("1")}
    Column (
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 14.dp, end = 14.dp, top = 17.dp, bottom = 17.dp)

    ){
        AssignTitleRow(
            showDetailAssign,
            onCancel,
        )

        AssignBody(
            assignType,
            onAssignTypeSelected,
            waitingAssignUserCount,
            sessionNum,
            setSessionNum
        )

        AssignBottomButton(
            onCreateBreakoutSession = { },
            onCancel = onCancel
        )

    }
}

@Composable
fun AssignTitleRow(
    showDetailAssign: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = stringResource(id = R.string.back),
            modifier = Modifier
                .clickable { onCancel() }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_breakout_sesssion),
            contentDescription = stringResource(id = R.string.breakout_sessions),
            modifier = Modifier
                .padding(7.dp),
        )

        Text(
            text = stringResource(id = R.string.breakout_sessions),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_detail_assign),
            contentDescription = stringResource(id = R.string.cd_done),
            modifier = Modifier
                .clickable { showDetailAssign() }
                .padding(3.dp),
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = stringResource(id = R.string.cd_done),
            modifier = Modifier
                .clickable { onCancel() }
                .padding(3.dp),
        )
    }
}



@Composable
fun AssignBody(
    assignType: AssignType,
    onAssignTypeSelected: (AssignType) -> Unit,
    waitingAssignUserCount: Int,
    sessionNum: String,
    onSessionNumChange: (String) -> Unit,
){
    Column {
        InputSessionNum(
            sessionNum = sessionNum,
            onSessionNumChange = onSessionNumChange
        )

        AssignHint(
            assignType,
            waitingAssignUserCount,
            sessionNum)

        Spacer(modifier = Modifier.height(20.dp))

        AssignOption(
            AssignType.ASSIGN_PARTICIPANTS_AUTOMATICALLY,
            onAssignTypeSelected,
            stringResource(id = R.string.assign_participants_manually),
            assignType == AssignType.ASSIGN_PARTICIPANTS_AUTOMATICALLY,
        )

        AssignOption(
            AssignType.ASSIGN_PARTICIPANTS_MANUALLY,
            onAssignTypeSelected,
            stringResource(id = R.string.assign_participants_manually),
            assignType == AssignType.ASSIGN_PARTICIPANTS_MANUALLY,
        )

        AssignOption(
            AssignType.LET_PARTICIPANTS_CHOOSE_ANY_SESSION,
            onAssignTypeSelected,
            stringResource(id = R.string.let_participants_choose_any_session),
            assignType == AssignType.LET_PARTICIPANTS_CHOOSE_ANY_SESSION,
        )



    }
}

fun getSessionNum(sessionNum: String,) : Int{
    try {
        return sessionNum.toInt()
    } catch (e: NumberFormatException){

    }
    return 0
}

@Composable
fun getAssignTip(sessionNum: Int, waitingAssignUserCount: Int, assignType: AssignType): String {
    if(sessionNum == 0){
        return ""
    }
    when(assignType){
        AssignType.ASSIGN_PARTICIPANTS_AUTOMATICALLY -> {
            var average = waitingAssignUserCount/sessionNum
            when (average){
                0 -> {
                    return if(sessionNum > 0 && waitingAssignUserCount > 0){
                        stringResource(id = R.string.participant_per_session_dash, 0, 1)
                    } else {
                        stringResource(id = R.string.participant_per_session, 0)
                    }
                }
                1 -> {
                    return when {
                        waitingAssignUserCount > sessionNum -> {
                            stringResource(id = R.string.participants_per_session_dash, 1, 2)
                        }
                        waitingAssignUserCount == sessionNum -> {
                            stringResource(id = R.string.participant_per_session, 1)
                        }
                        else -> {
                            stringResource(id = R.string.participant_per_session_dash, 0, 1)
                        }
                    }
                }
                else -> {
                    return when {
                        waitingAssignUserCount > sessionNum * average -> {
                            stringResource(id = R.string.participants_per_session_dash, average, average + 1)
                        }
                        waitingAssignUserCount == sessionNum * average -> {
                            stringResource(id = R.string.participants_per_session, average)
                        }
                        else -> {
                            stringResource(id = R.string.participants_per_session_dash, average - 1, average)
                        }
                    }
                }
            }

        }
        AssignType.ASSIGN_PARTICIPANTS_MANUALLY -> {
            return stringResource(id = R.string.assign_participants_manually_later)
        }
        AssignType.LET_PARTICIPANTS_CHOOSE_ANY_SESSION -> {
            return stringResource(id = R.string.no_assigned_participants)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputSessionNum(
    sessionNum: String,
    onSessionNumChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
){
    Row(verticalAlignment = Alignment.CenterVertically,){
        val keyboardController = LocalSoftwareKeyboardController.current

        Text(
            text = stringResource(id = R.string.bo_number_of_breakout_sessions),
            fontSize = 16.sp,
        )

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = sessionNum,
            onValueChange = {
                if (getSessionNum(it) in 0..99) onSessionNumChange(it)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.bo_color_1),
                focusedIndicatorColor =  Color.Transparent,
                unfocusedIndicatorColor =  Color.Transparent
            ),
            modifier = Modifier
                .width(100.dp),
            maxLines = 1,
            shape = RoundedCornerShape(8.dp),
            //配置软键盘
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onImeAction()
                keyboardController?.hide() //隐藏键盘
            })
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AssignHint(
    assignType: AssignType,
    waitingAssignUserCount: Int,
    sessionNum: String,
){
    AnimatedVisibility(visible = getSessionNum(sessionNum) > 0) {
        var assignTip = getAssignTip(getSessionNum(sessionNum), waitingAssignUserCount, assignType)
        Text(
            text = assignTip,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun AssignOption(
    assignType: AssignType,
    onAssignTypeSelected: (AssignType) -> Unit,
    text: String,
    selected: Boolean,
){
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            modifier = Modifier.padding(8.dp),
            onClick = { onAssignTypeSelected(assignType) }
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

@Composable
fun AssignCoHost(){

}

@Composable
fun AssignBottomButton(
    onCreateBreakoutSession: () -> Unit,
    onCancel: () -> Unit,
){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){

        Spacer(modifier = Modifier.width(6.dp))

        BlackBgButton(
            text = stringResource(id = R.string.create_breakout_session),
            onClick = onCreateBreakoutSession,
        )
    }
}