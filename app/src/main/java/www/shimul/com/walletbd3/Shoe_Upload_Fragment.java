package www.shimul.com.walletbd3;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Shoe_Upload_Fragment extends Fragment implements View.OnClickListener{


    public Shoe_Upload_Fragment() {
        // Required empty public constructor
    }

    EditText shoeCode,shoeTitle,shoePrice,shoeDes;
    Button shoeUploadBtn;
    private ImageView shoeImg_one,shoeImg_two;
    private static final int GALLERY_REQUEST=1;
    private static final int GALLERY_REQUEST2=1;
    private Uri mImageUri,mImageUri2;

    private int imageShoeV=0;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    private ProgressDialog shoeUploadProgrss;
    private String code,title,des,price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View shoeView= inflater.inflate(R.layout.fragment_shoe__upload_, container, false);

        databaseReference=FirebaseDatabase.getInstance().getReference("Upload/Shoes");
        storageReference=FirebaseStorage.getInstance().getReference("Shoes");
        // Inflate the layout for this fragment

        shoeUploadBtn=shoeView.findViewById(R.id.shoeUploadBtnId);
        shoeUploadBtn.setOnClickListener(this);

        //upload taks
        shoeCode=shoeView.findViewById(R.id.shoeCodeEditTextId);
        shoeTitle=shoeView.findViewById(R.id.shoeTitleEditTextId);
        shoePrice=shoeView.findViewById(R.id.shoePriceEditTextId);
        shoeDes=shoeView.findViewById(R.id.shoeDesEditTextId);

        shoeImg_one=shoeView.findViewById(R.id.shoeImgUpId1);
        shoeImg_one.setOnClickListener(this);

        shoeImg_two=shoeView.findViewById(R.id.shoeImgUpId2);
        shoeImg_two.setOnClickListener(this);

        return shoeView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.shoeUploadBtnId:

                if(uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(getActivity(), "Uploading in Progress", Toast.LENGTH_SHORT).show();
                }else{
                    shoeUploadProgrss = new ProgressDialog(v.getContext());
                    shoeUploadProgrss.setCancelable(true);
                    shoeUploadProgrss.setMessage("Wait!! Product Uploading...");
                    shoeUploadProgrss.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    shoeUploadProgrss.show();
                    saveData();
                }
                break;

            case R.id.shoeImgUpId1:
                imageShoeV=1;
                openFileChooser();
                break;

            case R.id.shoeImgUpId2:
                imageShoeV=2;
                openFileChooser2();
                break;

        }
    }

            //----------- SaveData Method work details---------------
            public void saveData(){

                code=shoeCode.getText().toString().trim();
                title=shoeTitle.getText().toString().trim();
                price=shoePrice.getText().toString().trim();
                des=shoeDes.getText().toString().trim();

                if (code.isEmpty()){
                    shoeCode.setError("Shoe Code");
                    shoeCode.requestFocus();
                    return;
                }
                else if (title.isEmpty()){
                    shoeTitle.setError("Shoe Title");
                    shoeTitle.requestFocus();
                    return;
                }

                else if (price.isEmpty()){
                    shoePrice.setError("Shoe Price");
                    shoePrice.requestFocus();
                    return;
                }

                else if (des.isEmpty()){
                    shoeDes.setError("Shoe details");
                    shoeDes.requestFocus();
                    return;
                }



                StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                final StorageReference ref2 = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri2));

                //-----------------upload a file-------------------
                ref.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                //-------3--------------
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                final Uri downloadUrl = urlTask.getResult();

                                ref2.putFile(mImageUri2)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @SuppressLint("ResourceAsColor")
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Get a URL to the uploaded content

                                                Task<Uri> urlTask2 = taskSnapshot.getStorage().getDownloadUrl();

                                                while (!urlTask2.isSuccessful()) ;
                                                Uri downloadUrl2 = urlTask2.getResult();

                                                Upload upload=new Upload(downloadUrl.toString(),downloadUrl2.toString(),code,title,price,des);

                                                String uploadId = databaseReference.push().getKey();
                                                databaseReference.child(uploadId).setValue(upload);

                                                shoeUploadProgrss.dismiss();
                                                Toast.makeText(getActivity(), "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                textFieldClear();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                Toast.makeText(getActivity(), "Product Not Upload", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(getActivity(), "Product Not Upload", Toast.LENGTH_SHORT).show();
                            }
                        });

            }


            //-----------------Clear uploading text field------------------
            private void textFieldClear(){

                shoeCode.setText("");
                shoeTitle.setText("");
                shoePrice.setText("");
                shoeDes.setText("");
            }


            //---------for image Selected File chooser---------------
            private void openFileChooser()
            {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
            private void openFileChooser2()
            {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST2);
            }
            //---------for image Selected File chooser---------------


            //-----------for Image Extension---------
            public String getFileExtension(Uri imageUri){

                ContentResolver contentResolver=getActivity().getContentResolver();
                MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
                return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

            }


            @Override
            public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


                super.onActivityResult(requestCode, resultCode, data);

                if (imageShoeV==1 && requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                    mImageUri=data.getData();

                    Picasso.with(getActivity()).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(shoeImg_one);
                }
                else if (imageShoeV==2 && requestCode==GALLERY_REQUEST2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                    mImageUri2=data.getData();

                    Picasso.with(getActivity()).load(mImageUri2).networkPolicy(NetworkPolicy.OFFLINE).into(shoeImg_two);
                }
            }


}
