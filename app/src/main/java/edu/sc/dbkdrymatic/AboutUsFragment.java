package edu.sc.dbkdrymatic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;

public class AboutUsFragment extends Fragment {

  //private AboutUsModel aboutUsModel;
  View myView;

  public AboutUsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);

    SharedPreferences preferences = PreferenceManager
        .getDefaultSharedPreferences(this.getContext());

  }


  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    myView = inflater.inflate(R.layout.fragment_about_us, container, false);
    getActivity().setTitle("About Us");
    return myView;
  }

  public void onViewCreated(View view, Bundle savedState){
    super.onViewCreated(view, savedState);

    getView().findViewById(R.id.dbk_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clicked_btn("https://dbkusa.com/");
      }
    });

    getView().findViewById(R.id.fb_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clicked_btn("https://www.facebook.com/DBKUSA");
      }
    });

    getView().findViewById(R.id.yt_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clicked_btn("https://www.youtube.com/channel/UCUvOa5FODGDW5aAclrhWsWw");
      }
    });

    getView().findViewById(R.id.li_btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        clicked_btn("https://www.linkedin.com/company/27151163/");
      }
    });

  }

  public void clicked_btn(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }
}
