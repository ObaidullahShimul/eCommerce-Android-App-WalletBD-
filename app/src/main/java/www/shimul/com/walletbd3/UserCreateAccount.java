package www.shimul.com.walletbd3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserCreateAccount extends AppCompatActivity implements View.OnClickListener{

    //private Toolbar toolbar;
    private ProgressBar progressBar;
    private EditText email;
    private EditText password;
    private Button signup;
    private Button backSignIn;
    private Button forgotPass;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_account);

        //toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        signup = findViewById(R.id.btnSignup);
        backSignIn = findViewById(R.id.btnGosignIn);
        //forgotPass = findViewById(R.id.btnUserForgottPass);

        //toolbar.setTitle("Sign Up");


        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(this);
        backSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSignup:
                //-----user Registration------
                userRegistration();
                break;

            case R.id.btnGosignIn:
                Intent intent=new Intent(UserCreateAccount.this,UserLoginActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void userRegistration() {


        String usEmail=email.getText().toString().trim();
        String userPassword=password.getText().toString().trim();



        if (usEmail.isEmpty()){
            email.setError("Enter Your Email Please");
            email.requestFocus();
            return;
        }


        if (userPassword.isEmpty()){
            password.setError("Enter Your Password Please");
            password.requestFocus();
            return;
        }

        if (userPassword.length()<8){
            password.setError("Minimum length of Password Should be 8");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(UserCreateAccount.this, "Registered successfully. Please check your email for verification",
                                                        Toast.LENGTH_LONG).show();
                                                email.setText("");
                                                password.setText("");

                                                //-----------Back Log In Activity--------
                                                Intent intent=new Intent(UserCreateAccount.this,UserLoginActivity.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(UserCreateAccount.this,  task.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                        } else {
                            Toast.makeText(UserCreateAccount.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }
}
