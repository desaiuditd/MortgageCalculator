package in.incognitech.mortgagecalculator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import in.incognitech.mortgagecalculator.util.NumberTextWatcher;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private float decimal_interest_rate;
    private int loan_term;
    private boolean tax_insurance;

    public float getDecimal_interest_rate() {
        return decimal_interest_rate;
    }

    public void setDecimal_interest_rate(float decimal_interest_rate) {
        this.decimal_interest_rate = decimal_interest_rate;
    }

    public int getLoan_term() {
        return loan_term;
    }

    public void setLoan_term(int loan_term) {
        this.loan_term = loan_term;
    }

    public boolean isTax_insurance() {
        return tax_insurance;
    }

    public void setTax_insurance(boolean tax_insurance) {
        this.tax_insurance = tax_insurance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MainActivity that = this;

        final EditText amt_borrowed = ( EditText ) findViewById( R.id.amt_borrowed );
        amt_borrowed.addTextChangedListener(new NumberTextWatcher(amt_borrowed));
        amt_borrowed.requestFocus();

        SeekBar interest_rate = ( SeekBar ) findViewById( R.id.interest_rate );
        interest_rate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                that.setDecimal_interest_rate((float) progress / 10);
                TextView interest_rate_value = (TextView) findViewById(R.id.interest_rate_value);
                interest_rate_value.setText(String.valueOf(that.getDecimal_interest_rate()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        interest_rate.setProgress(50);

        RadioGroup loan_term = ( RadioGroup ) findViewById( R.id.loan_term_group );
        loan_term.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int lt;
                switch (checkedId) {
                    case R.id.loan_term_7:
                        lt = 7;
                        break;
                    case R.id.loan_term_15:
                        lt = 15;
                        break;
                    case R.id.loan_term_30:
                        lt = 30;
                        break;
                    default:
                        lt = 7;
                        break;
                }
                that.setLoan_term(lt);
            }
        });

        CheckBox tax_insurance = ( CheckBox ) findViewById( R.id.tax_insurance );
        tax_insurance.setOnCheckedChangeListener( new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                that.setTax_insurance( isChecked );
            }
        } );

        this.setLoan_term(7);
        this.setTax_insurance(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amt_borrowed_str = ((EditText) findViewById(R.id.amt_borrowed)).getText().toString().replace(",", "");

                if (amt_borrowed_str.equals("")) {
                    Toast.makeText(that.getApplicationContext(), R.string.amt_borrowed_error, Toast.LENGTH_LONG).show();
                    return;
                }

                float amt_borrowed = Float.parseFloat(amt_borrowed_str);
                float monthly_payment;

                float tax = that.isTax_insurance() ? amt_borrowed * (float) 0.001 : (float) 0.0;
                float months = that.getLoan_term() * 12;

                if (that.getDecimal_interest_rate() == 0) {
                    monthly_payment = (amt_borrowed / months) + tax;
                } else {
                    float interest = that.getDecimal_interest_rate() / 1200;
                    monthly_payment = (float) (amt_borrowed * (interest / (1 - Math.pow((double) 1 + interest, (double) -1 * months)))) + tax;
                }

                ((TextView) findViewById(R.id.monthly_payment)).setText(String.valueOf(monthly_payment));
                ((TextView) findViewById(R.id.monthly_payment_label)).setVisibility(TextView.VISIBLE);
                ((TextView) findViewById(R.id.monthly_payment)).setVisibility(TextView.VISIBLE);
            }
        });
    }
}
