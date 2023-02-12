package io.edugma.data.base.utils.retrofit.callAdapters.flowResult

import io.edugma.data.base.utils.retrofit.callAdapters.common.BaseCallAdapterFactory
import io.edugma.data.base.utils.retrofit.callAdapters.common.FlowCallAdapter
import io.edugma.data.base.utils.retrofit.callAdapters.common.ResponseMapper
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class FlowResultCallAdapterFactory(
    private val onResponse: (code: Int, body: Any?) -> Unit,
) : BaseCallAdapterFactory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {

        val insideFlowType = checkFlow(returnType) ?: return null
        val insideResultType = checkResult(insideFlowType) ?: return null

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, insideResultType, annotations)

        return FlowCallAdapter<Any>(
            insideResultType,
            ResponseMapper(errorBodyConverter, onResponse),
        )
    }
}
