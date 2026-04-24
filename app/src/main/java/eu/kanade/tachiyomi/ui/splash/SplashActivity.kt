package eu.kanade.tachiyomi.ui.splash

import android.app.Activity
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import eu.kanade.tachiyomi.ui.main.MainActivity

class SplashActivity : Activity() {

    private var animationStarted = false
    private lateinit var container: LinearLayout
    private lateinit var titleText: TextView
    private lateinit var japaneseText: TextView
    private lateinit var divider: View
    private lateinit var tagline: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Do not let the launcher create a new activity http://stackoverflow.com/questions/16283079
        if (!isTaskRoot) {
            finish()
            return
        }

        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK
        window.decorView.setBackgroundColor(Color.BLACK)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
        }

        container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
            )
        }

        titleText = TextView(this).apply {
            text = "KOMA"
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 56f)
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
            gravity = Gravity.CENTER
            letterSpacing = 0.2f
            alpha = 0f
            scaleX = 0.6f
            scaleY = 0.6f
        }

        japaneseText = TextView(this).apply {
            text = "コマ"
            setTextColor(Color.parseColor("#80FFFFFF"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            gravity = Gravity.CENTER
            letterSpacing = 0.4f
            alpha = 0f
            setPadding(0, dpToPx(6), 0, dpToPx(20))
        }

        divider = View(this).apply {
            setBackgroundColor(Color.parseColor("#40FFFFFF"))
            alpha = 0f
            scaleX = 0f
            layoutParams = LinearLayout.LayoutParams(
                dpToPx(40),
                dpToPx(1),
            ).apply {
                gravity = Gravity.CENTER
                bottomMargin = dpToPx(16)
            }
        }

        tagline = TextView(this).apply {
            text = "Every chapter. Everywhere."
            setTextColor(Color.parseColor("#99FFFFFF"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            typeface = Typeface.create("sans-serif-light", Typeface.ITALIC)
            gravity = Gravity.CENTER
            letterSpacing = 0.08f
            alpha = 0f
        }

        container.addView(titleText)
        container.addView(japaneseText)
        container.addView(divider)
        container.addView(tagline)

        setContentView(container)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && !animationStarted) {
            animationStarted = true
            startSplashAnimation()
        }
    }

    private fun startSplashAnimation() {
        // 1. "KOMA" — instant punch in
        val titleFadeIn = ObjectAnimator.ofFloat(titleText, View.ALPHA, 0f, 1f).apply {
            duration = 200
        }
        val titleScaleX = ObjectAnimator.ofFloat(titleText, View.SCALE_X, 0.6f, 1f).apply {
            duration = 300
            interpolator = OvershootInterpolator(2f)
        }
        val titleScaleY = ObjectAnimator.ofFloat(titleText, View.SCALE_Y, 0.6f, 1f).apply {
            duration = 300
            interpolator = OvershootInterpolator(2f)
        }
        val titleSet = AnimatorSet().apply {
            playTogether(titleFadeIn, titleScaleX, titleScaleY)
        }

        // 2. "コマ"
        val jpFadeIn = ObjectAnimator.ofFloat(japaneseText, View.ALPHA, 0f, 1f).apply {
            duration = 200
            startDelay = 200
        }

        // 3. Divider
        val divFadeIn = ObjectAnimator.ofFloat(divider, View.ALPHA, 0f, 1f).apply {
            duration = 150
        }
        val divScaleX = ObjectAnimator.ofFloat(divider, View.SCALE_X, 0f, 1f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }
        val divSet = AnimatorSet().apply {
            playTogether(divFadeIn, divScaleX)
            startDelay = 350
        }

        // 4. Tagline
        val tagFadeIn = ObjectAnimator.ofFloat(tagline, View.ALPHA, 0f, 1f).apply {
            duration = 250
            startDelay = 500
            interpolator = DecelerateInterpolator()
        }

        val masterSet = AnimatorSet().apply {
            playTogether(titleSet, jpFadeIn, divSet, tagFadeIn)
        }
        masterSet.start()

        // After animation completes: launch MainActivity immediately, let IT handle the transition
        masterSet.doOnEnd {
            // Brief hold so user can read
            container.postDelayed({
                // Launch MainActivity FIRST, then fade out splash on top
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)

                // Ultra-fast 100ms fadeout — splash vanishes as main app is already underneath
                val fadeOut = ValueAnimator.ofFloat(1f, 0f).apply {
                    duration = 100
                    addUpdateListener { va ->
                        container.alpha = va.animatedValue as Float
                    }
                    doOnEnd {
                        finish()
                    }
                }
                fadeOut.start()
            }, 600)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics,
        ).toInt()
    }
}
