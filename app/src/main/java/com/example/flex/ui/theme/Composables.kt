package com.example.flex.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

fun Modifier.cell() : Modifier = padding(16.dp,12.dp).fillMaxWidth()

fun Modifier.workoutRow() : Modifier = cell()    .
    clip(shape = RoundedCornerShape(6.dp))
    .background(Color.White, shape = RoundedCornerShape(15.dp))
    .border(BorderStroke(2.dp, SolidColor(Blue100)), shape =  RoundedCornerShape(15.dp))

fun Modifier.currentExercise() : Modifier = fillMaxWidth()
    .clip(shape = RoundedCornerShape(20.dp))
    .padding(10.dp)
    .background(Green50, shape = RoundedCornerShape(15.dp))
    .border(BorderStroke(2.dp, SolidColor(Green400)), shape =  RoundedCornerShape(15.dp))

fun Modifier.completedExercise() : Modifier = fillMaxWidth()
    .clip(shape = RoundedCornerShape(20.dp))
    .padding(10.dp)
    .background(Navy50, shape = RoundedCornerShape(15.dp))
    .border(BorderStroke(2.dp, SolidColor(Navy200)), shape =  RoundedCornerShape(15.dp))

fun Modifier.upcomingExercise() : Modifier = fillMaxWidth()
    .clip(shape = RoundedCornerShape(20.dp))
    .padding(10.dp)
    .background(Blue50, shape = RoundedCornerShape(15.dp))
    .border(BorderStroke(2.dp, SolidColor(Blue100)), shape =  RoundedCornerShape(15.dp))

fun Modifier.upcomingRest() : Modifier = fillMaxWidth()
    .clip(shape = RoundedCornerShape(20.dp))
    .padding(10.dp)
    .background(Color.White, shape = RoundedCornerShape(15.dp))
    .border(BorderStroke(2.dp, SolidColor(Blue50)), shape =  RoundedCornerShape(15.dp))


@Composable
fun ScreenHeading(text: String){
    Row(Modifier.cell()) {
        Text(text = text, style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun LabelValueCell(text1: String, text2: String, modifier: Modifier = Modifier) {
    Row(modifier) {
        Text(text = text1, style = bodyLabel)
        Text(text = text2, style = bodyValue)
    }
}

@Composable
@Preview
fun WorkoutCell() {
    Row(Modifier.cell(), Arrangement.SpaceBetween) {
        Text("workout name", style = MaterialTheme.typography.headlineSmall)
        Column (Modifier, horizontalAlignment = Alignment.End){
            Text("Sets: n", style = MaterialTheme.typography.bodyMedium)
            Text("Reps: n", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

