package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var welcomeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configurar Toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Inicializar NavigationView y DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        welcomeTextView = findViewById(R.id.welcomeTextView)

        // Configurar NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar AppBar con NavigationUI
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.productListFragment,
                R.id.createProductFragment,
                R.id.nav_home
            ),
            drawerLayout
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)

        // Obtener datos del Intent
        val username = intent.getStringExtra("USERNAME")
        val email = intent.getStringExtra("EMAIL")

        // Mostrar en el TextView principal
        if (!username.isNullOrEmpty()) {
            welcomeTextView.text = "¡Bienvenido, $username!"
        } else {
            Log.w("HomeActivity", "No username passed in intent")
        }

        // Mostrar en el header del NavigationView
        val headerView = navigationView.getHeaderView(0)
        val headerUsername = headerView.findViewById<TextView>(R.id.nav_header_username)
        val headerEmail = headerView.findViewById<TextView>(R.id.nav_header_email)

        headerUsername.text = username ?: "Usuario"
        headerEmail.text = email ?: "correo@ejemplo.com"

        // Cerrar sesión manualmente
        navigationView.menu.findItem(R.id.nav_cerrar).setOnMenuItemClickListener {
            cerrarSesion()
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun cerrarSesion() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}