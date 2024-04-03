package raf.rma.catalist.breeds.details

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.breedsDetails(
    route: String,
    navController: NavController
) = composable(
    route = route
){ navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required")

    // We have to provide factory class to instantiate our view model
    // since it has a custom property in constructor
    val breedsDetailsViewModel = viewModel<BreedsDetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // We pass the passwordId which we read from arguments above
                return BreedsDetailsViewModel(breedId = dataId) as T
            }
        },
    )
    val state = breedsDetailsViewModel.state.collectAsState()

}