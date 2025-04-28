package ca.uqac.friendschallenge.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.uqac.friendschallenge.R
import ca.uqac.friendschallenge.ui.theme.FriendsChallengeTheme
import ca.uqac.friendschallenge.utils.ToastUtils

/**
 * The RegisterScreen composable function displays the registration screen of the application.
 *
 * @param modifier The modifier to be applied to the root layout.
 * @param onLoginButtonClicked Callback function to be invoked when the login button is clicked.
 * @param onRegisterButtonClicked Callback function to be invoked when the register button is clicked.
 * @param isRegisterLoading Boolean flag indicating whether the registration process is loading.
 */
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onLoginButtonClicked: () -> Unit,
    onRegisterButtonClicked: (email: String, password: String, username: String) -> Unit,
    isRegisterLoading: Boolean = false,
) {
    // State variables for the registration form inputs
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.register_title), style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.username)) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            visualTransformation = PasswordVisualTransformation(), // visual transformation for obscuring input
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(id = R.string.confirm_password)) },
            visualTransformation = PasswordVisualTransformation(), // visual transformation for obscuring input
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register button with loading indicator
        Button(
            onClick = {
                if (password == confirmPassword){
                    onRegisterButtonClicked(email, password, username)
                } else {
                    ToastUtils.show(context, "Les mots de passe ne correspondent pas")
                }
            },
            enabled = !isRegisterLoading
        ) {
            if (isRegisterLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(text = stringResource(id = R.string.register_button))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { onLoginButtonClicked() },
            enabled = !isRegisterLoading
        ) {
            Text(text = stringResource(id = R.string.login_prompt))
        }
    }
}

/**
 * Preview function for the RegisterScreen.
 * This function is used to display a preview of the RegisterScreen in the Android Studio design editor.
 */
@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    FriendsChallengeTheme {
        RegisterScreen(onLoginButtonClicked = {}, onRegisterButtonClicked = {_, _, _ -> })
    }
}
