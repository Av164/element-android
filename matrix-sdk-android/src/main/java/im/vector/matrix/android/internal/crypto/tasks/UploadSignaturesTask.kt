/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.matrix.android.internal.crypto.tasks

import im.vector.matrix.android.api.failure.Failure
import im.vector.matrix.android.internal.crypto.api.CryptoApi
import im.vector.matrix.android.internal.crypto.model.rest.SignatureUploadResponse
import im.vector.matrix.android.internal.network.executeRequest
import im.vector.matrix.android.internal.task.Task
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

internal interface UploadSignaturesTask : Task<UploadSignaturesTask.Params, Unit> {
    data class Params(
            val signatures: Map<String, Map<String, Any>>
    )
}

internal class DefaultUploadSignaturesTask @Inject constructor(
        private val cryptoApi: CryptoApi,
        private val eventBus: EventBus
) : UploadSignaturesTask {

    override suspend fun execute(params: UploadSignaturesTask.Params) {
        try {
            val response = executeRequest<SignatureUploadResponse>(eventBus) {
                apiCall = cryptoApi.uploadSignatures(params.signatures)
            }
            if (response.failures?.isNotEmpty() == true) {
                throw Throwable(response.failures.toString())
            }
            return
        } catch (f: Failure) {
            throw f
        }
    }
}