package io.edugma.features.account.authorization

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.*
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.textField.EdTextField
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.account.R
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.*
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel = getViewModel()) {
    with(viewModel) {
        val state by state.collectAsState()

        AuthContent(
            state = state,
            onLoginClick = ::authorize,
            onLoggedClick = ::exit,
            onLoginChange = ::setLogin,
            onPasswordChange = ::setPassword,
            onCheckBoxChanged = ::setCheckBox,
            onLogout = ::logout,
        )
    }
}

@Composable
fun AuthContent(
    state: AuthState,
    onLoginClick: ClickListener,
    onLoggedClick: ClickListener,
    onPasswordChange: Typed1Listener<String>,
    onLoginChange: Typed1Listener<String>,
    onCheckBoxChanged: Typed1Listener<Boolean>,
    onLogout: ClickListener,
) {
    Column {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (content, filter) = createRefs()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(content) {
                    linkTo(parent.start, filter.start)
                    width = Dimension.fillToConstraints
                },
            ) {
                IconButton(onClick = onLoggedClick) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                    )
                }
                SpacerWidth(width = 15.dp)
                Text(
                    text = "Авторизация",
                    style = EdTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (state.auth) {
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier.constrainAs(filter) {
                        linkTo(parent.top, parent.bottom)
                        end.linkTo(parent.end)
                    },
                ) {
                    Icon(
                        painterResource(id = FluentIcons.ic_fluent_sign_out_24_regular),
                        contentDescription = "Выход",
                    )
                }
            }
        }
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
    onCheckBoxChanged: Typed1Listener<Boolean>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .verticalScroll(rememberScrollState()),
    ) {
        EdTextField(
            value = state.login,
            placeholder = "Логин",
            onValueChange = onLoginChange,
        )
        SpacerHeight(height = 12.dp)
        EdTextField(
            value = state.password,
            placeholder = "Пароль",
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
        )
        SpacerHeight(height = 25.dp)
        EdButton(
            modifier = Modifier.defaultMinSize(minWidth = 250.dp),
            onClick = onAuthorize,
            isLoading = state.isLoading,
            text = "Войти",
        )
    }
}

@Composable
fun Authorized(state: AuthState, listener: ClickListener) {
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxSize(),
    ) {
        val (anim, button) = createRefs()
        val randomAnim = remember { R.raw.sch_relax_1 }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(randomAnim))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever,
        )
        Column(
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .constrainAs(anim) {
                    top.linkTo(parent.top)
                },
        ) {
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),

            )
            Text(
                text = "Привет, ${state.name}!",
                style = EdTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
        EdButton(
            text = "Перейти к меню",
            onClick = listener,
            modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    linkTo(parent.start, parent.end)
                    width = Dimension.fillToConstraints
                },
        )
    }
}
