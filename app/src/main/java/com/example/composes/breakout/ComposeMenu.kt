package com.example.composes.breakout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.composes.R
import com.example.composes.breakout.repo.BroadcastType

@Composable
fun ComposeMenu(
    menuItems: List<BroadcastType>,
    menuExpandedState: Boolean,
    selectedIndex: Int,
    updateMenuExpandStatus: () -> Unit,
    onDismissMenuView: () -> Unit,
    onMenuItemClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .border(0.3.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable(
                onClick = {
                    updateMenuExpandStatus()
                },
            ),

        ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val (lable, iconView) = createRefs()

            menuItems[selectedIndex].name?.let {
                Text(
                    text = it,
                    //                color = seletedIndex.dropdownDisableColor(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(lable) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(iconView.start)
                            width = Dimension.fillToConstraints
                        }
                )
            }

            val displayIcon: Painter = painterResource(
                id = R.drawable.ic_drop_down
            )

            Icon(
                painter = displayIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 20.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                tint = MaterialTheme.colors.onSurface
            )

            DropdownMenu(
                expanded = menuExpandedState,
                onDismissRequest = { onDismissMenuView() },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface)
            ) {
                menuItems.forEachIndexed { index, broadcastType ->
                    DropdownMenuItem(
                        onClick = {
                            onMenuItemClick(index)
                        }) {
                        broadcastType.name?.let { Text(text = it) }
                    }
                }
            }
        }
    }
}

@Composable
fun broadcastDropDownMenu(
    modifier: Modifier = Modifier,
    menuItems: List<BroadcastType>,
    selectedIndex: Int,
    onIndexChange: (Int) -> Unit,
){
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        ComposeMenu(
            menuItems = menuItems,
            menuExpandedState = menuExpanded,
            selectedIndex = selectedIndex,
            updateMenuExpandStatus = {
                menuExpanded = true
            },
            onDismissMenuView = {
                menuExpanded = false
            },
            onMenuItemClick = { index->
                onIndexChange(index)
                menuExpanded = false
            }
        )
    }
}