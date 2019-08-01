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


public class Beg_Upload_Fragment extends Fragment implements View.OnClickListener{


    public Beg_Upload_Fragment() {
        // Required empty public constructor
    }


    private EditText bagCode,bagTitle,bagPrice,bagDes;
    private Button bagUploadBtn;
    private ImageView bagImg_one,bagImg_two;
    private static final int GALLERY_REQUEST=1;
    private static final int GALLERY_REQUEST2=1;
    private Uri mImageUri,mImageUri2;

    private int imageBagV=0;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    private ProgressDialog bagUploadProgrss;
    private String code,title,des,price;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View bagView=inflater.inflate(R.layout.fragment_beg__upload_, container, false);

        databaseReference=FirebaseDatabase.getInstance().getReference("Upload/Bags");
        storageReference=FirebaseStorage.getInstance().getReference("Bags");
        // Inflate the layout for this fragment

        bagUploadBtn=bagView.findViewById(R.id.bagUploadBtnId);
        bagUploadBtn.setOnClickListener(this);

        //------------upload taks------------------
        bagCode=bagView.findViewById(R.id.bagCodeEditTextId);
        bagTitle=bagView.findViewById(R.id.bagTitleEditTextId);
        bagPrice=bagView.findViewById(R.id.bagPriceEditTextId);
        bagDes=bagView.findViewById(R.id.bagDesEditTextId);

        bagImg_one=bagView.findViewById(R.id.bagImgUpId1);
        bagImg_one.setOnClickListener(this);

        bagImg_two=bagView.findViewById(R.id.bagImgUpId2);
        bagImg_two.setOnClickListener(this);

        return bagView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bagUploadBtnId:

                if(uploadTask!=null && uploadTask.isInProgress()){

                    Toast.makeText(getActivity(), "Uploading in Progress", Toast.LENGTH_SHORT).show();

                }else{
                    bagUploadProgrss = new ProgressDialog(v.getContext());
                    bagUploadProgrss.setCancelable(true);
                    bagUploadProgrss.setMessage("Wait!! Product Uploading...");
                    bagUploadProgrss.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    bagUploadProgrss.show();
                    saveData();
                }
                break;


            case R.id.bagImgUpId1:
                imageBagV=1;
                openFileChooser();
                break;

            case R.id.bagImgUpId2:
                imageBagV=2;
                openFileChooser2();
                break;

        }
    }


            //----------- SaveData Method work details---------------
            public void saveData(){

                code=bagCode.getText().toString().trim();
                title=bagTitle.getText().toString().trim();
                price=bagPrice.getText().toString().trim();
                des=bagDes.getText().toString().trim();

                if (code.isEmpty()){
                    bagCode.setError("Bag Code");
                    bagCode.requestFocus();
                    return;
                }
                else if (title.isEmpty()){
                    bagTitle.setError("Bag Title");
                    bagTitle.requestFocus();
                    return;
                }

                else if (price.isEmpty()){
                    bagPrice.setError("Bag Price");
                    bagPrice.requestFocus();
                    return;
                }

                else if (des.isEmpty()){
                    bagDes.setError("Bag details");
                    bagDes.requestFocus();
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

                                                bagUploadProgrss.dismiss();
                                                Toast.makeText(getActivity(), "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                textFieldClear();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(getActivity(), "Stage 1!! Product Not Upload", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getActivity(), "Final Stage!! Product Not Upload", Toast.LENGTH_SHORT).show();
                    }
                });


            }


            //-----------------Clear uploading text field------------------
            private void textFieldClear(){

                bagCode.setText("");
                bagTitle.setText("");
                bagPrice.setText("");
                bagDes.setText("");
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
    }//---------for image Selected File chooser---------------


    //---------------for Image Extension---------
    public String getFileExtension(Uri imageUri){

        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }//---------------for Image Extension---------


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (imageBagV==1 && requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri=data.getData();

            Picasso.with(getActivity()).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(bagImg_one);
        }else if (imageBagV==2 && requestCode==GALLERY_REQUEST2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri2=data.getData();

            Picasso.with(getActivity()).load(mImageUri2).networkPolicy(NetworkPolicy.OFFLINE).into(bagImg_two);
        }
    }

}
