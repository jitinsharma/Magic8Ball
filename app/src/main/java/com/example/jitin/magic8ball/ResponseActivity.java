package com.example.jitin.magic8ball;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ResponseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width= size.x;
        int height= size.y;

        int offset = (width*height)/9216;


        //setContentView(R.layout.activity_response);
        Intent intent = getIntent();
        //String result = intent.getStringExtra(MainActivity.VOICE_RESULT);
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);


        TextView textView = new TextView(this);

        textView.setTextSize(20);
        //textView.setTextColor(getResources().getColor(R.color.Red));
        textView.setText(message);
        //textView.setText(result);

        int length = message.length();
        int var1 = (width-length)/2 - offset;
        //textView.setText(width);
        int no_line = (width - 2*var1)/length;

        //String var2 = var1.toString();

        textView.setPadding((width-length)/2-offset,100,(width-length)/2-offset,0);

        if(message.contentEquals("Ask again later")||message.contentEquals("Reply hazy try again")
        ||message.contentEquals("Better not tell you now")||message.contentEquals("Cannot predict now")
        ||message.contentEquals("Concentrate and ask again")){
            textView.setTextColor(getResources().getColor(R.color.Blue));
        }
        else if(message.contentEquals("Don't count on it")||message.contentEquals("My reply is no")
                ||message.contentEquals("My sources say no")||message.contentEquals("Outlook not so good")
                ||message.contentEquals("Very doubtful")){
            textView.setTextColor(getResources().getColor(R.color.Red));
        }
        else{
            textView.setTextColor(getResources().getColor(R.color.Green));
        }
        setContentView(textView);
        //textView.setPadding(10,10,10,10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_response, menu);
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

    public void askAgain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
