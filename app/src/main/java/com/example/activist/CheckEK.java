package com.example.activist;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CheckEK {

    final ArrayList<String> ElementsEK = new ArrayList<>();
    public CheckEK() {

        FillArray();
    }


    public void FillArray()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ElectorKeys")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ElementsEK.add(document.get("EK").toString());

                            }
                            Log.w(TAG, "--------------------------------------------------------------------: "+ElementsEK.size(), task.getException());

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });



    }




}
