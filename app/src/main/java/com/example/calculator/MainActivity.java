package com.example.calculator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText firstNumberInput;
    private EditText secondNumberInput;
    private EditText resultEditText;
    private Button addButton;
    private Button subButton;
    private Button mulButton;
    private Button divButton;
    private Button piButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Binding
        firstNumberInput = (EditText) findViewById(R.id.firstNumberInput);
        secondNumberInput = (EditText) findViewById(R.id.secondNumberInput);
        resultEditText = (EditText) findViewById(R.id.resultEditText);

        addButton = (Button) findViewById(R.id.addButton);
        subButton = (Button) findViewById(R.id.subButton);
        mulButton = (Button) findViewById(R.id.mulButton);
        divButton = (Button) findViewById(R.id.divButton);
        piButton = (Button) findViewById(R.id.piButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Handle actions
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBound) {
                    Double value1 = getValueFromInput(firstNumberInput);
                    Double value2 = getValueFromInput(secondNumberInput);

                    if (value1 != null && value2 != null) {
                        double result = logicService.add(value1, value2);
                        setResult(result);
                    }
                }
            }
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBound) {
                    Double value1 = getValueFromInput(firstNumberInput);
                    Double value2 = getValueFromInput(secondNumberInput);

                    if (value1 != null && value2 != null) {
                        double result = logicService.sub(value1, value2);
                        setResult(result);
                    }
                }
            }
        });

        divButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBound) {
                    Double value1 = getValueFromInput(firstNumberInput);
                    Double value2 = getValueFromInput(secondNumberInput);

                    if (value1 != null && value2 != null) {
                        double result = logicService.div(value1, value2);
                        setResult(result);
                    }
                }
            }
        });

        mulButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mBound) {
                    Double value1 = getValueFromInput(firstNumberInput);
                    Double value2 = getValueFromInput(secondNumberInput);

                    if (value1 != null && value2 != null) {
                        double result = logicService.mul(value1, value2);
                        setResult(result);
                    }
                }
            }
        });

        piButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressBar.setProgress(0);
                new PiComputeTask().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    LogicService logicService;
    boolean mBound = false;
    private ServiceConnection logicConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LogicService.LocalBinder binder = (LogicService.LocalBinder) service;
            logicService = binder.getService();
            mBound = true;
            Toast.makeText(MainActivity.this, "Logic Service Connected!",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            logicService = null;
            mBound = false;
            Toast.makeText(MainActivity.this, "Logic Service Disconnected!",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            this.bindService(new Intent(MainActivity.this, LogicService.class),
                    logicConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            mBound = false;
            this.unbindService(logicConnection);
        }
    }

    private Double getValueFromInput(EditText editTextInput) {
        String inputTextValue = editTextInput.getText().toString();
        if (inputTextValue.equals("")) {
            return null;
        }
        return Double.parseDouble(inputTextValue);
    }

    private void setResult(double result) {
        resultEditText.setText(Double.toString(result));
    }

    private class PiComputeTask extends AsyncTask<Void, Integer, Double> {
        protected Double doInBackground(Void... voids) {
            int k = 0;
            int n = 1000000;
            double x, y;
            for (int i = 0; i < n; i++) {
                x = Math.random();
                y = Math.random();
                if (x * x + y * y <= 1) k++;

                if (i % 1000 == 0) {
                    publishProgress(i*100/n);
                }
            }
            return 4. * k / n;
        }

        protected void onPostExecute(Double result) {
            firstNumberInput.setText(result.toString());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }
}
