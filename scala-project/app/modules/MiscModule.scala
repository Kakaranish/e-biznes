package modules

//
import net.ceedubs.ficus.Ficus._
import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.crypto.{Crypter, CrypterAuthenticatorEncoder}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings}
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util.{Clock, IDGenerator}
import com.mohiva.play.silhouette.api.{Environment, EventBus, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticator, JWTAuthenticatorService, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.util.SecureRandomIDGenerator
import controllers.misc.{DefaultEnv, UserService, UserServiceImpl}
import net.codingwell.scalaguice.ScalaModule
//import play.api.Configuration
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api._

import scala.concurrent.ExecutionContext.Implicits._

class MiscModule extends AbstractModule with ScalaModule {
  override def configure() {
    bind[Silhouette[DefaultEnv]].to[SilhouetteProvider[DefaultEnv]]
    bind[UserService].to[UserServiceImpl]
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
  }

  @Provides
  def provideEnvironment(userService: UserService,
                         authenticatorService: AuthenticatorService[JWTAuthenticator],
                         eventBus: EventBus) = Environment[DefaultEnv](
    userService,
    authenticatorService,
    Seq(),
    eventBus
  )

  @Provides
  def provideAuthenticatorService(
                                   @Named("authenticator-crypter") crypter: Crypter,
                                   idGenerator: IDGenerator,
                                   configuration: Configuration,
                                   clock: Clock): AuthenticatorService[JWTAuthenticator] = {


    val settings = JWTAuthenticatorSettings(sharedSecret = configuration.get[String]("play.http.secret.key"))
    val encoder = new CrypterAuthenticatorEncoder(crypter)

    new JWTAuthenticatorService(settings, None, encoder, idGenerator, clock)
  }

  @Provides
  @Named("authenticator-crypter")
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    val config = configuration.underlying.as[JcaCrypterSettings]("silhouette.authenticator.crypter")

    new JcaCrypter(config)
  }

}