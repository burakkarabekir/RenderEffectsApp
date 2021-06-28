package com.androiddevs.rendereffectsapp.effects.input

import android.app.Application
import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.rendereffectsapp.R
import kotlinx.coroutines.flow.*

class InputViewModel(application: Application) : AndroidViewModel(application) {

    // region Blur Offset Effect
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

    private val _blurOffset = MutableStateFlow(false)
    val blurOffset: StateFlow<Boolean> = _blurOffset
    fun setBlurOffset(newValue: Boolean) {
        _blurOffset.value = newValue
        if (newValue) {
            _blurBitmap.value = false
        }
    }
    // endregion

    // region Blur Bitmap Effect
    private val _blurBitmap = MutableStateFlow(false)
    val blurBitmap: StateFlow<Boolean> = _blurBitmap
    fun setBlurBitmap(newValue: Boolean) {
        _blurBitmap.value = newValue
        if (newValue) {
            _blurOffset.value = false
        }
    }

    @RequiresApi(31)
    val blurBitmapEffectFlow = combine(_valueX, _valueY, _blurBitmap) { x, y, isBlurEnabled ->
        if (isBlurEnabled) {
            RenderEffect.createBlurEffect(
                x, y,
                RenderEffect.createBitmapEffect(
                    BitmapFactory.decodeResource(
                        application.resources,
                        R.drawable.img_test
                    )
                ), Shader.TileMode.DECAL
            )
        } else null
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
    // endregion

    @RequiresApi(31)
    val blurOffsetEffectFlow =
        combine(
            _valueX,
            _valueY,
            _offsetX,
            _offsetY,
            _blurOffset
        ) { valueX, valueY, offsetX, offsetY, isFilterEnabled ->
            if (isFilterEnabled) {
                RenderEffect.createBlurEffect(
                    valueX,
                    valueY,
                    RenderEffect.createOffsetEffect(offsetX, offsetY),
                    Shader.TileMode.DECAL
                )
            } else null
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    // endregion
}