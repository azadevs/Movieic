package android.azadevs.movieic.ui

import android.azadevs.movieic.R
import android.azadevs.movieic.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var listener: NavController.OnDestinationChangedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navController = findNavController(R.id.fragmentContainerView)
        configureBottomNav()

        listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieDetailsFragment, R.id.movieTrailerFragment, R.id.moviesFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }

                else -> binding.bottomNav.visibility = View.VISIBLE
            }
        }

    }

    private fun configureBottomNav() {
        val popupMenu = PopupMenu(this, binding.bottomNav)
        popupMenu.inflate(R.menu.menu_bottom_nav)
        val menu = popupMenu.menu
        binding.bottomNav.setupWithNavController(menu, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }
}