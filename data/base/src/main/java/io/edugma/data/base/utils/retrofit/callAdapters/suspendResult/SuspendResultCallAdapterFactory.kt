package io.edugma.data.base.utils.retrofit.callAdapters.suspendResult

import io.edugma.data.base.utils.retrofit.callAdapters.common.BaseCallAdapterFactory
import io.edugma.data.base.utils.retrofit.callAdapters.common.ResponseMapper
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type

class SuspendResultCallAdapterFactory(
    private val onResponse: (code: Int, body: Any?) -> Unit,
) : BaseCallAdapterFactory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        val insideCallType = checkCall(returnType) ?: return null
        val insideResultType = checkResult(insideCallType) ?: return null

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, insideResultType, annotations)

        return CatchingCallAdapter(insideResultType, ResponseMapper(errorBodyConverter, onResponse))
    }

    private class CatchingCallAdapter<T : Any>(
        private val type: Type,
        private val mapper: ResponseMapper<T>,
    ) : CallAdapter<T, Call<Result<T>>> {
        override fun responseType(): Type = type
        override fun adapt(call: Call<T>): Call<Result<T>> = CatchingCall(call, mapper)
    }

    private class CatchingCall<T : Any>(
        private val delegate: Call<T>,
        private val mapper: ResponseMapper<T>,
    ) : Call<Result<T>> {

        override fun enqueue(callback: Callback<Result<T>>) = delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(this@CatchingCall, Response.success(mapper.mapResponse(response)))
                } else {
                    val throwable = HttpException(response)
                    callback.onResponse(
                        this@CatchingCall,
                        Response.success(mapper.mapFailure(throwable)),
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@CatchingCall,
                    Response.success(mapper.mapFailure(t)),
                )
            }
        })

        override fun clone(): Call<Result<T>> = CatchingCall(delegate, mapper)
        override fun execute(): Response<Result<T>> =
            throw UnsupportedOperationException("Suspend function should not be blocking.")

        override fun isExecuted(): Boolean = delegate.isExecuted
        override fun cancel(): Unit = delegate.cancel()
        override fun isCanceled(): Boolean = delegate.isCanceled
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()
    }
}
