package org.spend.usefull.chargermap.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;

@EFragment(R.layout.fragment_welcome)
public class WelcomeFragment extends Fragment {

    @SystemService
    protected ConnectivityManager connectivity;

    @Click({R.id.imageButtonHeader, R.id.relativeLayout})
    public void openMap() {
        if (isConnectionToInternet()) {
            FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.comming_in_from_down, R.anim.comming_out_to_bottom);
            fragmentTransaction.replace(android.R.id.content, new MapSpecialFragment_()).commit();
        } else {
            Toast.makeText(getActivity(), R.string.please_connect_to_the_internet, Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isConnectionToInternet() {
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info == null)
                return false;
            for (NetworkInfo anInfo : info) {
                if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
