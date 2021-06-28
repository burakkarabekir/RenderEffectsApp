package com.androiddevs.rendereffectsapp.effects.single

import android.graphics.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

@RequiresApi(31)
class SingleViewModel : ViewModel() {

    // region Blur Effect
    private val _valueX = MutableStateFlow(4f)
    val valueX: StateFlow<Float> = _valueX
    fun setValueX(newValue: Float) {
        _valueX.value = newValue
    }

    private val _valueY = MutableStateFlow(4f)
    val valueY: StateFlow<Float> = _valueY
    fun setValueY(newValue: Float) {
        _valueY.value = newValue
    }

    private val _blur = MutableStateFlow(true)
    val blur: StateFlow<Boolean> = _blur
    fun setBlurred(newValue: Boolean) {
        _blur.value = newValue
        if (newValue) {
            _offset.value = false
            _filter.value = false
        }
    }

    @RequiresApi(31)
    val blurEffectFlow = combine(_valueX, _valueY, _blur) { x, y, isFilterEnabled ->
        if (isFilterEnabled) {
            RenderEffect.createBlurEffect(x, y, Shader.TileMode.DECAL)
        } else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // endregion

    //region Color Filter Effect
    private val _valueR = MutableStateFlow(0f)
    val valueR: StateFlow<Float> = _valueR
    fun setValueR(newValue: Float) {
        _valueR.value = newValue
    }

    private val _valueG = MutableStateFlow(0f)
    val valueG: StateFlow<Float> = _valueG
    fun setValueG(newValue: Float) {
        _valueG.value = newValue
    }

    private val _valueB = MutableStateFlow(0f)
    val valueB: StateFlow<Float> = _valueB
    fun setValueB(newValue: Float) {
        _valueB.value = newValue
    }

    private val _filter = MutableStateFlow(false)
    val filter: StateFlow<Boolean> = _filter
    fun setColorFilter(newValue: Boolean) {
        _filter.value = newValue
        if (newValue) {
            _offset.value = false
            _blur.value = false
        }
    }

    @RequiresApi(31)
    val colorFilterEffectFlow =
        combine(_valueR, _valueG, _valueB, _filter) { r, g, b, isFilterEnabled ->
            if (isFilterEnabled) {
                RenderEffect.createColorFilterEffect(
                    BlendModeColorFilter(
                        Color.rgb(r, g, b),
                        BlendMode.COLOR
                    )
                )
            } else null
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @RequiresApi(31)
    val colorFlow =
        combine(_valueR, _valueG, _valueB, _filter) { r, g, b, isFilterEnabled ->
            if (isFilterEnabled) {
                Color.rgb(r, g, b)
            } else Color.WHITE
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // endregion

    // region Offset Effect
    private val _offsetX = MutableStateFlow(0f)
    val offsetX: StateFlow<Float> = _offsetX
    fun setOffsetX(newValue: Float) {
        _offsetX.value = newValue
    }

    private val _offsetY = MutableStateFlow(0f)
    val offsetY: StateFlow<Float> = _offsetY
    fun setOffsetY(newValue: Float) {
        _offsetY.value = newValue
    }

    private val _offset = MutableStateFlow(false)
    val offset: StateFlow<Boolean> = _offset
    fun setOffsetEnabled(newValue: Boolean) {
        _offset.value = newValue
        if (newValue) {
            _blur.value = false
            _filter.value = false
        }
    }

    @RequiresApi(31)
    val offsetEffectFlow = combine(_offsetX, _offsetY, _offset) { x, y, isOffsetEnabled ->
        if (isOffsetEnabled) {
            RenderEffect.createOffsetEffect(x, y)
        } else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // endregion

}