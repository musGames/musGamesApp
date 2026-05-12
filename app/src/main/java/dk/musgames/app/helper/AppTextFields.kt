// helper/AppOutlinedTextField.kt
package dk.musgames.app.helper

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val c: Color = LocalContentColor.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,

        singleLine = singleLine,
        maxLines   = if (singleLine) 1 else Int.MAX_VALUE,

        visualTransformation = visualTransformation,
        modifier = modifier,

        textStyle = LocalTextStyle.current.copy(color = c),

        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor   = c,
            unfocusedTextColor = c,
            disabledTextColor  = c.copy(alpha = 0.38f),
            errorTextColor     = c,

            cursorColor      = c,
            errorCursorColor = c,

            focusedLabelColor   = c,
            unfocusedLabelColor = c.copy(alpha = 0.80f),

            focusedPlaceholderColor   = c.copy(alpha = 0.60f),
            unfocusedPlaceholderColor = c.copy(alpha = 0.60f),

            focusedBorderColor   = c,
            unfocusedBorderColor = c.copy(alpha = .6f),
            disabledBorderColor  = c.copy(alpha = .38f),
            errorBorderColor     = Color.Red
        )
    )
}
