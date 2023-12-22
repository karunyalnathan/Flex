# METCS683Assignments
Karunya Nathan


Flex helps women cycle sync their workouts. Cycle syncing is when a woman adjusts her lifestyle—diet, workouts, workflow etc.—to work synergistically with her monthly hormonal fluctuations. The Flex app provides women with a workout schedule based on the various phases of a woman’s menstrual cycle. Flex also allows users to view and follow rep and set-based workouts, log weight used, and track their progress. 

The list of workouts in a given phase can be found on the home page of the application. A user can then select a workout and start a workout. The app will then take the user through each exercise in the workout, allowing the user to enter the weight used. The weight used for a given exercise is recorded, encouraging progressive overload.





WorkoutPlan Database:
Holds four WorkoutPlans, one for each phase of the cycle. A WorkoutPlan object holds the phase name, a list of workout names, and a list of booleans which indicate if the corresponding workout has been completed. 

Workout Database:
Holds a list of Workouts, each containing a name, a list of exercise names, and a type. Note: the type field has been left for future work and is not currently used. 

Exercise Database:
Holds of list of ExerciseLogs, each with a name, a isTime boolean, and four integer values recording rest, sets, reps, and weight. Note: if the isTime boolean is true, reps is interpreted as a time interval in seconds 

Exercise Info API: 
Makes a call to an external API, hosted by API Ninjas (https://api.api-ninjas.com/v1/exercises?name=), to retrieve more details for a given exercise logged in the Exercise Database
WorkoutPlan Repository: Accesses the WorkoutPlan Database

Workout Repository: Accesses the Workout Database

Exercise Repository: Accesses the ExerciseLog Database and makes a call using Exercise Info API 

Active Workout UseCase: The invoke function gets a specified workout from the WorkoutRepository, then for each exercise name in the workout it creates an ExerciseSet Object, adds it to a list, and returns the completed list. When the isActive variable is specified as true, the returned list contains an expanded workout—an ExerciseSet item for every set and every rest. When the isActive variable is specified as false, the returned list just contains an ExerciseSet item for each exercise in the workout. 

HomeViewModel:  
The HomeViewModel launches a coroutine and collects all workout plans from the WorkoutPlanRepository and stores them in a map for easy access. There are two public variables that are accessed by HomeFragment: 1. phase which is the current selected phase as a StateFlow<String> 2. uiState as StateFlow<HomeUIState> which has the values of either Loading of Success(val workoutPlan: WorkoutPlan). The HomeViewModel provides workoutPlans to HomeFragment, and facilitates the updating and setting of the current selected phase, as well as the completed workout history. 

Home Fragment:  
Collects phase and uiState as State with Lifecycle from The HomeViewModel, and displays the Welcome Screen and HomeScreen. On first install the HomeViewModel checks if a selected phase is stored in Shared Preferences. If there is no stored phase, the Welcome Screen is launched by the Home Fragment. Once a phase is selected the home screen is loaded.

Welcome Screen:
Welcomes the user to the app and allows them to select a current phase. Saving the selected phase navigates the user to the Home Screen 

Home Screen:
Displays a list of workouts for a given phase. Presents a segmented button bar to switch between phases. Selected a segmented button updates the phase in the HomeViewModel, which updates the uiState collected by the HomeFragment, which updates the displayed list of workouts. When a workout is completed the list of booleans stored in the WorkoutPlan Database is updated, marking a workout as completed. A completed workout is marked by a checkmark on the right-hand side of the row listing the workout name. The “reset phase” button on the bottom right of the home screen, will launch a coroutine from the HomeViewModel, which will update the specified WorkoutPlan in the WorkoutPlanDatabase, clearing the completed workout history for the given phase. The coroutines and state flows in the HomeViewModel automatically propagate the changes and all checkmarks will are removed. 


Workout View Model:
Has five public variables accessed by WorkoutFragment: 1. uiState which is a StateFlow holding WorkoutUIState, 2. workoutName as a String, 3. isActive which is a boolean indicating if the workout has been started/isActive, 4. currentSet which is a StateFlow holding a Pair containing the current ExerciseSet and its index in a list 5. timer which is a State of a Pair of Longs representing the minute and second values of a timer. WorkoutUIState has 5 states: 1. Loading 2. Success(val exercise List<ExerciseSet>) 3. Active(val sets List<ExerciseSet>), 4. ActivityTimer, 5. Completed.Once a workout name is set, a coroutine is launched and a List<ExerciseSet> is collected from ActiveWorkoutUseCase. After which the UIState is set to Success(List<ExerciseSet>). Here ActiveWorkoutUseCase is called from within a while loop to ensure that the List<ExerciseSet> loaded from the repositories is complete before loading the Workout Screen. To start a workout the view model launches a coroutine and List<ExerciseSet> is collected from ActiveWorkoutUseCase. The active state of the workout is specified to the ActiveWorkoutUseCase. If the returned list is empty the WorkoutUIState is set to ActivityTimer, otherwise, the WorkoutUIState is set to Active(List<ExerciseSet>), and the currentSet state flow is set to the first item in the list.  This view model also maintains a timer used for some workouts and exercises and launches a coroutine to do so. Upon completing a workout the view model launches a coroutine to update the WorkoutPlanRepository and mark the workout as complete. 

ExerciseViewModel:
Exposes three StateFlow variables to the Workout Fragment: 1.exerciseName which is a state flow of a String 2. exerciseLog which is a state flow of ExerciseLogUIState 3. exerciseInfo which is a state flow of ExerciseInfoUIState. 

Workout Fragment:
Loads the Workout Screen with 5 different states: 1. Loading 2. Success(val exercise List<ExerciseSet>) 3. Active(val sets List<ExerciseSet>), 4. ActivityTimer, 5. Completed
Launches The Exercise Bottom Sheet for 3 different ExerciseTypes: Reps, Timed, and Rest
Sets an OnBackPressedCallback to warn the user before exiting an active workout. 

Workout Screen (WorkoutUIState = Success(List<ExerciseSet>)):
Displays a list of all exercises in the workout with the number of reps and sets to be performed. Tapping on an exercise will launch an exercise details bottom sheet. 
Displays a start workout button.


Active Workout Screen (WorkoutUIState = Active(List<ExerciseSet>)):
While the workout is active, the currentSet StateFlow from the WorkoutViewModel triggers the recomposition of the Active Workout Screen and the Exercise Details bottom sheet. In the list of sets, completed sets are rendered in navy blue, while the current set is highlighted in green. If the Exercise Details bottom sheet is open it is re-rendered with the information of the new currentSet. This screen starts with the exercise Details bottom sheet open.

Exercise Details Bottom Sheet:
The exercise bottom sheet displays three workout types: Reps, Timed, or Rest.
The details for the reps and timed types are loaded from the Exercise view model.
ExerciseLog provides the name, total sets, reps, and last weight used. ExerciseInfo provides other details regarding the exercise but is loaded separately/asynchronously from the ExerciseLog. Thus if there is no network connection the workout can still load and be completed without the extra exercise information. 
 
While the workout is active, info from the current set provided by a state flow from the Workout View Model is used for all exercise types. Additionally, the active workout version of the exercise bottom sheet provides a text field to log weight used for an exercise (which is logged via the ExerciseViewModel) and also displays previous and next buttons to navigate to the next or previous set. The next and previous buttons update the the currentSet StateFlow via the WorkoutViewModel.


Active Workout Screen (WorkoutUIState = ActivityTimer):
Displays an active workout screen for an activity such as yoga, walking, or pilates. Starts and displays a timer for 30 minutes.


Completed Screen (WorkoutUIState = Completed):
This state/screen is accessed by either tapping the end workout button or by tapping next on the last exercise set in the workout via the exercise bottom sheet. This screen displays a banner congratulating the user on completing their workout. The user can navigate back to the home screen by either tapping the return to home screen button or by tapping the back arrow on the Android bottom navigation. 


Enum types:
Phase: used to reference specific phases and phase-specific WorkoutPlans
Exercise Types: used to render the exercise bottom sheet for an active workout
Workout Types: for future development 

ui.theme package: contains files that help style Composable used in the app

