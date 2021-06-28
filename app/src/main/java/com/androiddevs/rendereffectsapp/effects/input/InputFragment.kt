package com.androiddevs.rendereffectsapp.effects.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.androiddevs.rendereffectsapp.databinding.FragmentInputBinding
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class InputFragment : Fragment() {
    private lateinit var binding: FragmentInputBinding
    private val viewModel: InputViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(31)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sliderValueX.bind(viewModel.valueX, viewModel::setValueX)
        binding.sliderValueY.bind(viewModel.valueY, viewModel::setValueY)

        binding.sliderOffsetX.bind(viewModel.offsetX, viewModel::setOffsetX)
        binding.sliderOffsetY.bind(viewModel.offsetY, viewModel::setOffsetY)

        // region Blur&Offset Input Effect
        binding.switchBlurOffset.bind(viewModel.blurOffset, viewModel::setBlurOffset)
        viewModel.blurOffsetEffectFlow.onEach {
            binding.groupBlur.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.groupOffset.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)
        // endregion
        // region Blur&Bitmap Input Effect
        binding.switchBlurBitmap.bind(viewModel.blurBitmap, viewModel::setBlurBitmap)
        viewModel.blurBitmapEffectFlow.onEach {
            binding.groupBlur.visibility = if (it != null) View.VISIBLE else View.GONE
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