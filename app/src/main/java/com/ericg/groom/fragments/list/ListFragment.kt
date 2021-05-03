package com.ericg.groom.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ericg.groom.R
import com.ericg.groom.data.User
import com.ericg.groom.data.UserViewModel
import com.ericg.groom.databinding.FragmentListBinding

class ListFragment : Fragment(), ListAdapter.ItemClicked {

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

            userViewModel = ViewModelProvider(this@ListFragment).get(UserViewModel::class.java)

            initRecyclerView()
            getData()

            setHasOptionsMenu(true)

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
            return root
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
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
        userViewModel.readData().observe(viewLifecycleOwner, { userList ->
            adapter.list = userList
            adapter.notifyDataSetChanged()

            if (userList.isEmpty()) {
                Toast.makeText(requireContext(), "Click + to add data", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(
                requireContext(),
                "found ${userList.size} items!",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    private fun deleteData(user: User, name: String){
        AlertDialog.Builder(requireContext(), 2).apply {
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

    private fun deleteAllData(){
        AlertDialog.Builder(requireContext(), 4).apply {
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

    override fun clicked(view: View, position: Int, id: Int?, userID: Int) {
        val user = adapter.list[position]
        val name = adapter.list[position].firstName

        when (id) {
            R.id.btnDelete -> deleteData(user, name)

            R.id.rowItemRoot -> {
                // TODO: edit this item
                Toast.makeText(
                    requireContext(),
                    "wanna edit ${adapter.list[position].firstName}?",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

            }
        }
    }

    override fun onDestroy() {
        _listBinding = null
        super.onDestroy()
    }
}