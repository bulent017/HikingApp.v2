package com.example.hikingapp.View

import com.example.hikingapp.Model.User
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hikingapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FragmentSignUp : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private lateinit var  database: DatabaseReference
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

        auth = Firebase.auth
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        binding.apply {

            buttonLogin.setOnClickListener {
                when{
                    TextUtils.isEmpty(emailEditTextSignUp.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(nameEditTextSignUp.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter Name",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(surnameEditTextSignUp.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please enter Surname",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(passwordEdittext1SignUp.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please Enter password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TextUtils.isEmpty(passwordEdittext2SignUp.text.toString().trim{it<= ' '}) -> {
                        Toast.makeText(
                            requireActivity(),
                            "Please Enter Password",
                            Toast.LENGTH_SHORT
                        ).show()


                    }
                    else ->{
                        if (passwordEdittext1SignUp.text.toString().equals(passwordEdittext2SignUp.text.toString())){
                            val email: String = emailEditTextSignUp.text.toString().trim {it <= ' '} // trim boşluk girdiyse silimemize olanak tanır

                            val password: String = passwordEdittext1SignUp.text.toString().trim {it <= ' '}
                            // Create an instance and create a register a user with e mail and password
                            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity()){
                                if (it.isSuccessful){
                                    Toast.makeText(requireActivity(),"You are registered succesfully.",
                                        Toast.LENGTH_SHORT).show()
                                    saveUserToFirebase()
                                    startActivity(Intent(requireContext(), MainActivity::class.java))


                                }
                                else{
                                    Toast.makeText(requireActivity(),it.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else{
                            Toast.makeText(
                                requireActivity(),
                                "Passwords doesn't match , re-enter",
                                Toast.LENGTH_SHORT
                            ).show()
                            passwordEdittext1SignUp.text?.clear()
                            passwordEdittext2SignUp.text?.clear()


                        }

                    }
                }

            }
        }


        return binding.root
    }
    private fun saveUserToFirebase(){

        database = Firebase.database.reference

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        //println(uid)
        val user = User(binding.emailEditTextSignUp.text.toString(),binding.passwordEdittext1SignUp.text.toString(),binding.nameEditTextSignUp.text.toString(),binding.surnameEditTextSignUp.text.toString())
        if (uid != null) {
            database.child("user").child(uid).child("User Info").setValue(user)
        }
    }


}