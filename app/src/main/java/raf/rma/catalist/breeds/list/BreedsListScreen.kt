package raf.rma.catalist.breeds.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import raf.rma.catalist.breeds.domain.BreedsData

@ExperimentalMaterial3Api
fun NavGraphBuilder.breedsListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val breedsListViewModel = viewModel<BreedsListViewModel>()
    val state by breedsListViewModel.state.collectAsState()

    BreedsListScreen(
        state = state,
        onItemClick = {
            navController.navigate(route = "breeds/${it.id}")
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun BreedsListScreen(
    state: BreedsListState,
    onItemClick: (BreedsData) -> Unit
) {

}