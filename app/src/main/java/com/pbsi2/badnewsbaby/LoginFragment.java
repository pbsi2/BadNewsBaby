package com.pbsi2.badnewsbaby;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class LoginFragment extends Fragment {
    private Intent NewsIntent;
    @Override
    public View onCreateView(

            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText keyText = view.findViewById(R.id.password_edit_text);
        keyText.setEnabled(true);
        passwordTextInput.setBoxBackgroundColor(getResources().getColor(R.color.design_default_color_background, null));
        MaterialButton nextButton = view.findViewById(R.id.next_button);
        final CheckBox defaultKeyBox = view.findViewById(R.id.checkbox);
        defaultKeyBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (defaultKeyBox.isChecked()) {
                    MainActivity.okeyText = getString(R.string.default_key);
                    MainActivity.guardianUrl = "https://content.guardianapis.com/search?from-date="
                            + MainActivity.startDate
                            + "&to-date="
                            + MainActivity.endDate
                            + "&show-tags=contributor&q=trump&api-key=0528910f-90e1-4678-b91d-dd32d6f56805";
                    keyText.setText("");
                    keyText.setEnabled(false);
                    passwordTextInput.setBoxBackgroundColor(getResources().getColor(R.color.disabled, null));
                } else {
                    keyText.setEnabled(true);
                    passwordTextInput.setBoxBackgroundColor(getResources().getColor(R.color.design_default_color_background, null));

                    MainActivity.okeyText = keyText.getText().toString();
                    MainActivity.guardianUrl = "https://content.guardianapis.com/search?from-date="
                            + MainActivity.startDate
                            + "&to-date="
                            + MainActivity.endDate
                            + "&show-tags=contributor&api-key="
                            + MainActivity.okeyText;

                }

            }
        });
        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (defaultKeyBox.isChecked()) {
                    NewsIntent = new Intent(getActivity(), NewsActivity.class);
                    Toast.makeText(getContext(), "Link: " + MainActivity.guardianUrl,
                            Toast.LENGTH_LONG).show();
                    // Start the new activity
                    startActivity(NewsIntent);
                } else

                    passwordTextInput.setBoxBackgroundColor(getResources().getColor(R.color.design_default_color_background, null));

                MainActivity.okeyText = keyText.getText().toString();
                if (MainActivity.okeyText.isEmpty()) {
                    Toast.makeText(getContext(), "You need to enter a valid KEY",
                            Toast.LENGTH_LONG).show();
                } else {
                    MainActivity.guardianUrl = "https://content.guardianapis.com/search?from-date="
                            + MainActivity.startDate
                            + "&to-date="
                            + MainActivity.endDate
                            + "&show-tags=contributor&api-key="
                            + MainActivity.okeyText;
                    NewsIntent = new Intent(getActivity(), NewsActivity.class);
                    Toast.makeText(getContext(), "Link: " + MainActivity.guardianUrl,
                            Toast.LENGTH_LONG).show();
                    // Start the new activity
                    startActivity(NewsIntent);
                }
            }
        });


        return view;
    }

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */

}