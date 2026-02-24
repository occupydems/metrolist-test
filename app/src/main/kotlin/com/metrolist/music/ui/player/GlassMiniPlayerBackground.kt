package com.metrolist.music.ui.player

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private val shaderCode = """
uniform shader content;

half4 main(float2 coord) {
    // refraction strength
    float2 offsetR = float2(-0.006, 0.0);
    float2 offsetG = float2(0.0, 0.0);
    float2 offsetB = float2(0.006, 0.0);

    half4 r = content.eval(coord + offsetR);
    half4 g = content.eval(coord + offsetG);
    half4 b = content.eval(coord + offsetB);

    // dispersion: split RGB channels slightly
    return half4(r.r, g.g, b.b, 1.0);
}
"""

@Composable
fun GlassMiniPlayerBackground(content: @Composable () -> Unit) {

    if (Build.VERSION.SDK_INT >= 33) {
        val shader = RuntimeShader(shaderCode)

        Box(
            Modifier
                .fillMaxSize()
                .clipToBounds()
                .graphicsLayer {
                    renderEffect = RenderEffect
                        .createRuntimeShaderEffect(shader, "content")
                        .asComposeRenderEffect()
                }
        ) {
            content()
        }

    } else {
        // fallback: just draw normally
        Box(Modifier.fillMaxSize()) {
            content()
        }
    }
}
