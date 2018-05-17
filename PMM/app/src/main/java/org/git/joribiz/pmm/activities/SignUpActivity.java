package org.git.joribiz.pmm.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.data.SQLiteHelper;
import org.git.joribiz.pmm.data.UserDAO;
import org.git.joribiz.pmm.model.User;

public class SignUpActivity extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailText = findViewById(R.id.activity_sign_up_email);
        passwordText = findViewById(R.id.activity_sign_up_password);
        signUpButton = findViewById(R.id.activity_sign_up_signUpButton);
        TextView loginLink = findViewById(R.id.activity_sign_up_loginLink);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volvemos a la actividad de login
                finish();
            }
        });
    }

    /**
     * La lógica de registro se implementa en este método. Comprueba que los datos introducidos por
     * el usuario tienen un formato válido y ejecuta un insert en la base de datos en un nuevo
     * hilo para comprobar que dicho usuario existe.
     */
    public void signUp() {
        if (!validate()) {
            onSignUpFailed();
            return;
        }
        signUpButton.setEnabled(false);

        InputMethodManager manager = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.creating_account_message));
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(getApplicationContext());
                UserDAO userDAO = new UserDAO(sqLiteHelper);
                // Comprobamos que el email del nuevo usuario no esté ya en la base de datos.
                Cursor cursor = userDAO.getUserByEmail(email);

                if (cursor.moveToFirst()) {
                    emailText.post(new Runnable() {
                        @Override
                        public void run() {
                            emailText.setError(getResources()
                                    .getString(R.string.error_email_already_used));
                            onSignUpFailed();
                        }
                    });
                } else {
                    userDAO.insertUser(new User(email, password));
                    onSignUpSuccess(email);
                }
                cursor.close();
                sqLiteHelper.close();
                progressDialog.dismiss();
            }
        }, 3000);
    }

    /**
     * Activa el botón de registro de nuevo y le comunica a LoginActivity que el usuario se ha
     * registrado con éxito.
     */
    private void onSignUpSuccess(String email) {
        signUpButton.setEnabled(true);
        Intent data = new Intent();
        data.putExtra("email", email);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Comunica al usuario que el registro no se ha llevado a cabo con éxito y activa el botón de
     * registro de nuevo.
     */
    private void onSignUpFailed() {
        Toast.makeText(getBaseContext(), R.string.error_signup, Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    /**
     * Comprueba si los datos introducidos como email y contraseña tienen un formato válido, y
     * consulta en la base de datos si un usuario con esos datos ya existe.
     *
     * @return True o false según el email y la contraseña sean válidos  o no.
     */
    private boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // En el caso de que el email introducido no sea válido mostramos un mensaje de error
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getResources().getString(R.string.error_email));
            valid = false;
        } else {
            // Si el email es válido pero la contraseña no, retiramos el error
            emailText.setError(null);
        }

        // En el caso de que la contraseña no sea válida mostramos un mensaje de error
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError(getResources().getString(R.string.error_password));
            valid = false;
        } else {
            // Si la contraseña es válida pero el email no, retiramos el error
            passwordText.setError(null);
        }
        return valid;
    }
}
