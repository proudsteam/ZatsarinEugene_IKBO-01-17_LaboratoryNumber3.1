package android.example.laboratorynumber31;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.laboratorynumber31.R;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText name;
    EditText secondName;
    EditText family;
    Button print;
    Button addLine;
    Button Ivanov;
    Button Upgrade;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String pathDatabase = "/data/user/0/android.example.laboratorynumber31/databases/Student";
        File file = new File(pathDatabase);
        int currentVersion = 1;
        if (file.exists()) {
            SQLiteDatabase currentDatabase = SQLiteDatabase.openDatabase("/data/user/0/android.example.laboratorynumber31/databases/Student", null, SQLiteDatabase.OPEN_READONLY);
            currentVersion = currentDatabase.getVersion();
        }
        final DBhelper dbhelp = new DBhelper(MainActivity.this,MainActivity.this, currentVersion);
        db = dbhelp.getWritableDatabase();
        Log.d("Zheka",db.getPath());
       // String FioOfStudents[] = {"Golovin Artem Valerievich", "Ulbi Timur Valerievich", "Zatsarin Evgeniy Andreevich", "Ananiev Filipp Alexandrovich", "Arakelyan Andrey Andreevich", "Bugaev Ivan Mihailovich", "Esin Alexandr Sergeevich", "Zagorulko Kostyan Alexandrovich", "Klushin Mihail Alexeevich", "Krukov Mihail Sergeevich", "Kru4kov Matvey Romanovich", "Kuznetsov Nikolay Alexandrovich", "Krasov Alexaey Vitalievich", "Korneev Anton Dmitrievich", "Turina Valeriya Sergeevna", "Suhov Igor Andreevich"};


       // FillingData(FioOfStudents);

        //Initialisation
        print = findViewById(R.id.printInfo);
        addLine = findViewById(R.id.addLine);
        Ivanov = findViewById(R.id.ChangeToIvanov);
        name = findViewById(R.id.name_field);
        secondName = findViewById(R.id.secondName);
        family = findViewById(R.id.fam_field);
        Upgrade = findViewById(R.id.Upgrade);
        if (db.getVersion() != 1)
        {
            Upgrade.setVisibility(View.INVISIBLE);
            Upgrade.setEnabled(false);
        }

        Upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.getVersion() < 2) {
                    int vers = 2;
                    dbhelp.onUpgrade(dbhelp.getReadableDatabase(),db.getVersion(),vers);
                    db = dbhelp.getWritableDatabase();
                    db.setVersion(vers);
                    Log.d("Zheka", "vers1" + db.getVersion());
                }

            }
        });
        Log.d("Zheka", "vers2" + db.getVersion());


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TableActivity.class);
                startActivity(intent);


            }
        });


        addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(name.getText().toString().equals(""))) && (!(secondName.getText().toString().equals(""))) && (!(family.getText().toString().equals("")))) {
                    if (db.getVersion() == 1)
                    {

                        String fio;
                        ContentValues cv = new ContentValues();
                        fio = family.getText().toString() + " " + name.getText().toString() + " " + secondName.getText().toString();
                        cv.put("FIO",fio);
                        Date currentTime = Calendar.getInstance().getTime();
                        String currentDate = DateFormat.getDateInstance().format(currentTime);
                        cv.put("Date", currentDate);
                        db.insert("Student",null,cv);
                    }
                    if (db.getVersion() > 1)
                    {

                        String fio;
                    ContentValues cv = new ContentValues();
                    fio = family.getText().toString() + " " + name.getText().toString() + " " + secondName.getText().toString();
                    Date currentTime = Calendar.getInstance().getTime();
                    String currentDate = DateFormat.getDateInstance().format(currentTime);

                    String fields[] = fio.split(" ");
                    cv.put("name", fields[1]);
                    cv.put("family", fields[0]);
                    cv.put("secondName", fields[2]);
                    cv.put("Date", currentDate);
                    //     Log.d("Zheka", fields[0] + " " + fields[1] + " " +fields[2]);

                    db.insert("Student", null, cv);
                }
                }
            }
        });
        Ivanov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = db.query("Student",null,null,null,null,null,null);

                if (db.getVersion() == 1)
                {


                    int count = cursor.getCount();
                    ContentValues cv = new ContentValues();
                    String countString = "" + count;
                    cv.put("FIO","Ivanov Ivan Ivanovich");
                    Date currentTime = Calendar.getInstance().getTime();
                    String currentDate = DateFormat.getDateInstance().format(currentTime);
                    cv.put("Date", currentDate);
                    db.update("Student",cv,"id = ?", new String[] {countString});
                }
                if (db.getVersion() > 1) {
                    int count = cursor.getCount();
                    ContentValues cv = new ContentValues();
                    String countString = "" + count;
                    cv.put("family", "Ivanov");
                    cv.put("name", "Ivan");
                    cv.put("secondName", "Ivanovich");
                    Date currentTime = Calendar.getInstance().getTime();
                    String currentDate = DateFormat.getDateInstance().format(currentTime);
                    cv.put("Date", currentDate);
                    db.update("Student", cv, "id = ?", new String[]{countString});
                }
                cursor.close();
            }
        });



    }

    void FillingData(String names[])
    {
        ContentValues cv = new ContentValues();
        for (int i=1; i <= 5; i ++)
        {
            Date currentTime = Calendar.getInstance().getTime();
            String currentDate = DateFormat.getDateInstance().format(currentTime);
            cv.put("Date", currentDate);
            if (db.getVersion() == 1) {
                cv.put("FIO", names[new Random().nextInt(names.length)]);
            }
            else
            {
                String mainInfo = names[new Random().nextInt(names.length)];
                String fields[] = mainInfo.split(" ");
                cv.put("family", fields[0]);
                cv.put("name", fields[1]);
                cv.put("secondName", fields[2]);
            }
            db.insert("Student",null,cv);

        }
    }
}


