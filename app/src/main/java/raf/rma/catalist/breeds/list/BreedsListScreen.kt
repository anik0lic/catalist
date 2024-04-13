package raf.rma.catalist.breeds.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import raf.rma.catalist.R
import raf.rma.catalist.breeds.list.BreedsListContract.BreedsListState
import raf.rma.catalist.breeds.list.BreedsListContract.BreedsListUiEvent
import raf.rma.catalist.breeds.model.BreedsUiModel
import raf.rma.catalist.breeds.repository.SampleData
import raf.rma.catalist.core.compose.SearchBar
import raf.rma.catalist.core.theme.CatalistTheme
import raf.rma.catalist.core.theme.LightOrange
import raf.rma.catalist.core.theme.Orange

@ExperimentalMaterial3Api
fun NavGraphBuilder.breedsListScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val breedsListViewModel = viewModel<BreedsListViewModel>()
    val state by breedsListViewModel.state.collectAsState()

    BreedsListScreen(
        state = state,
        eventPublisher = {
            breedsListViewModel.setEvent(it)
        },
        onItemClick = {
            navController.navigate(route = "breeds/${it.id}")
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun BreedsListScreen(
    state: BreedsListState,
    eventPublisher: (uiEvent: BreedsListUiEvent) -> Unit,
    onItemClick: (BreedsUiModel) -> Unit
) {
    Scaffold (
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Cat Breeds",
                            style = TextStyle(
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    navigationIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.cat_logo),
                            contentDescription = "logo",
                            modifier = Modifier
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = LightOrange
                    )
                )
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightOrange)
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    onQueryChange = { query ->
                        eventPublisher(
                            BreedsListUiEvent.SearchQueryChanged(
                                query = query
                            )
                        )
                    },
                    onCloseClicked = { eventPublisher(BreedsListUiEvent.CloseSearchMode) },
                    query = state.query,
                    activated = state.isSearchMode
                )
            }
        },
        content = {
            BreedsList(
                paddingValues = it,
                items = if (state.isSearchMode) state.filteredBreeds else state.breeds,
                onItemClick = onItemClick
            )

            if (state.breeds.isEmpty()) {
                when (state.loading) {
                    true -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    false -> {
                        if (state.error) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "Something went wrong while fetching the data")
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = "No breeds.")
                            }
                        }
                    }
                }
            }
            if(state.isSearchMode && state.filteredBreeds.isEmpty()){
                if (!state.loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "No breeds with that name.")
                    }
                }
            }

            if(state.loading){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    )
}

@Composable
private fun BreedsList(
    items: List<BreedsUiModel>,
    paddingValues: PaddingValues,
    onItemClick: (BreedsUiModel) -> Unit
){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        items.forEach {
            Column {
                key(it.id) {
                    BreedsListItem(
                        data = it,
                        onClick = {
                            onItemClick(it)
                        },
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}

@Composable
private fun BreedsListItem(
    data: BreedsUiModel,
    onClick: () -> Unit
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Orange),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = if (data.alternativeName.isNotEmpty()) data.name + " (" + data.alternativeName + ")" else data.name,
                style = TextStyle(
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.description.take(250).plus("..."),
                style = TextStyle(
                    fontSize = 18.sp,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            val threeTemperaments = data.temperament.split(",")
            Text(
                text = "Temperament: " + threeTemperaments.shuffled().take(3).joinToString(", "),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewBreedsListScreen() {
    CatalistTheme {
        BreedsListScreen(
            state = BreedsListState(breeds = SampleData),
            eventPublisher = {},
            onItemClick = {}
        )
    }
}
