package mchehab.com.formdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

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
}