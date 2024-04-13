package raf.rma.catalist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import raf.rma.catalist.navigation.AppNavigation
import raf.rma.catalist.core.theme.CatalistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalistTheme {
                AppNavigation()
            }
        }
    }
}