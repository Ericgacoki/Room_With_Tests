package com.ericg.groom.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericg.groom.R
import com.ericg.groom.data.User
import com.ericg.groom.data.UserViewModel
import com.ericg.groom.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment: Fragment (), ListAdapter.ItemClicked, SearchView.OnQueryTextListener {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: ListAdapter
    private var list: List<User> = listOf()

    private var _listBinding: FragmentListBinding? = null
    private val listBinding get() = _listBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _listBinding = FragmentListBinding.inflate(inflater, container, false)
        listBinding.apply {

            userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

            setHasOptionsMenu(true)
            initRecyclerView()
            getData()

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
            return root
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val search = menu.findItem(R.id.menuSearch)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuBtnDeleteAll -> deleteAllData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        adapter = ListAdapter(list, this)

        listBinding.recyclerView.apply {
            adapter = this@ListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getData() {
        val data = userViewModel.readData()

        data.observe(viewLifecycleOwner) { userList ->
            adapter.list = userList
            adapter.notifyDataSetChanged()

            if (userList.isEmpty()) {
                Toast.makeText(requireContext(), "Click + to add data", Toast.LENGTH_SHORT).show()
            } else {
                val numItems =
                    if (userList.size == 1) "item" else if (userList.size > 1) "items" else "items"
                Toast.makeText(
                    requireContext(),
                    "found ${userList.size} $numItems!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun deleteData(user: User, name: String) {
        AlertDialog.Builder(requireContext(), 6).apply {
            setIcon(R.drawable.ic_warning); setTitle("Delete $name?")
            setMessage("This can't be undone!")
            setNegativeButton("cancel") { _, _ -> }

            setPositiveButton("ok") { _, _ ->
                userViewModel.deleteUSer(user)

                Toast.makeText(
                    requireContext(),
                    "Successfully deleted $name!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            setCancelable(false)
        }.create().show()
    }

    private fun deleteAllData() {
        AlertDialog.Builder(requireContext(), 6).apply {
            setIcon(R.drawable.ic_warning)
            setTitle("Delete All?")
            setMessage("This can't be undone!")

            setNegativeButton("cancel") { _, _ -> }
            setPositiveButton("ok") { _, _ ->

                userViewModel.deleteAll()
                Toast.makeText(requireContext(), "deleted all!", Toast.LENGTH_SHORT)
                    .show()
            }
        }.create().show()
    }

    override fun clicked(
        view: View, position: Int,
        id: Int?, userID: Int
    ) {
        val user = adapter.list[position]
        val name = adapter.list[position].firstName

        when (id) {
            R.id.btnDelete -> deleteData(user, name)

            R.id.rowItemRoot -> {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(user)
                findNavController().navigate(action)
            }

            else -> {

            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchData(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchData(newText)
        return true
    }

    private fun searchData(query: String?) {
        userViewModel.searchData("%$query%").observe(this, { list ->
            adapter.list = list
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroy() {
        _listBinding = null
        super.onDestroy()
    }
}