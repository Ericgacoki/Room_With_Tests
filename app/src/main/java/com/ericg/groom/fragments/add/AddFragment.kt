package com.ericg.groom.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ericg.groom.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private var _addBinding: FragmentAddBinding? = null
    private val addBinding get() = _addBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _addBinding = FragmentAddBinding.inflate(inflater, container, false)
        return addBinding.root.apply {

        }
    }
}