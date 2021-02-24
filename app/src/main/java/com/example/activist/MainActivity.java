package com.example.activist;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activist.Login.SingIN;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    AlertDialog DialogExitSession;
    ImageView Photo;
    AlertDialog DialogNewActivist;
    AlertDialog DialogDeletActivist;
    AlertDialog DialogInformationActivist;

    File photoFile;
    String AuxPhotoPath;
    Bitmap compressedFile;
    private Uri compressedFilePath;

    String imageFileName;

    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath=null;


    RecyclerView RecyclerViewActivist;
    AdapterRecyclerActivist AdapterElementRecyclerActivist;
    final ArrayList<ElementRecyclerActivist> ElementsRecyclerActivis = new ArrayList<>();

    TextView ElectorKey;

    SharedPreferences sharedPreferences;
    ImageView BtnExit;

    AlertDialog waitUploadData;

    TextView FechaDeEvento;

    int Aux2=0;

    final ArrayList<String> ElementsEK = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int estadoDePermiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // Aquí el usuario dio permisos para acceder a la cámara
        } else {
            // Si no, entonces pedimos permisos...
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }

        int estadoDePermiso2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (estadoDePermiso2 == PackageManager.PERMISSION_GRANTED) {
            // Aquí el usuario dio permisos para acceder a la cámara
        } else {
            // Si no, entonces pedimos permisos...
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        int estadoDePermiso3 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (estadoDePermiso3 == PackageManager.PERMISSION_GRANTED) {
            // Aquí el usuario dio permisos para acceder a la cámara
        } else {
            // Si no, entonces pedimos permisos...
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }



        ElectorKey=findViewById(R.id.ElectorKey);
        BtnExit=findViewById(R.id.imageButton);

        sharedPreferences = this.getSharedPreferences("REGISTRO", MODE_PRIVATE);
        ElectorKey.setText(sharedPreferences.getString("ElectorKey", "Null"));


        FloatingActionButton BtnSaveFirestorage= findViewById(R.id.BtnUpFirestorage);
        FloatingActionButton FAB_NewActivist=findViewById(R.id.FAB_NewActivist);

        RecyclerViewActivist = findViewById(R.id.RecyclerActivist);
        RecyclerViewActivist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        AdapterElementRecyclerActivist = new AdapterRecyclerActivist(ElementsRecyclerActivis,this);
        AdapterElementRecyclerActivist.notifyDataSetChanged();
        RecyclerViewActivist.setAdapter(AdapterElementRecyclerActivist);

        FillRecycler();


        BtnExit.setOnClickListener(v ->
        {
            DialogExit();
            DialogExitSession.show();

        });


        AdapterElementRecyclerActivist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogInformationActivist(
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getId(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getPhotoPath(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getName(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getDirection(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getPhone(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getElectorKey(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getDirectBoss(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getNotes(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getDate(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getSection(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getBirthday(),
                        ElementsRecyclerActivis.get(RecyclerViewActivist.getChildAdapterPosition(v)).getUp()
                );
                DialogInformationActivist.show();

            }
        });

        BtnSaveFirestorage.setOnClickListener(v -> FillArray());

        FAB_NewActivist.setOnClickListener(v ->
        {
            DialogNewActivist();
            DialogNewActivist.show();
        });


    }

    public int Remaining()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Cursor w;
        BD_Calles dbHelper2 = new BD_Calles(this);
        final SQLiteDatabase dbsql2 = dbHelper2.getWritableDatabase();

        int Aux=0;

        w= dbsql2.rawQuery("SELECT _id ,photopath ,name ,direction ,phone ,electorkey ,directboss ,notes ,date ,up    FROM tabladecalles", null);
        w.moveToFirst();
        do
        {
            if((w.getString(w.getColumnIndex("up"))).equals("0")) {
                Aux++;

            }


            }while (w.moveToNext());

        return Aux;
    }


    public void FillArray()
    {
        waitUploadData = newProgressDialog("Subiendo datos a la nube", "NO salga de la aplicacion... ", false);
        waitUploadData.show();
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
                            FillFirebase();
                            Log.w(TAG, "--------------------------------------------------------------------: "+ElementsEK.size(), task.getException());

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });



    }

    public void FillFirebase()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {


            int Aux=Remaining();

            if(Aux>0)
            {
                //waitUploadData.show();
                Aux2=0;
                // Si hay conexión a Internet en este momento
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Cursor w;
                BD_Calles dbHelper2 = new BD_Calles(this);
                final SQLiteDatabase dbsql2 = dbHelper2.getWritableDatabase();

                w= dbsql2.rawQuery("SELECT _id ,photopath ,name ,direction ,phone ,electorkey ,directboss ,notes ,date ,up    FROM tabladecalles", null);
                w.moveToFirst();
                do
                {
                    try {
                        if((w.getString(w.getColumnIndex("up"))).equals("0"))
                        {

                            // Create a new user with a first and last name
                            String AuxId=w.getString(w.getColumnIndex("_id"));
                            Map<String, Object> Activist = new HashMap<>();
                            Activist.put("PotoPath", (w.getString(w.getColumnIndex("photopath"))));
                            Activist.put("Name", (w.getString(w.getColumnIndex("name"))));
                            Activist.put("Direction", (w.getString(w.getColumnIndex("direction"))));
                            Activist.put("Phone", (w.getString(w.getColumnIndex("phone"))));
                            Activist.put("ElectorKey", (w.getString(w.getColumnIndex("electorkey"))));
                            Activist.put("DirectBoss",(w.getString(w.getColumnIndex("directboss"))));
                            Activist.put("Notes", (w.getString(w.getColumnIndex("notes"))));
                            Activist.put("Date", (w.getString(w.getColumnIndex("date"))));
                            Activist.put("Employment", 1815);
                            Activist.put("Section", 1815);

                            Map<String, Object> ElectorKey = new HashMap<>();
                            ElectorKey.put("EK", (w.getString(w.getColumnIndex("electorkey"))));

                            StorageReference mStorage;
                            StorageReference filePath;

                            mStorage = FirebaseStorage.getInstance().getReference();
                            filePath = mStorage.child(w.getString(w.getColumnIndex("electorkey")));


                            ///checar por queno encuentra eventoss regitrados

                            if(CheckEList(w.getString(w.getColumnIndex("electorkey"))))
                            {

                                if(w.getString(w.getColumnIndex("photopath"))!=null)
                                {
                                    File imgFile = new File(w.getString(w.getColumnIndex("photopath")));

                                    if(imgFile.exists()){

                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        compressedFile = Bitmap.createScaledBitmap(myBitmap, 720, 720, true);
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        compressedFile.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                        byte[] data = baos.toByteArray();
                                        filePath.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {



                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                // Add a new document with a generated ID
                                                db.collection("Elements")
                                                        .add(Activist)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {



                                                                db.collection("ElectorKeys")
                                                                        .add(ElectorKey)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {

                                                                                String[] args = new String []{"1",AuxId};

                                                                                dbsql2.execSQL("UPDATE tabladecalles SET up=? WHERE  _id=?",args);

                                                                                Aux2++;
                                                                                if(Aux2==Aux)
                                                                                {
                                                                                    FillRecycler();
                                                                                    waitUploadData.dismiss();
                                                                                }
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w(TAG, "Error adding document", e);
                                                                            }
                                                                        });




                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error adding document", e);
                                                            }
                                                        });

                                            }
                                        });
                                    }

                                }
                                else
                                {

                                    db.collection("Elements")
                                            .add(Activist)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {



                                                    db.collection("ElectorKeys")
                                                            .add(ElectorKey)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {

                                                                    String[] args = new String []{"1",AuxId};

                                                                    dbsql2.execSQL("UPDATE tabladecalles SET up=? WHERE  _id=?",args);

                                                                    Aux2++;
                                                                    if(Aux2==Aux)
                                                                    {
                                                                        FillRecycler();
                                                                        waitUploadData.dismiss();
                                                                    }
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error adding document", e);
                                                                }
                                                            });




                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });

                                }




                            }else
                            {
                                Toast.makeText(this,"Esta persona ya ha sido registrada antes ",Toast.LENGTH_LONG).show();
                                Aux2++;
                                if(Aux2==Aux)
                                {
                                    FillRecycler();
                                    waitUploadData.dismiss();
                                }
                            }


                        }



                    }catch (Exception e)
                    {
                        Toast.makeText(this,"Error ",Toast.LENGTH_LONG).show();

                    }


                }while (w.moveToNext());


            }else
            {Toast.makeText(this, "Todas las personas ya estan registradas :)", Toast.LENGTH_SHORT).show();
                waitUploadData.dismiss();
            }

        } else {
            // No hay conexión a Internet en este momento
            Toast toast1 = Toast.makeText(this, "No se puede actalizar su nube de datos\nTalvez no esta conectado a internet o no tiene los datos moviles activados", Toast.LENGTH_SHORT);toast1.show();
        }


    }

    public void  FillRecycler()
    {
        ElementsRecyclerActivis.clear();
        Cursor w;
        BD_Calles dbHelper2 = new BD_Calles(this);
        final SQLiteDatabase dbsql2 = dbHelper2.getWritableDatabase();


        w= dbsql2.rawQuery("SELECT _id ,photopath ,name ,direction ,phone ,electorkey ,directboss ,notes ,date ,section ,birthday ,up    FROM tabladecalles", null);
        w.moveToFirst();
        do
        {

            try {

                ElementsRecyclerActivis.add(new ElementRecyclerActivist(
                        (w.getString(w.getColumnIndex("_id"))),
                        (w.getString(w.getColumnIndex("photopath"))),
                        (w.getString(w.getColumnIndex("name"))),
                        (w.getString(w.getColumnIndex("direction"))),
                        (w.getString(w.getColumnIndex("phone"))),
                        (w.getString(w.getColumnIndex("electorkey"))),
                        (w.getString(w.getColumnIndex("directboss"))),
                        (w.getString(w.getColumnIndex("notes"))),
                        (w.getString(w.getColumnIndex("date"))),
                        (w.getString(w.getColumnIndex("section"))),
                        (w.getString(w.getColumnIndex("birthday"))),
                        (w.getString(w.getColumnIndex("up"))))

                );


            }catch (Exception e)
            {

            }

            AdapterElementRecyclerActivist.notifyDataSetChanged();
            RecyclerViewActivist.setAdapter(AdapterElementRecyclerActivist);
        }while (w.moveToNext());

    }

    public boolean CheckEList(String EK)
    {
        System.out.println("''''''''''''''''''''''''''''''Entro");
        boolean Aux=true;
        for (int i=0;i<ElementsEK.size();i++)
        {
            System.out.println("&&&&&&&&&&&&&&&&&&&&& ElementsEK.get(i)= "+ElementsEK.get(i)+"    "+"EK= "+EK);
            if(ElementsEK.get(i).equals(EK))
            {
                Aux=false;
            }

        }

        return Aux;

    }


    //Insertar un nuevo comentario
    public void InsertToDB(String PhotoPath,
                           String name,
                           String direction,
                           String phone,
                           String electrokey,
                           String directboss,
                           String notes,
                           String date,
                           String section,
                           String birthday,
                           String up){


        BD_Calles dbHelper = new BD_Calles(this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db != null) {
            if(!name.equals("")&&!direction.equals("")&&!phone.equals("")&&!electrokey.equals("")&&!name.equals(""))
            {
               // if(PhotoPath!=null)

                    ContentValues casa =new ContentValues();

                    casa.put("photopath",PhotoPath);
                    casa.put("name", name);
                    casa.put("direction", direction);
                    casa.put("phone", phone);
                    casa.put("electorkey", electrokey);
                    casa.put("directboss", directboss);
                    casa.put("notes", notes);
                    casa.put("date", date);
                    casa.put("section", section);
                    casa.put("birthday", birthday);
                    casa.put("up", up);


                    db.insert("tabladecalles", null, casa);
                    DialogNewActivist.dismiss();
                    FillRecycler();



            }
            else
            {
                Toast.makeText(this,"Todos Los campos con * deben ser llenados", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void DialogNewActivist()
    {

        currentPhotoPath=null;
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_activist,null);

        Button BtnSave=view.findViewById(R.id.BtnSave);
        Button BtnCancel=view.findViewById(R.id.BtnCancel);

        FloatingActionButton BtnPhoto   =view.findViewById(R.id.FABPhoto);
        Photo                 =view.findViewById(R.id.Photo);
        EditText Name                   =view.findViewById(R.id.EditName);
        EditText Direction             =view.findViewById(R.id.EditDirection);
        EditText ElectorKey             =view.findViewById(R.id.EditElectorKey);
        EditText Notes                =view.findViewById(R.id.EditNotes);
        EditText Phone                  =view.findViewById(R.id.EditPhone);
        EditText Section                  =view.findViewById(R.id.EditSection);
        TextView Birthday                  =view.findViewById(R.id.EditBirthday);



        BtnPhoto.setOnClickListener(v -> {


            dispatchTakePictureIntent();


        });


        Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar Calendario= Calendar.getInstance();
                int Dia=Calendario.get(Calendar.DAY_OF_MONTH);
                int Mes=Calendario.get(Calendar.MONTH);
                int Año=Calendario.get(Calendar.YEAR);

                DatePickerDialog CalendarioDialog=new DatePickerDialog(MainActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        Birthday.setText(dayOfMonth+"/"+month+"/"+year);


                    }
                },Año,Mes,Dia);

                CalendarioDialog.show();
            }
        });


        sharedPreferences = this.getSharedPreferences("REGISTRO", MODE_PRIVATE);

        BtnSave.setOnClickListener(v -> InsertToDB(currentPhotoPath,Name.getText().toString(),Direction.getText().toString(),Phone.getText().toString(),ElectorKey.getText().toString(),sharedPreferences.getString("ElectorKey", "Null"),Notes.getText().toString(),GetDate(),Section.getText().toString(),Birthday.getText().toString(),"0"));
        BtnCancel.setOnClickListener(v -> DialogNewActivist.dismiss());

        builder.setView(view);
        DialogNewActivist =builder.create();
        DialogNewActivist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public String GetDate()
    {
        Calendar Calendario= Calendar.getInstance();
        int Mes=Calendario.get(Calendar.MONTH);
        Mes=Mes+1;
        String Date=Calendario.get(Calendar.DAY_OF_MONTH)+"/"+Mes+"/"+Calendario.get(Calendar.YEAR);

        return Date;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent


        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.activist",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            String filePath2Compress = photoFile.getPath();
            Bitmap bitmap2Compress = BitmapFactory.decodeFile(filePath2Compress);
            compressedFile = Bitmap.createScaledBitmap(bitmap2Compress, 720, 720, true);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), compressedFile, getImageFileName(), null);
            compressedFilePath = Uri.parse(path);

            Toast.makeText(this,path, Toast.LENGTH_SHORT).show();

           Picasso.get()
                    .load(compressedFilePath)
                    .fit()
                    .centerCrop()
                   .rotate(90)
                    .into(Photo);

        }
        else
        {
            currentPhotoPath=null;
        }
    }




    private File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getImageFileName() {
        return imageFileName;
    }


    public void dialogInformationActivist(
            String Id,
            String photoPath,
            String name,
            String direction,
            String phone ,
            String electorKey ,
            String directBoss,
            String notes ,
            String date ,
            String section ,
            String birthday ,
            String up )
    {
        currentPhotoPath=null;

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this).setCancelable(true);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_information_activist,null);

        Button Save=view.findViewById(R.id.BtnSave);
        Button Cancel=view.findViewById(R.id.BtnCancel);

        Photo=view.findViewById(R.id.Photo);

        EditText Name=view.findViewById(R.id.Name);
        EditText Direction=view.findViewById(R.id.Direction);
        EditText Phone=view.findViewById(R.id.Phone);
        EditText ElectorKey=view.findViewById(R.id.ElectorKey);
        EditText Notes=view.findViewById(R.id.Notes);
        EditText Section                  =view.findViewById(R.id.EditSection);
        TextView Birthday                  =view.findViewById(R.id.EditBirthday);

        TextView Auxphoto=view.findViewById(R.id.Date);
        TextView Up=view.findViewById(R.id.Up);


        FloatingActionButton Delet=view.findViewById(R.id.FAB_Delet);
        FloatingActionButton Edit=view.findViewById(R.id.FAB_Edit);

        FloatingActionButton EditPhoto=view.findViewById(R.id.FABPhoto);

        EditPhoto.setEnabled(false);
        Name.setEnabled(false);
        Direction.setEnabled(false);
        Phone.setEnabled(false);
        ElectorKey.setEnabled(false);
        Notes.setEnabled(false);
        Save.setEnabled(false);
        Section.setEnabled(false);
        Birthday.setEnabled(false);

                AuxPhotoPath=photoPath;

        Name.setText(name);
        Direction.setText(direction);
        Phone.setText(phone);
        ElectorKey.setText(electorKey);
        Notes.setText(notes);
        Auxphoto.setText(date);
        Section.setText(section);
        Birthday.setText(birthday);
        Up.setText(up);


        Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar Calendario= Calendar.getInstance();
                int Dia=Calendario.get(Calendar.DAY_OF_MONTH);
                int Mes=Calendario.get(Calendar.MONTH);
                int Año=Calendario.get(Calendar.YEAR);

                DatePickerDialog CalendarioDialog=new DatePickerDialog(MainActivity.this, R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        Birthday.setText(dayOfMonth+"/"+month+"/"+year);


                    }
                },Año,Mes,Dia);

                CalendarioDialog.show();
            }
        });



       try {
           File imgFile = new File(AuxPhotoPath);
           if(imgFile.exists()){

               Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               Photo.setImageBitmap(myBitmap);
           }

       }catch (Exception e)
       {

       }

       Delet.setOnClickListener(v ->
       {


           if(up.equals("0"))
           {
               DialogEliminarActivist(Id);
               DialogDeletActivist.show();
           }
           else
           {
               Toast.makeText(this,"No se pueden borrar Usuarios que ya se subieron a la nube.",Toast.LENGTH_LONG).show();
           }

       });

       Save.setOnClickListener(v ->
       {
           if(currentPhotoPath!=null)
           {
               AuxPhotoPath=currentPhotoPath;
           }

           if(!Name.getText().toString().equals("")&&!Direction.getText().toString().equals("")&&!Phone.getText().toString().equals("")&&!ElectorKey.getText().toString().equals(""))
           {
               BD_Calles dbHelper2 = new BD_Calles(this);
               final SQLiteDatabase dbsql2 = dbHelper2.getWritableDatabase();

               String[] args = new String []{AuxPhotoPath,
                       Name.getText().toString(),
                       Direction.getText().toString(),
                       Phone.getText().toString(),
                       ElectorKey.getText().toString(),
                       Notes.getText().toString(),
                       Section.getText().toString(),
                       Birthday.getText().toString(),Id};
               dbsql2.execSQL("UPDATE tabladecalles SET photopath=? ,name=? ,direction=? ,phone=? ,electorkey=? ,notes=? ,section=? ,birthday=? WHERE  _id=?",args);
               DialogInformationActivist.dismiss();
               FillRecycler();
           }
           else
           {
               Toast.makeText(this,"Todos Los campos con * deben ser llenados", Toast.LENGTH_LONG).show();

           }

       });

       Cancel.setOnClickListener(v ->
       {
           DialogInformationActivist.dismiss();

       });

       Edit.setOnClickListener(v ->
       {
           if(up.equals("0"))
           {
               Save.setEnabled(true);
               EditPhoto.setEnabled(true);
               Name.setEnabled(true);
               Direction.setEnabled(true);
               Phone.setEnabled(true);
               ElectorKey.setEnabled(true);
               Notes.setEnabled(true);
               Section.setEnabled(true);
               Birthday.setEnabled(true);
           }
           else
           {
               Toast.makeText(this,"No se pueden editar Usuarios que ya se subieron a la nube.",Toast.LENGTH_LONG).show();
           }


       });

       EditPhoto.setOnClickListener(v ->
       {
           dispatchTakePictureIntent();


       });
        builder.setView(view);
        DialogInformationActivist =builder.create();
        DialogInformationActivist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void DialogExit()
    {

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_aceptar_cancelar,null);

        TextView Titulo =view.findViewById(R.id.TituloAC);
        TextView Descripcion =view.findViewById(R.id.DescripcionAC);
        Button BtnAceptar=view.findViewById(R.id.BtnAceptarDialogEliminarEvento);
        Button BtnCancelar=view.findViewById(R.id.BtnCancelarDialogEliminarEvento);

        Titulo.setText("Desea cerrar sesion?");
        Descripcion.setText("Esto solo se recomienda si introdujo una clave electoral erronea, las personas que usted agrego no se perderan por esta acción.");

        BtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sharedPreferences = getSharedPreferences("REGISTRO", MODE_PRIVATE);
                sharedPreferences.edit().putInt("Registrado",0).apply();

                Intent intent = new Intent(getApplicationContext(), SingIN.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        BtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogExitSession.dismiss();

            }
        });



        builder.setView(view);
        DialogExitSession =builder.create();
        DialogExitSession.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void DialogEliminarActivist(final String EntId)
    {

        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_aceptar_cancelar,null);

        TextView Titulo =view.findViewById(R.id.TituloAC);
        TextView Descripcion =view.findViewById(R.id.DescripcionAC);
        Button BtnAceptar=view.findViewById(R.id.BtnAceptarDialogEliminarEvento);
        Button BtnCancelar=view.findViewById(R.id.BtnCancelarDialogEliminarEvento);

        Titulo.setText("Desea borrar a esta persona?");
        Descripcion.setText("Esto accion no se puede deshacer. pero si puede agregar a esta persona nuevamente");

        Cursor w;
        BD_Calles dbHelper2 = new BD_Calles(this);
        final SQLiteDatabase dbsql2 = dbHelper2.getWritableDatabase();

        BtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] args = new String[]{EntId};
                dbsql2.delete("tabladecalles", "_id=?", args);
                DialogDeletActivist.dismiss();
                DialogInformationActivist.dismiss();
                FillRecycler();


            }
        });

        BtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDeletActivist.dismiss();

            }
        });



        builder.setView(view);
        DialogDeletActivist =builder.create();
        DialogDeletActivist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public ProgressDialog newProgressDialog(String Title, String Message, boolean isCancelable){
        ProgressDialog nPD = new ProgressDialog(this);
        nPD.setTitle(Title); // Setting Title
        nPD.setMessage(Message); // Setting Message
        nPD.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        if (isCancelable){
            nPD.setCancelable(true);
        } else {
            nPD.setCancelable(false);
        }
        return nPD;
    }


}