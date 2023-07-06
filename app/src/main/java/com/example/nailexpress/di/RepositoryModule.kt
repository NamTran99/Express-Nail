package com.example.nailexpress.di

import android.app.Application
import com.example.nailexpress.datasource.local.SharePrefs
import com.example.nailexpress.datasource.remote.*
import com.example.nailexpress.factory.*
import com.example.nailexpress.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideGeneralApi(retrofit: Retrofit): GeneralApi = retrofit.create(GeneralApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCvApi(retrofit: Retrofit): CvApi = retrofit.create(CvApi::class.java)

    @Provides
    @Singleton
    fun provideBookingApi(retrofit: Retrofit): RecruitmentBookingStaffApi =
        retrofit.create(RecruitmentBookingStaffApi::class.java)

    @Provides
    @Singleton
    fun provideSalonApi(retrofit: Retrofit): SalonApi = retrofit.create(SalonApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

    @Singleton
    @Provides
    fun provideTextFormatter(cxt: Application): TextFormatter = TextFormatter(cxt)

    // factory
    @Singleton
    @Provides
    fun provideGeneralFactory(textFormatter: TextFormatter): GeneralFactory =
        GeneralFactory(textFormatter)

    @Singleton
    @Provides
    fun provideCvFactory(textFormatter: TextFormatter): BookingCvFactory = BookingCvFactory(textFormatter)

    @Singleton
    @Provides
    fun provideSalonFactory(textFormatter: TextFormatter): SalonFactory =
        SalonFactory(textFormatter)

    @Singleton
    @Provides
    fun provideProfileFactory(textFormatter: TextFormatter): ProfileFactory =
        ProfileFactory(textFormatter)

    // repository
    @Singleton
    @Provides
    fun provideCvRepo(
        userDataSource: SharePrefs,
        application: Application,
        cvApi: CvApi,
        cvFactory: BookingCvFactory
    ): CvRepository = CvRepository(userDataSource, application, cvApi, cvFactory)

    @Singleton
    @Provides
    fun provideBookingStaffRepo(
        userDataSource: SharePrefs,
        application: Application,
        cvFactory: BookingCvFactory,
        bookingApi: RecruitmentBookingStaffApi
    ): BookingStaffRepository =
        BookingStaffRepository(userDataSource, application, bookingApi, cvFactory)

    @Singleton
    @Provides
    fun provideSalonRepo(
        userDataSource: SharePrefs,
        application: Application,
        cvFactory: SalonFactory,
        bookingApi: SalonApi
    ): SalonRepository = SalonRepository(userDataSource, application, bookingApi, cvFactory)

    @Singleton
    @Provides
    fun provideAuthRepo(
        userDataSource: SharePrefs,
        application: Application,
        authApi: AuthApi,
        profileFactory: ProfileFactory
    ): AuthRepository = AuthRepository(userDataSource, application, authApi, profileFactory)

    @Singleton
    @Provides
    fun provideProfileRepo(
        userDataSource: SharePrefs,
        application: Application,
        profileApi: ProfileApi,
        profileFactory: ProfileFactory
    ): ProfileRepository =
        ProfileRepository(userDataSource, application, profileApi, profileFactory)


    @Singleton
    @Provides
    fun provideGeneralRepo(
        userDataSource: SharePrefs,
        application: Application,
        api: GeneralApi,
        factory: GeneralFactory
    ): GeneralRepository =
        GeneralRepository(userDataSource, application, api, factory)
}