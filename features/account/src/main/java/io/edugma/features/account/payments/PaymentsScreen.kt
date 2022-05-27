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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
        onQrClickListener = {},
        backListener = viewModel::exit,
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PaymentsContent(
    state: PaymentsState,
    retryListener: ClickListener,
    onPaymentChange: Typed1Listener<Int>,
    onQrClickListener: ClickListener,
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
        if (state.placeholders) {
            PaymentsPlaceholder()
        } else {
            HorizontalPager(
                count = state.data.size,
                state = paymentsPagerState,
                key = {state.getTypeByIndex(it) ?: PaymentType.Dormitory}
            ) { page ->
                state.getPaymentsByIndex(page)?.let { payment ->
                    Payments(payment, onQrClickListener)
                }
            }
        }
    }
}

@Composable
fun Payments(payments: Payments, onQrClickListener: ClickListener) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        val expanded = rememberSaveable { mutableStateOf(false) }
        payments.level?.let {
            TextWithIcon(
                text = "Степень образования: ${payments.level}",
                icon = painterResource(id = R.drawable.acc_ic_teacher_24),
                modifier = Modifier
            )
        }
        payments.dormRoom?.let {
            TextWithIcon(
                text = payments.dormNum?.let { "Общежитие №$it, " }.orEmpty() + "комната $it",
                icon = painterResource(id = FluentIcons.ic_fluent_building_24_regular),
            )
        }
        TextWithIcon(
            text = "Срок действия: ${payments.startDate.format()} - ${payments.endDate.format()}",
            icon = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier
        )
        if (payments.balance != "0") {
            TextWithIcon(
                text = "Осталось выплатить: ${payments.balance}",
                icon = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
            )
        }
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            SpacerHeight(height = 10.dp)
            HorizontalText(label = "Сумма договора: ", text = payments.sum)
            SpacerHeight(height = 10.dp)
            Column() {
                val elements = if (!expanded.value) payments.payments.take(3) else payments.payments
                elements.forEach {
                    Payment(it)
                    SpacerHeight(height = 5.dp)
                }
                if (!expanded.value && payments.payments.size>3) Expander { expanded.value = !expanded.value }
            }
            val balanceCurrentText: String
            val balanceCurrentColor: Color
            when {
                payments.balanceCurrent.contains("-") -> {
                    balanceCurrentText = "Переплата на текущую дату: ${payments.balanceCurrent.replace("-", "")}"
                    balanceCurrentColor = Color.Green.copy(alpha = 0.85f)
                }
                payments.balanceCurrent == "0" -> {
                    balanceCurrentText = "Все оплачено, задолженности нет"
                    balanceCurrentColor = Color.Green.copy(alpha = 0.85f)
                }
                else -> {
                    balanceCurrentText = "Задолженность на текущую дату: ${payments.balanceCurrent}"
                    balanceCurrentColor = MaterialTheme3.colorScheme.error
                }
            }
            SpacerHeight(height = 5.dp)
            Text(text = balanceCurrentText, style = MaterialTheme3.typography.titleMedium, fontWeight = FontWeight.Bold, color = balanceCurrentColor, fontSize = 17.sp)
        }
        PrimaryButton(onClick = onQrClickListener, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 15.dp)) {
            Text(text = "Оплатить через QR код".uppercase())
        }
    }
}

@Composable
fun PaymentsPlaceholder() {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .heightIn(min = 40.dp)
    ) {
        TextWithIcon(
            text = "",
            icon = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier.placeholder(true)
        )
        TextWithIcon(
            text = "",
            icon = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
            modifier = Modifier.placeholder(true)
        )
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            SpacerHeight(height = 10.dp)
            HorizontalText(modifier = Modifier.placeholder(true), "", "")
            SpacerHeight(height = 10.dp)
            LazyColumn() {
                items(3) {
                    PaymentPlaceholder()
                    SpacerHeight(height = 5.dp)
                }
            }
            Text(text = "", style = MaterialTheme3.typography.titleMedium,
                fontWeight = FontWeight.Bold, fontSize = 17.sp, modifier = Modifier.placeholder(true).widthIn(100.dp))
        }
    }
}

@Composable
fun HorizontalText(modifier: Modifier = Modifier, label: String, text: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, modifier = modifier
            .align(Alignment.CenterStart)
            .widthIn(min = 50.dp), style = MaterialTheme3.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme3.colorScheme.primary)
        Text(text = text, modifier = modifier
            .align(Alignment.CenterEnd)
            .widthIn(min = 20.dp), style = MaterialTheme3.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme3.colorScheme.primary)
    }
}

@Composable
fun Payment(payment: Payment) {
    TonalCard(shape = MaterialTheme3.shapes.extraSmall) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .heightIn(min = 50.dp)) {
            Text(text = payment.date.format(), modifier = Modifier.align(Alignment.CenterStart))
            Text(text = payment.value, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun PaymentPlaceholder() {
    TonalCard(shape = MaterialTheme3.shapes.extraSmall) {
        Box(modifier = Modifier
            .placeholder(true)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .heightIn(min = 50.dp)) {}
    }
}

@Composable
fun Expander(onClickListener: ClickListener) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onClickListener, modifier = Modifier.fillMaxWidth()) {
            Icon(painterResource(id = FluentIcons.ic_fluent_ios_arrow_rtl_24_filled), contentDescription = null, modifier = Modifier.rotate(90f))
        }
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
