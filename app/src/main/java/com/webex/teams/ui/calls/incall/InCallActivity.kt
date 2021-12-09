package com.webex.teams.ui.calls.incall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.composes.R
import com.example.composes.databinding.ActivityInCallBinding


class InCallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInCallBinding
    private val navController get() = findNavController(R.id.nav_host_fragment_content_in_call)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}