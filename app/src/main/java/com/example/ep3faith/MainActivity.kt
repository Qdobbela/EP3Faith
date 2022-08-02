package com.example.ep3faith

import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.ep3faith.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val jongere: Jongere = Jongere("John Doe")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.jongere = jongere

        binding.wijzigButton.setOnClickListener{
            binding.apply{
                naamText.visibility = View.GONE
                wijzigButton.visibility = View.GONE
                naamInput.visibility = View.VISIBLE
                opslaanButton.visibility = View.VISIBLE
            }
        }

        binding.opslaanButton.setOnClickListener{
            binding.apply{
                jongere?.naam = naamInput.text.toString()
                invalidateAll()
                naamText.visibility = View.VISIBLE
                wijzigButton.visibility = View.VISIBLE

                naamInput.visibility = View.GONE
                opslaanButton.visibility = View.GONE
            }
        }
    }
}