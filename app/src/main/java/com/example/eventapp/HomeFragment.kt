package com.example.eventapp



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.example.eventapp.databinding.FragmentHomeBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var listView: ListView
    private lateinit var db: EventAppDB
    private lateinit var eventAdapter: EventAdapter
    private val eventList: MutableList<EventLocation> = mutableListOf()
    private lateinit var addEventBtn: Button

    // переменная для проверки, является ли пользователь администратором
    private var isAdmin: Boolean = true

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

        addEventBtn?.setOnClickListener {
            val intent = Intent(requireContext(),InsertEventActivity::class.java);
            startActivity(intent);
        }

        if (isAdmin) {
            addEventBtn.visibility = View.VISIBLE // Показываем кнопку
        } else {
            addEventBtn.visibility = View.GONE // Скрываем кнопку
        }

        listView = view.findViewById(R.id.eventsView)
        eventAdapter = EventAdapter(requireContext(), eventList)
        //listView.adapter = eventAdapter

        loadEvents()
    }

    private fun loadEvents() {
        db = EventAppDB.getDB(requireContext())
        val eventLocationDao = db.eventLocationDao()

        // асинхронный запрос данных
        lifecycleScope.launch {
            eventLocationDao.getAllEventLocations().collect { events ->
                eventList.clear() // Очищаем старые данные
                eventList.addAll(events) // Добавляем новые данные
                eventAdapter.notifyDataSetChanged() // Обновляем адаптер
            }
            listView.adapter = eventAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun navigateToReviewsActivity() {
        val intent = Intent(requireContext(), ReviewsActivity::class.java)
        startActivity(intent)
    }

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