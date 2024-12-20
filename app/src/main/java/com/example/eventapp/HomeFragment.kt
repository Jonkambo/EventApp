package com.example.eventapp



import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.eventapp.databinding.FragmentHomeBinding
import androidx.lifecycle.lifecycleScope
import com.example.eventapp.Data.getUserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var listView: ListView
    private lateinit var listViewUsers: ListView
    private lateinit var db: EventAppDB
    private lateinit var eventAdapter: EventAdapter
    private lateinit var usersAdapter: UsersAdapter
    private val eventList: MutableList<EventLocation> = mutableListOf()
    private val userList: MutableList<User> = mutableListOf()
    private lateinit var addEventBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding?.reviewsbtn?.setOnClickListener {
            navigateToReviewsActivity()
        }

        addEventBtn = view.findViewById(R.id.addEventBtn)

        addEventBtn.setOnClickListener {
            val intent = Intent(requireContext(),InsertEventActivity::class.java)
            startActivity(intent)
        }

        // скрываем кнопку или показываем в зависимости от роли
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            addEventBtn.visibility = if (isAdmin()) View.VISIBLE else View.GONE
        }

        listView = view.findViewById(R.id.eventsView)
        eventAdapter = EventAdapter(requireContext(), eventList, object : EventAdapter.OnItemClickListener {
            override fun onItemClick(eventLocation: EventLocation) {
                val intent = Intent(requireContext(), AreaDetailsActivity::class.java).apply {
                    putExtra("event_title", eventLocation.eventTitle)
                }
                startActivity(intent)
            }
        })
        loadEvents()



        listViewUsers = view.findViewById(R.id.usersView)
        usersAdapter = UsersAdapter(requireContext(), userList, object : UsersAdapter.OnItemClickListener {
            override fun onItemClick(user: User) {
                // Обработка клика по пользователю
                Toast.makeText(requireContext(), "Clicked: ${user.login}", Toast.LENGTH_SHORT).show()
            }
        })
        loadUsers() // Загрузка пользователей
    }

    // Загрузка пользователей из БД
    private fun loadUsers() {
        db = EventAppDB.getDB(requireContext())
        val userDao = db.userDao()

        lifecycleScope.launch {
            try {
                userDao.getAllUsers().collect { users ->
                    userList.clear()
                    userList.addAll(users)
                    usersAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error loading users: ${e.message}")
                Toast.makeText(requireContext(), "Error loading users", Toast.LENGTH_SHORT).show()
            }
        }
        listViewUsers.adapter = usersAdapter
    }

    // достаем из БД мероприятия и запихиваем в адаптер
    private fun loadEvents() {
        db = EventAppDB.getDB(requireContext())
        val eventLocationDao = db.eventLocationDao()

        // асинхронный запрос данных
        lifecycleScope.launch {
            try {
                eventLocationDao.getAllEventLocations().collect { events ->
                    eventList.clear() // Очищаем старые данные
                    eventList.addAll(events) // Добавляем новые данные
                    eventAdapter.notifyDataSetChanged() // Обновляем адаптер
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error loading events: ${e.message}")
                Toast.makeText(requireContext(), "Error loading events", Toast.LENGTH_SHORT).show()
            }
        }
        listView.adapter = eventAdapter
    }

    // проверка пользвателя на его роль
    private suspend fun isAdmin(): Boolean {
        val user = EventAppDB.getDB(requireContext()).userDao().getUserById(getUserId(requireContext()))
        return user?.roleId == 2
     }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun navigateToReviewsActivity() {
        val intent = Intent(requireContext(), ReviewsActivity::class.java)
        startActivity(intent)
    }

    // здесь можно передавать параметры между фрагментами
    companion object {

        private const val ARG_PARAM1 = "param1"

        @JvmStatic
        fun newInstance(param1: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

}