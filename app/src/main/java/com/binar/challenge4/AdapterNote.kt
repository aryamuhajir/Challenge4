package com.binar.challenge4

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.editnote.view.*
import kotlinx.android.synthetic.main.item_adapter_note.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AdapterNote(val listDataNote : List<Note>) : RecyclerView.Adapter<AdapterNote.ViewHolder>(){
    //inisialisasi database
    private var mdb : NoteDatabase? = null

    class ViewHolder(layout : View) : RecyclerView.ViewHolder(layout){

    }
    //inflate item adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterNote.ViewHolder {
        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.item_adapter_note, parent, false)
        return ViewHolder(viewItem)
    }
    //proses yang akan dilakukan di setiap ite
    override fun onBindViewHolder(holder: AdapterNote.ViewHolder, position: Int) {
        //ubah text sesuai dari urutan data pada di database

        holder.itemView.txtHari.text = listDataNote[position].hari.toString()
        holder.itemView.txtCatatan.text = listDataNote[position].catatan.toString()

        //menghapus data di database
        holder.itemView.btnHapus.setOnClickListener {

            mdb = NoteDatabase.getInstance(it.context)
            AlertDialog.Builder(it.context)
                .setTitle("Hapus Note")
                .setMessage("Yakin hapus note?")

                .setPositiveButton("YA"){dialogInterface : DialogInterface, i : Int ->
                    GlobalScope.async {
                        // perintah mmemanggil fungsi delete dari dao
                        val result = mdb?.noteDao()?.deleteNote(listDataNote[position])
                        (holder.itemView.context as MainActivity ).runOnUiThread{
                            if (result != 0){
                                //berhasil
                                Toast.makeText(it.context, "NOTE TERHAPUS", Toast.LENGTH_LONG).show()
                                //recreate
                                (it.context as MainActivity).recreate()

                            }else{
                                Toast.makeText(it.context, "GAGAL DIHAPUS", Toast.LENGTH_LONG).show()

                            }
                        }
                        // ambil ulang data untuk ditampilkan lagi
                        (holder.itemView.context as Home).getDataNote()

                    }


                }
                    //tidak jadi hapus
                .setNegativeButton("TIDAK"){dialogInterface : DialogInterface, i : Int ->
                    dialogInterface.dismiss()
                }
                .show()
        }
        //edit data
        holder.itemView.btnEdit.setOnClickListener {
            mdb = NoteDatabase.getInstance(it.context)

            //custom dialog
            val customDialog1 = LayoutInflater.from(it.context).inflate(R.layout.editnote, null, false)
            val editDialog = AlertDialog.Builder(it.context)
                .setView(customDialog1)
                .create()

            //set data sebelummnya ke dalam edit text pada dialog
            customDialog1.editHari1.setText(listDataNote[position].hari)
            customDialog1.editCatatan1.setText(listDataNote[position].catatan)

            //button edit pada dialog
            customDialog1.btnEdit1.setOnClickListener {
                val hari1 = customDialog1.editHari1.text.toString()
                val catatan1 = customDialog1.editCatatan1.text.toString()
                listDataNote[position].hari = hari1
                listDataNote[position].catatan = catatan1

                GlobalScope.async {
                    //perintah update dari interface dao
                    val command = mdb?.noteDao()?.updateNote(listDataNote[position])
                    (customDialog1.context as MainActivity).runOnUiThread{
                        if(command != 0){
                            //berhasil
                            Toast.makeText(it.context, "BERHASIL TERUPDATE", Toast.LENGTH_SHORT).show()
                            //
                            (customDialog1.context as MainActivity).recreate()
                        }else{
                            Toast.makeText(it.context, "GAGAL DIUPDATE", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
                editDialog.show()
        }






    }

    override fun getItemCount(): Int {
        return listDataNote.size
    }

}