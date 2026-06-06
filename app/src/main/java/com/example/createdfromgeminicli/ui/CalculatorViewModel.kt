package com.example.createdfromgeminicli.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.createdfromgeminicli.domain.CalculatorEngine

class CalculatorViewModel : ViewModel() {

    private val engine = CalculatorEngine()

    private val _display = MutableLiveData("0")
    val display: LiveData<String> = _display

    private var lastNumeric = false
    private var lastDot = false
    private var stateError = false

    fun onNumberPressed(number: String) {
        if (stateError || _display.value == "0") {
            _display.value = number
            stateError = false
        } else {
            _display.value += number
        }
        lastNumeric = true
    }

    fun onOperatorPressed(operator: String) {
        if (lastNumeric && !stateError) {
            _display.value += operator
            lastNumeric = false
            lastDot = false
        }
    }

    fun onDotPressed() {
        if (lastNumeric && !stateError && !lastDot) {
            _display.value += "."
            lastNumeric = false
            lastDot = true
        }
    }

    fun onClearPressed() {
        _display.value = "0"
        lastNumeric = false
        lastDot = false
        stateError = false
    }

    fun onDeletePressed() {
        val current = _display.value ?: return
        if (current != "0") {
            if (current.length <= 1 || stateError) {
                onClearPressed()
            } else {
                _display.value = current.dropLast(1)
                val newCurrent = _display.value!!
                lastNumeric = newCurrent.last().isDigit()
                lastDot = newCurrent.contains(".")
            }
        }
    }

    fun onEqualPressed() {
        if (lastNumeric && !stateError) {
            val result = engine.calculate(_display.value ?: "")
            _display.value = result
            if (result == "Error") {
                stateError = true
                lastNumeric = false
            } else {
                lastNumeric = true
                lastDot = result.contains(".")
            }
        }
    }
}
