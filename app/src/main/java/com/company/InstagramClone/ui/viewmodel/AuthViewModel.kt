package com.company.InstagramClone.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.InstagramClone.data.AuthRepository
import com.company.InstagramClone.data.FirebaseAuthRepository
import com.company.InstagramClone.data.UserProfile
import com.company.InstagramClone.data.remote.EmailRequest
import com.company.InstagramClone.data.remote.EmailService
import com.company.InstagramClone.data.remote.Recipient
import com.company.InstagramClone.data.remote.Sender
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

enum class VerificationType {
    Signup, Login, PasswordReset
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object ProfileSaved : AuthState()
    object OtpSent : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository = FirebaseAuthRepository()) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _signupData = MutableStateFlow(UserProfile())
    val signupData = _signupData.asStateFlow()

    private val _generatedOtp = MutableStateFlow<String?>(null)

    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId = _verificationId.asStateFlow()

    private val _currentVerificationType = MutableStateFlow(VerificationType.Signup)
    val currentVerificationType = _currentVerificationType.asStateFlow()

    private val emailService = EmailService.create()
    private val BREVO_API_KEY = com.company.InstagramClone.BuildConfig.BREVO_API_KEY

    fun updateSignupData(update: (UserProfile) -> UserProfile) {
        _signupData.value = update(_signupData.value)
    }

    fun setVerificationId(id: String) {
        _verificationId.value = id
    }

    fun setVerificationType(type: VerificationType) {
        _currentVerificationType.value = type
    }

    fun generateAndSendOtp(email: String, type: VerificationType = VerificationType.Signup) {
        val otp = (100000..999999).random().toString()
        _generatedOtp.value = otp
        _currentVerificationType.value = type
        
        Log.d("EMAIL_OTP", "Generated OTP for $email: $otp (Type: $type)")
        
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val request = EmailRequest(
                    sender = Sender(
                        name = "Instagram(Clone)",
                        email = "mainprathamchawda1@gmail.com"
                    ),
                    to = listOf(Recipient(email.trim())),
                    subject = "Instagram Clone Verification Code",
                    htmlContent = "<p>Your verification code is: <strong>$otp</strong></p>"
                )
                Log.d("EMAIL_OTP", "Sending Brevo email request to $email with code $otp")
                
                val response = emailService.sendEmail(
                    apiKey = BREVO_API_KEY,
                    request = request
                )
                
                if (response.isSuccessful) {
                    Log.d("EMAIL_OTP", "Brevo email sent successfully! Message ID: ${response.body()?.messageId}")
                    _authState.value = AuthState.OtpSent
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("EMAIL_OTP", "Failed to send Brevo email. Code: ${response.code()}, Error: $errorBody")
                    _authState.value = AuthState.Error("Failed to send verification code. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("EMAIL_OTP", "Error during Brevo API call: ${e.message}", e)
                _authState.value = AuthState.Error("Network error. Please check your connection.")
            }
        }
    }

    fun verifyEmailOtp(enteredCode: String): Boolean {
        val isValid = enteredCode == _generatedOtp.value
        if (isValid && _currentVerificationType.value == VerificationType.Login) {
            // If it's a login OTP, we should probably trigger a sign-in or similar
            // For now, we'll just set Authenticated if valid
            _authState.value = AuthState.Authenticated
        }
        return isValid
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
                _authState.value = AuthState.Idle
                // Log/Success message handled in UI
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to send reset email")
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signUpWithEmail(email, password)
                .onSuccess {
                    updateSignupData { it.copy(email = email) }
                    _authState.value = AuthState.Authenticated
                }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Signup failed") }
        }
    }

    fun completeSignup() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val currentUser = FirebaseAuth.getInstance().currentUser
            Log.d("AUTH_VM", "Completing signup for UID: ${currentUser?.uid}")
            
            if (currentUser == null) {
                _authState.value = AuthState.Error("User session not found. Please try logging in again.")
                return@launch
            }

            val profile = _signupData.value.copy(
                uid = currentUser.uid,
                email = currentUser.email ?: _signupData.value.email,
                phoneNumber = currentUser.phoneNumber ?: _signupData.value.phoneNumber
            )
            
            Log.d("AUTH_VM", "Saving profile to Firestore: $profile")
            
            repository.saveUserProfile(profile)
                .onSuccess { 
                    Log.d("AUTH_VM", "Profile saved successfully")
                    _authState.value = AuthState.ProfileSaved 
                }
                .onFailure { 
                    Log.e("AUTH_VM", "Failed to save profile: ${it.message}", it)
                    _authState.value = AuthState.Error(it.message ?: "Failed to save profile") 
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signInWithEmail(email, password)
                .onSuccess { _authState.value = AuthState.Authenticated }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Signin failed") }
        }
    }

    fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signInWithCredential(credential)
                .onSuccess { _authState.value = AuthState.Authenticated }
                .onFailure { _authState.value = AuthState.Error(it.message ?: "Signin with credential failed") }
        }
    }

    fun verifyPhoneNumber(phoneNumber: String, activity: Activity, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        _authState.value = AuthState.Loading
        repository.verifyPhoneNumber(phoneNumber, activity, callbacks)
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
