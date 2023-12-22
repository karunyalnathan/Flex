package com.example.flex.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.flex.ExerciseTypes
import com.example.flex.FlexApplication
import com.example.flex.R
import com.example.flex.data.exercise.ExerciseLog
import com.example.flex.domain.ExerciseSet
import com.example.flex.ui.theme.LabelValueCell
import com.example.flex.ui.theme.Blue100
import com.example.flex.ui.theme.FlexButton
import com.example.flex.ui.theme.FlexButtonColors
import com.example.flex.ui.theme.FlexOutlinedButton
import com.example.flex.ui.theme.FlexTextFieldColors
import com.example.flex.ui.theme.Navy50
import com.example.flex.ui.theme.Navy500
import com.example.flex.ui.theme.banner
import com.example.flex.ui.theme.bannerBold
import com.example.flex.ui.theme.bannerSmall
import com.example.flex.ui.theme.bodyLabel
import com.example.flex.ui.theme.bodyValue
import com.example.flex.ui.theme.cell
import com.example.flex.ui.theme.clock
import com.example.flex.ui.theme.completedExercise
import com.example.flex.ui.theme.currentExercise
import com.example.flex.ui.theme.screenHeading
import com.example.flex.ui.theme.upcomingExercise
import com.example.flex.ui.theme.upcomingRest
import com.example.flex.ui.theme.workoutRow
import com.example.flex.ui.viewmodels.ExerciseInfoUIState
import com.example.flex.ui.viewmodels.ExerciseLogUIState
import com.example.flex.ui.viewmodels.ExerciseViewModel
import com.example.flex.ui.viewmodels.ExerciseViewModelFactory
import com.example.flex.ui.viewmodels.WorkoutUIState
import com.example.flex.ui.viewmodels.WorkoutViewModel
import com.example.flex.ui.viewmodels.WorkoutViewModelFactory
import java.util.concurrent.TimeUnit


