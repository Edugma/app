package io.edugma.data.base.utils.retrofit.call_adapters.flow_result

import io.edugma.data.base.utils.retrofit.call_adapters.common.BaseCallAdapterFactory
import io.edugma.data.base.utils.retrofit.call_adapters.common.FlowCallAdapter
import io.edugma.data.base.utils.retrofit.call_adapters.common.ResponseMapper
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class FlowResultCallAdapterFactory(
    private val onResponse: (code: Int, body: Any?) -> Unit
) : BaseCallAdapterFactory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        val insideFlowType = checkFlow(returnType) ?: return null
        val insideResultType = checkResult(insideFlowType) ?: return null

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, insideResultType, annotations)

        return FlowCallAdapter<Any>(
            insideResultType,
            ResponseMapper(errorBodyConverter, onResponse)
        )
    }
}