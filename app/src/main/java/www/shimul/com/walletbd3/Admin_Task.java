package www.shimul.com.walletbd3;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class Admin_Task extends AppCompatActivity {

    Spinner spinner;

    Wallet_Upload_Fragment walletUploadFragment;
    Beg_Upload_Fragment begUploadFragment;
    Belt_Upload_Fragment beltUploadFragment;
    Shoe_Upload_Fragment shoeUploadFragment;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__task);

        spinner=findViewById(R.id.adminSpinner);

        walletUploadFragment=new Wallet_Upload_Fragment();
        beltUploadFragment=new Belt_Upload_Fragment();
        shoeUploadFragment=new Shoe_Upload_Fragment();
        begUploadFragment=new Beg_Upload_Fragment();

        //ArrayList arrayList= new ArrayList(Arrays.asList(getResources().getStringArray(R.array.spinner_title)));

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(Admin_Task.this,R.layout.custom_spinner,
                getResources().getStringArray(R.array.spinner_title));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //spinner.setBackgroundColor(R.color.colorPrimary);

        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        setFragment(walletUploadFragment);
                        break;

                    case 1:
                        setFragment(beltUploadFragment);
                        break;

                    case 2:
                        setFragment(shoeUploadFragment);
                        break;
                    case 3:
                        setFragment(begUploadFragment);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.spinnerFragment,fragment);
        fragmentTransaction.commit();

    }
}
