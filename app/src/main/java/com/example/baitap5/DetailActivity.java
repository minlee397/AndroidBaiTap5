package com.example.baitap5;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.baitap5.MainActivity.datastudent;

public class DetailActivity extends AppCompatActivity {
    public static final String TAG = "HoangDangKhoa";
    TextView txtFirstName, txtLastName, txtStudentID, txtDay, txtTime, txtUpdate;
    ImageView imgPortrait;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getLayout();
        final Intent intent = getIntent();
        String mFirstName = intent.getStringExtra("Student_FirstName");
        String mLastName = intent.getStringExtra("Student_LastName");
        String mImages = intent.getStringExtra("Student_Images");
        String mStudentID = intent.getStringExtra("Student_ID");
        String mDay = intent.getStringExtra("Student_Day");
        String mTime = intent.getStringExtra("Student_Time");

        LocalTime timeNow = java.time.LocalTime.now();
        DateTimeFormatter fmt =  DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatTime = timeNow.format(fmt);

        String[] arrTimeLastet = mTime.split("\\.");

        txtFirstName.setText(mFirstName);
        txtLastName.setText(mLastName);
        txtStudentID.setText(mStudentID);
        txtUpdate.setText(java.time.LocalDate.now().toString()+ " At " + formatTime);
        txtDay.setText(mDay + " At "+arrTimeLastet[0]);

        int resID = getResources().getIdentifier(mImages,"drawable",getPackageName());
        imgPortrait.setImageResource(resID);

        for (int i=0;i<datastudent.size();i++)
        {
            Student tempstudent =  datastudent.get(i);
            if(tempstudent.getmMSSV().equals(mStudentID))
            {
                datastudent.get(i).setmUpdatedDay(java.time.LocalDate.now());
                datastudent.get(i).setmUpdatedTime(java.time.LocalTime.now());
                break;
            }
        }
        writeOnFile();
        for (int i=0;i<datastudent.size();i++) {
            Log.wtf(TAG,"after:"+datastudent.get(i).getmLastName()+" "+datastudent.get(i).getmUpdatedDay()+" "+datastudent.get(i).getmUpdatedTime());
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeOnFile(){
        try {
            FileOutputStream out = openFileOutput("data.csv",0);
            OutputStreamWriter writer = new OutputStreamWriter(out);
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
            Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLayout()
    {
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtStudentID = findViewById(R.id.txtStudentID);
        txtDay = findViewById(R.id.txtDay);
        txtTime = findViewById(R.id.txtTime);
        imgPortrait = findViewById(R.id.imgPortrait);
        txtUpdate = findViewById(R.id.txtUpdate);
    }
    public void setPortrait (String id) {

        if (id.equals("3115410001")) {
            imgPortrait.setImageResource(R.drawable.portrait_001);
        } else if (id.equals("3115410002")) {
            imgPortrait.setImageResource(R.drawable.portrait_002);
        } else if (id.equals("3115410003")) {
            imgPortrait.setImageResource(R.drawable.portrait_003);
        } else if (id.equals("3115410004")) {
            imgPortrait.setImageResource(R.drawable.portrait_004);
        } else if (id.equals("3115410005")) {
            imgPortrait.setImageResource(R.drawable.portrait_005);
        } else if (id.equals("3115410006")) {
            imgPortrait.setImageResource(R.drawable.portrait_006);
        } else if (id.equals("3115410007")) {
            imgPortrait.setImageResource(R.drawable.portrait_007);
        } else if (id.equals("3115410008")) {
            imgPortrait.setImageResource(R.drawable.portrait_008);
        } else if (id.equals("3115410009")) {
            imgPortrait.setImageResource(R.drawable.portrait_009);
        } else if (id.equals("3115410010")) {
            imgPortrait.setImageResource(R.drawable.portrait_010);
        }
    }
}

