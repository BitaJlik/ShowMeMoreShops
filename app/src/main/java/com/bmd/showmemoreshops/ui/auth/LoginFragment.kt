package com.bmd.showmemoreshops.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.bmd.showmemoreshops.data.models.*
import com.bmd.showmemoreshops.ui.map.MapFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class LoginFragment : Fragment() {
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_auth_login, container, false)
        val email : TextInputEditText = view.findViewById(R.id.email)
        val password : TextInputEditText = view.findViewById(R.id.password)
        val login : MaterialButton = view.findViewById(R.id.btn_login)
        val forgot : TextView = view.findViewById(R.id.forgot)
        login.setOnClickListener {
            if (TextUtils.isEmpty(email.toString())) {
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                Snackbar.make(view, getString(R.string.input_email), Snackbar.LENGTH_LONG)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show()
            } else {
                if (Objects.requireNonNull(password.text).toString().length < 5) {
                    password.error = getString(R.string.password_less)
                    password.setText("")
                } else {
                    // LogIn User from DB
                    mainViewModel.dataBase.auth.signInWithEmailAndPassword(Objects.requireNonNull(email.text).toString(), password.text.toString())
                        .addOnSuccessListener {
                            if (!mainViewModel.dataBase.auth.currentUser?.isEmailVerified!!) {
                                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(view.windowToken, 0)
                                Snackbar.make(view, getString(R.string.verify_email), Snackbar.LENGTH_LONG)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                    .show()
                                return@addOnSuccessListener
                            }
                            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                            for (user in mainViewModel.liveDataUsers.value!!) {
                                if (user.value.email == email.text.toString()) {
                                    user.value.UUID = (FirebaseAuth.getInstance().uid.toString())
                                    mainViewModel.liveDataUser.value = user.value
                                    break
                                }
                            }
                            mainViewModel.liveDataIsViewMap.value = true
                            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment, MapFragment())?.commit()
                        }
                        .addOnFailureListener {
                            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                            Snackbar.make(view, getString(R.string.incorrect_login), Snackbar.LENGTH_LONG)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .show()
                        }
                }
            }
        }
        forgot.setOnClickListener {
            val dialog = BottomDialogForgot()
            dialog.show(childFragmentManager, "Forgot")
        }



        return view
    }

    class BottomDialogForgot : BottomSheetDialogFragment(){
        private val mainViewModel : MainViewModel by activityViewModels()
        @SuppressLint("StaticFieldLeak")
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

            val view: View = inflater.inflate(R.layout.fragment_auth_forgot, container, false)
            val button: MaterialButton = view.findViewById(R.id.btn_login_forgot)
            val inputEditText: TextInputEditText = view.findViewById(R.id.email)
            inputEditText.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                    if (!hasFocus) {
                        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
            button.setOnClickListener {
                if (Objects.requireNonNull(inputEditText.text).toString().isEmpty()) return@setOnClickListener
                if (Objects.requireNonNull(inputEditText.text).toString().length <= 5) return@setOnClickListener
                mainViewModel.dataBase.auth.sendPasswordResetEmail(Objects.requireNonNull(inputEditText.text).toString())
            }
            return view
        }

    }

}