package com.picsart.studio.Login_and_sign_up.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.picsart.studio.DBHelper.FirebaseHelper;
import com.picsart.studio.Models.User;
import com.picsart.studio.R;
import com.picsart.studio.Student.Activities.Student_main;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient client;
    FirebaseHelper firebaseHelper;
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseHelper = new FirebaseHelper();
        setContentView(R.layout.intro_sign_up_and_login);
        GoogleSignInOptions options =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        client = GoogleSignIn.getClient(this, options);

                setSlideShower();
        SignInButton sign_in_google = findViewById(R.id.sign_in_google);
        Button login_btn = findViewById(R.id.login_btn);
        Button register_btn = findViewById(R.id.register_button);
        sign_in_google.setOnClickListener(l->{
            startActivityForResult(client.getSignInIntent(), 1234);
        });
        register_btn.setOnClickListener(l -> {
            startActivity(new Intent(getApplicationContext(), Register.class));
        });
        login_btn.setOnClickListener(l -> {
            startActivity(new Intent(getApplicationContext(), login.class));
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(l -> {
                    if (l.isSuccessful()) {
                        String email = l.getResult().getUser().getEmail();
                        checkUserExists(email, l.getResult().getUser().getDisplayName());
                    } else {
                        Toast.makeText(this, "Internet connection is down", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
        @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !loginActivityRunning()) {
            Manual_Loggin_Inner manualLogginInner = new Manual_Loggin_Inner(this);
            manualLogginInner.login_using_username_and_password(username, password);
        }
    }
    public void setSlideShower() {
        ImageSlider slider = findViewById(R.id.ImageSlider);
        List<SlideModel> slider_data = new ArrayList<>();
        slider_data.add(new SlideModel(R.drawable.img_1_intro, ScaleTypes.CENTER_INSIDE));
        slider_data.add(new SlideModel(R.drawable.img_2_intro, ScaleTypes.CENTER_INSIDE));
        slider_data.add(new SlideModel(R.drawable.img_3_intro, ScaleTypes.CENTER_INSIDE));
        slider.setImageList(slider_data, ScaleTypes.FIT);
    }
    private boolean loginActivityRunning() {
        return false;
    }
    private void checkUserExists(String email, String displayName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");
        Query query = usersCollection.whereEqualTo("password", email);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean userExists = !task.getResult().isEmpty();
                if (!userExists) {
                    firebaseHelper.addUser(new User(displayName, "2131165352", "Beginner", "00-00-0000", email, "Student"));
                    username = displayName;
                    password = email;
                    Manual_Loggin_Inner manualLogginInner = new Manual_Loggin_Inner(this);
                    manualLogginInner.login_using_username_and_password(username, password);
                    Toast.makeText(this, "Username is " + displayName + " and password is your email i.e. " + email, Toast.LENGTH_SHORT).show();
                } else {
                    username = displayName;
                    password = email;
                    Manual_Loggin_Inner manualLogginInner = new Manual_Loggin_Inner(this);
                    manualLogginInner.login_using_username_and_password(username, password);
                }
            } else {
                Toast.makeText(this, "Error checking user existence", Toast.LENGTH_SHORT).show();
            }
        });
    }
}