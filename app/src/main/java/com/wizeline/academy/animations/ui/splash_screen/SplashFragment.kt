package com.wizeline.academy.animations.ui.splash_screen

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wizeline.academy.animations.databinding.SplashFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var target: View

    private val rotateAnimator by lazy {
        ObjectAnimator.ofFloat(target, View.ROTATION, 0f, 360f).apply {
            repeatCount = 2
            repeatMode = ObjectAnimator.RESTART
            duration = 2000
            interpolator = AnticipateOvershootInterpolator()
        }
    }

    private val blinkAnimator by lazy {
        ObjectAnimator.ofFloat(target, View.ALPHA, 0f, 1f).apply {
            repeatCount = 2
            repeatMode = ObjectAnimator.REVERSE
            duration = 750
            interpolator = LinearInterpolator()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)
        target = binding.ivWizelineLogo
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            runAnimation(rotateAnimator)
            runAnimation(blinkAnimator)
            delay(2000)
            goToHomeScreen()
        }
    }

    private fun runAnimation(objectAnimator: ObjectAnimator) {
        objectAnimator
            .apply { target = this@SplashFragment.target }
            .start()
    }

    private fun goToHomeScreen() {
        val directions = SplashFragmentDirections.toMainFragment()
        findNavController().navigate(directions)
    }
}