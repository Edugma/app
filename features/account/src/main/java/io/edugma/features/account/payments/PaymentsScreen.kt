package io.edugma.features.account.payments

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.domain.account.model.*
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentsScreen(viewModel: PaymentsViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        scrimColor = Color.Black.copy(alpha = 0.5f),
        sheetBackgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        sheetContent = { BottomSheetLayout(state) },
    ) {
        PaymentsContent(
            state,
            retryListener = viewModel::load,
            onPaymentChange = viewModel::typeChange,
            onQrClickListener = { scope.launch { bottomState.show() } },
            backListener = viewModel::exit,
        )
    }
}

@Composable
fun BottomSheetLayout(
    state: PaymentsState,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp),
    ) {
        SpacerHeight(height = 15.dp)
        Text(
            text = "QR код",
            style = MaterialTheme3.typography.headlineMedium,
            modifier = Modifier.padding(start = 8.dp),
        )
        SpacerHeight(height = 20.dp)
        AsyncImage(
            model = state.selectedPayment?.qr,
            contentDescription = "qr code",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Intent(Intent.ACTION_VIEW, Uri.parse(state.selectedPayment?.qr)).apply(
                        context::startActivity,
                    )
                },
        )
        SpacerHeight(height = 20.dp)
        EdLabel(
            text = "Вы можете сделать скриншот экрана или скачать QR-код на устройство, затем открыть его в мобильном приложении вашего банка:\nОплата по QR-коду -> Загрузить изображение",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_info_24_regular),
        )
        SpacerHeight(height = 20.dp)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PaymentsContent(
    state: PaymentsState,
    retryListener: ClickListener,
    onPaymentChange: Typed1Listener<Int>,
    onQrClickListener: ClickListener,
    backListener: ClickListener,
) {
    SwipeRefresh(state = rememberSwipeRefreshState(state.isRefreshing), onRefresh = retryListener) {
        Column {
            val paymentsPagerState = rememberPagerState(state.selectedIndex)
            paymentsPagerState.bindTo(state.selectedIndex)
            paymentsPagerState.onPageChanged(onPaymentChange::invoke)
            AppBar(state, backListener)
            when {
                state.isError && state.types.isNull() -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item { ErrorView(retryAction = retryListener) }
                    }
                }
                state.placeholders -> {
                    SelectableTypesRowPlaceholders()
                    PaymentsPlaceholder()
                }
                else -> {
                    SelectableOneTypesRow(
                        state.types ?: emptyList(),
                        state.selectedType,
                        { it.toLabel() },
                    ) {
                        state.data?.keys?.indexOf(it)?.let(onPaymentChange::invoke)
                    }
                    HorizontalPager(
                        count = state.data?.size ?: 0,
                        state = paymentsPagerState,
                        key = { state.getTypeByIndex(it) ?: PaymentType.Dormitory },
                    ) { page ->
                        state.getPaymentsByIndex(page)?.let { payment ->
                            Payments(payment, onQrClickListener)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(
    state: PaymentsState,
    backListener: ClickListener,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 10.dp),
    ) {
        IconButton(onClick = backListener) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Назад",
            )
        }
        SpacerWidth(width = 15.dp)
        Text(
            text = state.selectedPayment?.let { "Договор №${it.number}" } ?: "Оплаты",
            style = MaterialTheme3.typography.headlineSmall,
        )
    }
}

@Composable
fun Payments(payments: Payments, onQrClickListener: ClickListener) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        val expanded = rememberSaveable { mutableStateOf(false) }
        payments.level?.let {
            EdLabel(
                text = "Степень образования: ${payments.level}",
                iconPainter = painterResource(id = R.drawable.acc_ic_teacher_24),
                modifier = Modifier,
            )
        }
        payments.dormRoom?.let {
            EdLabel(
                text = payments.dormNum?.let { "Общежитие №$it, " }.orEmpty() + "комната $it",
                iconPainter = painterResource(id = FluentIcons.ic_fluent_building_24_regular),
            )
        }
        EdLabel(
            text = "Срок действия: ${payments.startDate.format()} - ${payments.endDate.format()}",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier,
        )
        if (payments.balance != "0") {
            EdLabel(
                text = "Осталось выплатить: ${payments.balance}",
                iconPainter = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
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
                if (!expanded.value && payments.payments.size > 3) Expander { expanded.value = !expanded.value }
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
        EdButton(
            onClick = onQrClickListener,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 15.dp),
            text = "Оплатить через QR код".uppercase(),
        )
    }
}

@Composable
fun PaymentsPlaceholder() {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .heightIn(min = 40.dp),
    ) {
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier.placeholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(id = FluentIcons.ic_fluent_money_24_regular),
            modifier = Modifier.placeholder(true),
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
            Text(
                text = "",
                style = MaterialTheme3.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .placeholder(true)
                    .widthIn(100.dp),
            )
        }
    }
}

@Composable
fun HorizontalText(modifier: Modifier = Modifier, label: String, text: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = modifier
                .align(Alignment.CenterStart)
                .widthIn(min = 50.dp),
            style = MaterialTheme3.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme3.colorScheme.primary,
        )
        Text(
            text = text,
            modifier = modifier
                .align(Alignment.CenterEnd)
                .widthIn(min = 20.dp),
            style = MaterialTheme3.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme3.colorScheme.primary,
        )
    }
}

@Composable
fun Payment(payment: Payment) {
    TonalCard(shape = MaterialTheme3.shapes.extraSmall) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .heightIn(min = 50.dp),
        ) {
            Text(text = payment.date.format(), modifier = Modifier.align(Alignment.CenterStart))
            Text(text = payment.value, modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun PaymentPlaceholder() {
    TonalCard(shape = MaterialTheme3.shapes.extraSmall) {
        Box(
            modifier = Modifier
                .placeholder(true)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .heightIn(min = 50.dp),
        ) {}
    }
}

@Composable
fun Expander(onClickListener: ClickListener) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clickable(onClick = onClickListener),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Показать больше",
                style = MaterialTheme3.typography.bodyLarge,
            )
            SpacerWidth(width = 20.dp)
            Icon(painterResource(id = FluentIcons.ic_fluent_ios_arrow_rtl_24_filled), contentDescription = null, modifier = Modifier.rotate(90f))
        }
    }
}
