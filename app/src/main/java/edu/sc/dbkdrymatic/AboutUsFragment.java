package edu.sc.dbkdrymatic;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import javax.annotation.Nullable;

import edu.sc.dbkdrymatic.internal.viewmodels.SettingsModel;




public class AboutUsFragment extends Fragment {


   //private AboutUsModel aboutUsModel;
   View myView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.getContext());

    }


    @Nullable
    @Override
    //Populates view with information from layout file
    public View onCreateView(
            LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_about_us, container, false);
        return myView;
    }

    //On creation of the About us view
    public void onViewCreated(View view, Bundle savedState){
        super.onViewCreated(view, savedState);

        //Go to DBK website when DBK is pressed
        getView().findViewById(R.id.dbk_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_btn("https://dbkusa.com/");
            }
        });

        //Go to DBK facebook page when the fb_icon is pressed
        getView().findViewById(R.id.fb_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_btn("https://www.facebook.com/DBKUSA");
            }
        });

        //Go to DBK Youtube page when the yt_icon is pressed
        getView().findViewById(R.id.yt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_btn("https://www.youtube.com/channel/UCUvOa5FODGDW5aAclrhWsWw");
            }
        });

        //Go to DBK linkedin page when li_icon is pressed
        getView().findViewById(R.id.li_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_btn("https://www.linkedin.com/company/27151163/");
            }
        });

    }

    // This is a helper method that opens a link when a button is clicked
    private void clicked_btn(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}
