package com.androiddevs.rendereffectsapp.effects.chain

import android.app.Application
import android.graphics.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.rendereffectsapp.R
import kotlinx.coroutines.flow.*

@RequiresApi(31)
class ChainViewModel(application: Application) : AndroidViewModel(application) {
    // region Blur Bitmap Effect
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

    private val _blurBitmap = MutableStateFlow(false)
    val blurBitmap: StateFlow<Boolean> = _blurBitmap
    fun setBlurBitmap(newValue: Boolean) {
        _blurBitmap.value = newValue
        if (newValue) {
            _blurOffset.value = false
            _offsetBitmap.value = false
            _colorFilterBitmap.value = false
            _blendMode.value = false
        }
    }

    private val _blurOffset = MutableStateFlow(false)
    val blurOffset: StateFlow<Boolean> = _blurOffset
    fun setBlurOffset(newValue: Boolean) {
        _blurOffset.value = newValue
        if (newValue) {
            _blurBitmap.value = false
            _offsetBitmap.value = false
            _colorFilterBitmap.value = false
            _blendMode.value = false
        }
    }

    @RequiresApi(31)
    val blurBitmapEffectFlow = combine(_valueX, _valueY, _blurBitmap) { x, y, isFilterEnabled ->
        if (isFilterEnabled) {
            RenderEffect.createChainEffect(
                RenderEffect.createBlurEffect(x, y, Shader.TileMode.DECAL),
                RenderEffect.createBitmapEffect(
                    BitmapFactory.decodeResource(
                        application.resources,
                        R.drawable.img_test
                    )
                )
            )
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

    private val _colorFilterBitmap = MutableStateFlow(true)
    val colorFilterBitmap: StateFlow<Boolean> = _colorFilterBitmap
    fun setColorFilterBitmap(newValue: Boolean) {
        _colorFilterBitmap.value = newValue
        if (newValue) {
            _offsetBitmap.value = false
            _blurBitmap.value = false
            _blurOffset.value = false
            _blendMode.value = false
        }
    }

    @RequiresApi(31)
    val colorFilterEffectFlow =
        combine(_valueR, _valueG, _valueB, _colorFilterBitmap) { r, g, b, isFilterEnabled ->
            if (isFilterEnabled) {
                RenderEffect.createChainEffect(
                    RenderEffect.createColorFilterEffect(
                        BlendModeColorFilter(
                            Color.rgb(r, g, b),
                            BlendMode.COLOR
                        )
                    ), RenderEffect.createBitmapEffect(
                        BitmapFactory.decodeResource(
                            application.resources,
                            R.drawable.img_test
                        )
                    )
                )
            } else null
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @RequiresApi(31)
    val colorFlow =
        combine(_valueR, _valueG, _valueB, _colorFilterBitmap) { r, g, b, isFilterEnabled ->
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

    private val _offsetBitmap = MutableStateFlow(false)
    val offsetBitmap: StateFlow<Boolean> = _offsetBitmap
    fun setOffsetBitmapEnabled(newValue: Boolean) {
        _offsetBitmap.value = newValue
        if (newValue) {
            _blurBitmap.value = false
            _colorFilterBitmap.value = false
            _blurOffset.value = false
            _blendMode.value = false
        }
    }

    @RequiresApi(31)
    val offsetBitmapEffectFlow =
        combine(_offsetX, _offsetY, _offsetBitmap) { x, y, isOffsetEnabled ->
            if (isOffsetEnabled) {
                RenderEffect.createChainEffect(
                    RenderEffect.createOffsetEffect(x, y),
                    RenderEffect.createBitmapEffect(
                        BitmapFactory.decodeResource(
                            application.resources,
                            R.drawable.img_test
                        )
                    )
                )
            } else null
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // endregion

    // region Blur Offset Effect

    val blurOffsetEffectFlow = combine(
        _valueX,
        _valueY,
        _offsetX,
        _offsetY,
        _blurOffset
    ) { valueX, valueY, x, y, isFilterEnabled ->
        if (isFilterEnabled) {
            RenderEffect.createChainEffect(
                RenderEffect.createBlurEffect(valueX, valueY, Shader.TileMode.DECAL),
                RenderEffect.createOffsetEffect(x, y)
            )
        } else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // endregion

    // region Blend Mode Effect
    private val _blendMode = MutableStateFlow(false)
    val blendMode: StateFlow<Boolean> = _blendMode
    fun setBlendMode(newValue: Boolean) {
        _blendMode.value = newValue
        if (newValue) {
            _blurBitmap.value = false
            _colorFilterBitmap.value = false
            _blurOffset.value = false
            _offsetBitmap.value = false
        }
    }

    @RequiresApi(31)
    val blendModeEffectFlow = combine(
        _valueX,
        _valueY,
        _offsetX,
        _offsetY,
        _blendMode
    ) { x, y, offsetX, offsetY, isFilterEnabled ->
        if (isFilterEnabled) {
            RenderEffect.createBlendModeEffect(
                RenderEffect.createBlurEffect(x, y, Shader.TileMode.DECAL),
                RenderEffect.createOffsetEffect(offsetX, offsetY),
                BlendMode.values().random()
            )
        } else null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // endregion
}