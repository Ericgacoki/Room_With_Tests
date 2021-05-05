package com.ericg.groom.fragments.update

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgsLazy
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ericg.groom.R
import com.ericg.groom.data.User
import com.ericg.groom.data.UserViewModel
import com.ericg.groom.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    private lateinit var args: NavArgsLazy<UpdateFragmentArgs>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        args = navArgs<UpdateFragmentArgs>()

        binding.apply {
            setInitialValues()
            binding.updateFabOk.setOnClickListener {
                if (isFilled()) {
                    val user = User(
                        args.value.clickedUser.id,
                        binding.updateFirstName.text.toString(),
                        binding.updateLastName.text.toString(),
                        binding.updateAge.text.toString().toInt()
                    )
                    userViewModel.updateUser(user)

                    // dismiss keyboard
                    val inputManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(binding.updateFabOk.windowToken, 0)

                    Toast.makeText(requireContext(), "updated successfully!", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                }
            }

            return root
        }
    }

    private fun setInitialValues(){
        binding.updateFirstName.setText(args.value.clickedUser.firstName)
        binding.updateLastName.setText( args.value.clickedUser.lastName)
        binding.updateAge.setText(args.value.clickedUser.age.toString())
    }

    private fun isFilled(): Boolean {
        var isFilled = true

        listOf<EditText>(
            binding.updateFirstName, binding.updateLastName, binding.updateAge
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
        _binding = null
        super.onDestroy()
    }
}