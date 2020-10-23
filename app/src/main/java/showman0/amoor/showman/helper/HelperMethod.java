package showman0.amoor.showman.helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class HelperMethod {

    public static void replace(Fragment fragment, FragmentManager supportFragmentManager, int id) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static String getTextFromTil(TextInputLayout textInputLayout) {
        String text = textInputLayout.getEditText().getText().toString().trim();
        return text;
    }
    public static boolean isValidate(Context context, String... values) {
        String[] msgs = {"ProductCode", "ProductName", "SizeInMeters", "TonC", "ProductQuantity","ProductSize"};
        for (int i = 0; i < values.length; i++) {
            boolean empty = TextUtils.isEmpty(values[i]);
            if (empty) {
                showToastMsg(context, msgs[i] + " is empty");
                return false;
            }
        }
        return true;

    }
    public static void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getTextFromSpinner(Spinner spinner) {
        String text = spinner.getSelectedItem().toString().trim();
        return text;
    }

    public static void setSpinnerAdapter(Activity activity, List<String> list, Spinner spinner)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }


    public static void setTilEmpty(TextInputLayout... registerFragmentTilEmailAddress) {
        for (TextInputLayout registerFragmentTilEmailAddress1 : registerFragmentTilEmailAddress) {
            registerFragmentTilEmailAddress1.getEditText().setText("");
        }
    }


    public static void showDateDialog(Activity activity, final TextView textView) {
        final Calendar myCalender = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat mFormat = new DecimalFormat("00", symbols);
                textView.setText((year) + ("-") + (mFormat.format(Double.valueOf(month + 1))) + ("-") + mFormat.format(Double.valueOf(day)));
            }
        }, myCalender.get(YEAR), myCalender.get(MONTH), myCalender.get(DAY_OF_MONTH));
        datePickerDialog.show();

    }


    public static String getCurrentDate() {
        Calendar myCalender = Calendar.getInstance();
        int year = myCalender.get(YEAR);
        int month = myCalender.get(MONTH);
        int day = myCalender.get(DAY_OF_MONTH);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat mFormat = new DecimalFormat("00", symbols);

        return (year) + ("-") + (mFormat.format(Double.valueOf(month + 1))) + ("-") + mFormat.format(Double.valueOf(day));

    }
    public static String getCurrentTime()
    {
        Date date = new Date();
        int hours = date.getHours()-12;
        int minutes = date.getMinutes();
        return hours+":"+minutes;
    }

}
