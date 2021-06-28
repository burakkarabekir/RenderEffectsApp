package com.androiddevs.rendereffectsapp.effects.chain

import android.graphics.BlendMode
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.androiddevs.rendereffectsapp.R
import com.androiddevs.rendereffectsapp.databinding.FragmentChainBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChainFragment : Fragment() {
    private lateinit var binding: FragmentChainBinding
    private val viewModel: ChainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChainBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(31)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // region Blur&Bitmap Effect
        binding.sliderValueX.bind(viewModel.valueX, viewModel::setValueX)
        binding.sliderValueY.bind(viewModel.valueY, viewModel::setValueY)
        binding.switchBlurBitmap.bind(viewModel.blurBitmap, viewModel::setBlurBitmap)
        viewModel.blurBitmapEffectFlow.onEach {
            binding.groupBlur.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)
        // endregion

        // region Color Filter Effect
        binding.sliderRed.bind(viewModel.valueR, viewModel::setValueR)
        binding.sliderGreen.bind(viewModel.valueG, viewModel::setValueG)
        binding.sliderBlue.bind(viewModel.valueB, viewModel::setValueB)
        binding.switchColorFilterBitmap.bind(viewModel.colorFilterBitmap, viewModel::setColorFilterBitmap)
        viewModel.colorFilterEffectFlow.onEach {
            binding.groupColorFilter.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)

        viewModel.colorFlow.onEach {
            binding.viewColor.setBackgroundColor(it!!)
        }.launchIn(viewModel.viewModelScope)
        // endregion

        // region Offset&Bitmap Effect
        binding.sliderOffsetX.bind(viewModel.offsetX, viewModel::setOffsetX)
        binding.sliderOffsetY.bind(viewModel.offsetY, viewModel::setOffsetY)
        binding.switchOffsetBitmap.bind(viewModel.offsetBitmap, viewModel::setOffsetBitmapEnabled)
        viewModel.offsetBitmapEffectFlow.onEach {
            binding.groupOffset.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)
        // endregion

        // region Blur&Offset Effect
        binding.switchBlurOffset.bind(viewModel.blurOffset, viewModel::setBlurOffset)
        viewModel.blurOffsetEffectFlow.onEach {
            binding.groupOffset.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.groupBlur.visibility = if (it != null) View.VISIBLE else View.GONE
            binding.imageViewTest.setRenderEffect(it)
        }.launchIn(viewModel.viewModelScope)
        // endregion

        // region BlendMode Effect
        binding.switchBlend.bind(viewModel.blendMode, viewModel::setBlendMode)
        viewModel.blendModeEffectFlow.onEach {
            binding.groupBlur.visibility = if (it != null) View.VISIBLE else View.GONE
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