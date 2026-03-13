package com.example.questlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.questlog.databinding.FragmentCreateTaskBinding
import kotlinx.coroutines.launch

class CreateTaskFragment : Fragment() {
    private var _binding : FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao

        val viewModelFactory = CreateTaskViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(CreateTaskViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToList.collect { navigate ->
                if (navigate) {
                    view.findNavController().navigate(
                        CreateTaskFragmentDirections
                            .actionCreateTaskFragmentToTasksFragment()
                    )
                    viewModel.onNavigatedToList()
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}