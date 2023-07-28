package com.picsart.studio.Login_and_sign_up.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.picsart.studio.R;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    LoginButton fb_login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_sign_up_and_login);
        setSlideShower();
        Button login_btn = findViewById(R.id.login_btn);
        Button register_btn = findViewById(R.id.register_button);
        register_btn.setOnClickListener(l->{
            startActivity(new Intent(getApplicationContext(),Register.class));
        });
        login_btn.setOnClickListener(l->{
            startActivity(new Intent(getApplicationContext(),login.class));
        });
    }

    public void setSlideShower(){
        ImageSlider slider = findViewById(R.id.ImageSlider);
        List<SlideModel> slider_data = new ArrayList<>();
        slider_data.add(new SlideModel(R.drawable.img_1_intro, ScaleTypes.CENTER_INSIDE));
        slider_data.add(new SlideModel(R.drawable.img_2_intro, ScaleTypes.CENTER_INSIDE));
        slider_data.add(new SlideModel(R.drawable.img_3_intro, ScaleTypes.CENTER_INSIDE));
        slider.setImageList(slider_data, ScaleTypes.FIT);
    }
}