package cinescout.design

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Suppress("PreviewNaming")
object AdaptivePreviews {

    @Preview(
        device = Devices.PHONE,
        name = Name.Phone
    )
    @Preview(
        device = Spec.PhoneLandScape,
        name = Name.PhoneLandscape
    )
    @Preview(
        device = Devices.FOLDABLE,
        name = Name.Foldable
    )
    @Preview(
        device = Devices.TABLET,
        name = Name.Tablet
    )
    annotation class Plain

    @Preview(
        device = Devices.PHONE,
        name = Name.Phone,
        showBackground = true
    )
    @Preview(
        device = Spec.PhoneLandScape,
        name = Name.PhoneLandscape,
        showBackground = true
    )
    @Preview(
        device = Devices.FOLDABLE,
        name = Name.Foldable,
        showBackground = true
    )
    @Preview(
        device = Devices.TABLET,
        name = Name.Tablet,
        showBackground = true
    )
    annotation class WithBackground

    @Preview(
        device = Devices.PHONE,
        name = Name.Phone,
        showSystemUi = true
    )
    @Preview(
        device = Spec.PhoneLandScape,
        name = Name.PhoneLandscape,
        showSystemUi = true
    )
    @Preview(
        device = Devices.FOLDABLE,
        name = Name.Foldable,
        showSystemUi = true
    )
    @Preview(
        device = Devices.TABLET,
        name = Name.Tablet,
        showSystemUi = true
    )
    annotation class WithSystemUi

    private object Name {

        const val Foldable = "Foldable"
        const val Phone = "Phone"
        const val PhoneLandscape = "Phone Landscape"
        const val Tablet = "Tablet"
    }

    private object Spec {

        const val PhoneLandScape = "spec:width=411dp,height=891dp,orientation=landscape"
    }
}
