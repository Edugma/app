package io.edugma.features.account.authorization

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.*
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    AuthContent(
        state = state,
        onLoginClick = { viewModel.authorize()},
        onLoggedClick = {viewModel.back()},
        onLoginChange = {viewModel.setLogin(it)},
        onPasswordChange = {viewModel.setPassword(it)},
        onCheckBoxChanged = {viewModel.setCheckBox(it)},
    )
}

@Composable
fun AuthContent(
    state: AuthState,
    onLoginClick: ClickListener,
    onLoggedClick: ClickListener,
    onPasswordChange: Typed1Listener<String>,
    onLoginChange: Typed1Listener<String>,
    onCheckBoxChanged: Typed1Listener<Boolean>
) {
    Column {
        PrimaryTopAppBar(title = "Авторизация", onBackClick = onLoggedClick)
        if (state.auth) {
            Authorized(state = state) { onLoggedClick.invoke() }
        } else {
            NotAuthorized(state = state, onAuthorize = onLoginClick, onPasswordChange, onLoginChange, onCheckBoxChanged)
        }
    }
}

@Composable
fun NotAuthorized(
    state: AuthState,
    onAuthorize: ClickListener,
    onPasswordChange: Typed1Listener<String>,
    onLoginChange: Typed1Listener<String>,
    onCheckBoxChanged: Typed1Listener<Boolean>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {
        TextBox(
            value = state.login,
            title = "Логин",
            onValueChange = onLoginChange)
        SpacerHeight(height = 12.dp)
        TextBox(
            value = state.password,
            title = "Пароль",
            onValueChange = onPasswordChange,
            passwordMode = true
        )
        SpacerHeight(height = 12.dp)
        CheckBox(
            title = "Запомнить меня",
            value = state.savePassword,
            onValueChange = onCheckBoxChanged,
            modifier = Modifier.fillMaxWidth()
        )
        SpacerHeight(height = 12.dp)
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            ButtonView(text = "Войти", onClick = onAuthorize)
        }
    }
}
@Composable
fun Authorized(state: AuthState, listener: ClickListener) {
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        val (anim, text, button) = createRefs()
        val randomAnim = remember { R.raw.sch_relax_1 }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(randomAnim))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        Column(modifier = Modifier.fillMaxHeight(0.6f).constrainAs(anim) {
            top.linkTo(parent.top)
        }) {
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)

            )
            Text(
                text = "Привет, ${state.name}!",
                style = MaterialTheme3.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        ButtonView(
            text = "Перейти к меню",
            onClick = listener,
            modifier = Modifier
                .constrainAs(text) {
                    bottom.linkTo(parent.bottom)
                    linkTo(parent.start, parent.end)
                    width = Dimension.fillToConstraints
                }
        )
    }
}
