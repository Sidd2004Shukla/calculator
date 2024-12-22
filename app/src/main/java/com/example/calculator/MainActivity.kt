package com.example.calculator

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var firstnumber = ""
    private var currentnumber = ""
    private var currentoperator = ""
    private var result = ""

    private fun evaluate(firstnumber: String, currentnumber: String, currentoperator: String): String {
        val num1 = firstnumber.toDouble()
        val num2 = currentnumber.toDouble()
        return when (currentoperator) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "*" -> (num1 * num2).toString()
            "/" -> (num1 / num2).toString()
            else -> ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.gv1.children.filterIsInstance<Button>().forEach { button: Button ->
            button.setOnClickListener {
                val buttontext = button.text.toString()
                when {
                    buttontext.matches(Regex("[0-9]")) -> {
                        if (currentoperator.isEmpty()) {
                            firstnumber += buttontext
                            binding.tv2.text = firstnumber
                        } else {
                            currentnumber += buttontext
                            binding.tv2.text = currentnumber
                        }
                    }
                    buttontext == "." -> {
                        if (currentoperator.isEmpty()) {
                            if (!firstnumber.contains('.')) {
                                if (firstnumber.isEmpty()) firstnumber = "0$buttontext" else firstnumber += buttontext
                                binding.tv2.text = firstnumber
                            }
                        } else {
                            if (!currentnumber.contains('.')) {
                                if (currentnumber.isEmpty()) currentnumber = "0$buttontext" else currentnumber += buttontext
                                binding.tv2.text = currentnumber
                            }
                        }
                    }
                    buttontext.matches(Regex("[+\\-*/]")) -> {
                        currentnumber=""
                        if (binding.tv2.text.toString().isNotEmpty()) {
                            currentoperator = buttontext
                            binding.tv2.text = "0"
                        }
                    }
                    buttontext == "C" -> {
                        firstnumber = ""
                        currentnumber = ""
                        currentoperator = ""
                        binding.tv1.text = ""
                        binding.tv2.text = "0"
                    }
                    buttontext == "=" -> {
                        if (currentoperator.isNotEmpty() && currentnumber.isNotEmpty()) {
                            result = evaluate(firstnumber, currentnumber, currentoperator)
                            binding.tv1.text = "$firstnumber $currentoperator $currentnumber"
                            binding.tv2.text = result
                            firstnumber = result
                            currentnumber = ""
                            currentoperator = ""
                        }
                    }
                }
            }
        }
    }
}
