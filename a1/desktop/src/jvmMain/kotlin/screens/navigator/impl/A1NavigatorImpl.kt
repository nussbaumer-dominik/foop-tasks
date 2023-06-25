package screens.navigator.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import screens.navigator.A1Navigator
import screens.navigator.NavigationDestination

class A1NavigatorImpl : A1Navigator {
    private val currentDestination: MutableStateFlow<NavigationDestination> =
        MutableStateFlow(NavigationDestination.START)

    override fun navigate(destination: NavigationDestination) {
        currentDestination.value = destination
    }

    override fun observeDestination(): StateFlow<NavigationDestination> =
        currentDestination
}

fun getNavigator(): A1Navigator = A1NavigatorImpl()