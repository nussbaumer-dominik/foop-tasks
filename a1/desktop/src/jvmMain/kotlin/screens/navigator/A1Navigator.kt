package screens.navigator

import kotlinx.coroutines.flow.StateFlow

interface A1Navigator {
    fun navigate(destination: NavigationDestination)
    fun observeDestination(): StateFlow<NavigationDestination>
}
