package com.androiddevs.rendereffectsapp.effects.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.androiddevs.rendereffectsapp.databinding.FragmentSingleBinding
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SingleFragment : Fragment() {
    private lateinit var binding: FragmentSingleBinding
    private val viewModel: SingleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(31)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // region Blur Effect
        binding.sliderValueX.bind(viewModel.valueX, viewModel::setValueX)
        binding.sliderValueY.bind(viewModel.valueY, viewModel::setValueY)
        binding.switchBlur.bind(viewModel.blur, viewModel::setBlurred)
        viewModel.blurEffectFlow.onEach {
            binding.groupBlur.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)
        // endregion

        // region Color Filter Effect
        binding.sliderRed.bind(viewModel.valueR, viewModel::setValueR)
        binding.sliderGreen.bind(viewModel.valueG, viewModel::setValueG)
        binding.sliderBlue.bind(viewModel.valueB, viewModel::setValueB)
        binding.switchColorFilter.bind(viewModel.filter, viewModel::setColorFilter)
        viewModel.colorFilterEffectFlow.onEach {
            binding.groupColorFilter.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)

        viewModel.colorFlow.onEach {
            binding.viewColor.setBackgroundColor(it!!)
        }.launchIn(viewModel.viewModelScope)
        // endregion

        // region Offset Effect
        binding.sliderOffsetX.bind(viewModel.offsetX, viewModel::setOffsetX)
        binding.sliderOffsetY.bind(viewModel.offsetY, viewModel::setOffsetY)
        binding.switchOffset.bind(viewModel.offset, viewModel::setOffsetEnabled)
        viewModel.offsetEffectFlow.onEach {
            binding.groupOffset.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)
        // endregion
    }

    private fun Slider.bind(flow: Flow<Float>, setter: (Float) -> Unit) {
        flow.onEach { newValue -> value = newValue }
            .launchIn(viewModel.viewModelScope)
        addOnChangeListener { _, value, _ ->
            setter(value)
        }
    }

    private fun CompoundButton.bind(flow: Flow<Boolean>, setter: (Boolean) -> Unit) {
        flow.onEach { newValue -> isChecked = newValue }
            .launchIn(viewModel.viewModelScope)
        setOnCheckedChangeListener { _, checked -> setter(checked) }
    }
}