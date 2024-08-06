package dev.grahamsmith.openfireandroiddemo.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.grahamsmith.openfireandroiddemo.data.SecurePreferences
import dev.grahamsmith.openfireandroiddemo.data.XmppManager
import dev.grahamsmith.openfireandroiddemo.data.XmppRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideXMPPManager(): XmppManager {
        return XmppManager
    }

    @Provides
    @Singleton
    fun provideXMPPRepository(xmppManager: XmppManager): XmppRepository {
        return XmppRepository(xmppManager.getConnection())
    }

    @Provides
    @Singleton
    fun provideSecurePreferences(@ApplicationContext context: Context): SecurePreferences {
        return SecurePreferences(context)
    }
}