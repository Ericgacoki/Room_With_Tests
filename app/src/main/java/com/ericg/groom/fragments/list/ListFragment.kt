package com.ericg.groom.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
            return root
        }
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

            if (userList.isEmpty()){
                Toast.makeText(requireContext(), "Click + to add data", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(
                requireContext(),
                "found ${userList.size} items!",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun clicked(view: View, position: Int, id: Int?, userID: Int) {
        when (id) {
            R.id.btnDelete -> AlertDialog.Builder(requireContext(),3).apply {
                setMessage("Delete?")
                setIcon(R.drawable.ic_warning)
                setNegativeButton("cancel") { _, _ -> }

                setPositiveButton("ok") { _, _ ->
                    // TODO: delete clicked item from the database
                }

                setCancelable(false)
            }.create().show()
        }
    }
}













