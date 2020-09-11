package com.example.bookinapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Time;

public class MainActivity extends AppCompatActivity {
    public static final String DEFAULT_ADMIN_EMAIL="jaycjacob@gmail.com";
    private static final String TAG=MainActivity.class.getSimpleName();
    private TextView title;
    private Weekview currentView;
    private Runnable screensaverLauncher;
    private Dialog dialog=null;
/**called when the activity is first created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper.setContext(this.getBaseContext());
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.title);
        currentView = (WeekView) findViewById(R.id.weekView);
        screensaverLauncher = new ScreensaverLauncher(this);

        if (UserDb.get(DEFAULT_ADMIN_EMAIL) == null) {
            User admin = new User(null, "bookin admin", DEFAULT_ADMIN_EMAIL, "012345", -1, true);
            UserDb.store(admin);
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        DataBaseHelper.getInstance().close();
        dismissChangePasswordDialog();
    }
    private void dismissChangePasswordDialog() {
        if (dialog!=null) {
            dialog.dismiss();
            dialog=null;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.week_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.goToday:
                Time now=new Time();
                now.setToNow();
                now.normalize(true);
                currentView.setSelectedDay(now);
                break;
            case R.id.aboutPopup:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.changeAdminPassword:
                showChangePasswordDialog();
                break;
        }
        return false;
    }
    private void showChangePasswordDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.changepassword);
        dialog.setTitle("change administrator password");
        dialog.setCancelable(true);
        dialog.show();

        Button okBt = (Button) dialog.findViewById(R.id.passButtonOk);
        final EditText currentPass = (EditText) dialog.findViewById(R.id.currintpass);
        final EditText newPass = (EditText) dialog.findViewById(R.id.newpass);
        final EditText newPassAgain = (EditText) dialog.findViewById(R.id.newpassagain);

        okBt.setOnClickListener(new View.onClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo admin = UserManager.getUser(DEFAULT_ADMIN_EMAIL);
                if (admin == null) {
                    Toast error = Toast.makeText(MainActivity.this, "Default admin user not found!", Toast.LENGTH_LONG);
                    error.show();
                    dismissChangePasswordDialog();
                } else {
                    String newPassStr = newPass.getText().toString();
                    if (!newPassStr.equals(newPassAgain.getText().toString())) {
                        Toast error = Toast.makeText(MainActivity.this, "New password dont match!", Toast.LENGTH_LONG);
                        error.show();
                    } else {
                        if (currentPass.getText().toString().equals(admin.getPassword())) {
                            UserInfo newAdmin = new UserInfo(admin.getId(), admin.getName(), admin.getEmail(), newPassStr, admin.getSalt());
                            UserManager.updatePassword(newAdmin);
                            Toast error = Toast.makeText(MainActivity.this, "Admin password changed!", Toast.LENGTH_LONG);
                            error.show();
                            dismissChangePasswordDialog();
                        } else {
                            Toast error = Toast.makeText(MainActivity.this, "invalid current password!", Toast.LENGTH_LONG);
                            error.show();
                        }
                    }
                }
            }
        });
        Button cancelBt = (Button) dialog.findViewById(R.id.passButtonCancel);
        cancelBt.setOnClickListener(new view.OnClickListener() {
            @Override
            public void onclick(View v) {
                dismissChangePasswordDialog();
            }
        });
    }

    @Override
        public Runnable getScreensaverLauncher(){
        return screensaverLauncher;
    }
        private static final class ScreensaverLauncher implements Runnable{
            private final MainActivity mainActivity;

            public ScreensaverLauncher(MainActivity mainActivity){
            this.mainActivity=mainActivity;
        }

            @Override
            public void run(){
            Log.d(TAG, "starting screen saver");
              mainActivity.startActivity(new Intent(mainActivity, Screensaver.class));
            }
        }


}