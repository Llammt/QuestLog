package com.example.questlog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.questlog.databinding.FragmentTasksBinding
import androidx.navigation.fragment.findNavController
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

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToTask.observe(viewLifecycleOwner, Observer { taskId ->
            taskId?.let {
                val action = TasksFragmentDirections.actionTasksFragmentToEditTaskFragment(taskId)
                this.findNavController().navigate(action)
                viewModel.onTaskNavigated()
            }
        })

        viewModel.navigateToCreateTask.observe(viewLifecycleOwner) { navigate ->
            if (navigate == true) {
                findNavController().navigate(
                    TasksFragmentDirections.actionTasksFragmentToCreateTaskFragment()
                )
                viewModel.onNavigated()
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