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
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register : Fragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs2 = requireActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE)


        btnRegister2.setOnClickListener {
            if (edtNoHP.text.isNotBlank() && edtEmail.text.isNotBlank()&& edtNama.text.isNotBlank()&& edtPassword2.text.isNotBlank()&& edtKonfirmasiPassword.text.isNotBlank() && edtPassword2.text.toString().equals(edtKonfirmasiPassword.text.toString())){
                save()

            }else{
                Toast.makeText(requireContext(), "DATA BELUM TERISI ATAU PASSWORD TIDAK SESUAI", Toast.LENGTH_LONG).show()

            }
        }
    }
    fun save(){

        val sf = prefs2.edit()
        val nohp = edtNoHP.text.toString()
        val email = edtEmail.text.toString()
        val nama = edtNama.text.toString()
        val password = edtPassword2.text.toString()
        sf.putString("NOHP", nohp)
        sf.putString("EMAIL", email)
        sf.putString("NAMA", nama)
        sf.putString("PASSWORD", password)
        sf.apply()
        dbUser = UserDatabase.getInstance(requireContext())
        GlobalScope.async {
            //fungsi insert data yang ada di interface dao
            val hasil = dbUser?.userDao()?.insertUser(User(null, nohp,email,nama,password))

            activity?.runOnUiThread {
                if (hasil != 0.toLong()){
                    Toast.makeText(requireActivity(), "SUKSES", Toast.LENGTH_LONG).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_register_to_login)
                }else
                    Toast.makeText(requireActivity(), "GAGAL", Toast.LENGTH_LONG).show()

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
         * @return A new instance of fragment Register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}