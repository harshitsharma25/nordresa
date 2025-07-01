package com.nordresa.travel.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.nordresa.travel.MainActivity
import com.nordresa.travel.R
import com.nordresa.travel.databinding.ActivitySplashScreenBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set immersive theme colors
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_app_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.primary_app_color)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views to starting positions
        initializeViews()

        // Start the sophisticated animation sequence
        startNordResaAnimations()

        // Navigate after complete animation sequence
        Handler(Looper.getMainLooper()).postDelayed({
            navigateWithTransition()
        }, 4000) // Extended duration for premium feel
    }

    private fun initializeViews() {
        // Set initial states for all animated elements
        binding.apply {
            // Logo elements
            transportIcon.alpha = 0f
            transportIcon.scaleX = 0.5f
            transportIcon.scaleY = 0.5f

            // App name
            appNameNord.alpha = 0f
            appNameNord.translationX = -100f
            appNameResa.alpha = 0f
            appNameResa.translationX = 100f

            // Tagline
            taglineText.alpha = 0f
            taglineText.translationY = 50f

            // Stockholm elements
            stockholmAccent.alpha = 0f
            stockholmAccent.scaleX = 0f

            // Loading elements
            loadingContainer.alpha = 0f
            progressBar.alpha = 0f

            // Background elements
            backgroundWave1.translationX = -200f
            backgroundWave2.translationX = 200f
            backgroundWave1.alpha = 0.3f
            backgroundWave2.alpha = 0.3f
        }
    }

    private fun startNordResaAnimations() {
        // Phase 1: Background waves entrance (0-800ms)
        animateBackgroundWaves()

        // Phase 2: Transport icon dramatic entrance (400-1400ms)
        Handler(Looper.getMainLooper()).postDelayed({
            animateTransportIcon()
        }, 400)

        // Phase 3: App name split animation (1000-2200ms)
        Handler(Looper.getMainLooper()).postDelayed({
            animateAppNameSplit()
        }, 1000)

        // Phase 4: Stockholm accent and tagline (1800-2800ms)
        Handler(Looper.getMainLooper()).postDelayed({
            animateStockholmElements()
        }, 1800)

        // Phase 5: Loading sequence (2500-3800ms)
        Handler(Looper.getMainLooper()).postDelayed({
            animateLoadingSequence()
        }, 2500)
    }

    private fun animateBackgroundWaves() {
        val wave1Slide = ObjectAnimator.ofFloat(binding.backgroundWave1, View.TRANSLATION_X, -200f, 0f)
        val wave2Slide = ObjectAnimator.ofFloat(binding.backgroundWave2, View.TRANSLATION_X, 200f, 0f)

        AnimatorSet().apply {
            playTogether(wave1Slide, wave2Slide)
            duration = 1200
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    private fun animateTransportIcon() {
        // Multi-phase icon animation: fade + scale + rotation
        val fadeIn = ObjectAnimator.ofFloat(binding.transportIcon, View.ALPHA, 0f, 1f)
        val scaleX = ObjectAnimator.ofFloat(binding.transportIcon, View.SCALE_X, 0.5f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.transportIcon, View.SCALE_Y, 0.5f, 1.2f, 1f)
        val rotation = ObjectAnimator.ofFloat(binding.transportIcon, View.ROTATION, -10f, 10f, 0f)

        AnimatorSet().apply {
            playTogether(fadeIn, scaleX, scaleY, rotation)
            duration = 1000
            interpolator = OvershootInterpolator(0.8f)
            start()
        }

        // Add subtle continuous floating animation
        startIconFloating()
    }

    private fun startIconFloating() {
        val floatUp = ObjectAnimator.ofFloat(binding.transportIcon, View.TRANSLATION_Y, 0f, -15f, 0f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        floatUp.start()
    }

    private fun animateAppNameSplit() {
        // "Nord" slides in from left
        val nordSlide = ObjectAnimator.ofFloat(binding.appNameNord, View.TRANSLATION_X, -100f, 0f)
        val nordFade = ObjectAnimator.ofFloat(binding.appNameNord, View.ALPHA, 0f, 1f)

        AnimatorSet().apply {
            playTogether(nordSlide, nordFade)
            duration = 600
            interpolator = DecelerateInterpolator()
            start()
        }

        // "Resa" slides in from right with slight delay
        Handler(Looper.getMainLooper()).postDelayed({
            val resaSlide = ObjectAnimator.ofFloat(binding.appNameResa, View.TRANSLATION_X, 100f, 0f)
            val resaFade = ObjectAnimator.ofFloat(binding.appNameResa, View.ALPHA, 0f, 1f)

            AnimatorSet().apply {
                playTogether(resaSlide, resaFade)
                duration = 600
                interpolator = DecelerateInterpolator()
                start()
            }
        }, 200)

        // Add text shimmer effect
        Handler(Looper.getMainLooper()).postDelayed({
            startTextShimmer()
        }, 800)
    }

    private fun startTextShimmer() {
        val shimmerAlpha = ObjectAnimator.ofFloat(binding.appNameNord, View.ALPHA, 1f, 0.7f, 1f).apply {
            duration = 1500
            repeatCount = 2
            interpolator = AccelerateDecelerateInterpolator()
        }

        val shimmerAlpha2 = ObjectAnimator.ofFloat(binding.appNameResa, View.ALPHA, 1f, 0.7f, 1f).apply {
            duration = 1500
            repeatCount = 2
            startDelay = 200
            interpolator = AccelerateDecelerateInterpolator()
        }

        shimmerAlpha.start()
        shimmerAlpha2.start()
    }

    private fun animateStockholmElements() {
        // Stockholm accent line expands
        val accentScale = ObjectAnimator.ofFloat(binding.stockholmAccent, View.SCALE_X, 0f, 1f)
        val accentFade = ObjectAnimator.ofFloat(binding.stockholmAccent, View.ALPHA, 0f, 1f)

        AnimatorSet().apply {
            playTogether(accentScale, accentFade)
            duration = 500
            interpolator = DecelerateInterpolator()
            start()
        }

        // Tagline appears with elegant slide
        Handler(Looper.getMainLooper()).postDelayed({
            val taglineSlide = ObjectAnimator.ofFloat(binding.taglineText, View.TRANSLATION_Y, 50f, 0f)
            val taglineFade = ObjectAnimator.ofFloat(binding.taglineText, View.ALPHA, 0f, 1f)

            AnimatorSet().apply {
                playTogether(taglineSlide, taglineFade)
                duration = 700
                interpolator = DecelerateInterpolator()
                start()
            }
        }, 300)
    }

    private fun animateLoadingSequence() {
        // Loading container fades in
        val containerFade = ObjectAnimator.ofFloat(binding.loadingContainer, View.ALPHA, 0f, 1f)
        containerFade.duration = 400
        containerFade.start()

        // Progress bar appears with delay
        Handler(Looper.getMainLooper()).postDelayed({
            val progressFade = ObjectAnimator.ofFloat(binding.progressBar, View.ALPHA, 0f, 1f)
            progressFade.duration = 300
            progressFade.start()

            // Animate progress bar
            animateProgressBar()
        }, 200)

        // Loading dots sequence
        Handler(Looper.getMainLooper()).postDelayed({
            animateLoadingDots()
        }, 400)
    }

    private fun animateProgressBar() {
        // Simulate loading progress
        val progressAnimator = ValueAnimator.ofInt(0, 100).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animator ->
                val progress = animator.animatedValue as Int
                binding.progressBar.progress = progress
            }
        }
        progressAnimator.start()
    }

    private fun animateLoadingDots() {
        val dots = listOf(binding.loadingDot1, binding.loadingDot2, binding.loadingDot3)

        dots.forEachIndexed { index, dot ->
            val scaleAnimation = ObjectAnimator.ofFloat(dot, View.SCALE_Y, 0.5f, 1.5f, 0.5f).apply {
                duration = 800
                repeatCount = ObjectAnimator.INFINITE
                startDelay = (index * 150).toLong()
                interpolator = AccelerateDecelerateInterpolator()
            }

            val colorAnimation = ObjectAnimator.ofArgb(
                dot,
                "backgroundColor",
                ContextCompat.getColor(this@SplashActivity, R.color.warm_gray),
                ContextCompat.getColor(this@SplashActivity, R.color.gold_accent),
                ContextCompat.getColor(this@SplashActivity, R.color.warm_gray)
            ).apply {
                duration = 800
                repeatCount = ObjectAnimator.INFINITE
                startDelay = (index * 150).toLong()
            }

            scaleAnimation.start()
            colorAnimation.start()
        }
    }

    private fun navigateWithTransition() {
        // Elegant exit animation
        val fadeOut = ObjectAnimator.ofFloat(binding.root, View.ALPHA, 1f, 0f)
        val scaleDown = ObjectAnimator.ofFloat(binding.root, View.SCALE_X, 1f, 0.95f)
        val scaleDownY = ObjectAnimator.ofFloat(binding.root, View.SCALE_Y, 1f, 0.95f)

        AnimatorSet().apply {
            playTogether(fadeOut, scaleDown, scaleDownY)
            duration = 400
            interpolator = AccelerateInterpolator()

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    performNavigation()
                }
            })

            start()
        }
    }

    private fun performNavigation() {


        val currentUser = FirebaseAuth.getInstance().currentUser
        println("--> current user is: $currentUser")

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("destination", if (currentUser != null) "home" else "signin")
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)

        // Custom transition
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Disable back button during splash
        // Do nothing - let the splash complete naturally
    }
}