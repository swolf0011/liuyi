package com.abcnv.nvone.lib_framework.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule

/**
 * @Description:
 *
 *
 * @Use:{非必须}
 *
 * @property
 *
 * @Author liuyi
 */
@GlideModule
class NvAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {}


    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //设置memory和Bitmap池的大小
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize
        val customBitmapPoolSize = 2L * defaultBitmapPoolSize
        val customMemoryCacheSize = 2L * defaultMemoryCacheSize
        val cacheSize100MegaBytes = 1024 * 1024 * 100L // 100 MB

        builder.setDefaultRequestOptions(NvGlideBuilder.getRequestOptions())
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize))
        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes))
    }

}