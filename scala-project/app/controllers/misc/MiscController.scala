package controllers.misc

import com.mohiva.play.silhouette.api.Silhouette
import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}

//@Singleton
//class SignUpController @Inject()(components: ControllerComponents,
//                                 silhouette: Silhouette[DefaultEnv],
//                                )(implicit ex: ExecutionContext)
//  extends AbstractController(components) with I18nSupport {
//  def signUp() = silhouette.UnsecuredAction(parse.json) { implicit request =>
//    Ok
//  }
//}

//@Singleton
//class ApplicationController @Inject() (implicit val env: Environment[DefaultEnv])
//  extends Silhouette[DefaultEnv] {
//
//}


@Singleton
class SignUpController @Inject()(cc: ControllerComponents, silhouette: Silhouette[DefaultEnv])
  extends AbstractController(cc)
    with I18nSupport {

  def signUp() = Action {
    Ok
  }
}