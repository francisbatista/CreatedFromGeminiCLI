package com.example.createdfromgeminicli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat

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

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val text = tvResult.text.toString()
            if (text.isNotEmpty() && text != "0") {
                if (text.length == 1 || text == "Error") {
                    tvResult.text = "0"
                    lastNumeric = false
                } else {
                    tvResult.text = text.substring(0, text.length - 1)
                    val newText = tvResult.text.toString()
                    lastNumeric = newText.last().isDigit()
                    lastDot = newText.contains(".")
                }
            }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (lastNumeric && !stateError && !lastDot) {
                tvResult.append(".")
                lastNumeric = false
                lastDot = true
            }
        }
        
        // Operations
        findViewById<Button>(R.id.btnPlus).setOnClickListener { addOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { addOperator("−") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { addOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { addOperator("÷") }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            onEqual()
        }
    }

    private fun addOperator(op: String) {
        if (lastNumeric && !stateError) {
            tvResult.append(op)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = tvResult.text.toString()
                .replace("×", "*")
                .replace("÷", "/")
                .replace("−", "-")
            
            try {
                val result = evaluate(txt)
                val df = DecimalFormat("#.#######")
                tvResult.text = df.format(result)
                lastDot = tvResult.text.contains(".")
            } catch (ex: Exception) {
                tvResult.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    // Simple expression evaluator for +, -, *, /
    private fun evaluate(expression: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm() // addition
                    else if (eat('-'.code)) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x *= parseFactor() // multiplication
                    else if (eat('/'.code)) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor() // unary plus
                if (eat('-'.code)) return -parseFactor() // unary minus

                var x: Double
                val startPos = pos
                if (eat('('.code)) { // parentheses
                    x = parseExpression()
                    eat(')'.code)
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = expression.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }

                return x
            }
        }.parse()
    }
}