class WorkoutFragment : Fragment() {
    private val viewModel: WorkoutViewModel by viewModels {
        WorkoutViewModelFactory(
            (requireActivity().application as FlexApplication).activeWorkoutUseCase,
            (requireActivity().application as FlexApplication).workoutPlanRepository,
            requireActivity().application as FlexApplication
        )
    }
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModelFactory((requireActivity().application as FlexApplication).exerciseRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!viewModel.isActive) arguments?.getString("WORKOUT")?.let { viewModel.setWorkout(it) }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WorkoutScreen()
                setOnBackPressedCallback()
            }
        }
    }

    private fun setOnBackPressedCallback() {
        val backPressedCallback = object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isActive) activity?.let {
                    ExitWorkoutDialogFragment { isEnabled = false }.show(
                        it.supportFragmentManager,
                        "EXIT_DIALOG"
                    )
                }
                else {
                    isEnabled = false
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, backPressedCallback
        )

    }

    @Composable
    fun WorkoutScreen() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        WorkoutScreen(uiState)
    }

    @Composable
    fun WorkoutScreen(uiState: WorkoutUIState) {
        when (uiState) {
            WorkoutUIState.Loading -> {}
            is WorkoutUIState.Success -> {
                WorkoutScreen(uiState.exercises)
            }

            is WorkoutUIState.Active -> {
                ActiveWorkoutScreen(uiState.sets)
            }

            WorkoutUIState.ActivityTimer -> ActivityDurationScreen()

            WorkoutUIState.Completed -> CompletedWorkout()

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WorkoutScreen(exercises: List<ExerciseSet>) {
        var isSheetOpen by rememberSaveable {
            mutableStateOf(false)
        }
        if (isSheetOpen) {
            ModalBottomSheet(onDismissRequest = { isSheetOpen = false }) {
                ExerciseBottomSheet(null)
            }
        }

        Column {
            Column(Modifier.cell()) {
                Text(text = viewModel.workoutName, style = screenHeading)
                FlexButton(onClick = { viewModel.startWorkout() }) {
                    Text(text = stringResource(id = R.string.start_workout))
                }
            }

            LazyColumn {
                items(exercises) {

                    ExerciseSummaryRow(it,
                        Modifier
                            .clickable {
                                exerciseViewModel.update(it.name)
                                isSheetOpen = true
                            }
                            .workoutRow())

                }
            }
        }
    }

    @Composable
    fun ExerciseSummaryRow(exercise: ExerciseSet, modifier: Modifier) {
        Row(modifier.cell(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = exercise.name, style = MaterialTheme.typography.bodyLarge)
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                ExerciseSummaryRowCellEnd(text = stringResource(id = R.string.sets_colon) + exercise.totalSets.toString())
                ExerciseSummaryRowCellEnd(stringResource(id = R.string.reps_colon) + exercise.reps.toString())
            }
        }
    }

    @Composable
    fun ExerciseSummaryRowCellEnd(text: String) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium
        )

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ActiveWorkoutScreen(sets: List<ExerciseSet>) {

        var isSheetOpen by rememberSaveable {
            mutableStateOf(true)
        }

        val currentSet by viewModel.currentSet.collectAsStateWithLifecycle()

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                ExerciseBottomSheet(currentSet?.second)
            }
        }
        Column {
            ActiveWorkoutHeader(Modifier.cell())
            LazyColumn {
                itemsIndexed(sets) { index, it ->
                    Row(Modifier.clickable {
                        if (index != viewModel.currentSet.value?.first) {
                            viewModel.updateCurrentExercise(index, it)
                            exerciseViewModel.update(it.name)
                        }
                        isSheetOpen = true
                    }) {
                        currentSet?.first?.let { currentIndex ->
                            if (index < currentIndex) {
                                ExerciseRowCompleted(it)
                            } else if (index == currentIndex) {
                                ExerciseRowCurrent(it)
                            } else {
                                ExerciseRowUncompleted(it)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ActiveWorkoutHeader(modifier: Modifier = Modifier) {
        Column(modifier) {
            Text(text = viewModel.workoutName, style = screenHeading)
            Row(
                modifier = Modifier.padding(0.dp, 12.dp, 24.dp, 0.dp),
                horizontalArrangement = Arrangement.End
            ) {
                FlexOutlinedButton(
                    modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp),
                    onClick = { viewModel.endWorkout() }) {
                    Text(text = stringResource(id = R.string.end_workout))
                }

                FlexButton(onClick = { completeWorkout() }) {
                    Text(text = stringResource(id = R.string.complete_workout))
                }
            }
        }
    }

    @Composable
    fun ExerciseRowCompleted(set: ExerciseSet) {
        Row(Modifier.background(Navy50)) {
            ActiveExerciseRow(Modifier.completedExercise(), set)
        }
    }

    @Composable
    fun ExerciseRowCurrent(set: ExerciseSet) {
        ActiveExerciseRow(Modifier.currentExercise(), set)
    }

    @Composable
    fun ExerciseRowUncompleted(set: ExerciseSet) {
        if (set.isRest) ActiveExerciseRow(Modifier.upcomingRest(), set)
        else ActiveExerciseRow(Modifier.upcomingExercise(), set)
    }

    @Composable
    fun ActiveExerciseRow(modifier: Modifier, set: ExerciseSet) {
        Row(modifier = modifier.cell(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = set.name, style = MaterialTheme.typography.bodyLarge)
            if (set.isTime) {
                Text(
                    text = set.reps.toString() + " " + stringResource(id = R.string.seconds),
                    style = MaterialTheme.typography.labelLarge
                )
            } else {
                Text(
                    text = set.reps.toString() + " " + stringResource(id = R.string.reps),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    @Composable
    fun ExerciseBottomSheet(currentSet: ExerciseSet?) {
        val uiState by exerciseViewModel.exerciseLog.collectAsStateWithLifecycle()
        currentSet?.name?.let { exerciseViewModel.update(it) }
        ExerciseBottomSheet(uiState, currentSet)
    }

    @Composable
    fun ExerciseBottomSheet(
        uiState: ExerciseLogUIState,
        currentSet: ExerciseSet?
    ) {
        Column(Modifier.padding(0.dp, 0.dp, 0.dp, 70.dp)) {
            when (uiState) {
                ExerciseLogUIState.Loading -> {
                    Text(stringResource(id = R.string.loading))
                }

                is ExerciseLogUIState.Success -> {
                    uiState.exercise?.let {
                        if (it.isTime) ExerciseBottomSheet(it, ExerciseTypes.Timed, currentSet)
                        else ExerciseBottomSheet(it, ExerciseTypes.Reps, currentSet)
                    }
                }

                ExerciseLogUIState.Rest -> ExerciseBottomSheet(null, ExerciseTypes.Rest, currentSet)

            }
        }
    }

    @Composable
    fun ExerciseBottomSheet(exercise: ExerciseLog?, type: ExerciseTypes, currentSet: ExerciseSet?) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            ExerciseView(exercise, type, currentSet)
        }
    }

    @Composable
    fun ActiveWorkoutNavButtons(
        keyboardController: SoftwareKeyboardController?,
        setInfo: @Composable () -> Unit
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp, 12.dp, 0.dp, 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (viewModel.isActive) FlexOutlinedButton(onClick = {
                keyboardController?.hide()
                viewModel.previousSet()
            }) { Text(stringResource(id = R.string.back)) }
            setInfo()
            if (viewModel.isActive) FlexOutlinedButton(onClick = {
                keyboardController?.hide()
                viewModel.nextSet().let {
                    if (!it) completeWorkout()
                }
            }) { Text(stringResource(id = R.string.next)) }
        }
    }

    @Composable
    fun ExerciseView(exercise: ExerciseLog?, type: ExerciseTypes, currentSet: ExerciseSet?) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(Modifier.cell()) {
            when (type) {
                ExerciseTypes.Reps -> {
                    exercise?.apply {
                        ExerciseTitle(exercise.name)
                        ActiveWorkoutNavButtons(keyboardController) {
                            Column(Modifier.semantics(mergeDescendants = true) {}) {
                                SetsLabel(
                                    currentSet?.thisSet.toString(),
                                    exercise.sets.toString()
                                )
                                LabelValueCell(
                                    stringResource(id = R.string.reps_colon),
                                    exercise.reps.toString()
                                )
                            }
                        }
                        ExerciseWeightAndDetails(exercise, keyboardController)
                    }
                }

                ExerciseTypes.Timed -> {
                    exercise?.apply {
                        ExerciseTitle(exercise.name)
                        ActiveWorkoutNavButtons(keyboardController) {
                            if (viewModel.isActive) StartExerciseDuration(
                                currentSet?.reps?.toLong() ?: 0, TimeUnit.SECONDS
                            )
                            else ExerciseDurationRow(exercise.reps.toString(), TimeUnit.SECONDS)
                        }
                        ExerciseWeightAndDetails(exercise, keyboardController)
                    }
                }

                ExerciseTypes.Rest -> {
                    ExerciseTitle(currentSet?.name ?: stringResource(id = R.string.rest))
                    ActiveWorkoutNavButtons(keyboardController) {
                        StartExerciseDuration(currentSet?.reps?.toLong() ?: 30, TimeUnit.SECONDS)
                    }
                }
            }
        }
    }


    @Composable
    fun ExerciseTitle(name: String) {
        Text(text = name, style = bannerSmall)
    }

    @Composable
    fun SetsLabel(currentSet: String, totalSets: String) {
        if (viewModel.isActive) {
            LabelValueCell(
                stringResource(id = R.string.sets_colon),
                currentSet + " " + stringResource(id = R.string.of) + " " + totalSets
            )
        } else {
            LabelValueCell(stringResource(id = R.string.sets_colon), totalSets)
        }
    }

    @Composable
    fun ExerciseWeightAndDetails(
        exercise: ExerciseLog,
        keyboardController: SoftwareKeyboardController?
    ) {
        if (!viewModel.isActive) WeightLabel(exercise.weight)
        else WeightTextField(exercise, keyboardController)
        ExerciseDetails()
    }

    @Composable
    fun WeightLabel(weight: Int) {
        LabelValueCell(
            stringResource(id = R.string.last_weight_used_colon),
            weight.toString() + " " + stringResource(id = R.string.lb),
        )
    }

    @Composable
    fun WeightTextField(exercise: ExerciseLog, keyboardController: SoftwareKeyboardController?) {
        var weight by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue((exercise.weight).toString(), TextRange(0, 3)))
        }
        var saveEnabled by rememberSaveable { mutableStateOf(false) }

        Row(verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it
                    saveEnabled = true
                },
                label = { Text(stringResource(id = R.string.weight_used)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = FlexTextFieldColors()
            )
            Button(
                modifier = Modifier.padding(3.dp, 0.dp, 0.dp, 0.dp),
                onClick = {
                    keyboardController?.hide()
                    if (weight.text.isNotEmpty()) exerciseViewModel.updateWeightUsed(
                        weight.text.toInt(),
                        exercise
                    )
                    saveEnabled = false
                }, enabled = saveEnabled,
                colors = FlexButtonColors(),
                contentPadding = PaddingValues(
                    start = 3.dp,
                    top = 3.dp,
                    end = 3.dp,
                    bottom = 3.dp
                )
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }

    @Composable
    fun StartExerciseDuration(time: Long, units: TimeUnit, modifier: Modifier = Modifier) {
        if (viewModel.isActive) {
            when (units) {
                TimeUnit.SECONDS -> {
                    viewModel.startTimer(time, 1)
                }

                TimeUnit.MINUTES -> {
                    viewModel.startTimer(time, 60)
                }

                else -> {}
            }
        }

        ExerciseDuration(units, modifier)
    }

    @Composable
    fun ExerciseDuration(units: TimeUnit, modifier: Modifier = Modifier) {
        val timer by viewModel.timer.collectAsStateWithLifecycle()
        when (units) {
            TimeUnit.SECONDS -> {
                timer.first?.let {
                    ExerciseDurationRow(it.toString(), units, modifier)
                }
            }

            TimeUnit.MINUTES -> {
                var time: String? = null
                timer.first?.let { first ->
                    time = first.toString()
                    timer.second?.let { second ->
                        time += ":$second"
                    }
                }
                time?.let { ExerciseDurationRow(it, units, modifier) }
            }

            else -> {}
        }

    }

    @Composable
    fun ExerciseDurationRow(time: String, units: TimeUnit, modifier: Modifier = Modifier) {
        Row(
            modifier.padding(5.dp, 5.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {

            when (units) {
                TimeUnit.SECONDS -> {
                    Text(text = time, style = clock)
                    Text(text = " " + stringResource(id = R.string.sec))
                }

                TimeUnit.MINUTES -> {
                    Text(text = time, style = clock)
                }

                else -> {
                    Text(text = time, style = clock)
                }
            }
        }
    }

    @Composable
    fun ExerciseDetails() {
        val uiState by exerciseViewModel.exerciseInfo.collectAsStateWithLifecycle()
        when (uiState) {
            ExerciseInfoUIState.Loading -> {} // view gone when no network
            is ExerciseInfoUIState.Success -> {
                (uiState as ExerciseInfoUIState.Success).exerciseInfo?.let {
                    Column(Modifier.padding(0.dp, 12.dp)) {
                        it.equipment.let {
                            if (it == "body_only") LabelValueCell(
                                stringResource(id = R.string.equipment_colon) + " ",
                                stringResource(id = R.string.body_only)
                            )
                            else LabelValueCell(
                                stringResource(id = R.string.equipment_colon) + " ",
                                it
                            )
                        }
                        LabelValueCell(
                            stringResource(id = R.string.target_muscle_colon) + " ",
                            it.muscle
                        )
                        Text(
                            text = stringResource(id = R.string.exercise_description_colon),
                            style = bodyLabel
                        )
                        Text(text = it.instructions, style = bodyValue)
                    }
                }
            }
        }

    }

    @Composable
    fun ActivityDurationScreen() {
        Column(Modifier.cell()) {
            ActiveWorkoutHeader()
            StartExerciseDuration(30, TimeUnit.MINUTES, Modifier.fillMaxWidth())
        }

    }

    @Preview
    @Composable
    fun CompletedWorkout() {
        Column(Modifier.cell()) {
            Text(stringResource(R.string.congrats), style = bannerBold, color = Blue100)
            Text(
                stringResource(R.string.completed_message) + " ${viewModel.workoutName}!",
                style = banner,
                color = Navy500
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                FlexButton(onClick = { activity?.onBackPressedDispatcher?.onBackPressed() }) {
                    Text(text = stringResource(R.string.return_home))

                }
            }

        }
    }

    private fun completeWorkout() {
        viewModel.completeWorkout()

    }

    class ExitWorkoutDialogFragment(val negativeButtonListener: () -> Unit) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage(getString(R.string.end_workout_sure))
                    .setPositiveButton(getString(R.string.no)) { _, _ ->
                    }
                    .setNegativeButton(getString(R.string.end)) { _, _ ->
                        negativeButtonListener()
                        it.onBackPressedDispatcher.onBackPressed()
                    }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }

        override fun onStart() {
            super.onStart()
            (dialog as AlertDialog?)!!.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.blue_700, activity?.theme))

            (dialog as AlertDialog?)!!.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.blue_700, activity?.theme))
        }
    }

}