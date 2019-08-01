package www.shimul.com.walletbd3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Admin_Login extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText employeeNo;
    EditText employeePass;
    Button adminLogin, createAccount;

    String emplyeeIdNo_ONE="123";
    String emplyeeIdNo_TWO="1234";
    String emplyeeIdNo_THREE="12345";

    String emplyeeIdNo_PASS_ONE="azan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        progressBar = findViewById(R.id.progressBar);
        employeeNo = findViewById(R.id.etEmployeeId);
        employeePass = findViewById(R.id.etEmpoyeePass);
        adminLogin = findViewById(R.id.btnAdminLogin);
        //createAccount=findViewById(R.id.btnCreateAccount);

        adminLogin.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnAdminLogin:
                //-----user Registration------

                //loading();
                adminSignInProcess();
                //loading();
                break;
        }
    }


    private void adminSignInProcess() {

        //loading();

        String email = employeeNo.getText().toString().trim();
        String password = employeePass.getText().toString().trim();


        if (email.isEmpty()) {
            employeeNo.setError("Enter Your Email Please");
            employeeNo.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            employeePass.setError("Enter Your Password Please");
            employeePass.requestFocus();
            return;
        }



        if (email.equals(emplyeeIdNo_ONE) && password.equals(emplyeeIdNo_PASS_ONE)) {
            progressBar.setVisibility(View.VISIBLE);
            loading();

        } else {
            Toast.makeText(this, "Input Correct Employee ID and Password", Toast.LENGTH_SHORT).show();
        }

    }


    public void loading(){


        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                    Intent adminTask = new Intent(Admin_Login.this, Admin_Task.class);
                    startActivity(adminTask);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

}
