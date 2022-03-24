package com.bmd.showmemoreshops.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bmd.showmemoreshops.R
import com.bmd.showmemoreshops.data.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class RegisterFragment : Fragment() {
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_auth_register, container, false)

        val name : TextInputEditText = view.findViewById(R.id.name)
        val email : TextInputEditText = view.findViewById(R.id.email)
        val password : TextInputEditText = view.findViewById(R.id.password)

        val register : MaterialButton = view.findViewById(R.id.btn_register)
        register.setOnClickListener{ check(name,email,password) }

        return view
    }


    private fun check(name : TextInputEditText,email : TextInputEditText,password : TextInputEditText): Boolean {
        if (Objects.requireNonNull(name.text).toString().isEmpty()) {
            name.error = getString(R.string.input_name)
            return false
        } else if (name.text.toString().length < 4) {
            name.error = getString(R.string.input_name)
            return false
        }
        if (Objects.requireNonNull(email.text).toString().isEmpty()) {
            email.error = getString(R.string.input_email)
            return false
        } else if (!email.text.toString().contains("@") && email.text.toString().length > 5
        ) {
            email.error = getString(R.string.input_email)
            return false
        }
        if (Objects.requireNonNull(password.text).toString().length < 5) {
            password.error = getString(R.string.password_less)
            return false
        }
        return true
    }

}