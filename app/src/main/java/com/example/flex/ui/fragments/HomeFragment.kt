package com.example.flex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import com.example.flex.FlexApplication
import com.example.flex.Phase
import com.example.flex.R
import com.example.flex.data.workoutplan.WorkoutPlan
import com.example.flex.ui.theme.FlexButton
import com.example.flex.ui.theme.FlexRadioButtonColors
import com.example.flex.ui.theme.cell
import com.example.flex.ui.theme.FlexSegmentedButtonColors
import com.example.flex.ui.theme.Navy500
import com.example.flex.ui.theme.bannerBold
import com.example.flex.ui.theme.bannerCaption
import com.example.flex.ui.theme.bannerSmall
import com.example.flex.ui.theme.caption
import com.example.flex.ui.theme.instruction
import com.example.flex.ui.theme.rowItem
import com.example.flex.ui.theme.screenHeading
import com.example.flex.ui.theme.subHeading
import com.example.flex.ui.theme.workoutRow
import com.example.flex.ui.viewmodels.HomeUIState
import com.example.flex.ui.viewmodels.HomeViewModel
import com.example.flex.ui.viewmodels.HomeViewModelFactory

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (requireActivity().application as FlexApplication).workoutPlanRepository,
            (requireActivity().application as FlexApplication)
        )
    }

    @ExperimentalMaterial3Api
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeScreen()
            }
        }
    }

    @Composable
    @ExperimentalMaterial3Api
    fun HomeScreen() {
        val phase by viewModel.phase.collectAsStateWithLifecycle()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        if (phase.isNotEmpty()) {
            HomeScreen(uiState)
        } else {
            SettingsView()
        }
    }

    @Composable
    @ExperimentalMaterial3Api
    private fun HomeScreen(uiState: HomeUIState) {
        when (uiState) {
            HomeUIState.Loading -> {
                Row(Modifier.cell()) { Text(text = stringResource(id = R.string.loading)) }
            }

            is HomeUIState.Success -> {
                Column {
                    Row(Modifier.cell()) {
                        Text(
                            text = stringResource(id = R.string.your_cycle_synced_workout_plan),
                            style = screenHeading
                        )
                    }
                    PhaseSelector()
                    Row(Modifier.cell()) {
                        Text(
                            text = stringResource(id = R.string.your_workouts_for_this_phase),
                            style = subHeading
                        )
                    }
                    PopulatePlan(uiState.workoutPlan)
                }
            }
        }
    }

    @Composable
    @ExperimentalMaterial3Api
    private fun PhaseSelector() {
        val phase: List<String> = viewModel.getPhases()

        MultiChoiceSegmentedButtonRow(
            Modifier
                .fillMaxWidth()
                .padding(2.dp, 12.dp)
        ) {
            phase.forEachIndexed { index, label ->

                SegmentedButton(

                    checked = label == viewModel.phase.value,

                    onCheckedChange = {
                        if (it) {
                            viewModel.updatePhase(label)
                            (requireActivity().application as FlexApplication).setPhase(phase = label)
                        }
                    }, shape = SegmentedButtonDefaults.itemShape(index = index, count = phase.size),
                    colors = FlexSegmentedButtonColors()
                ) {
                    Text(text = label)
                }
            }
        }
    }


    @Composable
    fun PopulatePlan(workoutPlan: WorkoutPlan?) {
        workoutPlan?.let {
            Column {
                LazyColumn {
                    itemsIndexed(it.workouts) { i, workout -> //index items
                        WorkoutRow(workout, it.completed.getOrNull(i) ?: false)
                    }
                    item {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 20.dp), horizontalAlignment = Alignment.End
                        ) {
                            FlexButton(onClick = { viewModel.resetPhase(viewModel.phase.value) }) {
                                Text(text = stringResource(id = R.string.reset_phase))
                            }
                            Text(
                                text = stringResource(id = R.string.reset_phase_description),
                                textAlign = TextAlign.End,
                                style = caption
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun WorkoutRow(workout: String, isCompleted: Boolean) {
        Column(Modifier.workoutRow()) {
            Row(
                Modifier
                    .cell()
                    .clickable {
                        view
                            ?.findNavController()
                            ?.navigate(R.id.action_homeFragment_to_workoutFragment,
                                Bundle().apply { putString("WORKOUT", workout) })
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = workout, style = rowItem)

                if (isCompleted)
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(id = R.string.completed),
                        tint = Navy500
                    )
            }
        }
    }


    @Composable
    fun SettingsView() {
        Column(Modifier.cell()) {
            Text(text = stringResource(id = R.string.welcome), style = bannerBold)
            Text(text = stringResource(id = R.string.welcome_what_we_are), style = bannerSmall)
            Row(Modifier.padding(0.dp, 6.dp)) {
                Text(
                    text = stringResource(id = R.string.welcome_what_we_do),
                    style = bannerCaption,
                    color = Color.Gray
                )
            }
            Row(Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp)) {
                Text(text = stringResource(id = R.string.select_phase), style = instruction)
            }
            val phases = Phase.values().toList()

            val (selectedPhase, onOptionSelected) = rememberSaveable { mutableStateOf(phases[0]) }

            Column(Modifier.selectableGroup()) {
                phases.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedPhase),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedPhase),
                            onClick = null,
                            colors = FlexRadioButtonColors()
                        )

                        Text(
                            text = text.toString(),
                            style = rowItem,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color.Black
                        )
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                FlexButton(onClick = {
                    (requireActivity().application as FlexApplication).setPhase(phase = selectedPhase.name)
                    viewModel.updatePhase(selectedPhase.name)
                }) {
                    Text(stringResource(id = R.string.save))
                }
            }
        }
    }

}