package com.example.jokesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.jokesapp.R
import com.example.jokesapp.databinding.FragmentTitleBinding
import com.example.jokesapp.viewModel.JokeViewModel


class TitleFragment : Fragment() {

    private lateinit var binding: FragmentTitleBinding

    private lateinit var viewModel: JokeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(JokeViewModel::class.java)
        binding.progressBar.visibility = View.GONE
        binding.generateJokebtn.setOnClickListener {
            val userType = when (binding.radioGroupP.checkedRadioButtonId) {
                R.id.radioButtonGenerl -> getString(R.string.general)
                R.id.radioButtonProg -> getString(R.string.programming)
                R.id.radioButtonRandom -> getString(R.string.random)
                else -> getString(R.string.noChoice)
            }
            if (userType == getString(R.string.noChoice)) {
                Toast.makeText(activity, "No category chosen", Toast.LENGTH_SHORT).show()
            }else {
                viewModel.getInfo(userType)
                viewModel.loading.observe(viewLifecycleOwner, { loading ->
                    if (loading) binding.progressBar.visibility = View.VISIBLE
                    else binding.progressBar.visibility = View.GONE
                })
                viewModel.list.observe(viewLifecycleOwner, { list ->
                    Navigation.findNavController(it).navigate(TitleFragmentDirections.actionTitleFragmentToPunchlineFragment(list[0]))
                })
                viewModel.error.observe(viewLifecycleOwner, { error ->
                    if (error) binding.titleErrorMessage.visibility = View.VISIBLE
                })
            }
        }
    }
}