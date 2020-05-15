package com.example.a2nddraft;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

public class getpass extends Fragment {

    private ImageView captureimagebtn ;
    static final int imgreq = 1;
    private Bitmap imageBitmap;
    private Uri uri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView,submit,pass;
    private DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_getpass, container, false);
        captureimagebtn= view.findViewById(R.id.button);
        imageView =view.findViewById(R.id.idcar);
        submit = view.findViewById(R.id.submit);
        pass = view.findViewById(R.id.pass);
        FirebaseApp.initializeApp(getContext());
        reference = FirebaseDatabase.getInstance().getReference();
        captureimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }


            }


        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageBitmap != null) {
                    detectTextFromImage();
                } else
                    Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==imgreq && resultCode == RESULT_OK && data!=null){
            uri= data.getData();

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(imageBitmap);
        }
    }


    private  void detectTextFromImage(){

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextDetector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextDetector.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error" + e, Toast.LENGTH_SHORT).show();
                Log.d("Error", e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText){
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if (blockList.size() == 0){
            Toast.makeText(getActivity(), "No text", Toast.LENGTH_SHORT).show();
        }else {
            int i = -1;
            String string = "";
            for (FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()){
                String text = block.getText();
                string = string + " " + text;
            }
            int index = string.indexOf("To\n") +2;
            System.out.println(index);
            int last_index = string.indexOf("56", index + 20);
            System.out.println(last_index);

            String add = "";

            for(int j=index; j<last_index-2;j++)
            {
                add = add + string.charAt(j);
            }
            //System.out.println(add);
            int new_index = add.indexOf("\n", 1);

            System.out.println(new_index);
            System.out.println(last_index);

            String new_add = "";
            String new_name = "";

            for(int j=1; j<new_index;j++)
            {
                new_name = new_name + add.charAt(j);
            }

            for(int j=new_index+ index + 1; j<last_index -2 ;j++)
            {
                new_add = new_add + string.charAt(j);
            }
            new_add.trim();
            new_add = new_add.replaceAll("[^a-zA-Z0-9 ]", "");
            new_name.trim();
            System.out.println("Add" + new_add);
            System.out.println("Name " + new_name);

            final String finalNew_add = new_add;
            final String finalNew_add1 = new_add;
            final String finalNew_name = new_name;
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(finalNew_add).exists()){
                        System.out.println("Error");
                        Toast.makeText(getActivity(), "Pass Already Issued for your Family", Toast.LENGTH_LONG).show();

                    }
                    else {
                        String id = reference.push().getKey();
                        reference.child(finalNew_add1).child("Name").setValue(finalNew_name);
                        reference.child(finalNew_add1).child("ID").setValue(id);

                        //Alarm Manager
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        Intent intent = new Intent(getContext(), Alarm2Receiver.class);
                        intent.putExtra("ADDR", finalNew_add1);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*60*60*2, pendingIntent);
                        Toast.makeText(getActivity(), "Pass Issued! \nValid for 2 hours", Toast.LENGTH_LONG).show();
                        Getpass(finalNew_add1, finalNew_name, id);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });
        }
    }

    public void Getpass(String add, String name, String id){

        pass.setImageBitmap(null);
        final Paint textPaint = new Paint() {
            {
                setColor(Color.WHITE);
                setTextAlign(Paint.Align.LEFT);
                setTextSize(40f);
                setAntiAlias(true);
            }
        };
        String text = "Pass for : \n\t" + name + "\n\nAddress : \n" +  add + "\n\nGenerated at : \n"
                + java.text.DateFormat.getDateTimeInstance().format(new Date()) +
                "\n\nPass Key : \n" + id;
        String nam = "Pass for : ";
        String time_start= "Generated at : ";
        String time_end= "Valid Till : ";
        String key = "Pass Key : ";
        final Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        final Bitmap bm = Bitmap.createBitmap(650, 750, Bitmap.Config.RGB_565); //use ARGB_8888 for better quality
        final Canvas canvas = new Canvas(bm);
        canvas.drawText(nam, 10, 90f, textPaint);
        canvas.drawText(name, 10, 150f, textPaint);
        canvas.drawText(time_start, 10, 240f, textPaint);
        canvas.drawText(java.text.DateFormat.getDateTimeInstance().format(new Date()), 10, 300f, textPaint);
        canvas.drawText(time_end, 10, 390f, textPaint);
        canvas.drawText( java.text.DateFormat.getDateInstance().format(new Date()) + " " +
                Integer.toString(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2) + ":" +
                Integer.toString(Calendar.getInstance().get(Calendar.MINUTE)), 10, 450f, textPaint);
        canvas.drawText(key, 10, 540f, textPaint);
        canvas.drawText(id, 10, 600f, textPaint);
        System.out.println(text);
        pass.setImageBitmap(bm);

        try {
            Thread.sleep(1*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        Toast.makeText(getContext(), "Setting Alarm", Toast.LENGTH_LONG).show();
        Intent intent_a = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent_a.putExtra(AlarmClock.EXTRA_HOUR, Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 2);
        startActivity(intent_a);

    }
}
