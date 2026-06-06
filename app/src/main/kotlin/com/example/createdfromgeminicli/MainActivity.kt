package com.example.createdfromgeminicli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        
        // Listeners for numeric buttons
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        
        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                if (stateError) {
                    tvResult.text = (it as Button).text
                    stateError = false
                } else {
                    if (tvResult.text.toString() == "0") {
                        tvResult.text = (it as Button).text
                    } else {
                        tvResult.append((it as Button).text)
                    }
                }
                lastNumeric = true
            }
        }

        // Action buttons
        findViewById<Button>(R.id.btnC).setOnClickListener {
            tvResult.text = "0"
            lastNumeric = false
            stateError = false
            lastDot = false
        }
        
        // Placeholder for operations
        val operators = listOf(R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide)
        operators.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                if (lastNumeric && !stateError) {
                    tvResult.append((it as Button).text)
                    lastNumeric = false
                    lastDot = false
                }
            }
        }
    }
}
