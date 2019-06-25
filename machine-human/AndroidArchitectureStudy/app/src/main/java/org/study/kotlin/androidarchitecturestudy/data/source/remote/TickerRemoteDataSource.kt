package org.study.kotlin.androidarchitecturestudy.data.source.remote

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.study.kotlin.androidarchitecturestudy.api.retorifit.RetrofitBuilder
import org.study.kotlin.androidarchitecturestudy.base.BaseDataSource

/**
 ***************************
BaseDataSource - structure

i = interface
f = function
 ***************************

i = BaseDataSource

i = GetTickerListCallback

f = onTickerListLoaded(tickerList: List<TickerModel>)
f = onDataNotAvailable(error: String)

f = requestMarkets(marketName: String, callback: GetTickerListCallback)

 */
class TickerRemoteDataSource private constructor(
) : BaseDataSource, RetrofitBuilder() {

    override fun requestMarkets(marketName: String, callback: BaseDataSource.GetTickerListCallback) {
        service.getMarket()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success ->
                    success.
                        map { it.market }?.
                        filter { it.substringBeforeLast("-") == marketName }.
                        joinToString().
                        let { getTickerList(it, callback) }
                },
                { failed -> callback.onDataNotAvailable(failed) })
    }

    private fun getTickerList(markets: String, callback: BaseDataSource.GetTickerListCallback) {
        service.getTicker(markets)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success -> callback.onTickerListLoaded(success) },
                { failed -> callback.onDataNotAvailable(failed) })
    }

    companion object {
        //static 접근을 허용할 프로터피/함수등 입력
        private var instance: TickerRemoteDataSource? = null

        operator fun invoke(): TickerRemoteDataSource {
            Log.e("TAG", "remoteinvoke")
            return instance ?: TickerRemoteDataSource()
                .apply { instance = this }

        }
    }
}