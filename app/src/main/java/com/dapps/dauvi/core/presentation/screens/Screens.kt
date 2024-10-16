package com.dapps.dauvi.core.presentation.screens

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens(val route: String) {

    @Serializable
    data object UviScreen : Screens("uvi_screen")

    @Serializable
    data object SettingsScreen : Screens("settings_screen")

    @Serializable
    data object TermsScreen : Screens("terms_screen")

}