package com.example.diabetes

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            // Set HomeFragment as the default fragment
            val destination = intent.getIntExtra("destination", R.id.nav_tentang)
            navigateToFragment(destination)
            navigationView.setCheckedItem(destination)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigateToFragment(item.itemId)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToFragment(itemId: Int) {
        val fragment: Fragment = when (itemId) {
            R.id.nav_tentang -> HomeFragment()
            R.id.nav_dataset -> DatasetFragment()
            R.id.nav_fitur -> FiturFragment()
            R.id.nav_model -> ModelFragment()
            R.id.nav_simulasimodel -> SimulasiFragment()
            else -> throw IllegalArgumentException("Unexpected itemId")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

        toolbar.title = getString(when (itemId) {
            R.id.nav_tentang -> R.string.nav_tentang_title
            R.id.nav_dataset -> R.string.nav_dataset_title
            R.id.nav_fitur -> R.string.nav_fitur_title
            R.id.nav_model -> R.string.nav_model_title
            R.id.nav_simulasimodel -> R.string.nav_simulasimodel_title
            else -> R.string.app_name
        })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
