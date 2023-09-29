package com.desafiolatam.desafio2_m6

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.desafiolatam.desafio2_m6.databinding.ActivityMainBinding
import com.desafiolatam.desafio2_m6.orm.TaskDatabase
import com.desafiolatam.desafio2_m6.orm.TaskEntity
import com.desafiolatam.desafio2_m6.orm.TasksDao
import com.desafiolatam.desafio2_m6.task.OnItemClickListener
import com.desafiolatam.desafio2_m6.task.TaskListAdapter
import com.desafiolatam.desafio2_m6.task.TaskUIDataHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onItemClick(taskItem: TaskUIDataHolder) {
        val dialogView = layoutInflater.inflate(R.layout.add_task, null)
        val taskText = dialogView.findViewById<EditText>(R.id.task_input)
        taskText.setText(taskItem.text)
        val dialogBuilder = AlertDialog
            .Builder(this)
            .setTitle("Editar una Tarea")
            .setView(dialogView)
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Editar") {
                    _: DialogInterface, _: Int ->
                //generar código para editar/actualizar la tarea
                val newText = taskText.text.toString()
                if (newText.isNotEmpty()) {
                    updateEntity(taskItem, newText)
                }
            }
        dialogBuilder.create().show()
    }

    private lateinit var list: RecyclerView
    private lateinit var adapter: TaskListAdapter
    // crear las variables para utilizar la base de datos

    private lateinit var dataBase: TaskDatabase
    private lateinit var dao: TasksDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setUpViews()
        //inicializar lo necesario para usar la base de datos
        dataBase = MyApplication.taskDatabase!!
        dao = dataBase.getTaskDao()
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            val newItems = mutableListOf<TaskUIDataHolder>()
            val tasksFromDatabase = dao.getAllTasks()
            for (entity in tasksFromDatabase) {
                val dataView = TaskUIDataHolder(id = entity.id, text = entity.taskDescription)
                newItems.add(dataView)
            }
            withContext(Dispatchers.Main) {
                adapter.updateData(newItems)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.add -> addTask()
            R.id.remove_all -> removeAll()
        }
        return true
    }

    private fun setUpViews() {
        list = binding.taskList
        list.layoutManager = LinearLayoutManager(this)
        adapter = TaskListAdapter( mutableListOf(), this, this)
        list.adapter = adapter
    }

    private fun updateEntity(taskItem: TaskUIDataHolder, newText: String) {
        //completar método para actualizar una tarea en la base de datos
        val entityToUpdate = TaskEntity(id = taskItem.id, taskDescription =
        newText)
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateTask(entityToUpdate)
            val newItems = createEntityListFromDatabase(dao.getAllTasks())
            withContext(Dispatchers.Main) {
                adapter.updateData(newItems)
            }
        }
    }

    private fun addTask() {
        val dialogView = layoutInflater.inflate(R.layout.add_task, null)
        val taskText = dialogView.findViewById<EditText>(R.id.task_input)
        val dialogBuilder = AlertDialog
            .Builder(this)
            .setTitle("Agrega una Tarea")
            .setView(dialogView)
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Agregar") {
                    dialog: DialogInterface, _: Int ->
                if (taskText.text?.isNotEmpty()!!) {
                    //Completar para agregar una tarea a la base de datos
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.insertTask(createEntity(taskText.text.toString()))
                        val newItems =
                            createEntityListFromDatabase(dao.getAllTasks())
                        withContext(Dispatchers.Main) {
                            adapter.updateData(newItems)

                        }
                    }
                    dialog.dismiss()
                }
            }
        dialogBuilder.create().show()
    }

    private fun removeAll() {
        val dialog = AlertDialog
            .Builder(this)
            .setTitle("Borrar Todo")
            .setMessage("¿Desea Borrar todas las tareas?")
            .setNegativeButton("Cerrar") {
                    dialog: DialogInterface, _: Int -> dialog.dismiss()}
            .setPositiveButton("Aceptar") { dialog: DialogInterface, _: Int ->
                //Código para eliminar las tareas de la base de datos
                CoroutineScope(Dispatchers.IO).launch{
                    dao.deleteAllTasks()
                    val items =
                        createEntityListFromDatabase(dao.getAllTasks())
                    withContext(Dispatchers.Main) {
                        adapter.updateData(items)
                        dialog.dismiss()
                    }
                }
            }
        dialog.show()
    }
    private fun createEntity(text:String): TaskEntity {
        //completar este método para retornar un Entity
        return TaskEntity(taskDescription = text)
    }

    private fun createEntityListFromDatabase(entities: List < TaskEntity >): MutableList<TaskUIDataHolder> {
        val dataList = mutableListOf<TaskUIDataHolder>()
        //completar método para crear una lista de datos compatibles con el adaptador, mire lo que
        //retorna el método. Este método debe recibir un parámetro también.
        if (entities.isNotEmpty()) {
            for (entity in entities) {
                val dataView = TaskUIDataHolder(id = entity.id, text = entity.taskDescription)
                dataList.add(dataView)
            }
        }
        return dataList
    }
}
