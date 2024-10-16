package com.dapps.dauvi.feature_settings.presentation

import android.Manifest
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dapps.dauvi.R
import com.dapps.dauvi.core.presentation.screens.Screens
import com.dapps.dauvi.feature_uvi.presentation.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    var loadingLocation by rememberSaveable { mutableStateOf(false) }

    if (locationPermissionState.shouldShowRationale) {
        Text(
            text = stringResource(R.string.location_permission_required),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    LaunchedEffect(locationPermissionState) {
        snapshotFlow { locationPermissionState.allPermissionsGranted }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.handleIntent(SettingsIntent.RequestLocation)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.OpenUrl -> {
                    uriHandler.openUri(event.url)
                }
                is SettingsEvent.NavigateToTerms -> {
                    navController.navigate(Screens.TermsScreen.route)
                }
                is SettingsEvent.RequestLocationPermission -> {
                    locationPermissionState.launchMultiplePermissionRequest()
                }
                is SettingsEvent.LocationRequestLoading -> {
                    loadingLocation = true
                }
                is SettingsEvent.LocationRequestCompleted -> {
                    loadingLocation = false
                }
                is SettingsEvent.LocationRequestFailed -> {
                    loadingLocation = false
                    Toast.makeText(context,
                        context.getString(R.string.unable_to_fetch_location), Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
    ) {
        SettingsSection(
            title = stringResource(id = R.string.settings_location_title),
            description = state.location?.let {
                stringResource(
                    id = R.string.settings_location_description,
                    if(it.city.isNullOrEmpty()) R.string.unknown_city else it.city,
                    if(it.country.isNullOrEmpty()) R.string.unknown_country else it.country
                )
            } ?: stringResource(id = R.string.settings_location_required_description),
            settings = {
                Button(
                    enabled = !loadingLocation,
                    onClick = {
                        if(!locationPermissionState.allPermissionsGranted){
                            viewModel.handleIntent(SettingsIntent.RequestLocationPermission)
                        }
                        else {
                            viewModel.handleIntent(SettingsIntent.RequestLocation)
                        }
                    }
                ) {
                    Text(stringResource(id = R.string.settings_location_button))
                }
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        SettingsSection(
            title = stringResource(id = R.string.settings_high_uvi_title),
            description = stringResource(
                id = R.string.settings_high_uvi_description,
                Constants.DEFAULT_HIGH_UVI
            ),
            settings = {
                SettingsRow(
                    label = stringResource(id = R.string.settings_high_uvi_label),
                    setting = {
                        BasicNumberPicker(
                            items = (1..15).toList(),
                            onItemClicked = { value ->
                                viewModel.handleIntent(SettingsIntent.SetHighUviValue(value))
                            },
                            value = state.highUviValue,
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        SettingsSection(
            title = stringResource(id = R.string.settings_daily_uvi_title),
            description = stringResource(id = R.string.settings_daily_uvi_description),
            settings = {
                SettingsRow(
                    label = if(state.dailyUviAlertOn) stringResource(id = R.string.settings_daily_uvi_on)
                            else stringResource(id = R.string.settings_daily_uvi_off),
                    setting = {
                        Switch(
                            checked = state.dailyUviAlertOn,
                            onCheckedChange = { isChecked ->
                                viewModel.handleIntent(SettingsIntent.SetDailyUviAlertOn(isChecked))
                            }
                        )
                    }
                )
                AnimatedVisibility(state.dailyUviAlertOn) {
                    SettingsRow(
                        label = stringResource(id = R.string.settings_daily_uvi_alert_time),
                        setting = {
                            var showTimePicker by rememberSaveable { mutableStateOf(false) }

                            val formattedTime by remember(state.dailyUviAlertTimeValue) {
                                derivedStateOf {
                                    getFormattedTime(
                                        state.dailyUviAlertTimeValue.hour,
                                        state.dailyUviAlertTimeValue.minute
                                    )
                                }
                            }

                            ClickableValueBox(
                                onClick = { showTimePicker = true },
                                modifier = Modifier.weight(0.5f),
                                value = formattedTime,
                            )
                            if (showTimePicker) {
                                TimePickerDialog(
                                    value = state.dailyUviAlertTimeValue,
                                    onDismiss = { showTimePicker = false },
                                    onConfirm = { timeValue ->
                                        viewModel.handleIntent(
                                            SettingsIntent.SetDailyUviAlertTimeValue(
                                                timeValue.hour,
                                                timeValue.minute
                                            )
                                        )
                                        showTimePicker = false
                                    },
                                )
                            }
                        }
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        SettingsSection(
            title = stringResource(id = R.string.settings_uvi_alert_title),
            description = stringResource(id = R.string.settings_uvi_alert_description),
            settings = {
                SettingsRow(
                    label = if(state.highUviAlertOn) stringResource(id = R.string.settings_uvi_alert_on)
                            else stringResource(id = R.string.settings_uvi_alert_off),
                    setting = {
                        Switch(
                            checked = state.highUviAlertOn,
                            onCheckedChange = { isChecked ->
                                viewModel.handleIntent(SettingsIntent.SetHighUviAlertOn(isChecked))
                            }
                        )
                    }
                )
                AnimatedVisibility(state.highUviAlertOn) {
                    SettingsRow(
                        label = stringResource(id = R.string.settings_uvi_alert_min_level),
                        setting = {
                            BasicNumberPicker(
                                items = (1..15).toList(),
                                onItemClicked = { value ->
                                    viewModel.handleIntent(SettingsIntent.SetHighUviAlertValue(value))
                                },
                                value = state.highUviAlertValue,
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                    )
                }
            }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 64.dp))

        SettingsLink(
            label = stringResource(id = R.string.settings_weather_data_provider),
            onClick = {
                viewModel.handleIntent(SettingsIntent.OpenUrl(Constants.WEATHER_PROVIDER_URI))
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        SettingsLink(
            label = stringResource(id = R.string.settings_what_is_uvi),
            onClick = {
                viewModel.handleIntent(SettingsIntent.OpenUrl(Constants.UVI_INFO_URI))
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        SettingsLink(
            label = stringResource(id = R.string.settings_terms_conditions_disclaimer),
            onClick = {
                viewModel.handleIntent(SettingsIntent.NavigateToTerms)
            }
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

fun getFormattedTime(hour: Int, minute: Int): String {
    val hourString = hour.toString().padStart(2, '0')
    val minuteString = minute.toString().padStart(2, '0')
    return "$hourString:$minuteString"
}

@Composable
fun ClickableValueBox(
    onClick: () -> Unit = {},
    value: String,
    modifier: Modifier = Modifier,
){
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = value,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BasicNumberPicker(
    items: List<Int>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    value: Int?,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        ClickableValueBox(
            value = value?.toString() ?: "",
//            onClick = { expanded = !expanded },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth()
        )
//        TextField(
//            value = "${ value ?: "" }",
//            onValueChange = {},
//            readOnly = true,
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable)
//        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = "$item") },
                    onClick = { onItemClicked(item); expanded = false }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    settings: @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(4.dp))
        if(description != null) Text(
            text = description,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Column(modifier = Modifier.padding(top = 16.dp)) {
            settings()
        }
    }
}

@Composable
private fun SettingsRow(label: String, setting: @Composable () -> Unit = {}) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            modifier = Modifier.weight(1f)
        )
        setting()
    }
}

@Composable
fun SettingsLink(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Row(modifier = modifier.clickable { onClick() }) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next",
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    value: TimePickerState,
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit = {},
) {
    val timePickerState = rememberTimePickerState(
        initialHour = value.hour,
        initialMinute = value.minute,
        is24Hour = value.is24hour,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.time_picker_dismiss))
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(timePickerState) }) {
                Text(stringResource(id = R.string.time_picker_confirm))
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
            )
        }
    )
}
