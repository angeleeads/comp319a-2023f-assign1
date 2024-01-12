package com.example.rollthedice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                DiceRoller()
            }
        }
    }
}

@Composable
fun DiceSelection(selectedDice: Set<Int>, onDiceSelected: (Int) -> Unit) {
    LazyRow {
        items(DICE_SIDES) { sides ->
            Row(
                modifier = Modifier
                    .padding(10.dp, top = 80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(if (sides in selectedDice) Color.Black else Color.DarkGray)
                        .clickable { onDiceSelected(sides) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "D$sides", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@Composable
fun DiceRoller() {
    var selectedDice by remember { mutableStateOf(emptySet<Int>()) }
    var rollResults by remember { mutableStateOf(emptyMap<Int, Int>()) }

    Text(
        text = "Welcome to Best Roll Dice App!",
        fontSize = 25.sp,
        modifier = Modifier.padding(start = 30.dp, bottom = 90.dp, top = 20.dp)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        DiceSelection(
            selectedDice = selectedDice,
            onDiceSelected = { die -> selectedDice = selectedDice.toggle(die) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                rollResults = selectedDice.associateWith { die -> rollDie(die) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Roll")
        }
        Button(
            onClick = {
                selectedDice = emptySet()
                rollResults = emptyMap()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Reset")
        }

        if (rollResults.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                rollResults.forEach { (die, result) ->
                    Text(
                        text = "D$die: $result",
                        fontSize = 25.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

fun rollDie(sides: Int): Int {
    return (1..sides).random()
}

fun Set<Int>.toggle(element: Int): Set<Int> =
    if (element in this) this - element else this + element

val DICE_SIDES = listOf(4, 6, 8, 10, 12, 20)
