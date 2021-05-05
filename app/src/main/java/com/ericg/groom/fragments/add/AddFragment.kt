package com.ericg.groom.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ericg.groom.R
import com.ericg.groom.data.User
import com.ericg.groom.data.UserViewModel
import com.ericg.groom.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    private var firstName: String? = null
    private var lastName: String? = null
    private var age: String? = null

    private var _addBinding: FragmentAddBinding? = null
    private val addBinding get() = _addBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _addBinding = FragmentAddBinding.inflate(inflater, container, false)
        addBinding.apply {

            fabOk.setOnClickListener {
                insertDatToDatabase()
            }
            userViewModel = ViewModelProvider(this@AddFragment).get(UserViewModel::class.java)

            return root
        }
    }

    private fun insertDatToDatabase() {
        firstName = addBinding.firstName.text.toString()
        lastName = addBinding.lastName.text.toString()
        age = addBinding.age.text.toString()

        if (isFilled()) {
            val user = User(0, firstName = firstName!!, lastName = lastName!!, age = age!!.toInt())
            userViewModel.addUser(user)
            Toast.makeText(requireContext(), "Added successfully!", Toast.LENGTH_SHORT).show()


            val inputManager =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(addBinding.fabOk.windowToken, 0)

            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isFilled(): Boolean {
        var isFilled = true

        listOf<EditText>(
            addBinding.firstName, addBinding.lastName, addBinding.age
        ).forEach { input ->
            if (input.text.isBlank()) {
                input.error = "required"
                isFilled = (isFilled) && false
            } else {
                isFilled = (isFilled) && true
            }
        }

        return isFilled
    }

    override fun onDestroy() {
        _addBinding = null
        super.onDestroy()
    }
}
