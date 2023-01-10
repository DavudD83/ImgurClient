package davydov.dmytro.feature_account.posts.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import davydov.dmytro.feature_account.posts.data.PostsApi
import retrofit2.Retrofit

@Module
interface PostsDataModule {

    companion object {
        @Provides
        @Reusable
        @JvmStatic
        fun providePostsApi(retrofit: Retrofit): PostsApi {
            return retrofit.create(PostsApi::class.java)
        }
    }
}