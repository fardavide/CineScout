package cinescout.design

import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache

fun ImageLoaderFactory(context: Context) = ImageLoaderFactory {
    ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()
}
