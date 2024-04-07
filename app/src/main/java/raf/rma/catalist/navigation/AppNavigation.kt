package raf.rma.catalist.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import raf.rma.catalist.breeds.details.breedsDetails
import raf.rma.catalist.breeds.list.breedsListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "breeds"
    ){
        breedsListScreen(
            route = "breeds",
            navController = navController
        )

        breedsDetails(
            route = "breeds/{id}",
            navController = navController
        )
    }
}