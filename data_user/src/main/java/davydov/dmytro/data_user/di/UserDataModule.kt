package davydov.dmytro.data_user.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import davydov.dmytro.data_user.data.UserApi
import davydov.dmytro.data_user.data.UserRepositoryImpl
import davydov.dmytro.data_user.domain.UserRepository
import retrofit2.Retrofit
import retrofit2.create

@Module
interface UserDataModule {
    @Binds
    @Reusable
    fun bindRepo(impl: UserRepositoryImpl): UserRepository

    companion object {
        @Provides
        @Reusable
        fun provideApi(retrofit: Retrofit): UserApi {
            return retrofit.create()
        }
    }
}