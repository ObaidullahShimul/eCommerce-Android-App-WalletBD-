package www.shimul.com.walletbd3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Wallet_Fragment extends Fragment{

    private static final String TAG="Wallet Fragment";

    private RecyclerView recyclerView;
    private List<Upload> uploadList;
    private MyAdapter myAdapter;
    DatabaseReference databaseReference;

    private ProgressBar progressBar;

    private Upload upload;

    /*----------grid view------------
    GridView walletGrid;
    private String[] studentnames;
    private int[] spic ={R.drawable.wallet_img,R.drawable.wallet_img,
            R.drawable.wallet_img, R.drawable.italyflag,R.drawable.wallet_img,
            R.drawable.italyflag,R.drawable.wallet_img,R.drawable.italyflag,
            R.drawable.wallet_img,R.drawable.italyflag,R.drawable.wallet_img};*/


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.wallet_fragment,container,false);

        /*walletGrid=(GridView) view.findViewById(R.id.walletGridId);
        studentnames=getResources().getStringArray(R.array.stdent_names);

        //-----for custorm adapter------
        //CustomAdapter adapter=new CustomAdapter(this,studentnames,spic);
        CustomAdapter adapter=new CustomAdapter(getActivity(),studentnames,spic);
        walletGrid.setAdapter(adapter);

        walletGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String value=studentnames[position];
                //Toast.makeText(Wallet_Fragment.this,value,Toast.LENGTH_SHORT).show();

            }
        })*/;

        recyclerView=view.findViewById(R.id.walletRecyclerId);
        recyclerView.setHasFixedSize(true);

        //progressBar=view.findViewById(R.id.RecyclerprogressBarId);

        uploadList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("Upload/Wallets");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    //upload=dataSnapshot1.getValue(Upload.class);
                    //uploadList.add(upload);
                    uploadList.add(dataSnapshot1.getValue(Upload.class));
                }

                myAdapter=new MyAdapter(getActivity(),uploadList);
                recyclerView.setAdapter(myAdapter);

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),3));
                //progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), "Error"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });


        return view;

    }

}
