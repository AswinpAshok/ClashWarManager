package clasher.clashwarmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FirstActivity extends AppCompatActivity {

    Button reg, login;
    ActionBar actionBar;
    AVLoadingIndicatorView av;
    TextView t1;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /////////SPLASH SCREEN GOES HERE//////////
        SharedPreferences prefs = getSharedPreferences(Global.PREF_NAME, MODE_PRIVATE);
        String uname = prefs.getString("local_uname", "");
        String pass = prefs.getString("local_pass", "");

        if (!uname.equals("")) {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            actionBar = getSupportActionBar();
            actionBar.hide();

            setContentView(R.layout.splash);

            t1 = (TextView) findViewById(R.id.splashtext);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Capture_it.ttf");
            t1.setTypeface(custom_font);
            av = (AVLoadingIndicatorView) findViewById(R.id.avload);
            new firstconnect(getBaseContext(), 1).execute(uname, pass);
            new warfetch(getBaseContext(),1).execute();

            ////////////////////////////////////////

        } else {
            setContentView(R.layout.activity_first);
            reg = (Button) findViewById(R.id.regButton);
            login = (Button) findViewById(R.id.loginButton);
            Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
            reg.setTypeface(custom_font);
            login.setTypeface(custom_font);
            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

        }
    }


    class firstconnect extends AsyncTask<String, Void, String>
    {
        private Context context;
        private int byGetOrPost = 0;
        private ProgressDialog progdialog;

        //flag 0 means get and 1 means post.(By default it is get.)
        public firstconnect(Context context, int flag) {
            this.context = context;
            byGetOrPost = flag;

        }

        protected void onPreExecute() {
            super.onPreExecute();
            av.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            if (byGetOrPost == 0) { //means by Get Method
                return "";
            } else {
                try {
                    String username = (String) arg0[0];
                    String password = (String) arg0[1];

                    String link = "https://testcoc.000webhostapp.com/app/login.php";
                    String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());


                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {


            if (result.contains("Unable to resolve host")) {
                Toast.makeText(getBaseContext(), "Please check your internet connection and try again...", Toast.LENGTH_LONG).show();
                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnable, 2000);

            }

            if (result.contains("Logged in..")) {
                String[] x = result.split(",");
                Global.welcome_string=x[0];
                Global.login_status++;
                System.out.println("********************** got result from login ************************");
                if(Global.login_status==2) {
                    av.hide();
                    Toast.makeText(getBaseContext(), Global.welcome_string, Toast.LENGTH_LONG).show();
                    Intent launchNextActivity;
                    launchNextActivity = new Intent(getBaseContext(), MainActivity.class);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(launchNextActivity);
                }
            }
        }

        private Runnable mMyRunnable = new Runnable() {
            @Override
            public void run() {

                finish();
                moveTaskToBack(true);
            }
        };

        ////////////////////////////////////////////////////////////////////////////////////////////
    }


    class warfetch extends AsyncTask<String, Void, String> {
        private Context context;
        private int byGetOrPost = 0;

        public warfetch(Context context, int flag) {
            this.context = context;
            byGetOrPost = flag;

        }

        protected void onPreExecute() {
            super.onPreExecute();
            av.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            if (byGetOrPost == 0) { //means by Get Method
                return "";
            } else {
                try {


                    String link = "https://testcoc.000webhostapp.com/app/warlist.php";

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());


                    wr.write(link);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {


            if (result.contains("Unable to resolve host")) {
                Toast.makeText(getBaseContext(), "Please check your internet connection and try again...", Toast.LENGTH_LONG).show();

            } else {
                try {

                    String[] x = result.split(",");
                    Global.warlist=new String[x.length];
                    for(int i=0;i<x.length;i++)
                    {
                        Global.warlist[i]=x[i];
                        System.out.println(Global.warlist[i]);
                    }
                    System.out.println("*******************Got result from warlist*******************");
                    Global.login_status++;
                    if(Global.login_status>=2) {
                        Toast.makeText(getBaseContext(), Global.welcome_string, Toast.LENGTH_LONG).show();
                        av.hide();
                        Intent launchNextActivity;
                        launchNextActivity = new Intent(getBaseContext(), MainActivity.class);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(launchNextActivity);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
