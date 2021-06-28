package com.androiddevs.rendereffectsapp.main

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.rendereffectsapp.R
import com.androiddevs.rendereffectsapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSingle.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_singleFragment) }
        binding.buttonInput.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_inputFragment) }
        binding.buttonChain.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_chainFragment) }
    }
}