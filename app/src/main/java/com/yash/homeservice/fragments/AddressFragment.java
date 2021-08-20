package com.yash.homeservice.fragments;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yash.homeservice.Global.Const;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.Registeractivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {


    public AddressFragment(String email, String password, String phone,String name,String aadharno,String val) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.Name=name;
        this.aadharno=aadharno;
        Val=val;
    }

    public String Fulladdress,aadharno;
    private EditText flat,street,city,pincode,state;
    private ProgressBar progressBar;
    private Button signup;
    private String Val;
    private TextView signintext;
    private String email,password,phone,Name;


        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Registeractivity)getActivity()).setFragment(new LoginFragment(Const.USER));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetField();
                if (TextUtils.isEmpty(flat.getText().toString().trim()))
                {
                    setError(flat,"Please enter house number");
                    flat.requestFocus();
                }
                else if(TextUtils.isEmpty(street.getText().toString().trim()))
                {
                    setError(street,"Please enter street ");
                    street.requestFocus();
                }else if (TextUtils.isEmpty(city.getText().toString().toLowerCase().trim()))
                {
                    setError(city,"Please enter your city");
                    city.requestFocus();
                }else if (TextUtils.isEmpty(pincode.getText().toString().trim()))
                {
                    setError(pincode,"Please enter pincode number");
                    pincode.requestFocus();
                }else if (pincode.getText().toString().trim().length()!=6)
                {
                    setError(pincode,"Pincode length must be 6");
                    pincode.requestFocus();
                }else if (TextUtils.isEmpty(state.getText().toString().trim()))
                {
                    setError(state,"Please select your state");
                    state.requestFocus();
                }else {
                     Fulladdress= (flat.getText().toString().trim()+","+street.getText().toString().trim()+","+city.getText().toString().trim()+","+state.getText().toString().trim()+"-"+pincode.getText().toString().trim());
                    ((Registeractivity)getActivity()).setFragment(new DetailsFragment(email,password,phone,Fulladdress,Name,aadharno,pincode.getText().toString().trim(),city.getText().toString().toLowerCase().trim(),Val));
                }

            }
        });
    }


    private void init(View view) {
        flat=view.findViewById(R.id.address1);
        street=view.findViewById(R.id.street);
        city=view.findViewById(R.id.city);
        pincode=view.findViewById(R.id.pincode);
        state=view.findViewById(R.id.state);
        progressBar=view.findViewById(R.id.ad_progressbar);
        signup=view.findViewById(R.id.createaccountbtn);
        signintext=view.findViewById(R.id.signintext);

    }
    private void resetField() {
        flat.setBackgroundResource(R.drawable.bg_input_field);
        street.setBackgroundResource(R.drawable.bg_input_field);
        city.setBackgroundResource(R.drawable.bg_input_field);
        pincode.setBackgroundResource(R.drawable.bg_input_field);
        state.setBackgroundResource(R.drawable.bg_input_field);

    }
    public void setError(EditText editText, String message) {
        editText.setBackgroundResource(R.drawable.bg_input_field_error);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
