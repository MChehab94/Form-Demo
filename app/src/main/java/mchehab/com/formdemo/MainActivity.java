package mchehab.com.formdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;

    private Spinner spinnerPizzaSize;

    private CheckedTextView checkboxBacon;
    private CheckedTextView checkboxExtraCheese;
    private CheckedTextView checkboxOnion;
    private CheckedTextView checkboxMushroom;

    private EditText editTextTime;
    private EditText editTextDelivery;

    private Button buttonPost;

    private boolean isJSONPosting = false;

    private BroadcastReceiver broadcastReceiverPost = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                String result = bundle.getString("result");
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Result")
                        .setMessage(result)
                        .setPositiveButton("Ok", null)
                        .create()
                        .show();
                isJSONPosting = false;
            }
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverPost, new
                IntentFilter(BroadcastConstants.JSON_POST));
    }

    @Override
    protected void onPause(){
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);

        spinnerPizzaSize = findViewById(R.id.spinnerToppings);

        checkboxBacon = initCheckedTextView(R.id.checkboxBacon);
        checkboxExtraCheese = initCheckedTextView(R.id.checkboxExtraCheese);
        checkboxOnion = initCheckedTextView(R.id.checkboxOnion);
        checkboxMushroom = initCheckedTextView(R.id.checkboxMushroom);

        editTextTime = findViewById(R.id.editTextTime);
        editTextDelivery = findViewById(R.id.editTextDelivery);

        buttonPost = findViewById(R.id.button);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(savedInstanceState != null){
            checkboxBacon.setChecked(savedInstanceState.getBoolean("checkboxBacon"));
            checkboxExtraCheese.setChecked(savedInstanceState.getBoolean("checkboxExtraCheese"));
            checkboxOnion.setChecked(savedInstanceState.getBoolean("checkboxOnion"));
            checkboxMushroom.setChecked(savedInstanceState.getBoolean("checkboxMushroom"));
        }

        setButtonOnClickListener();
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("checkboxBacon", checkboxBacon.isChecked());
        bundle.putBoolean("checkboxExtraCheese", checkboxExtraCheese.isChecked());
        bundle.putBoolean("checkboxOnion", checkboxOnion.isChecked());
        bundle.putBoolean("checkboxMushroom", checkboxMushroom.isChecked());
    }

    private CheckedTextView initCheckedTextView(int id){
        CheckedTextView checkedTextView = findViewById(id);
        checkedTextView.setOnClickListener(e->{
            checkedTextView.toggle();
        });
        return checkedTextView;
    }

    private void setButtonOnClickListener(){
        buttonPost.setOnClickListener(e->{
            try{
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArrayToppings = new JSONArray();

                JSONObject jsonObjectForm = new JSONObject();

                jsonObject.put("custname", editTextName.getText().toString());
                jsonObject.put("custemail", editTextEmail.getText().toString());
                jsonObject.put("custtel", editTextPhone.getText().toString());
                jsonObject.put("size", spinnerPizzaSize.getSelectedItem());
                jsonObject.put("delivery", editTextTime.getText().toString());
                jsonObject.put("comments", editTextDelivery.getText().toString());

                if(checkboxBacon.isChecked()){
                    jsonArrayToppings.put("bacon");
                }
                if(checkboxExtraCheese.isChecked()){
                    jsonArrayToppings.put("cheese");
                }
                if(checkboxOnion.isChecked()){
                    jsonArrayToppings.put("onion");
                }
                if(checkboxMushroom.isChecked()){
                    jsonArrayToppings.put("mushroom");
                }

                if(jsonArrayToppings.length() > 0){
                    jsonObject.put("toppings", jsonArrayToppings);
                }

                jsonObjectForm.put("form", jsonObject);

                new HttpAsyncTask(new WeakReference<Context>(this), BroadcastConstants
                        .JSON_POST, HTTP.POST, jsonObjectForm.toString())
                        .execute("http://httpbin.org/post");
                isJSONPosting = true;

            }catch (JSONException jsonException){
                jsonException.printStackTrace();
            }
        });
    }
}