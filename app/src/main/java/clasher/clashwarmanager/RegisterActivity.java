package clasher.clashwarmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private EditText username;

    @Password(min=3)
    @NotEmpty
    private EditText password;

    @ConfirmPassword
    @NotEmpty
    private EditText conpass;

    @NotEmpty
    private EditText playerhashtag;

    Button register;
    String uname,pass,hash;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=(EditText) findViewById(R.id.uname);
        password=(EditText) findViewById(R.id.password);
        conpass=(EditText) findViewById(R.id.conpass);
        playerhashtag=(EditText) findViewById(R.id.playerhash);

        validator = new Validator(this);
        validator.setValidationListener(this);

        register=(Button) findViewById(R.id.registerButton);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        username.setTypeface(custom_font);
        password.setTypeface(custom_font);
        conpass.setTypeface(custom_font);
        playerhashtag.setTypeface(custom_font);
        register.setTypeface(custom_font);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        uname=username.getText().toString();
        pass=password.getText().toString();
        hash=playerhashtag.getText().toString();
        new connect(getBaseContext(),1).execute(uname,pass,hash);
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

    class connect  extends AsyncTask<String,Void,String> {
        private Context context;
        private int byGetOrPost = 0;
        private ProgressDialog progdialog;

        //flag 0 means get and 1 means post.(By default it is get.)
        public connect(Context context,int flag) {
            this.context = context;
            byGetOrPost = flag;

        }

        protected void onPreExecute(){
            super.onPreExecute();
            progdialog = new ProgressDialog(RegisterActivity.this);
            progdialog.setMessage("Registering...");
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
                    String hash = (String)arg0[2];

                    String link="https://testcoc.000webhostapp.com/app/register.php";
                    String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("userhash", "UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8");
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

            Toast.makeText(context, result, Toast.LENGTH_LONG).show();

            if(result.contains("Registered successfully"))
            {
//                Intent intent=new Intent(getBaseContext(),LoginActivity.class);
//                startActivity(intent);
                finish();
            }


        }


    }

    ////////////////////////////////////////////////////////////////////////////////////////////
}
