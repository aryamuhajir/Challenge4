package com.binar.challenge4

import android.app.ProgressDialog.show
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.addnote.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //shared dan database
    lateinit var sf : SharedPreferences
    var dbNote : NoteDatabase? = null

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbNote = NoteDatabase.getInstance(requireContext())
        //ambil data sharedpreferences
        sf = requireActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE)
        val getNama = sf.getString("USERNAME","")
        usernameTxt.text = getNama

        //logout memakai dialog
        btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("LOGOUT")
                .setMessage("YAKIN LOGOUT ?")
                .setCancelable(true)
                .setPositiveButton("YA"){ dialogInterface : DialogInterface, i : Int ->
                    logout()
                }
                .setNegativeButton("TIDAK") { dialogInterface2 : DialogInterface, i : Int ->

                }
                .show()
        }

        // tambah data
        floating.setOnClickListener{
            //inflate custom dialog
            val customDialog = LayoutInflater.from(requireContext()).inflate(R.layout.addnote, null, false)
            val aa = AlertDialog.Builder(requireContext())
                .setView(customDialog)
                .create()

            customDialog.btnTambah.setOnClickListener {
                val hari = customDialog.editHari.text.toString()
                val catatan = customDialog.editCatatan.text.toString()


                GlobalScope.async {
                    //fungsi insert data yang ada di interface dao
                    val hasil = dbNote?.noteDao()?.insertNote(Note(null, hari, catatan))

                    activity?.runOnUiThread {
                        if (hasil != 0.toLong()){
                            //berhasil dan recreate untuk menampilkan data terbaru
                            Toast.makeText(requireActivity(), "SUKSES", Toast.LENGTH_LONG).show()
                            activity?.recreate()
                        }else
                        Toast.makeText(requireActivity(), "GAGAL", Toast.LENGTH_LONG).show()

                    }
                }
            }

            aa.show()

        }
        //memanggil fungsi recyclerview
        getDataNote()


    }
    fun getDataNote(){
        rv_note.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        GlobalScope.launch {
            //fungsi mengambil data dari database
            val listData = dbNote?.noteDao()?.getAllNote()

            activity?.runOnUiThread {
                listData.let {
                    val adapt = AdapterNote(it!!)
                    rv_note.adapter = adapt

                    if(adapt.itemCount == 0){
                        belumADA.visibility= View.VISIBLE

                    }
                }
            }
        }
    }
        //fungsi logout
    fun logout(){
        val logoutsf = sf.edit()
        logoutsf.clear()
        logoutsf.apply()

        Navigation.findNavController(requireView()).navigate(R.id.action_home2_to_login)

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}