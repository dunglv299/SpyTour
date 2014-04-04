package com.teusoft.spytour.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.teusoft.spytour.R;
import com.teusoft.spytour.entity.SubmitInfo;
import com.teusoft.spytour.entity.Tour;
import com.teusoft.spytour.util.Constant;
import com.teusoft.spytour.util.HttpRequest;
import com.teusoft.spytour.util.Utils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DungLV on 1/4/2014.
 */
public class BookActivity extends BaseActivity implements View.OnClickListener {
    private Tour tour;
    EditText mFirstName, mLastName, mPastport, mEmail, mQuantity;
    Spinner mSpinner;
    private static String[] optionArray = {"2 star", "3 star", "4 star", "5 star"};
    private String dateBookString;
    Button submitBtn, bookDateBtn;
    Calendar calendar;
    private SubmitInfo submitInfo;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        init();
    }

    private void init() {
        if (getIntent().hasExtra(Constant.TOUR)) {
            tour = (Tour) getIntent().getExtras().get("tour");

            submitInfo = new SubmitInfo();
            calendar = Calendar.getInstance();
            String defaultDate = DateFormat.format("dd/MM/yyyy",
                    calendar).toString();
            submitInfo.setDate(defaultDate);
            bookDateBtn.setText(defaultDate);
        }

    }

    public void initView() {
        setContentView(R.layout.activity_book_tour);
        mFirstName = (EditText) findViewById(R.id.firstName_editText);
        mLastName = (EditText) findViewById(R.id.lastName_editText);
        mPastport = (EditText) findViewById(R.id.passport_editText);
        mEmail = (EditText) findViewById(R.id.email_editText);
        mQuantity = (EditText) findViewById(R.id.quantity_editText);
        mSpinner = (Spinner) findViewById(R.id.option_spinner);
        addItemSpinnerOption();
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        bookDateBtn = (Button) findViewById(R.id.bookDate_btn);
        bookDateBtn.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.pro_dialog);

    }

    public void addItemSpinnerOption() {
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, optionArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String optionSelected = adapterView.getItemAtPosition(pos).toString();
                submitInfo.setOptions(optionSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
                submitInfo.setFirstName(getTextEditText(mFirstName));
                submitInfo.setLastName(getTextEditText(mLastName));
                submitInfo.setPassport(getTextEditText(mPastport));
                submitInfo.setEmail(getTextEditText(mEmail));
                submitInfo.setTourId(String.valueOf(tour.getTourId()));
                submitInfo.setPartnerId("1");
                submitInfo.setQuantity(getTextEditText(mQuantity));

                // Check invalid email
                if (Utils.isValidEmail(submitInfo.getEmail())) {
                    new SubmitInfoAsyntask().execute();
                } else {
                    Toast.makeText(this, "Invalid Email. Let's try again!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bookDate_btn:
                showDatePicker(bookDateBtn, calendar);
                break;
        }
    }

    /**
     * Show time picker dialog
     */
    public void showDatePicker(final Button button, Calendar c) {
        final DatePickerDialog dateDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        String dateBook = DateFormat.format("dd/MM/yyyy",
                                calendar).toString();
                        button.setText(dateBook);
                        // set date
                        submitInfo.setDate(dateBook);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dateDialog.show();
    }

    private class SubmitInfoAsyntask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("firstname", submitInfo.getFirstName()));
            nameValuePairs.add(new BasicNameValuePair("lastname", submitInfo.getLastName()));
            nameValuePairs.add(new BasicNameValuePair("passport", submitInfo.getPassport()));
            nameValuePairs.add(new BasicNameValuePair("email", submitInfo.getEmail()));
            nameValuePairs.add(new BasicNameValuePair("partnerID", submitInfo.getPartnerId()));
            nameValuePairs.add(new BasicNameValuePair("tourID", submitInfo.getTourId()));
            nameValuePairs.add(new BasicNameValuePair("quantity", submitInfo.getQuantity()));
            nameValuePairs.add(new BasicNameValuePair("option", submitInfo.getOptions()));
            nameValuePairs.add(new BasicNameValuePair("bookTime", submitInfo.getDate()));
            String result = null;
            try {
                String jsonResult = HttpRequest.makeHttpRequest(Constant.URL_SUBMMIT, "POST", nameValuePairs);
                Log.e("result", jsonResult);
                JSONObject jObject = new JSONObject(jsonResult);
                result = jObject.getString("status");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            if (result.equals("success")) {
                new AlertDialog.Builder(BookActivity.this)
                        .setMessage("Book successfully")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setResult(Activity.RESULT_OK);
                                BookActivity.this.finish();
                            }
                        })
                        .show();
            } else {
                Utils.showDialog(BookActivity.this, "Book failed");
            }
        }
    }

    private String getTextEditText(EditText editText) {
        if (editText == null) {
            return "";
        }
        return editText.getText().toString();
    }
}
