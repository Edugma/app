package io.edugma.features.account.payments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moriatsushi.insetsx.navigationBarsPadding
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.organism.chipRow.EdSelectableChipRow
import io.edugma.core.designSystem.organism.chipRow.EdSelectableChipRowPlaceholders
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNull
import io.edugma.core.utils.ui.bindTo
import io.edugma.core.utils.ui.onPageChanged
import io.edugma.domain.account.model.Payment
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Payments
import io.edugma.domain.account.model.toLabel
import io.edugma.domain.base.utils.format
import io.edugma.features.account.payments.bottomSheet.PaymentBottomSheet
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentsScreen(viewModel: PaymentsViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    FeatureScreen(navigationBarPadding = false) {
        ModalBottomSheetLayout(
            sheetState = bottomState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            scrimColor = Color.Black.copy(alpha = 0.5f),
            sheetBackgroundColor = EdTheme.colorScheme.surface,
            sheetContent = {
                PaymentBottomSheet(
                    qrUrl = state.selectedPayment?.qrCurrent.orEmpty(),
                    openUri = viewModel::onOpenUri,
                )
            },
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
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun PaymentsContent(
    state: PaymentsState,
    retryListener: ClickListener,
    onPaymentChange: Typed1Listener<Int>,
    onQrClickListener: ClickListener,
    backListener: ClickListener,
) {
    Column(Modifier.navigationBarsPadding()) {
        val paymentsPagerState = rememberPagerState(state.selectedIndex)
        paymentsPagerState.bindTo(state.selectedIndex)
        paymentsPagerState.onPageChanged(onPaymentChange::invoke)
        EdTopAppBar(
            title = state.selectedPayment?.let { "Договор №${it.number}" } ?: "Оплаты",
            onNavigationClick = backListener,
            actions = {
                if (!state.selectedPayment?.qrCurrent.isNullOrEmpty()) {
                    androidx.compose.material3.IconButton(
                        onClick = { onQrClickListener() },
                    ) {
                        Icon(
                            painterResource(EdIcons.ic_fluent_qr_code_24_regular),
                            contentDescription = "qr code",
                        )
                    }
                }
            },
        )
        EdPullRefresh(refreshing = state.isRefreshing, onRefresh = retryListener) {
            when {
                state.isError && state.types.isNull() -> {
                    ErrorWithRetry(modifier = Modifier.fillMaxSize(), retryAction = retryListener)
                }
                state.placeholders -> PaymentsScreenPlaceholder()
                else -> PaymentScreen(state, paymentsPagerState, onPaymentChange)
            }
        }
    }
}

@Composable
fun PaymentsScreenPlaceholder() {
    Column(Modifier.fillMaxSize()) {
        EdSelectableChipRowPlaceholders()
        PaymentsPlaceholder()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentScreen(
    state: PaymentsState,
    paymentsPagerState: PagerState,
    onPaymentChange: Typed1Listener<Int>,
) {
    Column(Modifier.fillMaxSize()) {
        EdSelectableChipRow(
            state.types ?: emptyList(),
            state.selectedType,
            { it.toLabel() },
        ) {
            state.data?.keys?.indexOf(it)?.let(onPaymentChange::invoke)
        }
        HorizontalPager(
            pageCount = state.data?.size ?: 0,
            state = paymentsPagerState,
            key = { state.getTypeByIndex(it) ?: PaymentType.Dormitory },
        ) { page ->
            state.getPaymentsByIndex(page)?.let { payment ->
                Payments(payment)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Payments(payments: Payments) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val elements = if (!expanded.value) payments.payments.take(3) else payments.payments
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
    ) {
        item {
            Column {
                SpacerHeight(height = 8.dp)
                payments.level?.let {
                    EdLabel(
                        text = "Степень образования: ${payments.level}",
                        iconPainter = painterResource("acc_ic_teacher_24"),
                        modifier = Modifier,
                    )
                    SpacerHeight(height = 8.dp)
                }
                payments.dormRoom?.let {
                    EdLabel(
                        text = payments.dormNum?.let { "Общежитие №$it, " }
                            .orEmpty() + "комната $it",
                        iconPainter = painterResource(EdIcons.ic_fluent_building_24_regular),
                    )
                    SpacerHeight(height = 8.dp)
                }
                EdLabel(
                    text = "Срок договора: ${payments.startDate.format()} - ${payments.endDate.format()}",
                    iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_24_regular),
                    modifier = Modifier,
                )
                SpacerHeight(height = 8.dp)
                if (payments.balance != "0") {
                    EdLabel(
                        text = "Осталось выплатить: ${payments.balance}",
                        iconPainter = painterResource(EdIcons.ic_fluent_money_24_regular),
                    )
                    SpacerHeight(height = 8.dp)
                }
            }
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                PaymentsLeftText(payments.balanceCurrent)
                SpacerHeight(height = 10.dp)
                HorizontalText(label = "Сумма договора: ", text = payments.sum)
                SpacerHeight(height = 10.dp)
            }
        }
        items(elements) {
            Payment(it)
            SpacerHeight(height = 5.dp)
        }
        if (!expanded.value && payments.payments.size > 3) {
            item {
                Expander { expanded.value = !expanded.value }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PaymentsPlaceholder() {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .heightIn(min = 40.dp),
    ) {
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_24_regular),
            modifier = Modifier.edPlaceholder(true),
        )
        EdLabel(
            text = "",
            iconPainter = painterResource(EdIcons.ic_fluent_money_24_regular),
            modifier = Modifier.edPlaceholder(true),
        )
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            SpacerHeight(height = 10.dp)
            HorizontalText(modifier = Modifier.edPlaceholder(true), "", "")
            SpacerHeight(height = 10.dp)
            LazyColumn() {
                items(3) {
                    PaymentPlaceholder()
                    SpacerHeight(height = 5.dp)
                }
            }
            Text(
                text = "",
                style = EdTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .edPlaceholder(true)
                    .widthIn(100.dp),
            )
        }
    }
}

@Composable
fun PaymentsLeftText(balanceCurrent: String) {
    val balanceCurrentText: String
    val balanceCurrentColor: Color
    when {
        balanceCurrent.contains("-") -> {
            balanceCurrentText = "Переплата на текущую дату: ${balanceCurrent.replace("-", "")}"
            balanceCurrentColor = Color.Green.copy(alpha = 0.85f)
        }
        balanceCurrent == "0" -> {
            balanceCurrentText = "В данный момент все оплачено, задолженности нет"
            balanceCurrentColor = Color.Green.copy(alpha = 0.85f)
        }
        else -> {
            balanceCurrentText = "Задолженность на текущую дату: $balanceCurrent"
            balanceCurrentColor = EdTheme.colorScheme.error
        }
    }
    Text(text = balanceCurrentText, style = EdTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = balanceCurrentColor, fontSize = 17.sp)
}

@Composable
fun HorizontalText(modifier: Modifier = Modifier, label: String, text: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = modifier
                .align(Alignment.CenterStart)
                .widthIn(min = 50.dp),
            style = EdTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = EdTheme.colorScheme.primary,
        )
        Text(
            text = text,
            modifier = modifier
                .align(Alignment.CenterEnd)
                .widthIn(min = 20.dp),
            style = EdTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = EdTheme.colorScheme.primary,
        )
    }
}

@Composable
fun Payment(payment: Payment) {
    EdCard(shape = EdTheme.shapes.extraSmall) {
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
    EdCard(shape = EdTheme.shapes.extraSmall) {
        Box(
            modifier = Modifier
                .edPlaceholder(true)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .heightIn(min = 50.dp),
        ) {}
    }
}

@OptIn(ExperimentalResourceApi::class)
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
                style = EdTheme.typography.bodyLarge,
            )
            SpacerWidth(width = 20.dp)
            Icon(
                painterResource(EdIcons.ic_fluent_ios_arrow_rtl_24_filled),
                contentDescription = null,
                modifier = Modifier.rotate(90f),
            )
        }
    }
}
