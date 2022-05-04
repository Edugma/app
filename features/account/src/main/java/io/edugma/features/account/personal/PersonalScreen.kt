package io.edugma.features.account.personal

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import io.edugma.domain.account.model.Order
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.model.print
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.base.elements.TextWithIcon
import io.edugma.features.base.elements.placeholder
import org.koin.androidx.compose.getViewModel
import kotlin.math.min

@Composable
fun PersonalScreen(viewModel: PersonalViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    PersonalContent(state,
    backListener = {viewModel.exit()})
}

@Composable
fun PersonalContent(state: PersonalState, backListener: ClickListener) {
    Column {
        val scrollState = rememberLazyListState()
        val scrollOffset: Float = min(
            1f,
            1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
        )
        CollapsingToolbar(state.personal, state.isPlaceholders, scrollOffset, backListener)
        LazyColumn(Modifier.padding(8.dp), state = scrollState) {
           item {
               Personal(state.personal, state.isPlaceholders)
           }
            if (state.isPlaceholders) {
                items(10) {
                    SpacerHeight(height = 3.dp)
                    Order(null, state.isPlaceholders)
                    Divider()
                }
            } else {
                items(state.orders) {
                    SpacerHeight(height = 3.dp)
                    Order(it, state.isPlaceholders)
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun CollapsingToolbar(
    personal: Personal?,
    placeholders: Boolean,
    scrollOffset: Float,
    onBack: ClickListener
) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        val imageSize by animateDpAsState(targetValue = max(55.dp, 80.dp * scrollOffset))
        val (image, name, info, icon) = createRefs()
        IconButton(onClick = onBack, modifier = Modifier.constrainAs(icon) {
            start.linkTo(parent.start)
            linkTo(parent.top, info.bottom)
        }) {
            Icon(painter = painterResource(FluentIcons.ic_fluent_arrow_left_20_filled), contentDescription = null)
        }
        Text(
            text = personal?.name.orEmpty(),
            style = MaterialTheme3.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 5.dp)
                .constrainAs(name) {
                    linkTo(start = icon.end, end = image.start, endMargin = 8.dp)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
                .placeholder(placeholders)
        )
        Text(
            text = "${personal?.degreeLevel} ${personal?.course} курса группы ${personal?.group}",
            style = MaterialTheme3.typography.bodySmall,
            modifier = Modifier
                .constrainAs(info) {
                    linkTo(start = icon.end, end = image.start, endMargin = 8.dp)
                    top.linkTo(name.bottom)
                    width = Dimension.fillToConstraints
                }
                .placeholder(placeholders)
        )
        Image(
            painter = rememberImagePainter(
                data = personal?.avatar,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .constrainAs(image) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .placeholder(placeholders)
        )
    }
}

@Composable
fun Personal(personal: Personal?, placeholders: Boolean) {
    Column(Modifier.padding(8.dp)) {
        TextWithIcon(
            text = personal?.faculty, 
            icon = painterResource(id = FluentIcons.ic_fluent_building_24_regular), 
            modifier = Modifier.placeholder(placeholders))
        TextWithIcon(
            text = personal?.specialty,
            icon = painterResource(id = R.drawable.acc_ic_teacher_24),
            modifier = Modifier.placeholder(placeholders))
//        TextWithIcon(
//            text = "Общежитие ${personal?.dormitory} комната ${personal?.dormitoryRoom}",
//            icon = painterResource(id = FluentIcons.ic_fluent_home_24_regular),
//            modifier = Modifier.placeholder(placeholders))
        TextWithIcon(
            text = (personal?.finance ) + " основа обучения",
            icon = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
            modifier = Modifier.placeholder(placeholders))
        TextWithIcon(
            text = "${personal?.enterYear} года поступления",
            icon = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier.placeholder(placeholders))
    }
}

@Composable
fun Order(order: Order?, placeholders: Boolean) {
    ConstraintLayout(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 55.dp)
    ) {
        val (number, date, info) = createRefs()
        Text(
            text = "Приказ №${order?.number}",
            style = MaterialTheme3.typography.titleSmall,
            modifier = Modifier
                .constrainAs(number) {
                    linkTo(parent.start, date.start, endMargin = 8.dp)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
                .defaultMinSize(minWidth = 100.dp)
                .placeholder(placeholders)
        )
        Text(
            text = order?.date?.format().orEmpty(),
            style = MaterialTheme3.typography.labelMedium,
            modifier = Modifier
                .constrainAs(date) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .defaultMinSize(minWidth = 50.dp)
                .placeholder(placeholders)
        )
        if (placeholders || order?.additionalInfo?.isNotNull() == true) {
            Text(
                text = order?.additionalInfo.orEmpty(),
                style = MaterialTheme3.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(info) {
                        linkTo(number.bottom, parent.bottom, bias = 1f, topMargin = 5.dp)
                    }
                    .placeholder(placeholders)
            )
        }
    }
}
