package com.infoempire.wavetoget;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerification extends Fragment {

    EditText email;
    Button send;
    ImageView backbut;

    TextView codevertext;
    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmailVerification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreSettingsPage1.
     */
    // TODO: Rename and change types and number of parameters
    public static EmailVerification newInstance(String param1, String param2) {
        EmailVerification fragment = new EmailVerification();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_email_verification, container, false);
        Account.verifiedEmail = "";
        backbut = view.findViewById(R.id.backbutton4);
        email =  view.findViewById(R.id.emailText);
        send =  view.findViewById(R.id.savecred);
        codevertext = view.findViewById(R.id.textView31);

        codevertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeVerification frag = new CodeVerification();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountCreationPrompt frag = new AccountCreationPrompt();
                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify();
            }
        });

        return view;
    }
    public void Verify()
    {
        if(email.getText().toString().isEmpty()){
            //add a error message here
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please fill in the email field...");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }
        if(!isValidEmail(email.getText().toString()))
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please enter a valid email address...");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }
        Map<String, String> fields = new HashMap<>();
        fields.put("action", "email-verification");
        fields.put("email", email.getText().toString());
        fields.put("key", "KEY REMOVED");
        Call<String> call = MainActivity.apiService.createPost(fields);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String _response = response.body();
                Log.d("email-verification", _response);
                if(_response.equals("nosession") || _response.equals("failed") || _response.isEmpty() || _response.equals("false"))
                {
                    //add success message here
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Something went wrong...");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else if(_response.equals("verified"))
                {
                    Account.verifiedEmail = email.getText().toString();
                    LoadAccountCreatePrompt();
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EmailVerification.this);
//                    builder1.setMessage("Email has been verified");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Okay",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
                }
                else
                {
                    SendVerifyEmail(_response);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void LoadAccountCreatePrompt()
    {
        CreateStoreAccount frag = new CreateStoreAccount();
        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
    }
    public void SendVerifyEmail(String genkey)
    {
        String messageContents = "";

        messageContents += "This is a email verification from the WaveToGet app.<br>";
        messageContents += "Please enter the code below into the app to verify your email.<br>";
        messageContents += genkey;
        //messageContents += "<meta http-equiv=\"Content-Security-Policy\" content=\"default-src *; style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://www.wavetoget.com/api4/index.php\">";
//        messageContents += "<form method=\"post\" action=\"https://www.wavetoget.com/api4/index.php\" class=\"inline\">" +
//                "<input type=\"hidden\" name=\"action\" value=\"verify-email\">" +
//                "<input type=\"hidden\" name=\"email\" value=\"" + email.getText().toString() + "\">" +
//                "<input type=\"hidden\" name=\"genkey\" value=\"" + genkey + "\">" +
//                "<button type=\"submit\" name=\"submit_param\" value=\"submit_value\" class=\"link-button\">" +
//                "Click here to verify email." +
//                "</button>" +
//                "</form>";

        try {
            Log.i("SendMailTask", "About to instantiate GMail...");
            GMail androidEmail = new GMail("app@infoempire.com",
                    "Rostislav21", email.getText().toString(), "WaveToGet Email Verification",
                    messageContents, "Email Verification has been sent.  Please check your email.");
            androidEmail.createEmailMessage();
            androidEmail.sendEmail();
            Log.i("SendMailTask good", "Mail Sent.");
        } catch (Exception e) {
            Log.e("SendMailTask fail", e.getMessage(), e);
        }
        //"Store registration request has been sent.  You will be contacted soon."
        //ron@infoempire.com
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Email Verification has been sent.  Please check your email.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        CodeVerification frag = new CodeVerification();
                        getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}