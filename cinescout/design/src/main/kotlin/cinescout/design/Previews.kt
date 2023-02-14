package cinescout.design

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Suppress("PreviewNaming")
object AdaptivePreviews {

    @Preview(device = Devices.PHONE)
    @Preview(device = "spec:width=411dp,height=891dp,orientation=landscape")
    @Preview(device = Devices.FOLDABLE)
    @Preview(device = Devices.TABLET)
    annotation class Plain

    @Preview(showBackground = true, device = Devices.PHONE)
    @Preview(showBackground = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
    @Preview(showBackground = true, device = Devices.FOLDABLE)
    @Preview(showBackground = true, device = Devices.TABLET)
    annotation class WithBackground

    @Preview(showSystemUi = true, device = Devices.PHONE)
    @Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
    @Preview(showSystemUi = true, device = Devices.FOLDABLE)
    @Preview(showSystemUi = true, device = Devices.TABLET)
    annotation class WithSystemUi
}
