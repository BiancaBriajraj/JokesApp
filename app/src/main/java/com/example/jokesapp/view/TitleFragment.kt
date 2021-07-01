package com.example.jokesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentTitleBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(JokeViewModel::class.java)
            binding.progressBar.visibility =View.GONE

        binding.generateJokebtn.setOnClickListener {
            val userType  = when(binding.radioGroupP.checkedRadioButtonId){
                R.id.radioButtonGenerl -> getString(R.string.general)
                R.id.radioButtonProg-> getString(R.string.programming)
                R.id.radioButtonRandom-> getString(R.string.general)
                else -> "Not checked"
            }
            viewModel.getInfo(userType)


            viewModel.loading.observe(viewLifecycleOwner, {
                    loading ->  if(loading) binding.progressBar.visibility = View.VISIBLE
             else binding.progressBar.visibility = View.GONE
            })
            viewModel.list.observe(viewLifecycleOwner, {
                list -> Navigation.findNavController(it).navigate(TitleFragmentDirections.actionTitleFragmentToPunchlineFragment(list[0]))
            })


        }



    }

}