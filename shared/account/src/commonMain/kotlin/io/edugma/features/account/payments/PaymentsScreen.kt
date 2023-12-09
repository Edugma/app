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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.api.utils.format
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.chipRow.EdSelectableChipRow
import io.edugma.core.designSystem.organism.chipRow.EdSelectableChipRowPlaceholders
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.SecondaryContent
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.designSystem.utils.navigationBarsPadding
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.isNull
import io.edugma.core.utils.ui.bindTo
import io.edugma.core.utils.ui.onPageChanged
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.account.domain.model.payments.Contract
import io.edugma.features.account.domain.model.payments.Payment
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.payments.bottomSheet.PaymentBottomSheet
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun PaymentsScreen(viewModel: PaymentsViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.selectedPaymentMethod) {
        if (state.selectedPaymentMethod != null) {
            scope.launch { bottomState.show() }
        }
    }

    LaunchedEffect(bottomState) {
        snapshotFlow {
            bottomState.currentState
        }.collect {
            if (it == ModalBottomSheetValue.Hidden) {
                viewModel.onBottomSheetClosed()
            }
        }
    }

    FeatureBottomSheetScreen(
        navigationBarPadding = false,
        sheetState = bottomState,
        sheetContent = {
            state.selectedPaymentMethod?.let { selectedPaymentMethod ->
                PaymentBottomSheet(
                    paymentMethod = selectedPaymentMethod,
                    openUri = viewModel::onOpenUri,
                )
            }
        },
    ) {
        when {
            state.showError -> {
                PaymentsError(viewModel::exit, viewModel::load)
            }

            state.isNothingToShow -> {
                NoPayments(viewModel::exit)
            }

            else -> {
                PaymentsContent(
                    state,
                    retryListener = viewModel::load,
                    onPaymentChange = viewModel::typeChange,
                    backListener = viewModel::exit,
                    onPaymentMethodClick = viewModel::onPaymentMethodClick,
                )
            }
        }
    }
}

@Composable
fun NoPayments(backListener: ClickListener) {
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Оплаты",
            onNavigationClick = backListener,
        )
        EdNothingFound(Modifier.fillMaxSize(), message = "Оплат нет :)")
    }
}

@Composable
fun PaymentsError(backListener: ClickListener, retryListener: ClickListener) {
    Column(Modifier.navigationBarsPadding()) {
        EdTopAppBar(
            title = "Оплаты",
            onNavigationClick = backListener,
        )
        ErrorWithRetry(Modifier.fillMaxSize(), retryAction = retryListener)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentsContent(
    state: PaymentsState,
    retryListener: ClickListener,
    onPaymentChange: Typed1Listener<Int>,
    backListener: ClickListener,
    onPaymentMethodClick: (PaymentMethod) -> Unit,
) {
    Column(Modifier.navigationBarsPadding()) {
        val paymentsPagerState = rememberPagerState(state.selectedIndex) { state.data?.size ?: 0 }
        paymentsPagerState.bindTo(state.selectedIndex)
        paymentsPagerState.onPageChanged(onPaymentChange::invoke)
        EdTopAppBar(
            title = state.selectedPayment?.let { it.title } ?: "Оплаты",
            onNavigationClick = backListener,
        )
        EdPullRefresh(refreshing = state.isRefreshing, onRefresh = retryListener) {
            when {
                state.isError && state.types.isNull() -> {
                    ErrorWithRetry(modifier = Modifier.fillMaxSize(), retryAction = retryListener)
                }
                state.placeholders -> PaymentsScreenPlaceholder()
                else -> PaymentScreen(
                    state = state,
                    paymentsPagerState = paymentsPagerState,
                    onPaymentChange = onPaymentChange,
                    onPaymentMethodClick = onPaymentMethodClick,
                )
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
    onPaymentMethodClick: (PaymentMethod) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        EdSelectableChipRow(
            types = state.types ?: emptyList(),
            selectedType = state.selectedType,
            nameMapper = { it },
        ) {
            state.data?.keys?.indexOf(it)?.let(onPaymentChange::invoke)
        }
        HorizontalPager(
            state = paymentsPagerState,
            key = { state.getTypeByIndex(it) ?: "" },
        ) { page ->
            state.getPaymentsByIndex(page)?.let { payment ->
                Payments(
                    contract = payment,
                    onPaymentMethodClick = onPaymentMethodClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Payments(
    contract: Contract,
    onPaymentMethodClick: (PaymentMethod) -> Unit,
) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val elements = if (!expanded.value) contract.payments.take(3) else contract.payments
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
    ) {
        item {
            Column {
                SpacerHeight(height = 8.dp)
                EdLabel(
                    text = contract.description,
                    iconPainter = painterResource(EdIcons.ic_fluent_building_24_regular),
                )
                SpacerHeight(height = 8.dp)
                EdLabel(
                    text = "Срок договора: ${contract.startDate.format()} - ${contract.endDate.format()}",
                    iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_24_regular),
                    modifier = Modifier,
                )
                SpacerHeight(height = 8.dp)
                val textColor = if (contract.isNegativeBalance) {
                    EdTheme.colorScheme.error
                } else {
                    LocalContentColor.current
                }
                EdLabel(
                    text = "Баланс: ${contract.balance}",
                    color = textColor,
                    iconPainter = painterResource(EdIcons.ic_fluent_money_24_regular),
                )
                SpacerHeight(height = 8.dp)
            }
        }
        items(contract.paymentMethods) { paymentMethod ->
            PaymentMethodItem(
                paymentMethod = paymentMethod,
                onClick = { onPaymentMethodClick(paymentMethod) },
            )
        }
        items(elements) {
            Payment(it)
            SpacerHeight(height = 5.dp)
        }
        if (!expanded.value && contract.payments.size > 3) {
            item {
                Expander { expanded.value = !expanded.value }
            }
        }
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
private fun PaymentMethodItem(
    paymentMethod: PaymentMethod,
    onClick: () -> Unit,
) {
    EdButton(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        text = paymentMethod.title,
        onClick = onClick,
    )
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
    EdCard(shape = EdTheme.shapes.small) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                EdLabel(
                    text = payment.title,
                    style = EdTheme.typography.bodyMedium,
                    modifier = Modifier,
                )
                SecondaryContent {
                    EdLabel(
                        text = payment.description,
                        style = EdTheme.typography.bodySmall,
                        modifier = Modifier,
                    )
                }
            }
            val textColor = if (payment.isNegative) {
                EdTheme.colorScheme.error
            } else {
                LocalContentColor.current
            }
            EdLabel(
                text = payment.value,
                color = textColor,
                modifier = Modifier,
            )
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
