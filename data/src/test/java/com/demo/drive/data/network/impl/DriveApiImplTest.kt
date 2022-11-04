package com.demo.drive.data.network.impl

import com.demo.drive.core.model.ListParams
import com.demo.drive.data.network.ApiClient
import com.demo.drive.data.network.GoogleDriveService
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.demo.drive.core.model.Result
import com.demo.drive.core.model.listParams
import com.demo.drive.data.model.Error
import com.demo.drive.data.model.ErrorResponseDTO
import com.demo.drive.data.model.ListResponseDTO
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response

class DriveApiImplTest {

    private val exception = Exception("test exception")
    private val listParams: ListParams = listParams {  }
    private val listResponseDTO = ListResponseDTO()
    private val errorResponseDTO = ErrorResponseDTO(Error(null, "test message"))

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var apiClient: ApiClient

    @MockK
    private lateinit var googleDriveService: GoogleDriveService

    @MockK
    private lateinit var httpException: HttpException
    @MockK
    private lateinit var response: Response<*>
    @MockK
    private lateinit var errorBody: ResponseBody
    @MockK
    private lateinit var responseBodyConverter: Converter<ResponseBody, ErrorResponseDTO>

    private lateinit var driveApiImpl: DriveApiImpl

    @Before
    fun setup() {
        every {
            apiClient.createService(GoogleDriveService::class.java)
        } returns googleDriveService

        every {
            apiClient.responseBodyConverter<ErrorResponseDTO>(ErrorResponseDTO::class.java, emptyArray())
        } returns responseBodyConverter

        every {
            responseBodyConverter.convert(any())
        } returns errorResponseDTO

        every {
            response.errorBody()
        } returns errorBody

        every {
            httpException.response()
        } returns response

        driveApiImpl = DriveApiImpl(apiClient)
    }

    @Test
    fun `list returns Result#Error when Exception is caught`() = runBlocking {
        every {
            runBlocking {
                googleDriveService.list(any(), any(), any(), any(), any())
            }
        } throws exception

        val result = driveApiImpl.list(listParams, null)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception.message).isEqualTo(exception.message)
    }

    @Test
    fun `list returns Result#Error with message from response body when HttpException#errorBody is ErrorResponseDTO`() = runBlocking {
        every {
            runBlocking {
                googleDriveService.list(any(), any(), any(), any(), any())
            }
        } throws httpException

        val result = driveApiImpl.list(listParams, null)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception.message).isEqualTo(errorResponseDTO.error?.message)
    }

    @Test
    fun `list returns Result#Success when success response`() = runBlocking {
        every {
            runBlocking {
                googleDriveService.list(any(), any(), any(), any(), any())
            }
        } returns listResponseDTO

        val result = driveApiImpl.list(listParams, null)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(listResponseDTO)
    }
}