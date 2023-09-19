package com.example.shuddhavayu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val icon=findViewById<ImageView>(R.id.icon)
        val moto=findViewById<TextView>(R.id.moto)
        val sihLogo = findViewById<ImageView>(R.id.sihLogo)
        val gov = findViewById<TextView>(R.id.government)
        val name = findViewById<ImageView>(R.id.appName)

        icon.alpha=0f;
        icon.animate().setDuration(2000).alpha(1f).withEndAction{
            val i= Intent(this,SignUpActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }
        val motoAnim = AnimationUtils.loadAnimation(this, R.anim.moto_anim)
        moto.startAnimation(motoAnim)
        icon.startAnimation(motoAnim)
        sihLogo.startAnimation(motoAnim)
        gov.startAnimation(motoAnim)
        name.startAnimation(motoAnim)


    }
}