package com.example.createdfromgeminicli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.example.createdfromgeminicli.ui.CalculatorViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvResult: TextView = findViewById(R.id.tvResult)

        // Observe UI State
        viewModel.display.observe(this) { 
            tvResult.text = it
        }

        // Numbers
        val numericButtons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2", R.id.btn3 to "3",
            R.id.btn4 to "4", R.id.btn5 to "5", R.id.btn6 to "6", R.id.btn7 to "7",
            R.id.btn8 to "8", R.id.btn9 to "9"
        )
        numericButtons.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener { viewModel.onNumberPressed(value) }
        }

        // Operators
        val operatorButtons = mapOf(
            R.id.btnPlus to "+", R.id.btnMinus to "−", 
            R.id.btnMultiply to "×", R.id.btnDivide to "÷"
        )
        operatorButtons.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener { viewModel.onOperatorPressed(value) }
        }

        // Controls
        findViewById<Button>(R.id.btnC).setOnClickListener { viewModel.onClearPressed() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { viewModel.onDeletePressed() }
        findViewById<Button>(R.id.btnDot).setOnClickListener { viewModel.onDotPressed() }
        findViewById<Button>(R.id.btnEqual).setOnClickListener { viewModel.onEqualPressed() }
    }
}
