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
import com.example.jokesapp.databinding.FragmentCategoryBinding
import com.example.jokesapp.viewModel.JokeViewModel


class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: JokeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarType.visibility = View.GONE
        viewModel = ViewModelProvider(this).get(JokeViewModel::class.java)
        binding.typeGenButton.setOnClickListener {
            val userType: String = when (binding.radioGroupType.checkedRadioButtonId) {
                R.id.typeRadioButtonGenral -> getString(R.string.general)
                R.id.typeRadioButtonProg -> getString(R.string.programming)
                R.id.typeRadioButtonRandom -> getString(R.string.random)
                else -> getString(R.string.noChoice)
            }
            if (userType == getString(R.string.noChoice)) {
                Toast.makeText(activity, "No category chosen", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getInfo(userType)
                viewModel.loading.observe(viewLifecycleOwner, { loading ->
                    if (loading) {
                        binding.typeGenButton.visibility = View.GONE
                        binding.radioGroupType.visibility = View.GONE
                        binding.typeChooseTextView.visibility = View.GONE
                        binding.progressBarType.visibility = View.VISIBLE
                    } else binding.progressBarType.visibility = View.GONE
                })
                viewModel.list.observe(viewLifecycleOwner, { list ->
                    Navigation.findNavController(it).navigate(CategoryFragmentDirections.actionCategoryFragmentToPunchlineFragment(list[0]))
                })

                viewModel.error.observe(viewLifecycleOwner, { error ->
                    if (error) {
                        binding.typeErrorMessage.visibility = View.VISIBLE
                        binding.progressBarType.visibility = View.GONE
                    }
                })
            }
        }
    }
}