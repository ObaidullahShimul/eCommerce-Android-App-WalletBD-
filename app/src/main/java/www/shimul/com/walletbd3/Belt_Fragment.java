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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Belt_Fragment extends Fragment {

    private static final String TAG="Belt Fragment";

    private RecyclerView recyclerView;
    private List<Upload> uploadList;
    private MyAdapter myAdapter;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View beltView=inflater.inflate(R.layout.belt_fragment,container,false);

        recyclerView=beltView.findViewById(R.id.beltRecyclerId);
        recyclerView.setHasFixedSize(true);
        
        uploadList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("Upload/Belts");


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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), "Error"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return beltView;
    }
}
