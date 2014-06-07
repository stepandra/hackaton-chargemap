package org.spend.usefull.chargermap.app;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

@EActivity(R.layout.activity_main)
@WindowFeature({Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
public class MainActivity extends Activity {

    @Override
    public void onBackPressed() {
        WelcomeFragment_ myFragment = (WelcomeFragment_) getFragmentManager().findFragmentByTag(TAG);
        if (myFragment == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.comming_in_from_down, R.anim.comming_out_to_bottom);
            fragmentTransaction.replace(android.R.id.content, new WelcomeFragment_(), TAG).commit();
            fragmentTransaction.remove(getFragmentManager().findFragmentById(R.id.map));
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(android.R.id.content, new WelcomeFragment_(), getClass().getName()).commit();
    }


}
