package com.example.composes.breakout.anyonecanjoin

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import com.example.composed.bean.BoSessionInfo
import com.example.composed.bean.BreakoutUser
import com.example.composes.R
import com.example.composes.breakout.anyonecanjoin.repo.SampleData.ROLE_COHOST
import com.example.composes.breakout.anyonecanjoin.repo.SampleData.ROLE_GUEST
import com.example.composes.breakout.anyonecanjoin.repo.SampleData.ROLE_HOST
import com.example.composes.breakout.anyonecanjoin.repo.SampleData.ROLE_PANELIST
import com.example.composes.breakout.anyonecanjoin.repo.SampleData.ROLE_PRESENTER
import com.example.composes.breakout.anyonecanjoin.repo.SampleData.USER_TYPE_DEVICE

class BreakoutSessionJoinComponent {
    @Composable
    fun SessionToJoinPanel(breakoutSessionToJoinViewModel: BreakoutSessionToJoinViewModel) {
        Column(Modifier.fillMaxWidth()) {
            NormalTipPanel(R.string.choose_to_join_breakout_session_tip)
            Spacer(modifier = Modifier.height(10.dp))
            BreakoutSessionToJoinPanel(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp),
                breakoutSessionToJoinViewModel
            )
        }
    }

    @Composable
    fun BreakoutSessionToJoinPanel(
        modifier: Modifier,
        viewModel: BreakoutSessionToJoinViewModel
    ) {
        val collapsedIds = viewModel.collapsedIDsIdsList.collectAsState()
        LazyColumn(modifier = modifier) {
            viewModel.breakoutSessions?.onEachIndexed { index, boSessionInfo ->
                val isItemColapsed = collapsedIds.value.contains(boSessionInfo.sessionUUID)
                // header
                item {
                    Header(boSessionInfo, viewModel.onHeaderItemClick, viewModel.joinBreakOutSession, isItemColapsed)
                }
                // item
                if (!isItemColapsed) {
                    items(boSessionInfo.assignedUserList) { item ->
                        if (item.userType == USER_TYPE_DEVICE) {
                            detail(breakoutUser = item, Modifier.padding(start = 10.dp))
                            item.associateUsers?.onEachIndexed { _, breakoutUser ->
                                detail(breakoutUser = breakoutUser, Modifier.padding(start = 30.dp))
                            }
                        } else {
                            detail(breakoutUser = item, Modifier.padding(start = 10.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Header(
        breakoutSessionInfo: BoSessionInfo,
        onClick: (String) -> Unit,
        onJoinBreakoutMeeting:(String)->Unit,
        isItemColapsed: Boolean
    ) {
        val rotateDegree = remember { Animatable(0f) }
        LaunchedEffect(isItemColapsed) {
            rotateDegree.animateTo(if (isItemColapsed) 180f else 0f)
        }
        Row(
            modifier = Modifier
//            .background(colorResource(id = R.color.bo_indicator_bg))
            , verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(
                        1f
                    )
                    .clickable { onClick(breakoutSessionInfo.sessionUUID) }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_vectoraccow_down),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.rotate(rotateDegree.value)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(
                        id = R.string.wait_join_breakout_session_name,
                        breakoutSessionInfo.sessionName,
                        breakoutSessionInfo.currentJoinedCount,
                        breakoutSessionInfo.totalAssignedCount
                    )
                )
            }
            Text(
                modifier = Modifier.clickable {
                    onJoinBreakoutMeeting(breakoutSessionInfo.sessionUUID)
                }, text = stringResource(id = R.string.join),
                style = TextStyle(textDecoration = TextDecoration.Underline), color = Color.Blue
            )
        }
    }

    @Composable
    fun detail(breakoutUser: BreakoutUser, modifier: Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = modifier
                .height(70.dp)
                .clickable(enabled = false, onClick = { })
        ) {
            Image(
                painter = painterResource(id = R.drawable.test),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(40.dp)
                    .fillMaxSize(0.5F)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colors.secondary, CircleShape),
            )
            Spacer(modifier = Modifier.width(2.dp))
            Column() {
                Text(
                    text = breakoutUser.displayName,
//                    color = MaterialTheme.colors.primary.copy(alpha = alpha),
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text =
                    when (breakoutUser.role) {
                        ROLE_GUEST -> "Guest"
                        ROLE_PANELIST -> "Panelist"
                        ROLE_COHOST -> "Cohost"
                        ROLE_HOST -> "Host"
                        ROLE_PRESENTER -> "Presenter"
                        else -> "Guest"
                    },
                    modifier = Modifier.padding(all = 2.dp),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }

    @Composable
    @Preview
    fun Preview() {
        var savedStateHandle = SavedStateHandle()
        var breakoutSessionToJoinViewModel: BreakoutSessionToJoinViewModel =
            BreakoutSessionToJoinViewModel(savedStateHandle)
        SessionToJoinPanel(breakoutSessionToJoinViewModel)
    }

    /**
     * a panel with normal icon
     *
     */
    @Composable
    public fun NormalTipPanel(resourceId: Int) {
        TipPanel(resourceId = resourceId, imageIcon = R.drawable.ic_bo_warning)
    }

    /**
     * a panel with warning icon
     *
     */
    @Composable
    public fun WarningTipPanel(resourceId: Int) {
        TipPanel(resourceId = resourceId, imageIcon = R.drawable.ic_bo_warning)
    }

    @Composable
    private fun TipPanel(resourceId: Int, imageIcon: Int) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(), shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        colorResource(R.color.bo_indicator_bg)
                    )
                    .padding(10.dp, 5.dp, 10.dp, 5.dp)
            ) {
                Image(
                    painter = painterResource(id = imageIcon),
                    contentDescription = stringResource(id = R.string.app_name),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(id = resourceId),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }

}