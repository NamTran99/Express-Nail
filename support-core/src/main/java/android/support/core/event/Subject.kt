package android.support.core.event

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.Observer

interface Subject<T> {
    fun addObserver(observer: Observer<T>)
    fun removeObserver(observer: Observer<T>)

    fun notifyChange(data: T)

    class Impl<T> : Subject<T> {
        private val mObservers = mutableSetOf<Observer<T>>()
        override fun addObserver(observer: Observer<T>) {
            mObservers.add(observer)
        }

        override fun removeObserver(observer: Observer<T>) {
            mObservers.remove(observer)
        }

        @SuppressLint("RestrictedApi")
        override fun notifyChange(data: T) {
            ArchTaskExecutor.getInstance().executeOnMainThread {
                mObservers.forEach {
                    it.onChanged(data)
                }
            }
        }
    }
}