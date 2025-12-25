package ru.mironov.pwgen.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mironov.pwgen.ui.screens.main.presentation.MainEffect
import ru.mironov.pwgen.ui.screens.main.presentation.MainState
import ru.mironov.pwgen.ui.screens.main.presentation.MainViewModel

class MainScreen : Screen {

    @Composable
    @Preview
    override fun Content() {
        val viewModel = koinViewModel<MainViewModel>()

        val state by viewModel.collectAsState()
        MainContent(state, viewModel)
        viewModel.collectSideEffect(sideEffect = ::handleSideEffect)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainContent(state: MainState, viewModel: MainViewModel) {
        val passwordGenerationSettings = state.passwordGenerationSettings
        MaterialTheme {
            Snackbar {  }
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("6")
                    val sliderState = SliderState(
                        value = passwordGenerationSettings.length.toFloat(),
                        valueRange = 6f..16f,
                        steps = 10,
                    )
                    sliderState.onValueChange = { value -> viewModel.changePasswordLength(value.toInt()) }
                    Slider(
                        state = sliderState,
                        modifier = Modifier.weight(1f)
                            .padding(horizontal = 16.dp),
                    )
                    Text("16")
                }
            }
        }
    }

    private fun handleSideEffect(effect: MainEffect) = Unit
}