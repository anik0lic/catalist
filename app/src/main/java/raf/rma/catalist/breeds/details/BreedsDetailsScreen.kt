package raf.rma.catalist.breeds.details

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import raf.rma.catalist.breeds.details.BreedsDetailsContract.BreedsDetailsState
import raf.rma.catalist.breeds.model.BreedsUiModel
import raf.rma.catalist.breeds.model.ImageUiModel
import raf.rma.catalist.core.compose.AppIconButton
import raf.rma.catalist.core.compose.DetailsWidget
import raf.rma.catalist.core.compose.NoDataContent
import raf.rma.catalist.core.theme.LightOrange
import raf.rma.catalist.core.theme.Orange

fun NavGraphBuilder.breedsDetails(
    route: String,
    navController: NavController,
) = composable(
    route = route
){ navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("id")
        ?: throw IllegalArgumentException("id is required")

    val breedsDetailsViewModel = viewModel<BreedsDetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
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
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    state.data?.let {
                        Text(
                            text = it.name,
                            style = TextStyle(
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightOrange
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.error) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Something went wrong while fetching the data")
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
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState),
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.CenterHorizontally),
            model = image.url,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
        Divider(thickness = 1.dp, color = Orange)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Description",
            style = TextStyle(
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = data.description,
            style = TextStyle(
                fontSize = 18.sp,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider(thickness = 1.dp, color = Orange)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            text = "Origin: " + data.origin,
            style = TextStyle(
                fontSize = 18.sp
            )
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            style = TextStyle(
                fontSize = 18.sp
            ),
            text = "Temperament: " + data.temperament,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            style = TextStyle(
                fontSize = 18.sp
            ),
            text = "Life Span: " + data.lifeSpan,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            style = TextStyle(
                fontSize = 18.sp
            ),
            text = "Weight(Metrics): " + data.weight,
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            style = TextStyle(
                fontSize = 18.sp
            ),
            text = if (data.rare == 1) "Rare Breed" else "Not Rare Breed",
        )

        Spacer(modifier = Modifier.height(8.dp))
        Divider(thickness = 1.dp, color = Orange)
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DetailsWidget(
                number = data.adaptability,
                trait = "Adaptability"
            )
            DetailsWidget(
                number = data.affectionLevel,
                trait = "Affection"
            )
            DetailsWidget(
                number = data.intelligence,
                trait = "Intelligence"
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DetailsWidget(
                number = data.childFriendly,
                trait = "Child Friendly"
            )
            DetailsWidget(
                number = data.socialNeeds,
                trait = "Social Needs"
            )
        }

        Spacer(modifier = Modifier.height(25.dp))
        val openWikipedia = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        }
        FilledIconButton(
            onClick = {
                val wikipediaUrl = data.wikipediaURL
                if (wikipediaUrl.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl))
                    openWikipedia.launch(intent)
                }
            },
            modifier = Modifier
                .height(55.dp)
                .width(250.dp)
                .align(Alignment.CenterHorizontally),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Orange,
                contentColor = Color.White,
            )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                ),
                text = "WIKIPEDIA",
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Preview(heightDp = 1100)
@Composable
fun PreviewDetailsScreen() {
    Surface {
        BreedsDetailsScreen(
            state = BreedsDetailsState(
                breedId = "1",
                data = BreedsUiModel(
                    id="2",
                    name="Macka2",
                    description = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium.",
                    alternativeName = "macka",
                    temperament = "dasfsd,sdafsa,sadfsa",
                    origin = "Serbia",
                    lifeSpan = "10",
                    weight = "10",
                    wikipediaURL = "http://wikipedia.com",
                    rare = 1,
                    adaptability = 1,
                    affectionLevel = 2,
                    referenceImageId = "0XYvRd7oD",
                    socialNeeds = 3,
                    childFriendly = 5,
                    intelligence = 5
                ),
                image = ImageUiModel(
                    id = "1",
                    url = "https://cdn.thedogapi.com/images/Hylo4Snaf.jpeg"
                )
            ),
            onClose = {}
        )
    }
}