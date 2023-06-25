package screens

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class A1Screen<T>(
    initialState: T,
) {
    private val state: MutableStateFlow<T> = MutableStateFlow(initialState)

    @Composable
    abstract fun render()

    protected fun updateState(callback: (state: T) -> T) {
        state.value = callback(state.value)
    }

    protected fun observeState(): StateFlow<T> =
        state

    protected fun getState(): T =
        state.value
}
