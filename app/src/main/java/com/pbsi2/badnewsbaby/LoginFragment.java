package com.pbsi2.badnewsbaby;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        MaterialButton nextButton = view.findViewById(R.id.next_button);
        final CheckBox defaultKeyBox = view.findViewById(R.id.checkbox);
        defaultKeyBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (defaultKeyBox.isChecked()) {
                    MainActivity.keyText = getString(R.string.default_key);
                    MainActivity.guardianUrl = "https://content.guardianapis.com/search?from-date=2018-12-26&to-date=2018-12-27&show-tags=contributor&q=trump&api-key=0528910f-90e1-4678-b91d-dd32d6f56805";
                    passwordEditText.setEnabled(false);
                    passwordTextInput.setBoxBackgroundColor(getResources().getColor(R.color.colorAccent, null));
                } else {
                    MainActivity.keyText = passwordEditText.getText().toString();
                    MainActivity.guardianUrl = "https://content.guardianapis.com/search?from-date=2018-12-26&to-date=2018-12-27&show-tags=contributor&api-key=" + MainActivity.keyText;
                }
            }
        });
        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewsIntent = new Intent(getActivity(), NewsActivity.class);

                // Start the new activity
                startActivity(NewsIntent);
            }
        });


        return view;
    }

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }
}