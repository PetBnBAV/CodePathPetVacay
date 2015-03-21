package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.petbnbcodepath.R;
import com.codepath.petbnbcodepath.activities.LoginSignupActivity;

import java.util.ArrayList;

/**
 * Created by vibhalaljani on 3/14/15.
 */
public class LoginSignUpFragment extends Fragment {
    private ImageView ivBg;
    private Button btnLogin;
    private Button btnSignUp;
    private TextView tvTerms;

    private OnButtonClickedListener listener;

    private String fontHtmlBeg;
    private String fontHtmlEnd = "</font>";
    private String fontHtmlRedBeg;
    private String fontHtmlRedEnd = "</font>";

    public interface OnButtonClickedListener {
        public void onLogin();
        public void onSignUp();
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnButtonClickedListener) {
            listener = (OnButtonClickedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_signup, container, false);

        setupViews(view);

        return view;
    }

    private void setupViews(View view) {
        fontHtmlBeg = "<font color=\"" + getResources().getColor(R.color.vl_white) + "\">";
        fontHtmlRedBeg = "<font color=\"" + getResources().getColor(R.color.vl_red) + "\">";
        ivBg = (ImageView) view.findViewById(R.id.ivBg);
        tvTerms = (TextView) view.findViewById(R.id.tvTerms);

        /*WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int targetWidth = size.x;

        Drawable bgDrawable = getResources().getDrawable(R.drawable.two_cats_summer_fishing);
        int currWidth = bgDrawable.getIntrinsicWidth();
        int currHeight = bgDrawable.getIntrinsicHeight();
        int origAspectRatio = currWidth / currHeight;
        int targetHeight = targetWidth / origAspectRatio;

        // Load a bitmap from the drawable folder
        Bitmap bMap = BitmapFactory.decodeResource(getResources(),
                R.drawable.two_cats_summer_fishing);
        // Resize the bitmap to 150x100 (width x height)
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, targetWidth, targetHeight, true);
        // Loads the resized Bitmap into an ImageView
        ivBg.setImageBitmap(bMapScaled);*/
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        String[] termsArr = getResources().getStringArray(R.array.terms_array);
        StringBuilder terms = new StringBuilder();
        terms.append(fontHtmlBeg);
        terms.append(fontHtmlEnd);
        terms.append(getResources().getString(R.string.bySigningUp));
        terms.append(" ");
        for (int i = 0; i < termsArr.length; i++) {
            terms.append(fontHtmlRedBeg).append(termsArr[i]).append(fontHtmlRedEnd);
            if (i < termsArr.length - 1) {
                terms.append(fontHtmlBeg).append(getResources().getString(R.string.comma))
                        .append(" ").append(fontHtmlEnd);
            }
            if (i == termsArr.length - 2) {
                terms.append(fontHtmlBeg).append(getResources().getString(R.string.and))
                        .append(" ").append(fontHtmlEnd);
            }
            if (i == termsArr.length - 1) {
                terms.append(fontHtmlBeg).append(getResources().getString(R.string.full_stop))
                        .append(fontHtmlEnd);
            }
        }

        tvTerms.setText(Html.fromHtml(terms.toString()));

        setupViewListeners();
    }

    private void setupViewListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLogin();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSignUp();
            }
        });
    }

    // Creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
