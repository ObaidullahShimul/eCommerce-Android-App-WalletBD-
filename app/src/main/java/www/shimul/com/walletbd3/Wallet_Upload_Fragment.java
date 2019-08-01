package www.shimul.com.walletbd3;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
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


public class Wallet_Upload_Fragment extends Fragment implements View.OnClickListener {


    public Wallet_Upload_Fragment() {
        // Required empty public constructor

        //button=find
    }

    EditText wCode,wTitle,wPrice,wDes;
    Button wUploadBtn;
    ImageView wImg_one,wImage_two;
    private static final int GALLERY_REQUEST=1;
    private static final int GALLERY_REQUEST2=1;
    private Uri mImageUri,mImageUri2;

    int imageV=0;
    // //---2-----
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    private ProgressDialog walletUploadProgrss;
    private String code,title,des,price;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet__upload_, container, false);

        //------------------Database Reference-----------------
        databaseReference=FirebaseDatabase.getInstance().getReference("Upload/Wallets");
        storageReference=FirebaseStorage.getInstance().getReference("Wallets");


        wUploadBtn=view.findViewById(R.id.walletUploadBtnId);
        wUploadBtn.setOnClickListener(this);

        //upload taks
        wCode=view.findViewById(R.id.walletPCodeId);
        wTitle=view.findViewById(R.id.walletPTitleId);
        wPrice=view.findViewById(R.id.walletPPriceId);
        wDes=view.findViewById(R.id.walletPDes);

        wImg_one=view.findViewById(R.id.wImgId1);
        wImg_one.setOnClickListener(this);

        wImage_two=view.findViewById(R.id.wImgId2);
        wImage_two.setOnClickListener(this);

        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.walletUploadBtnId:

                if(uploadTask!=null && uploadTask.isInProgress()){

                    Toast.makeText(getActivity(), "Uploading in Progress", Toast.LENGTH_SHORT).show();

                }else{
                    walletUploadProgrss = new ProgressDialog(v.getContext());
                    walletUploadProgrss.setCancelable(true);
                    walletUploadProgrss.setMessage("Wait!! Product Uploading...");
                    walletUploadProgrss.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    walletUploadProgrss.show();
                    saveData();
                }

                break;


            case R.id.wImgId1:
                imageV=1;
                openFileChooser();
                break;

            case R.id.wImgId2:
                imageV=2;
                openFileChooser2();
                break;

        }
    }


    //-------------walletButtonId Save Data---------------
    public void saveData(){

        code=wCode.getText().toString().trim();
        title=wTitle.getText().toString().trim();
        price=wPrice.getText().toString().trim();
        des=wDes.getText().toString().trim();

        if (code.isEmpty()){
            wCode.setError("Wallet Code");
            wCode.requestFocus();
            return;
        }
            else if (title.isEmpty()){
                wTitle.setError("Wallet Title");
                wTitle.requestFocus();
                return;
            }

            else if (price.isEmpty()){
                wPrice.setError("Wallet Price");
                wPrice.requestFocus();
                return;
            }

            else if (des.isEmpty()){
                wDes.setError("Your Wallet Description");
                wDes.requestFocus();
                return;
            }


        //else {
            //Intent intent=new Intent(getActivity(),MainActivity.class);
            //startActivity(intent);
            //}


            StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            final StorageReference ref2 = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri2));

            //-----------------upload a file-------------------
            ref.putFile(mImageUri)
            //ref2.putFile(mImageUri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            final Uri downloadUrl = urlTask.getResult();

                            ref2.putFile(mImageUri2)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                        @SuppressLint("ResourceAsColor")
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get a URL to the uploaded content
                                            //wUploadBtn.setBackgroundColor(R.color.green);
                                            //Toast.makeText(getActivity(), "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();

                                            Task<Uri> urlTask2 = taskSnapshot.getStorage().getDownloadUrl();

                                            while (!urlTask2.isSuccessful()) ;
                                            Uri downloadUrl2 = urlTask2.getResult();

                                            Upload upload=new Upload(downloadUrl.toString(),downloadUrl2.toString(),code,title,price,des);

                                            String uploadId = databaseReference.push().getKey();
                                            databaseReference.child(uploadId).setValue(upload);

                                            walletUploadProgrss.dismiss();
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

        wCode.setText("");
        wTitle.setText("");
        wPrice.setText("");
        wDes.setText("");
    }



    //---------for image Selected File chooser---------------
    private void openFileChooser()
    {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
        //break;
    }

    private void openFileChooser2()
    {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST2);
        //break;
    }


    //-----------for Image Extension---------
    public String getFileExtension(Uri imageUri){

        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        //super.onActivityResult(requestCode, resultCode, data);

        if (imageV==1 && requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri=data.getData();
            Picasso.with(getActivity()).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(wImg_one);
            //Picasso.with(getActivity()).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(wImage_two);

        }else if (imageV==2 && requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageUri2=data.getData();
            Picasso.with(getActivity()).load(mImageUri2).networkPolicy(NetworkPolicy.OFFLINE).into(wImage_two);
            //Picasso.with(getActivity()).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(wImage_two);

        }
    }


}
