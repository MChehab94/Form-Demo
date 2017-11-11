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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editTextName) protected EditText editTextName;
    @BindView(R.id.editTextEmail) protected EditText editTextEmail;
    @BindView(R.id.editTextPhone) protected EditText editTextPhone;

    @BindView(R.id.spinnerToppings) protected Spinner spinnerPizzaSize;

    @BindView(R.id.checkboxBacon) protected CheckedTextView checkboxBacon;
    @BindView(R.id.checkboxExtraCheese) protected CheckedTextView checkboxExtraCheese;
    @BindView(R.id.checkboxOnion) protected CheckedTextView checkboxOnion;
    @BindView(R.id.checkboxMushroom) protected CheckedTextView checkboxMushroom;

    @BindView(R.id.editTextTime) protected EditText editTextTime;
    @BindView(R.id.editTextDelivery) protected EditText editTextDelivery;

    @BindView(R.id.button) protected Button buttonPost;

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

        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        checkboxBacon.setOnClickListener(e -> checkboxBacon.toggle());
        checkboxExtraCheese.setOnClickListener(e -> checkboxExtraCheese.toggle());
        checkboxMushroom.setOnClickListener(e -> checkboxMushroom.toggle());
        checkboxOnion.setOnClickListener(e -> checkboxOnion.toggle());

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