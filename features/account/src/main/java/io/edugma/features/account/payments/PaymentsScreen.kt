package io.edugma.features.account.payments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.edugma.domain.account.model.*
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.format
import io.edugma.features.base.elements.Chip
import io.edugma.features.base.elements.ErrorView
import io.edugma.features.base.elements.SelectableChip
import io.edugma.features.base.elements.SpacerWidth
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun PaymentsScreen(viewModel: PaymentsViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    PaymentsContent(state,
        retryListener = viewModel::load,
        backListener = viewModel::exit
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PaymentsContent(
    state: PaymentsState,
    retryListener: ClickListener,
    backListener: ClickListener
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            IconButton(onClick = backListener) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
            SpacerWidth(width = 15.dp)
            Text(
                text = "Оплата",
                style = MaterialTheme3.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()
        val selectedPage = remember{mutableStateOf<Int>(0)}
        TypesRow(
            state.types,
            state.types.getOrNull(selectedPage.value)
        ) {
            coroutineScope.launch {
                pagerState.scrollToPage(state.types.indexOf(it))
            }
        }
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect{
                selectedPage.value = it
            }
        }
        HorizontalPager(count = state.data.size, state = pagerState, key = {state.data.keys.toList()[it]}) { page ->
            Text(
                text = "Page: ${state.data.values.toList()[page]}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PaymentInfo(payment: Payment) {
    Card(shape = MaterialTheme.shapes.medium, elevation = 2.dp, modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)) {
        ConstraintLayout(modifier = Modifier
            .padding(5.dp)
            .heightIn(20.dp)) {
            val (date, sum) = createRefs()
            Text(payment.date.format(), modifier = Modifier.constrainAs(date) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(sum.start)
                width = Dimension.fillToConstraints
            })
            Text(payment.value, modifier = Modifier.constrainAs(sum) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            })
        }
    }
}

@Composable
fun Payment(payment: Payments) {
    Card(shape = MaterialTheme.shapes.medium, elevation = 2.dp, modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)) {
        Column(modifier = Modifier
            .padding(5.dp)
            .heightIn(40.dp)) {
            Text("Номер договора: ${payment.id}")
            Text("Дата договора: ${payment.startDate.format()}")
            Text("Сумма договора: ${payment.sum}")
            Text("Задолженность: ${payment.balanceCurrent}")
        }
    }
}

@Composable
fun TypesRow(
    types: List<PaymentType>,
    selectedType: PaymentType?,
    clickListener: Typed1Listener<PaymentType>
) {
    LazyRow {
        items(
            count = types.size,
            key = {types[it]}
        ) {
            SelectableChip(
                selectedState = types[it] == selectedType,
                onClick = { clickListener.invoke(types[it]) }) {
                Text(
                    text = types[it].toLabel(),
                    style = MaterialTheme3.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
