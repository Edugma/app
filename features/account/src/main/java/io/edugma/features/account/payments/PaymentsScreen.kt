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
import io.edugma.features.base.elements.*
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
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect{
                selectedPage.value = it
            }
        }
        if (state.placeholders) {
            TypesRowPlaceholders()
        } else {
            TypesRow(
                state.types,
                state.types.getOrNull(selectedPage.value)
            ) {
                coroutineScope.launch {
                    pagerState.scrollToPage(state.types.indexOf(it))
                }
            }
        }
        HorizontalPager(
            count = state.data.size,
            state = pagerState,
            key = {state.getTypeByIndex(it) ?: PaymentType.Dormitory}
        ) { page ->
            Column(modifier = Modifier.fillMaxSize()) {
                state.getPaymentsByIndex(page)?.let { payment ->
                    LazyColumn() {
                        item(key = "header"){
                            Payments(payment)
                        }
                        items(
                            count = payment.payments.size,
                            key = { payment.payments[it].date },
                        ) {
                            Payment(payment = payment.payments[it])
                        }
                    }
                }
            } ?: let {
//                if (state.placeholders)
            }
        }
    }
}

@Composable
fun Payments(payments: Payments) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .heightIn(min = 40.dp)
    ) {
        Text("Номер договора: ${payments.id}")
        Text("Дата договора: ${payments.startDate.format()}")
        Text("Сумма договора: ${payments.sum}")
        Text("Задолженность: ${payments.balanceCurrent}")
    }
}

@Composable
fun Payment(payment: Payment) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (date, value) = createRefs()
        Text(
            text = payment.value,
            modifier = Modifier.constrainAs(value) {
                start.linkTo(parent.start)
            }
        )
        Text(
            text = payment.date.format(),
            modifier = Modifier.constrainAs(date) {
                end.linkTo(parent.end)
            }
        )

    }
}

@Composable
fun TypesRow(
    types: List<PaymentType>,
    selectedType: PaymentType?,
    clickListener: Typed1Listener<PaymentType>
) {
    LazyRow() {
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

@Composable
fun TypesRowPlaceholders() {
    LazyRow() {
        items(
            count = 2
        ) {
            SelectableChip(
                modifier = Modifier.placeholder(true)
            ) {}
        }
    }
}
