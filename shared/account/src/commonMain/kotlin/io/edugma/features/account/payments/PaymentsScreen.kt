package io.edugma.features.account.payments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.api.model.contentType
import io.edugma.core.api.model.key
import io.edugma.core.api.utils.format
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.NavigationBarSpacer
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.EdScaffold
import io.edugma.core.designSystem.organism.actionCard.EdActionCardWidth
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.organism.cell.EdCell
import io.edugma.core.designSystem.organism.cell.EdCellDefaults
import io.edugma.core.designSystem.organism.cell.EdCellPlaceholder
import io.edugma.core.designSystem.organism.cell.EdCellSize
import io.edugma.core.designSystem.organism.chipRow.EdChipLabelLazyRow
import io.edugma.core.designSystem.organism.chipRow.EdChipRowPlaceholders
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.navigationBarsPadding
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureBottomSheetScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.account.domain.model.payments.PaymentMethod
import io.edugma.features.account.payments.bottomSheet.PaymentBottomSheet
import io.edugma.features.account.payments.model.ContractUiModel
import io.edugma.features.account.payments.model.PaymentUiModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun PaymentsScreen(viewModel: PaymentsViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    val bottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val scope = rememberCoroutineScope()

    val onAction = viewModel.rememberOnAction()

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
        statusBarPadding = false,
        sheetState = bottomState,
        sheetContent = {
            state.selectedPaymentMethod?.let { selectedPaymentMethod ->
                PaymentBottomSheet(
                    paymentMethod = selectedPaymentMethod,
                    openUri = {
                        onAction(PaymentsAction.OnOpenUrl)
                    },
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
                    state = state,
                    retryListener = viewModel::load,
                    backListener = viewModel::exit,
                    onAction = onAction,
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
    state: PaymentsUiState,
    retryListener: ClickListener,
    backListener: ClickListener,
    onAction: (PaymentsAction) -> Unit,
) {
    EdScaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                EdTopAppBar(
                    title = state.selectedContract?.title ?: "Оплаты",
                    onNavigationClick = backListener,
                    windowInsets = WindowInsets.statusBars,
                )
                if (state.types != null) {
                    EdChipLabelLazyRow(
                        items = state.types,
                        selectedItem = state.selectedType,
                        modifier = Modifier.padding(bottom = 10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp),
                    ) { selectedTitle ->
                        val id = state.data?.values
                            ?.firstOrNull { it.title == selectedTitle }
                            ?.id
                        if (id != null) {
                            onAction(PaymentsAction.OnContractSelected(id))
                        }
                    }
                }
            }
        },
    ) {
        EdPullRefresh(refreshing = state.isRefreshing, onRefresh = retryListener) {
            when {
                state.isError && state.types.isNull() -> {
                    ErrorWithRetry(modifier = Modifier.fillMaxSize(), retryAction = retryListener)
                }
                state.placeholders -> PaymentsScreenPlaceholder()
                else -> {
                    // TODO No contracts placeholder
                    if (state.selectedContract != null) {
                        Payments(
                            contract = state.selectedContract,
                            onPaymentMethodClick = {
                                onAction(PaymentsAction.OnPaymentMethodClick(it))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentsScreenPlaceholder() {
    Column(Modifier.fillMaxSize()) {
        EdChipRowPlaceholders()
        PaymentsPlaceholder()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Payments(
    contract: ContractUiModel,
    onPaymentMethodClick: (PaymentMethod) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        item {
            EdSurface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                shape = EdTheme.shapes.large,
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                ) {
                    EdLabel(
                        text = contract.description,
                        iconPainter = painterResource(EdIcons.ic_fluent_building_24_regular),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    )
                    EdLabel(
                        text = "Срок договора: ${contract.startDate.format()} - ${contract.endDate.format()}",
                        iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_24_regular),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    )
                    val textColor = if (contract.isNegativeBalance) {
                        EdTheme.colorScheme.error
                    } else {
                        LocalContentColor.current
                    }
                    EdLabel(
                        text = "Баланс: ${contract.balance}",
                        color = textColor,
                        iconPainter = painterResource(EdIcons.ic_fluent_money_24_regular),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(start = 10.dp, end = 10.dp, top = 8.dp),
                    ) {
                        contract.paymentMethods.forEachIndexed { index, paymentMethod ->
                            val modifier = if (index == 0) {
                                Modifier
                            } else {
                                Modifier.padding(start = 10.dp)
                            }
                            PaymentMethodItem(
                                paymentMethod = paymentMethod,
                                onClick = { onPaymentMethodClick(paymentMethod) },
                                modifier = modifier,
                            )
                        }
                    }
                }
            }
        }
        items(
            items = contract.payments,
            key = contract.payments.key(),
            contentType = contract.payments.contentType(),
        ) {
            when (it) {
                is PaymentUiModel.Date -> PaymentDate(it)
                is PaymentUiModel.Payment -> Payment(it)
            }
        }
        item {
            NavigationBarSpacer()
        }
    }
}

@Composable
fun PaymentsPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        repeat(6) {
            PaymentPlaceholder()
            SpacerHeight(height = 5.dp)
        }
    }
}

@Composable
private fun PaymentMethodItem(
    paymentMethod: PaymentMethod,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = rememberAsyncImagePainter(paymentMethod.icon)

    EdIconCard(
        title = paymentMethod.title,
        icon = icon,
        onClick = onClick,
        modifier = modifier,
        width = EdActionCardWidth.medium,
    )
}

@Composable
internal fun PaymentDate(date: PaymentUiModel.Date) {
    EdLabel(
        text = date.date,
        style = EdTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
    )
}

@Composable
internal fun Payment(payment: PaymentUiModel.Payment) {
    EdCell(
        title = payment.title,
        subtitle = payment.description,
        avatar = payment.avatar,
        size = EdCellSize.small,
        contentPadding = EdCellDefaults.contentPadding(
            start = 16.dp,
            end = 16.dp,
        ),
        modifier = Modifier,
    ) {
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

@Composable
fun PaymentPlaceholder() {
    EdCellPlaceholder(
        Modifier.fillMaxWidth(),
        size = EdCellSize.small,
    )
}
