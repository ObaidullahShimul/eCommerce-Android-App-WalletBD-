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
public class Belt_Upload_Fragment extends Fragment implements View.OnClickListener{


    public Belt_Upload_Fragment() {
        // Required empty public constructor
    }

    EditText beltCode,beltTitle,beltPrice,beltDes;
    Button beltUploadBtn;
    ImageView beltImg_one,beltImg_two;
    private static final int GALLERY_REQUEST=1;
    private static final int GALLERY_REQUEST2=1;
    private Uri mImageUri,mImageUri2;

    int imageBeltV=0;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    private ProgressDialog beltUploadProgrss;
    private String code,title,des,price;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View beltView=inflater.inflate(R.layout.fragment_belt__upload_, container, false);

        databaseReference=FirebaseDatabase.getInstance().getReference("Upload/Belts");
        storageReference=FirebaseStorage.getInstance().getReference("Belts");
        // Inflate the layout for this fragment

        beltUploadBtn=beltView.findViewById(R.id.beltUploadBtnId);
        beltUploadBtn.setOnClickListener(this);

        //upload taks
        beltCode=beltView.findViewById(R.id.beltCodeEditTextId);
        beltTitle=beltView.findViewById(R.id.beltTitleEditTextId);
        beltPrice=beltView.findViewById(R.id.beltPriceEditTextId);
        beltDes=beltView.findViewById(R.id.beltDesEditTextId);

        beltImg_one=beltView.findViewById(R.id.beltImgUpId1);
        beltImg_one.setOnClickListener(this);

        beltImg_two=beltView.findViewById(R.id.beltImgUpId2);
        beltImg_two.setOnClickListener(this);

        return beltView;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.beltUploadBtnId:

                if(uploadTask!=null && uploadTask.isInProgress()){

                    Toast.makeText(getActivity(), "Uploading in Progress", Toast.LENGTH_SHORT).show();

                }else{
                    beltUploadProgrss = new ProgressDialog(v.getContext());
                    beltUploadProgrss.setCancelable(true);
                    beltUploadProgrss.setMessage("Wait!! Product Uploading...");
                    beltUploadProgrss.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    beltUploadProgrss.show();
                    saveData();
                }

                break;


            case R.id.beltImgUpId1:
                imageBeltV=1;
                openFileChooser();
                break;

            case R.id.beltImgUpId2:
                imageBeltV=2;
                openFileChooser2();
                break;

        }
    }


            //----------- SaveData Method work details---------------
            public void saveData(){

                code=beltCode.getText().toString().trim();
                title=beltTitle.getText().toString().trim();
                price=beltPrice.getText().toString().trim();
                des=beltDes.getText().toString().trim();

                if (code.isEmpty()){
                    beltCode.setError("Belt Code");
                    beltCode.requestFocus();
                    return;
                }
                else if (title.isEmpty()){
                    beltTitle.setError("Belt Title");
                    beltTitle.requestFocus();
                    return;
                }

                else if (price.isEmpty()){
                    beltPrice.setError("Belt Price");
                    beltPrice.requestFocus();
                    return;
                }

                else if (des.isEmpty()){
                    beltDes.setError("Belt details");
                    beltDes.requestFocus();
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


                                                Task<Uri> urlTask2 = taskSnapshot.getStorage().getDownloadUrl();

                                                while (!urlTask2.isSuccessful()) ;
                                                Uri downloadUrl2 = urlTask2.getResult();

                                                Upload upload=new Upload(downloadUrl.toString(),downloadUrl2.toString(),code,title,price,des);

                                                String uploadId = databaseReference.push().getKey();
                                                databaseReference.child(uploadId).setValue(upload);

                                                beltUploadProgrss.dismiss();
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

                // }



            }

            private void textFieldClear(){

                beltCode.setText("");
                beltTitle.setText("");
                beltPrice.setText("");
                beltDes.setText("");

                //GALLERY_REQUEST2=2;
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


                super.onActivityResult(requestCode, resultCode, data);

                if (imageBeltV==1 && requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                    mImageUri=data.getData();
                    //Picasso.with(this).load(mImageUri).into(mSelectImage);
                    Picasso.with(getActivity()).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(beltImg_one);
                }else if(imageBeltV==2 && requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                    mImageUri2=data.getData();
                    Picasso.with(getActivity()).load(mImageUri2).networkPolicy(NetworkPolicy.OFFLINE).into(beltImg_two);
                    //Picasso.with(getActivity()).load(mImageUri).networkPolicy(NetworkPolicy.OFFLINE).into(wImage_two);

                }
            }
}
