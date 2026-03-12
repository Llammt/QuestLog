package com.example.questlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.questlog.databinding.FragmentTasksBinding
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import listItemAnimator

class TasksFragment : Fragment() {
    private var _binding : FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val dao = TaskDatabase.getInstance(application).taskDao
        val viewModelFactory = TasksViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TasksViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = TaskItemAdapter(
            clickListener = { taskId ->
                viewModel.onTaskClicked(taskId)
            },
            onDeleteClick = { taskId ->
                viewModel.deleteTask(taskId)
            }
        )

        binding.tasksList.adapter = adapter
        binding.tasksList.itemAnimator = listItemAnimator()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasks.collect { tasks ->
                adapter.submitList(tasks)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToTask.collect { taskId ->
                taskId?.let {
                    val action =
                        TasksFragmentDirections.actionTasksFragmentToEditTaskFragment(taskId)
                    findNavController().navigate(action)
                    viewModel.onTaskNavigated()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToCreateTask.collect { navigate ->
                if (navigate) {
                    findNavController().navigate(
                        TasksFragmentDirections
                            .actionTasksFragmentToCreateTaskFragment()
                    )
                    viewModel.onNavigated()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tasksList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}