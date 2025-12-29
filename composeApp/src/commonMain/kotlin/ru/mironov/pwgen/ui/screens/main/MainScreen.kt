package ru.mironov.pwgen.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import pwgen.composeapp.generated.resources.Res
import pwgen.composeapp.generated.resources.generate_action
import pwgen.composeapp.generated.resources.include_digits
import pwgen.composeapp.generated.resources.include_special_characters
import pwgen.composeapp.generated.resources.length
import ru.mironov.pwgen.ui.ClipboardManager
import ru.mironov.pwgen.ui.screens.main.presentation.MainEffect
import ru.mironov.pwgen.ui.screens.main.presentation.MainState
import ru.mironov.pwgen.ui.screens.main.presentation.MainViewModel
import ru.mironov.pwgen.ui.theme.AppTheme

class MainScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<MainViewModel>()

        val state by viewModel.collectAsState()

        val clipboardManager: ClipboardManager = koinInject()

        AppTheme {
            MainContent(state, viewModel)
        }
        viewModel.collectSideEffect { sideEffect ->
            handleSideEffect(sideEffect, clipboardManager)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @Preview
    fun MainContent(state: MainState, viewModel: MainViewModel) {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            LengthSlider(state, viewModel)

            Spacer(modifier = Modifier.height(24.dp))

            PasswordSettingsSwitch(
                checked = state.passwordGenerationSettings.specialCharactersIncluded,
                text = stringResource(Res.string.include_special_characters),
                onCheckedChange = viewModel::specialCharacterChecked,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordSettingsSwitch(
                checked = state.passwordGenerationSettings.digitsIncluded,
                text = stringResource(Res.string.include_digits),
                onCheckedChange = viewModel::digitsChecked,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel::generate,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(
                        Res.string.generate_action,
                        state.passwordsCount,
                    ).uppercase(),
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.passwords) { password ->
                    PasswordItem(
                        password = password,
                        onCopyClick = { viewModel.copyPassword(password) }
                    )
                }
            }
        }
    }

    @Composable
    fun PasswordSettingsSwitch(checked: Boolean, text: String, onCheckedChange: (Boolean) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LengthSlider(
        state: MainState,
        viewModel: MainViewModel,
    ) {
        val passwordGenerationSettings = state.passwordGenerationSettings

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.length),
                    fontSize = 18.sp
                )
                Text(
                    text = passwordGenerationSettings.length.toString(),
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val sliderState = SliderState(
                value = passwordGenerationSettings.length.toFloat(),
                valueRange = state.minLength.toFloat()..state.maxLength.toFloat(),
                steps = state.lengthDelta,
            )
            sliderState.onValueChange = { value -> viewModel.changePasswordLength(value.toInt()) }

            Slider(
                state = sliderState,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.minLength.toString(),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = state.maxLength.toString(),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }

    @Composable
    private fun PasswordItem(
        password: String,
        onCopyClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = password,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onCopyClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    private fun handleSideEffect(
        effect: MainEffect,
        clipboardManager: ClipboardManager,
    ) = when(effect) {
        is MainEffect.CopyToClipboard -> clipboardManager.copyToClipboard(effect.text)
    }
}