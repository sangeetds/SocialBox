package com.socialbox.di

import com.socialbox.group.data.service.MovieService
import com.socialbox.group.data.service.GroupService
import com.socialbox.login.data.service.UserService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
// import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

  @Provides
  fun getBaseUrl() = "https://murmuring-spire-01887.herokuapp.com"

  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

  @Provides
  @Singleton
  fun provideMoshi(): Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  @Provides
  @Singleton
  fun provideRetrofit(
    okHttpClient: OkHttpClient,
    baseUrl: String,
    moshi: Moshi
  ): Retrofit =
    Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(okHttpClient)
      .build()

  @Provides
  @Singleton
  fun provideUserService(retrofit: Retrofit): UserService =
    retrofit.create(UserService::class.java)

  @Provides
  @Singleton
  fun provideGroupService(retrofit: Retrofit): GroupService =
    retrofit.create(GroupService::class.java)

  @Provides
  @Singleton
  fun provideMovieService(retrofit: Retrofit): MovieService =
    retrofit.create(MovieService::class.java)
}