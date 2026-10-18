package com.example.tebak_angka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GuessGameScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GuessGameScreen() {
    var secret by remember { mutableIntStateOf(Random.nextInt(0, 101)) }
    var score by remember { mutableIntStateOf(100) }
    var input by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Tebak angka antara 0 sampai 100") }
    var shownNumbers by remember { mutableStateOf(listOf<Int>()) }
    var finished by remember { mutableStateOf(false) }

    fun resetGame() {
        secret = Random.nextInt(0, 101)
        score = 100
        input = ""
        message = "Tebak angka antara 0 sampai 100"
        shownNumbers = emptyList()
        finished = false
    }

    fun checkGuess() {
        val guess = input.toIntOrNull()
        if (guess == null || guess !in 0..100) {
            message = "Masukkan angka antara 0 sampai 100"
            return
        }
        if (guess == secret) {
            message = "Benar! Angkanya $secret. Skor akhir: $score"
            shownNumbers = emptyList()
            finished = true
        } else {
            score -= 10
            shownNumbers = (1..guess).toList()
            if (score <= 0) {
                score = 0
                message = "Game over! Angka yang benar adalah $secret"
                finished = true
            } else {
                message = "Salah! Skor dikurangi 10"
            }
        }
        input = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tebak Angka", fontSize = 28.sp)
        Spacer(Modifier.height(8.dp))
        Text("Skor: $score", fontSize = 22.sp)
        Spacer(Modifier.height(16.dp))
        Text(message)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it.filter { c -> c.isDigit() }.take(3) },
            label = { Text("Angka tebakan") },
            enabled = !finished,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { checkGuess() }, enabled = !finished) { Text("Tebak") }
            OutlinedButton(onClick = { resetGame() }) { Text("Main Lagi") }
        }

        if (shownNumbers.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            Text("Angka 1 sampai ${shownNumbers.last()}:")
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                shownNumbers.forEach { Text("$it") }
            }
        }
    }
}