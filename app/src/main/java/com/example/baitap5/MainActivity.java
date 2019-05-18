package com.example.baitap5;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "LyBaDong";
    public static List<Student> datastudent = new ArrayList<>();
    public static List<Student> temp_search_list=null;
    public List<Student> filteredList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    Button btnSearch;
    Button btnRefresh;
    Button btnSendMail;
    TextView txtSearch;
    Button btnScan;
    public static TextView txtScanResult;
    public Context temp_context = this;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSearch = findViewById(R.id.txtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnSendMail = findViewById(R.id.btnSendMail);
        btnScan = findViewById(R.id.btnScan);

        ReadStudentData();
        buildRecyclerView();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtSearch.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                String searchString = txtSearch.getText().toString();
                if(searchString.length() == 0 || searchString.length() < 10)
                    Toast.makeText(getApplicationContext(),"ID must be at least 10 characters and is a number.",Toast.LENGTH_LONG).show();
                else
                    filter(searchString);
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                ReadStudentData();
                adapter.filterList(datastudent);
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan a QR code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMail();
            }
        });
        verifyStoragePermissions(MainActivity.this);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected  void SendMail(){
        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath());
        File file = new File(dir, "data.csv");
        try {
            FileOutputStream os = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(os);
            for (Student item : datastudent) {
                LocalDate Date = item.getmUpdatedDay();
                String Day = String.valueOf(Date.getDayOfMonth());
                String Month = String.valueOf(Date.getMonthValue());
                String Year = String.valueOf(Date.getYear());
                String formatDate = Day+"/"+Month+"/"+Year;
                String line = item.getmMSSV()+ ","+item.getmFirstName()+ ","+item.getmLastName()+ ","+item.getmImages()+ ","+formatDate+ ","+item.getmUpdatedTime().toString()+"\n";
                writer.write(line);
            }
            writer.close();
            try{
                String pathdir = Environment.getExternalStorageDirectory().getAbsolutePath();
                File newfile = new File(pathdir,"data.csv");
                Uri path = FileProvider.getUriForFile(MainActivity.this,"com.example.file.provider", newfile);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                String strSubject = "Danh sach điểm danh của ngày (" +formattedDate +") - Lớp DCT1152";
                String strContext = "Chào thầy/cô!\n " +
                        "Em xin gửi bảng danh sách điểm danh của lớp DCT1152 ạ.\n\n" +
                        "Em cám ơn Thầy/cô.\n\n" +
                        "Lý Bá Đông.";

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                String to[] = {"min.lee397@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, strSubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, strContext);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (path != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                }
                startActivity(Intent.createChooser(emailIntent , "Send email..."));
            }
            catch (Exception ex){
                String temp = ex.getMessage();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        ReadStudentData();
        adapter.filterList(datastudent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null) {
                Toast.makeText(this, "You cancel the scanning", Toast.LENGTH_SHORT).show();
            }
            else {
                int j=0;
                String temp_mssv = result.getContents();
                boolean Search_flag = false;
                for(Student temp : datastudent)
                {
                    if (temp.getmMSSV().equals(temp_mssv))
                    {
                        filteredList.add(temp);
                        Search_flag = true;
                        break;
                    }
                }
                if(Search_flag == false)
                    Toast.makeText(getApplicationContext(),"We didn't searched this student",Toast.LENGTH_LONG).show();
                else {
                    Student student_scan = new Student();
                    for(Student temp : datastudent)
                    {
                        if (temp.getmMSSV().equals(temp_mssv))
                        {
                            student_scan=temp;
                            Intent intent = new Intent(this,DetailActivity.class);
                            intent.putExtra("Student_FirstName",student_scan.getmFirstName());
                            intent.putExtra("Student_LastName",student_scan.getmLastName());
                            intent.putExtra("Student_Images",student_scan.getmImages());
                            intent.putExtra("Student_ID",student_scan.getmMSSV());
                            intent.putExtra("Student_Day",student_scan.getmUpdatedDay().toString());
                            intent.putExtra("Student_Time",student_scan.getmUpdatedTime().toString());
                            this.startActivity(intent);
                        }
                    }
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void buildRecyclerView()
    {
        recyclerView=findViewById(R.id.recyclerView_Main);
        adapter = new RecyclerViewAdapter(this,datastudent);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void filter(String searchString)
    {
        boolean Search_flag = false;
        Log.wtf(TAG, "filter - searchString: "+searchString);
        for(Student temp : datastudent)
        {
            if (temp.getmMSSV().equals(searchString))
            {
                filteredList.add(temp);
                Search_flag = true;
                break;
            }
        }
        if(Search_flag == false)
            Toast.makeText(getApplicationContext(),"We didn't searched this student",Toast.LENGTH_LONG).show();
        else {
            Log.wtf(TAG, "filter size:" + filteredList.size());
            adapter.filterList(filteredList);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ReadStudentData()
    {
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput("data.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
            String line="";
            datastudent = new ArrayList<Student>();
            try
            {
                while ((line=reader.readLine())!=null)
                {
                    String[] temp_data = line.split(",");
                    Student student = new Student();
                    student.setmMSSV(temp_data[0]);
                    student.setmFirstName(temp_data[1]);
                    student.setmLastName(temp_data[2]);
                    student.setmImages(temp_data[3]);
                    String stringDate = temp_data[4];
                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
                    Instant instant = date1.toInstant();
                    LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    student.setmUpdatedDay(localDate);

                    String stringTime = temp_data[5];
                    LocalTime localTime = LocalTime.parse(stringTime);
                    student.setmUpdatedTime(localTime);

                    datastudent.add(student);
                    Log.wtf(TAG,"Success add:"+student);

                }
            }
            catch (IOException ex)
            {
                Log.wtf("MyActivity","Error at line:"+line,ex);
                ex.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
            }
        } catch (FileNotFoundException e) {
            try {
                FileOutputStream out = openFileOutput("data.csv",0);
                OutputStreamWriter writer = new OutputStreamWriter(out);
                String line = "3115410001,Kamen Rider,Kuuga,portrait_001,01/01/2019,12:00:00\n" +
                        "3115410002,Kamen Rider,Agito,portrait_002,01/01/2019,12:00:00\n" +
                        "3115410003,Kamen Rider,Ryuki,portrait_003,01/01/2019,12:00:00\n" +
                        "3115410004,Kamen Rider,555,portrait_004,01/01/2019,12:00:00\n" +
                        "3115410005,Kamen Rider,Blade,portrait_005,01/01/2019,12:00:00\n" +
                        "3115410006,Kamen Rider,Kabuto,portrait_006,01/01/2019,12:00:00\n" +
                        "3115410007,Kamen Rider,Den-O,portrait_007,01/01/2019,12:00:00\n" +
                        "3115410008,Kamen Rider,Kiva,portrait_008,01/01/2019,12:00:00\n" +
                        "3115410009,Kamen Rider,Decade,portrait_009,01/01/2019,12:00:00\n" +
                        "3115410010,Kamen Rider,W,portrait_010,01/01/2019,12:00:00";
                writer.write(line);
                writer.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ReadStudentData();
        }

    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
