package raf.rma.catalist.breeds.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import raf.rma.catalist.breeds.details.BreedsDetailsContract.BreedsDetailsState
import raf.rma.catalist.breeds.model.BreedsUiModel
import raf.rma.catalist.breeds.model.ImageUiModel
import raf.rma.catalist.core.compose.AppIconButton
import raf.rma.catalist.core.compose.NoDataContent

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

    BreedsDetailsScreen(
        state = state.value,
        onClose = {
            navController.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedsDetailsScreen(
    state: BreedsDetailsState,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = state.data?.name ?: "Loading")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.fetching) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
//                        val errorMessage = when (state.error) {
//                            is BreedsDetailsState.DetailsError.DataUpdateFailed ->
//                                "Failed to load. Error message: ${state.error.cause?.message}."
//                        }
//                        Text(text = errorMessage)
                    }
                } else if (state.data != null) {
                    state.image?.let {
                        BreedsDataColumn(
                            data = state.data,
                            image = it
                        )
                    }
                } else {
                    NoDataContent(id = state.breedId)
                }
            }
        }
    )
}

@Composable
private fun BreedsDataColumn(
    data: BreedsUiModel,
    image: ImageUiModel
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        SubcomposeAsyncImage(
            modifier = Modifier.size(100.dp),
            model = image.url,
            contentDescription = null,
        )

//        val painter: Painter =
//            rememberAsyncImagePainter(
////            modifier = Modifier.size(200.dp), model = data.imageUrl, contentDescription = null
//                ImageRequest.Builder(LocalContext.current).data(data = data.image.url).apply ( block = fun ImageRequest.Builder.(){
//                    crossfade(true)
//                } ).build()
//            )
//
//        Image(painter = painter, contentDescription = "aaa",
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = data.description,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.origin,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.temperament,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.lifeSpan,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.weight,
        )

//        Text(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            style = MaterialTheme.typography.bodyLarge,
//            text = data.rare,
//        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = data.wikipediaURL,
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun PreviewDetailsScreen() {
    Surface {
        BreedsDetailsScreen(
            state = BreedsDetailsState(
                breedId = "1",
                data = BreedsUiModel(
                    id="1",
                    name="Macka1",
                    description = "dasjkghbdaslkghasdukghsklgdasjkdg",
                    alternativeName = "macka",
                    temperament = "dasfsd,sdafsa,sadfsa",
                    origin = "Serbia",
                    lifeSpan = "10",
                    weight = "10",
                    wikipediaURL = "http://wikipedia.com",
                    rare = 1,
                    adaptability = 1,
                    affectionLevel = 2,
                    referenceImageId = "0XYvRd7oD"
                ),
            ),
            onClose = {},
        )
    }
}