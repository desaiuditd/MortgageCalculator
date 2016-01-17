package in.incognitech.mortgagecalculator.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by udit on 16/01/16.
 */
public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText et;

    public NumberTextWatcher(EditText et) {
        this.df = new DecimalFormat("#,###.##");
        this.df.setDecimalSeparatorAlwaysShown(true);
        this.dfnd = new DecimalFormat("#,###");
        this.et = et;
        this.hasFractionalPart = false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if ( s.toString().contains( String.valueOf( this.df.getDecimalFormatSymbols().getDecimalSeparator() ) ) ) {
            this.hasFractionalPart = true;
        } else {
            this.hasFractionalPart = false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        this.et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = this.et.getText().length();

            String v = s.toString().replace(String.valueOf(this.df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = this.df.parse(v);
            int cp = this.et.getSelectionStart();
            if (this.hasFractionalPart) {
                this.et.setText(this.df.format(n));
            } else {
                this.et.setText(this.dfnd.format(n));
            }
            endlen = this.et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= this.et.getText().length()) {
                this.et.setSelection(sel);
            } else {
                // place cursor at the end?
                this.et.setSelection(this.et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        this.et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
}
