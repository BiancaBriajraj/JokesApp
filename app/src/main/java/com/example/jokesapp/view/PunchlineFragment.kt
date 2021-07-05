package com.example.jokesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.jokesapp.R
import com.example.jokesapp.databinding.FragmentPunchlineBinding
import com.example.jokesapp.model.Joke
import com.example.jokesapp.viewModel.JokeViewModel

class PunchlineFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentPunchlineBinding
    private lateinit var viewModel: JokeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPunchlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(JokeViewModel::class.java)
        binding.apply {
            progressBarP.visibility = View.GONE
            arguments?.let { bundle ->
                val myData = bundle.getParcelable<Joke>("jokeData")
                myData?.apply {
                    punchCatView.text = getString(R.string.catergoy_s, type.replaceFirstChar { it.uppercase() })
                    setupDetails.text = setup
                    showPunchlineBtn.setOnClickListener {
                        punchLineText.text = punchline
                    }
                    newJokeButton.setOnClickListener {
                        setupDetails.visibility = View.GONE
                        punchLineText.visibility = View.GONE
                        showPunchlineBtn.visibility = View.GONE
                        if (type == getString(R.string.random).lowercase()) {
                            viewModel.getInfo(getString(R.string.random))
                        } else {
                            viewModel.getInfo(type)
                        }
                        viewModel.loading.observe(viewLifecycleOwner, { loading ->
                            if (loading) progressBarP.visibility = View.VISIBLE
                            else progressBarP.visibility = View.GONE
                        })
                        viewModel.error.observe(viewLifecycleOwner, { error ->
                            if (error) {
                                punchErrorMessage.visibility = View.VISIBLE
                                imageView2.visibility = View.VISIBLE
                                setupDetails.visibility = View.GONE
                                punchLineText.visibility = View.GONE
                                showPunchlineBtn.visibility = View.GONE
                            }
                        })
                        viewModel.list.observe(viewLifecycleOwner, { list ->
                            setupDetails.text = list[0].setup
                            setupDetails.visibility = View.VISIBLE
                            showPunchlineBtn.visibility = View.VISIBLE
                            showPunchlineBtn.setOnClickListener {
                                punchLineText.text = list[0].punchline
                                punchLineText.visibility = View.VISIBLE
                            }
                        })
                    }
                }
            }
        }
        binding.NewCategoryButton.setOnClickListener {
            binding.NewCategoryButton.visibility = View.GONE
            binding.spinner.visibility = View.VISIBLE
            val spinner: Spinner = view.findViewById(R.id.spinner)
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.options,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
            spinner.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.apply {
            setupDetails.visibility = View.GONE
            punchLineText.visibility = View.GONE
            showPunchlineBtn.visibility = View.GONE
            if (parent != null) {
                viewModel.getInfo(parent.getItemAtPosition(position).toString())
                viewModel.loading.observe(viewLifecycleOwner, { loading ->
                    if (loading) {
                        showPunchlineBtn.visibility = View.GONE
                        progressBarP.visibility = View.VISIBLE
                    } else progressBarP.visibility = View.GONE
                })
                viewModel.error.observe(viewLifecycleOwner, { error ->
                    if (error) {
                        punchErrorMessage.visibility = View.VISIBLE
                        imageView2.visibility = View.VISIBLE
                        setupDetails.visibility = View.GONE
                        punchLineText.visibility = View.GONE
                        showPunchlineBtn.visibility = View.GONE
                    }
                })
                viewModel.list.observe(viewLifecycleOwner, { list ->
                    punchCatView.text = getString(R.string.catergoy_s, parent.getItemAtPosition(position).toString())
                    setupDetails.text = list[0].setup
                    setupDetails.visibility = View.VISIBLE
                    showPunchlineBtn.visibility = View.VISIBLE
                    showPunchlineBtn.setOnClickListener {
                        punchLineText.text = list[0].punchline
                        punchLineText.visibility = View.VISIBLE
                    }
                })

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        binding.apply {
            punchErrorMessage.visibility = View.VISIBLE
            imageView2.visibility = View.VISIBLE
            setupDetails.visibility = View.GONE
            punchLineText.visibility = View.GONE
            showPunchlineBtn.visibility = View.GONE
        }
    }
}


