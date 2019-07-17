package app.alarmservicedemo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView txtSelectDate;
    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtSelectDate = findViewById(R.id.action_text_select_date_and_time);

        txtSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // openDataAndTimePicker();

                displayDate();

            }
        });


    }

    private void openDataAndTimePicker() {


        setAlarm(System.currentTimeMillis());

    }

    int mYear;
    int month;
    int day;

    public void displayDate() {


        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance(); //opening date dialog
        new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        month = monthOfYear + 1;
                        day = dayOfMonth;

                        String specDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        Log.e("MainActivity", "Specified Time: " + specDate);

                        showTimePicker(specDate);

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private TimePickerDialog mTimePicker;

    private void showTimePicker(final String specDate) {
        //opening time dialog
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                int hour = selectedHour;
                int minute = selectedMinute;

                String specTime = selectedHour + ":" + selectedMinute;
                Log.e("MainActivity", "Specified Time: " + specTime);

                try {
                    final Date selectedDateAndTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(specDate + " " + specTime);
                //    Log.e("Selected_mili", "" +);

                    setAlarm( selectedDateAndTime.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, date.getTime().getHours(), date.getTime().getMinutes(), true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setAlarm(long currentTimeMillis) {

        Intent intent = new Intent(this, MyBroadcastReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTimeMillis, pendingIntent);
        Toast.makeText(this, "Alarm set in " + (currentTimeMillis / 1000 * 60) + " seconds",
                Toast.LENGTH_LONG).show();
    }
}
