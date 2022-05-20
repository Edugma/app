package io.edugma.features.account.payments

import android.util.DisplayMetrics
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun PaymentsScreen(viewModel: PaymentsViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    PaymentsContent(state,
        retryListener = viewModel::load,
        onPaymentChange = viewModel::typeChange,
        backListener = viewModel::exit,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PaymentsContent(
    state: PaymentsState,
    retryListener: ClickListener,
    onPaymentChange: Typed1Listener<Int>,
    backListener: ClickListener
) {
    Column {
        val paymentsPagerState = rememberPagerState(state.selectedIndex)
        paymentsPagerState.bindTo(state.selectedIndex)
        paymentsPagerState.onPageChanged(onPaymentChange::invoke)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            IconButton(onClick = backListener) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
            SpacerWidth(width = 15.dp)
            Text(
                text = state.selectedPayment?.let { "Договор №${it.number}" } ?: "Оплата",
                style = MaterialTheme3.typography.headlineSmall,
            )
        }
        if (state.placeholders) {
            TypesRowPlaceholders()
        } else {
            TypesRow(
                state.types,
                state.selectedType
            ) {
                onPaymentChange.invoke(state.data.keys.indexOf(it))
            }
        }
        HorizontalPager(
            count = state.data.size,
            state = paymentsPagerState,
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
        payments.level?.let {
            TextWithIcon(
                text = "степень образования: ${payments.level}",
                icon = painterResource(id = R.drawable.acc_ic_teacher_24),
                modifier = Modifier
            )
        }
        payments.dormRoom?.let {
            TextWithIcon(
                text = payments.dormNum?.let { "Общежитие №$it, " }.orEmpty() + "комната $it",
                icon = painterResource(id = FluentIcons.ic_fluent_building_24_regular),
                modifier = Modifier
            )
        }
        TextWithIcon(
            text = "Срок действия: ${payments.startDate.format()} - ${payments.endDate.format()}",
            icon = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier
        )
        TextWithIcon(
            text = "Сумма договора: ${payments.sum}" +
                    if (payments.balance != "0") ", осталось выплатить ${payments.balance}" else "",
            icon = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
            modifier = Modifier
        )
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
                modifier = Modifier
                    .placeholder(true)
                    .widthIn(80.dp)
            ) {}
        }
    }
}
