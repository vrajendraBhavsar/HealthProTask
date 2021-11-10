package com.example.healthprotask

import com.example.healthprotask.auth.model.DistanceResponse
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.auth.nework.AuthApiService
import com.example.healthprotask.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


open class GetDistanceApiCallUnitTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()  //use this when you are testing suspend class or coroutine.
//1. mock creation
    @InjectMocks
    private lateinit var authRepository: AuthRepository
    @Mock
    private lateinit var authRepositoryMock: AuthRepository
    @Mock
    private lateinit var authApiService: AuthApiService

    //To make sure a new mock is created for each new test and therefore all tests are independent, we will initialize the mocks at the “@Before” step of this test class.
    @ExperimentalCoroutinesApi
    @Before
    open fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun fetchUserData()  {
        runBlocking {
//2. created dummy model to get in response,
            val distanceResponse =  DistanceResponse(
                activitiesDistance = mutableListOf(
                    DistanceResponse.ActivitiesDistance("12Nov", "123"),
                    DistanceResponse.ActivitiesDistance("15Jul", "12"),
                    DistanceResponse.ActivitiesDistance("2Mar", "923"),
                    DistanceResponse.ActivitiesDistance("28Feb", "1230"),
                ))
//2. action on mock object
            //repository obj
            authRepositoryMock.getDistance("BearerToken abc!23XYZ", "23-12-2021")
            //stubbed method =>
            `when`(authRepositoryMock.getDistance("BearerToken abc!23XYZ", "23-12-2020")).thenReturn(distanceResponse)
//3. verification
            Mockito.verify(authRepositoryMock).getDistance("BearerToken abc!23XYZ", "23-12-2021")
        }
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}