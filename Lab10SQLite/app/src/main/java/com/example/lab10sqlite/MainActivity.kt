package com.example.lab10sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_delete_layout.view.*
import kotlinx.android.synthetic.main.insert_layout.view.*
import kotlinx.android.synthetic.main.insert_layout.view.edt_age
import kotlinx.android.synthetic.main.insert_layout.view.edt_id
import kotlinx.android.synthetic.main.insert_layout.view.edt_name

class MainActivity : AppCompatActivity() {
    var dbHandler: DatabaseHelper? = null
    var studentList = arrayListOf<Student>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DatabaseHelper(this)
        dbHandler?.getWritableDatabase()
        callStudentData()
        recycler_view.adapter = StudentsAdapter(studentList,applicationContext)
        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))

        recycler_view.addOnItemTouchListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View){
                editDeleteDialog(position)
            }
        })
    }
    fun callStudentData(){
        studentList.clear();
        studentList.addAll(dbHandler!!.getAllStudent())
        recycler_view.adapter?.notifyDataSetChanged()
    }
    fun addStudent(v: View){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.insert_layout,null)
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btnAdd.setOnClickListener {
            var id = mDialogView.edt_id.text.toString()
            var name = mDialogView.edt_name.text.toString()
            var age = mDialogView.edt_age.text.toString().toInt()
            var result = dbHandler?.insertStudent(Student(id=id,name=name,age=age))
            if(result!! > -1){
                Toast.makeText(applicationContext, "The student is added successfully", Toast.LENGTH_SHORT).show()
                callStudentData()
                mAlertDialog.dismiss()
            }else{
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            }
        }
        mDialogView.btnReset.setOnClickListener{
            mDialogView.edt_id.setText("")
            mDialogView.edt_name.setText("")
            mDialogView.edt_age.setText("")
        }
    }    fun editDeleteDialog(position: Int){
        val std = studentList[position]
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.edit_delete_layout, null)

        mDialogView.edt_id.setText(std.id)
        mDialogView.edt_id.isEnabled=false
        mDialogView.edt_name.setText(std.name)
        mDialogView.edt_age.setText(std.age.toString())

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btnUpdate.setOnClickListener{
            var id : String = mDialogView.edt_id.text.toString()
            var name:String = mDialogView.edt_name.text.toString()
            var age : Int = mDialogView.edt_age.text.toString().toInt()
            var result:Int? = dbHandler?.updateStudent(Student(id = id,name= name,age = age))

            if (result!! > -1){
                Toast.makeText(applicationContext,"The student is updated successfully", Toast.LENGTH_SHORT).show()
                callStudentData()
            }else{
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            }
            mAlertDialog.dismiss()
        }
    }
}

interface OnItemClickListener{
    fun onItemClicked(position: Int,view: View)
}
fun RecyclerView.addOnItemTouchListener(onClickListener: OnItemClickListener){
    this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener{
        override fun onChildViewDetachedFromWindow(view: View) {
            view?.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view?.setOnClickListener{
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }
    })
}
