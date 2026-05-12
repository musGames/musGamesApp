package dk.musgames.app.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {

    // private state, only this file can change it
    private val _ui = MutableStateFlow(LoginUiState())
    // public state, screen can read this
    val ui: StateFlow<LoginUiState> = _ui

    private val auth = Firebase.auth  // firebase login system

    private val db = Firebase.database // firebase database


    // changes email when user types in email input
    fun onEmailChange(v: String) {
        _ui.value = _ui.value.copy(email = v)
    }

    // changes password when user types in password input
    fun onPasswordChange(v: String) {
        _ui.value = _ui.value.copy(password = v)
    }

    // runs when login button is clicked
    fun login(onSuccess: () -> Unit) = viewModelScope.launch {

        val (email, pass) = _ui.value

        // checks if fields are empty (email and password)
        if (email.isBlank() || pass.isBlank()) {

            // show error message on screen
            _ui.value = _ui.value.copy(
                error = "Email og password skal udfyldes"
            )

            return@launch
        }

        // starts loading spinner and clear old error
        _ui.value = _ui.value.copy(
            isLoading = true,
            error = null
        )

        // tries to login with firebase
        auth.signInWithEmailAndPassword(email, pass)

            .addOnSuccessListener { cred ->

                // checks if email is verified
                if (!cred.user!!.isEmailVerified) {

                    // logout because email is not verified
                    auth.signOut()

                    _ui.value = _ui.value.copy(
                        isLoading = false,
                        error = "Verificér email før login"
                    )

                    return@addOnSuccessListener
                }

                // gets user data from firebase database
                db.reference
                    .child("users")
                    .child(cred.user!!.uid)
                    .get()


                    .addOnSuccessListener { snap ->
                        // gets display name from database
                        snap.child("displayName")
                            .getValue(String::class.java)
                            ?.let { dn ->
                                //dn means display name
                                // can save display name here later
                            }

                        //stop loading
                        _ui.value = _ui.value.copy(
                            isLoading = false
                        )

                        // goes to dashboard
                        onSuccess()
                    }
            }

            .addOnFailureListener { e ->

                //login failed, shows firebase error
                _ui.value = _ui.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
    }
}