package com.example.proyecto.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.proyecto.R
import com.example.proyecto.api.User

class SettingsActivity : AppCompatActivity() {
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        user = intent.getSerializableExtra("User") as User

        if (savedInstanceState == null) {
            val frgSettings: SettingsFragment =
                SettingsFragment.newInstance(user)
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings, frgSettings)
                .commit()
        }

        val toolBar = findViewById<Toolbar>(R.id.appbar_settings)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private var language: String? = null
        private lateinit var user: User

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                user = it.getSerializable("User") as User
            }
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val preference = findPreference<ListPreference>("language")
            preference?.setOnPreferenceChangeListener { _, newValue ->
                language = newValue as String
                saveChanges()
                true
            }
        }

        private fun saveChanges() {
            val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            prefs.edit().putString("language", language).apply()

            requireActivity().finish()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("User", user)
            startActivity(intent)
        }

        companion object {
            @JvmStatic
            fun newInstance(u: User) =
                SettingsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("User", u)
                    }
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

