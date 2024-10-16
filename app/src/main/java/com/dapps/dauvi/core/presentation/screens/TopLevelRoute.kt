package com.dapps.dauvi.core.presentation.screens

import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Any>(val name: String, val screen: T, val icon: ImageVector)