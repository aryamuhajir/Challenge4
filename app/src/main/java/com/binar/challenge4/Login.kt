package com.binar.challenge4

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var prefs2 : SharedPreferences
    var dbUser : UserDatabase? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs2 = requireActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE)


        btnRegister.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_login_to_register)
        }


        login()



    }
    fun login(){
        btnLogin.setOnClickListener {
            val sf = prefs2.edit()
            val nohp = edtNoHP2.text.toString()
            val password = edtPassword.text.toString()
            dbUser = UserDatabase.getInstance(requireContext())

            val user = dbUser?.userDao()?.cekUser(nohp, password)

            if (user.isNullOrEmpty()){

                Toast.makeText(requireContext(), "USERNAME ATAU PASSWORD SALAH", Toast.LENGTH_LONG).show()
            }else{
                sf.putString("USERNAME", nohp)
                sf.putString("PASSWORD", password)
                sf.putString("STATUS", "masuk")
                sf.apply()

                Navigation.findNavController(requireView()).navigate(R.id.action_login_to_home2)
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}