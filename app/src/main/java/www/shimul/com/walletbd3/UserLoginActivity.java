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

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener{


    ProgressBar progressBar;
    EditText userEmail;
    EditText userPass;
    FirebaseAuth firebaseAuth;

    private Button userLogIn,userCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        progressBar = findViewById(R.id.progressBar);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.etUserPass);
        //userLogIn = findViewById(R.id.btnUserSignInGId);
        //userCreateAccount=findViewById(R.id.btnUserSignUp);

        userLogIn=findViewById(R.id.btnUserSignInGId);
        userLogIn.setOnClickListener(this);

        userCreateAccount=findViewById(R.id.btnUserSignUp);
        userCreateAccount.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnUserSignInGId:
                //-----user Registration------
                userSignInProcess();
                break;

            case R.id.btnUserSignUp:
                Intent createAccount=new Intent(UserLoginActivity.this,UserCreateAccount.class);
                startActivity(createAccount);
                break;
        }
    }


    //------------------=--userSignInProcess(); START-----------------------
    private void userSignInProcess() {

        String email=userEmail.getText().toString().trim();
        String password=userPass.getText().toString().trim();



        if (email.isEmpty()){
            userEmail.setError("Enter Your Email Please");
            userEmail.requestFocus();
            return;
        }


        if (password.isEmpty()){
            userPass.setError("Enter Your Password Please");
            userPass.requestFocus();
            return;
        }

        //-----------if every thing is Ok---------------

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),
                userPass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                Intent loginOK=new Intent(UserLoginActivity.this,MainActivity.class);
                                startActivity(loginOK);
                                //startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                            }else{
                                Toast.makeText(UserLoginActivity.this, "Please verify your email address"
                                        , Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(UserLoginActivity.this, task.getException().getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }//------------------=--userSignInProcess(); END-------------------------
}
