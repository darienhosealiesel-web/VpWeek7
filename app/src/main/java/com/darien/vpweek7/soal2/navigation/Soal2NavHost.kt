package com.darien.vpweek7.soal2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.darien.vpweek7.soal2.ui.screen.AlbumDetailScreen
import com.darien.vpweek7.soal2.ui.screen.ArtistScreen

@Composable
fun Soal2AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ArtistRoute) {
        composable<ArtistRoute> {
            ArtistScreen(
                onAlbumClick = { albumId -> navController.navigate(AlbumDetailRoute(albumId)) },
            )
        }
        composable<AlbumDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AlbumDetailRoute>()
            AlbumDetailScreen(
                albumId = route.albumId,
                onBack = { navController.navigateUp() },
            )
        }
    }
}