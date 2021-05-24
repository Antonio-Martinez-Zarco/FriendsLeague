package com.anmaza.friendsleague;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anmaza.friendsleague.entities.Users;
import com.anmaza.friendsleague.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FloatingActionButton show_requires;
    private Button register;
    private CheckBox accept_terms;
    private boolean boolemail, booluser, boolpass, boolconfirmpass, boolterms;
    private TextInputEditText text_email, text_username, text_password, text_confirmpassword;
    private TextInputLayout email, username, password, confirmpassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //TextInputLayout
        email = findViewById(R.id.textInput_email);
        username = findViewById(R.id.textInput_username);
        password = findViewById(R.id.textInput_password);
        confirmpassword = findViewById(R.id.textInput_confirmpassword);

        //TextEditText
        text_email = findViewById(R.id.et_email);
        text_username = findViewById(R.id.et_username);
        text_password = findViewById(R.id.et_password);
        text_confirmpassword = findViewById(R.id.et_confirmpassword);
        accept_terms = findViewById(R.id.check_terms);
        register = findViewById(R.id.btn_register);

        //Floating
        show_requires = findViewById(R.id.show_requires);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();


        show_requires.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequires();
            }
        });
    }

    private boolean validarEmail(Editable email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void onValidate(View view) {

        //Comprobar email

        if (text_email.getText().toString().isEmpty()) {
            email.setError("El campo esta vacío");
        } else if (!validarEmail(text_email.getText())) {
            email.setError("Email no válido");
            boolemail = false;
        } else {
            email.setError(null);
            boolemail = true;
        }

        //Comprobar usuario

        if (text_username.getText().toString().isEmpty()) {
            username.setError("El campo esta vacío");
            booluser = false;
        } else if (text_username.getText().length() > 12) {
            username.setError("Máximo 12 cáracteres");
        } else {
            username.setError(null);
            booluser = true;
        }

        //Comprobar contraseña

        if (text_password.getText().toString().isEmpty()) {
            password.setError("El campo esta vacío");
        } else if (!text_password.getText().toString().matches(Constants.PASSWORD_PATTERN)) {
            password.setError("La contraseña no es válida");
            boolpass = false;
            //1 Mayúscula, 1 Minúscula, 1 número, entre (8-20)
        } else {
            password.setError(null);
            boolpass = true;
        }

        //Comporbar si coinciden las contraseñas

        if (text_confirmpassword.getText().toString().isEmpty()) {
            confirmpassword.setError("El campo esta vacío");
        } else if (!text_confirmpassword.getText().toString().equals(text_confirmpassword.getText().toString())) {
            confirmpassword.setError("Las contraseñas no coinciden");
            boolconfirmpass = false;
        } else {
            confirmpassword.setError(null);
            boolconfirmpass = true;
        }

        //Comprobar términos

        if (!accept_terms.isChecked()) {
            accept_terms.setError("El campo deber marcarse");
            boolterms = false;
        } else {
            accept_terms.setError(null);
            boolterms = true;
        }

        //Comprobar si el registro es correcto
        if (boolemail == true && booluser == true && boolpass == true && boolconfirmpass == true && boolterms == true) {
            confirmRegister();
        }
    }

    private void confirmRegister() {
        String email = text_email.getText().toString();
        String password = text_password.getText().toString();
        String name = text_username.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Users users = new Users(email, password, name);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Usuario registrado!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), VideogamesActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Fallo al registrar!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void showRequires() {

        View mDialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.popup_general, null);
        Button btn_yes = mDialogView.findViewById(R.id.btn_yes);
        Button btn_no = mDialogView.findViewById(R.id.btn_no);
        TextView txt_title = mDialogView.findViewById(R.id.txt_welcome);
        TextView txt_info = mDialogView.findViewById(R.id.txt_information);

        txt_title.setText(R.string.popup_requires);
        txt_info.setText(getString(R.string.text_requires) + "Username -> Máximo 12 carácteres \n" + "Password -> 1 Mayúscula, 1 Número, 8-20 Carácteres.");
        btn_no.setVisibility(View.INVISIBLE);
        btn_yes.setText(R.string.submit);

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setView(mDialogView).setCancelable(false);
        AlertDialog showAlert = builder.show();

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert.dismiss();
            }
        });
    }

}
