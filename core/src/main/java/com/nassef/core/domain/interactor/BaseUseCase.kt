package com.nassef.core.domain.interactor

import com.nassef.core.data.model.Resource
import com.nassef.core.domain.error.ErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


abstract class BaseUseCase<Domain, in Body>(protected val errorHandler: ErrorHandler)  {
//    protected val errorHandler: ErrorHandler by inject()

    protected fun requireBody(body: Body?): @UnsafeVariance Body =
        body ?: throw IllegalArgumentException("UseCase body is required and was null")

    protected abstract fun executeDS(body: Body? = null): Flow<Domain>

    operator fun invoke(
        scope: CoroutineScope, body: Body? = null, multipleInvoke: Boolean = false,
        onResult: (Resource<Domain>) -> Unit
    ) : Job {
        return scope.launch(Dispatchers.Main) {
            if (multipleInvoke.not()) onResult.invoke(Resource.Companion.loading())

            runFlow(executeDS(body), onResult).collect {
                onResult.invoke(
                    Resource.success(it)
                )
                if (multipleInvoke.not()) onResult.invoke(Resource.Companion.loading(false))
            }
        }
    }

    operator fun invoke(
        body: Body? = null, multipleInvoke: Boolean = false,
    ): Flow<Resource<Domain>> =
        channelFlow {
            if (multipleInvoke.not()) send(Resource.Companion.loading())
            runFlow(executeDS(body)) {
                send(it)
            }.collect {
                send(Resource.Companion.success(it))
                if (multipleInvoke.not()) send(Resource.Companion.loading(false))
            }
        }

    fun <M> runFlow(
        requestExecution: Flow<M>, onResult: suspend (Resource<Domain>) -> Unit
    ): Flow<M> = requestExecution.catch { e ->
        onResult(Resource.failure(errorHandler.getError(e)))
        onResult.invoke(Resource.Companion.loading(false))
    }.flowOn(Dispatchers.IO)
}