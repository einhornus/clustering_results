package com.alexvasilkov.foldablelayout.shading;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.Gravity;

public class SimpleFoldShading implements FoldShading {

    private static final int SHADOW_START_COLOR = 0xFF000000;
    private static final int SHADOW_END_COLOR = 0x00000000;
    //    private static final int SHADOW_COLOR = Color.BLACK;
    private static final int SHADOW_MAX_ALPHA = 192;

    private final Paint mSolidShadow;

    public SimpleFoldShading() {
        mSolidShadow = new Paint();
        mSolidShadow.setShader(new LinearGradient(0, 0, 0, 100, SHADOW_START_COLOR, SHADOW_END_COLOR, Shader.TileMode.CLAMP));
//        mSolidShadow.setColor(SHADOW_COLOR);
    }

    @Override
    public void onPreDraw(Canvas canvas, Rect bounds, float rotation, int gravity) {
        // NO-OP
    }

    @Override
    public void onPostDraw(Canvas canvas, Rect bounds, float rotation, int gravity) {
        float intensity = getShadowIntensity(rotation, gravity);
        if (intensity > 0) {
            int alpha = (int) (SHADOW_MAX_ALPHA * intensity);
            mSolidShadow.setAlpha(alpha);
            canvas.drawRect(bounds, mSolidShadow);
        }
    }

    private float getShadowIntensity(float rotation, int gravity) {
        float intensity = 0;
        if (gravity == Gravity.TOP) {
            if (rotation > -90 && rotation < 0) { // (-90; 0) - rotation is applied
                intensity = -rotation / 90f;
            }
        } else {
            if (rotation > 0 && rotation < 90) { // (0; 90) - rotation is applied
                intensity = rotation / 90f;
            }
        }
        return intensity;
    }

}

--------------------

package edu.berkeley.biomaterials.cellsorter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;


public class ActivityHome extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
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

    public void pickImage(View view){
        startActivity(new Intent(this, ActivityImagePicker.class));
    }

    public void parameters(View view){
        Intent intent = new Intent(this, ActivityParameters.class);
        startActivity(intent);
    }

    public void settings(){

    }
}

--------------------

package com.example.yuanje.nerdlauncher;

import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class NerdLauncherActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new NerdLauncherFragment();
    }
}

--------------------

