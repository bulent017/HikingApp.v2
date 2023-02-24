package com.example.hikingapp.View

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hikingapp.MainActivity
import com.example.hikingapp.R
import com.example.hikingapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FragmentLogin : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        auth = Firebase.auth

        if (auth.currentUser!= null){
            startActivity(Intent(requireContext(),MainActivity::class.java)) // burada eğer curretn user giriş yaptıysa daha önceden login işlemini tekrar yapmasına gerek  kalmaz
            onDestroy()
        }



        binding.apply {

            buttonSignUp.setOnClickListener { findNavController().navigate(R.id.action_fragmentLogin_to_fragmentSignUp) }
            buttonLogin.setOnClickListener {
                when{
                    TextUtils.isEmpty(emailEdittextLogin.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(passwordEdittextLogin.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else ->{
                        val email: String = emailEdittextLogin.text.toString() // trim boşluk girdiyse silimemize olanak tanır
                        val password: String = passwordEdittextLogin.text.toString()
                        // Sign in  instance and create a register a user with e mail and password
                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity()){task ->
                            if (task.isSuccessful){

                                startActivity(Intent(requireContext(),MainActivity::class.java)) // activiteye bağlandık
                                onDestroyView()
                                onDestroy()
                            }
                            else{
                                Toast.makeText(requireActivity(),task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                    }                    }
                //println(FirebaseAuth.getInstance().currentUser?.uid)
            }
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}