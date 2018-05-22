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
import org.git.joribiz.pmm.data.DBContract;
import org.git.joribiz.pmm.data.SQLiteHelper;
import org.git.joribiz.pmm.data.UserDAO;
import org.git.joribiz.pmm.model.User;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    EditText emailText;
    EditText passwordText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de elementos del layout
        emailText = findViewById(R.id.activity_login_email);
        passwordText = findViewById(R.id.activity_login_password);
        loginButton = findViewById(R.id.activity_login_loginButton);
        TextView signupLink = findViewById(R.id.activity_login_signUpLink);

        // Listener del botón de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Listener del texto de sign up
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciamos SingUpActivity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // Si el registro se ha llevado a cabo con éxito, volvemos a la actividad principal
                User user = data.getParcelableExtra("user");
                onLoginSuccess(user);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Desactivamos la posibilidad de ir de vuelta a la MainActivity
        moveTaskToBack(true);
    }

    /**
     * La lógica de autenticación se implementa en este método. Comprueba que los datos
     * introducidos por el usuario tienen un formato válido y ejecuta una consulta en la base de
     * datos en un nuevo hilo para comprobar que dicho usuario existe.
     */
    private void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        loginButton.setEnabled(false);

        // Ocultamos el teclado del usuario en el caso de que él no lo haya hecho
        InputMethodManager manager = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.authentication_message));
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(getApplicationContext());
                UserDAO userDAO = new UserDAO(sqLiteHelper);
                Cursor cursor = userDAO.getUserByEmail(email);

                // Comprobación del email
                if (!cursor.moveToFirst()) {
                    emailText.post(new Runnable() {
                        @Override
                        public void run() {
                            emailText.setError(getResources()
                                    .getString(R.string.error_email_not_found));
                            onLoginFailed();
                        }
                    });
                    // Comprobación de la contraseña
                } else if (!cursor.getString(cursor
                        .getColumnIndex(DBContract.UserEntry.KEY_PASSWORD)).equals(password)) {
                    passwordText.post(new Runnable() {
                        @Override
                        public void run() {
                            passwordText.setError(getResources()
                                    .getString(R.string.error_password_not_correct));
                            onLoginFailed();
                        }
                    });
                } else {
                    User user = new User(cursor);
                    onLoginSuccess(user);
                }
                cursor.close();
                sqLiteHelper.close();
                progressDialog.dismiss();
            }
        }, 3000); /* Como la consulta se va ejecutar muy rápido, el proceso espera 3
        segundos antes de ejecutarse */
    }

    /**
     * Activa el botón de login de nuevo y envía al usuario a la actividad principal.
     */
    private void onLoginSuccess(User user) {
        loginButton.setEnabled(true);
        // Volvemos a la actividad principal mandando el email del usuario
        Intent data = new Intent();
        data.putExtra("user", user);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Notifica al usuario que el login no ha tenido éxito y activa el botón de login de nuevo.
     */
    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), R.string.error_login, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /**
     * Comprueba si los datos introducidos como email y contraseña tienen un formato válido.
     *
     * @return True o false según el email y la contraseña tengan un formato válido o no.
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
