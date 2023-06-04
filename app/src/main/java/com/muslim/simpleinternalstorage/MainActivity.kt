package com.muslim.simpleinternalstorage

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muslim.simpleinternalstorage.ui.theme.SimpleInternalStorageTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleInternalStorageTheme {
                val textState = remember {
                    mutableStateOf("")
                }
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = textState.value,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        scope.launch{
                            textState.value = readFile(context)
                        }
                    }) {
                        Text(text = "Read")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = {
                        scope.launch {
                            save(context)
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

private suspend fun save(context: Context) {
    val text = "У лука моря дуб зеленый;\n" +
            "Златая цепь на дубе том:\n" +
            "И днем и ночью кот ученый\n" +
            "Все ходит по цепи кругом\n" +
            "Идет направо - песень заводит\n" +
            "Налево - сказку говорит."

    withContext(Dispatchers.IO) {
        context.openFileOutput("test.txt", Context.MODE_PRIVATE).use {
            it.write(text.toByteArray())
        }
    }
}

private suspend fun readFile(context: Context) = withContext(Dispatchers.IO){
    try {
        context.openFileInput("test.txt").bufferedReader().useLines {lines ->
            lines.fold("") {acc, s ->
                "$acc\n$s"
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

