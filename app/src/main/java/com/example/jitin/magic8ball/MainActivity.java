package com.example.jitin.magic8ball;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.example.jitin.Magic8Ball.MESSAGE";
    public final static String VOICE_RESULT = "com.example.jitin.Magic8Ball.VOICE";
    private static final int REQUEST_CODE = 1234;
    private ListView wordsList;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        Intent intent = new Intent();
        setContentView(R.layout.activity_main);

        Button speakButton = (Button) findViewById(R.id.speakButton);

        //wordsList = (ListView) findViewById(R.id.list);

        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                response(getCurrentFocus());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,    SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
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

    public void response(View view){
        //Intent intent = new Intent(this, ResponseActivity.class);
        String[] fruits = {"It is certain","Reply hazy try again","Don't count on it",
                "It is decidedly so","Ask again later","My reply is no","Without a doubt",
                "Better not tell you now","My sources say no","Yes definitely","Cannot predict now",
                "Outlook not so good","You may rely on it","Concentrate and ask again","Very doubtful",
                "As I see it, yes","Most likely","Outlook good","Yes","Signs point to yes",
        };

        String random = (fruits[new Random().nextInt(fruits.length)]);
        TextView voice_op = (TextView) findViewById(R.id.response_text);
        voice_op.setText(random);
        voice_op.setTextSize(20);

        if(random.contentEquals("Ask again later")||random.contentEquals("Reply hazy try again")
                ||random.contentEquals("Better not tell you now")||random.contentEquals("Cannot predict now")
                ||random.contentEquals("Concentrate and ask again")){
            voice_op.setTextColor(getResources().getColor(R.color.Blue));
        }
        else if(random.contentEquals("Don't count on it")||random.contentEquals("My reply is no")
                ||random.contentEquals("My sources say no")||random.contentEquals("Outlook not so good")
                ||random.contentEquals("Very doubtful")){
            voice_op.setTextColor(getResources().getColor(R.color.Red));
        }
        else{
            voice_op.setTextColor(getResources().getColor(R.color.Green));
        }


        //String random = "response";
        //intent.putExtra(EXTRA_MESSAGE, random);
        //startActivity(intent);
    }

    /**
     * Handle the action of the button being clicked
     */
    public void speakButtonClicked(View v)
    {
        startVoiceRecognitionActivity();
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Alright I'm listening, it's better we do this someplace quiet");
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            Intent intent = new Intent(this, ResponseActivity.class);
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            /*wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));*/
            String result = matches.get(0);
            /*TextView voice_op = (TextView) findViewById(R.id.voice_text);
            voice_op.setText(result);*/

            //intent.putExtra(VOICE_RESULT,result);
            //startActivity(intent);
            response(getCurrentFocus());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}
