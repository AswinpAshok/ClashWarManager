package clasher.clashwarmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    SharedPreferences sharedPreferences;

    @NotEmpty
    private EditText lo_uname;

    @NotEmpty
    @Password(min = 3)
    private EditText lo_pass;

    Validator validator;

    Button loginbtn;
    String luname,lpass;

    public static final String LoginCredentials = "MyPrefs" ;
    public static final String local_uname="";
    public static final String local_pass="";
    public static final String local_role="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences=getSharedPreferences(LoginCredentials,Context.MODE_PRIVATE);

        validator = new Validator(this);
        validator.setValidationListener(this);

        lo_uname=(EditText) findViewById(R.id.log_uname);
        lo_pass=(EditText) findViewById(R.id.log_pass);
        loginbtn=(Button) findViewById(R.id.log_Button);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        lo_uname.setTypeface(custom_font);
        lo_pass.setTypeface(custom_font);
        loginbtn.setTypeface(custom_font);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        luname=lo_uname.getText().toString();
        lpass=lo_pass.getText().toString();
        new loginconnect(getBaseContext(),1).execute(luname,lpass);
        new warfetch(getBaseContext(),1).execute();


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class loginconnect  extends AsyncTask<String,Void,String> {
        private Context context;
        private int byGetOrPost = 0;
        private ProgressDialog progdialog;

        //flag 0 means get and 1 means post.(By default it is get.)
        public loginconnect(Context context,int flag) {
            this.context = context;
            byGetOrPost = flag;

        }

        protected void onPreExecute(){
            super.onPreExecute();
            progdialog = new ProgressDialog(LoginActivity.this);
            progdialog.setMessage("Logging you in...");
            progdialog.setIndeterminate(false);
            progdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progdialog.setCancelable(false);
            progdialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            if(byGetOrPost == 0){ //means by Get Method
                return "";
            }
            else{
                try{
                    String username = (String)arg0[0];
                    String password = (String)arg0[1];

                    String link="https://testcoc.000webhostapp.com/app/login.php";
                    String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());


                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
        }

        @Override
        protected void onPostExecute(String result){

            System.out.println(result);
            progdialog.dismiss();

            if(result.contains("Unable to resolve host")){
                Toast.makeText(getBaseContext(),"Please check your internet connection and try again...",Toast.LENGTH_LONG).show();
            }

            if (result.contains("Logged in..")) {
                String[] x = result.split(",");

                SharedPreferences.Editor editor = getSharedPreferences(Global.PREF_NAME, MODE_PRIVATE).edit();

                editor.putString("local_role",x[1].trim());
                editor.putString("local_uname",x[2].trim());
                editor.putString("local_pass",x[3].trim());
                editor.commit();

                Global.welcome_string=x[0];
                Global.login_status++;
                if(Global.login_status==2) {
                    Toast.makeText(getBaseContext(),Global.welcome_string,Toast.LENGTH_LONG).show();
                    Intent launchNextActivity;
                    launchNextActivity = new Intent(getBaseContext(), MainActivity.class);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(launchNextActivity);
                }
        }
            else if(result.contains("Wrong")) {
                Toast.makeText(getBaseContext(),result,Toast.LENGTH_LONG).show();
            }


    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    }

    class warfetch extends AsyncTask<String, Void, String> {
        private Context context;
        private int byGetOrPost = 0;

        //flag 0 means get and 1 means post.(By default it is get.)
        public warfetch(Context context, int flag) {
            this.context = context;
            byGetOrPost = flag;

        }

        protected void onPreExecute() {
            super.onPreExecute();

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
