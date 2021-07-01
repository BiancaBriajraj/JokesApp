package com.example.jokesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.jokesapp.R
import com.example.jokesapp.databinding.FragmentPunchlineBinding
import com.example.jokesapp.model.Joke
import com.example.jokesapp.viewModel.JokeViewModel


class PunchlineFragment : Fragment() {

    private lateinit var binding: FragmentPunchlineBinding
    private lateinit var viewModel: JokeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding = FragmentPunchlineBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(JokeViewModel::class.java)
        binding.progressBarP.visibility = View.GONE

        arguments?.let {
            val myData = it.getParcelable<Joke>("jokeData")
            myData?.apply {
                binding.setupDetails.text = setup

                binding.showPunchlineBtn.setOnClickListener {
                    binding.punchLineText.text = punchline
                }

                binding.newJokeButton.setOnClickListener {

                    binding.setupDetails.visibility = View.GONE
                    binding.punchLineText.visibility = View.GONE
                    binding.showPunchlineBtn.visibility = View.GONE
                    if (type == getString(R.string.random).lowercase()) {
                        viewModel.getInfo(getString(R.string.random))
                    } else {
                        viewModel.getInfo(type)
                    }

                        viewModel.loading.observe(viewLifecycleOwner, { loading ->
                            if (loading) binding.progressBarP.visibility = View.VISIBLE
                            else binding.progressBarP.visibility = View.GONE
                        })
                        viewModel.error.observe(viewLifecycleOwner, { error ->
                            if (error) {
                                binding.apply {
                                    punchErrorMessage.visibility = View.VISIBLE
                                    imageView2.visibility = View.VISIBLE
                                    setupDetails.visibility = View.GONE
                                    punchLineText.visibility = View.GONE
                                    showPunchlineBtn.visibility = View.GONE
                                }
                            }
                        })
                        viewModel.list.observe(viewLifecycleOwner, { list ->
                            binding.setupDetails.text = list[0].setup
                            binding.setupDetails.visibility = View.VISIBLE
                            binding.showPunchlineBtn.visibility = View.VISIBLE
                            binding.showPunchlineBtn.setOnClickListener {
                                binding.punchLineText.text = list[0].punchline
                                binding.punchLineText.visibility = View.VISIBLE
                            }
                        })
                    }

            }
        }

        binding.NewCategoryButton.setOnClickListener {
                arguments?.clear()

                Navigation.findNavController(it).navigate(PunchlineFragmentDirections.actionPunchlineFragmentToCategoryFragment())
            }
        }
    }