class DBhelper extends SQLiteOpenHelper
{
   // static int DB_VERSION = 1;
    static final String DB_NAME = "Student";
    static final String FIO = "FIO";
    static final String DATE = "Date";
    private Context contextDB;
    private Activity activity;
    public DBhelper( Context context, Activity activity, int version)
    {
        super(context, DB_NAME,null,version);
        this.contextDB = context;
        this.activity = activity;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL("create table " + DB_NAME + " (id integer primary key, " + FIO + " text, " + DATE + " text);"  );
       // File pathDatabase = new File(Environment.getExternalStorageDirectory() + "/data/user/0/android.example.laboratorynumber3/databases/Student");
      // String pathOfDatabse ="/data/user/0/android.example.laboratorynumber3/databases/Student";
      // String path2 = "/data/data/android.example.laboratorynumber3/databases/Student.db";
      // SQLiteDatabase database = SQLiteDatabase.openDatabase(path2,null,SQLiteDatabase.OPEN_READWRITE);
      // Log.d("Zheka",database.getPath());
      // sqLiteDatabase = database;

        sqLiteDatabase.execSQL("create table " + DB_NAME + " (id integer primary key, " + FIO + " text, " + DATE + " text);"  );
        String names[] = {"Golovin Artem Valerievich", "Ulbi Timur Valerievich", "Zatsarin Evgeniy Andreevich", "Ananiev Filipp Alexandrovich", "Arakelyan Andrey Andreevich", "Bugaev Ivan Mihailovich", "Esin Alexandr Sergeevich", "Zagorulko Kostyan Alexandrovich", "Klushin Mihail Alexeevich", "Krukov Mihail Sergeevich", "Kru4kov Matvey Romanovich", "Kuznetsov Nikolay Alexandrovich", "Krasov Alexaey Vitalievich", "Korneev Anton Dmitrievich", "Turina Valeriya Sergeevna", "Suhov Igor Andreevich"};
        ContentValues cv = new ContentValues();
        for (int i=1; i <= 5; i ++)
        {
            Date currentTime = Calendar.getInstance().getTime();
            String currentDate = DateFormat.getDateInstance().format(currentTime);
            cv.put("Date", currentDate);
            cv.put("FIO", names[new Random().nextInt(names.length)]);

            sqLiteDatabase.insert("Student",null,cv);

        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
       //
        Button UpgradeButton = activity.findViewById(R.id.Upgrade);
        UpgradeButton.setEnabled(false);
        UpgradeButton.setVisibility(View.INVISIBLE);
        Log.d("Zheka", "Upgrade");
         sqLiteDatabase.execSQL("create table " + "StudentTest" + " (id integer primary key, family text, name text, secondName text, " + DATE + " text);"  );

        //sqLiteDatabase.execSQL("ALTER TABLE " + DB_NAME + " ADD family text, ADD name text, ADD secondName text");
        Cursor cursor = sqLiteDatabase.query(DB_NAME,new String[] {"FIO,DATE"}, null,null,null,null,null);
        ContentValues cv = new ContentValues();
        int fioIndex = cursor.getColumnIndex("FIO");
        int DateIndex = cursor.getColumnIndex("Date");
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
            do {

                String date = cursor.getString(DateIndex);
                String fioLine = cursor.getString(fioIndex);
                String fields[] = fioLine.split(" ");
                cv.put("family",fields[0]);
                cv.put("name",fields[1]);
                cv.put("secondName",fields[2]);
                cv.put("Date",date);
                sqLiteDatabase.insert("StudentTest",null,cv);



            } while (cursor.moveToNext());

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
            sqLiteDatabase.execSQL("ALTER TABLE StudentTest RENAME to Student");
        }
        cursor.close();
    }

    void checkDB() throws Exception {
        try {
            SQLiteDatabase dbe = SQLiteDatabase
                    .openDatabase(
                            "/data/data/yourpackagename/databases/yourfilename.sqlite",
                            null, 0);
            Log.d("opendb", "EXIST");
            dbe.close();
        } catch (Exception e) {

            AssetManager am = contextDB.getApplicationContext().getAssets();
            OutputStream os = new FileOutputStream(
                    "/data/data/yourpackagename/databases/yourfilename.sqlite");
            byte[] b = new byte[100];

            int r;
            InputStream is = am.open("yourfilename.sqlite");
            while ((r = is.read(b)) != -1) {
                os.write(b, 0, r);
            }
            Log.i("DATABASE_HELPER", "Copying the database ");
            is.close();
            os.close();
        }

    }
}
