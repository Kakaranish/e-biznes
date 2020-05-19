package models

import slick.lifted.TableQuery

trait TableDefinitions {
  val cartTable = TableQuery[CartTable]
  val cartItemTable = TableQuery[CartItemTable]
  val categoryTable = TableQuery[CategoryTable]
  val notificationTable = TableQuery[NotificationTable]
  val opinionTable = TableQuery[OpinionTable]
  val orderTable = TableQuery[OrderTable]
  val paymentTable = TableQuery[PaymentTable]
  val productTable = TableQuery[ProductTable]
  val shippingInfoTable = TableQuery[ShippingInfoTable]
  val wishlistItemTable = TableQuery[WishlistItemTable]
  val userTable = TableQuery[UserTable]
  val passwordInfoTable = TableQuery[PasswordInfoTable]
  val loginInfoTable = TableQuery[LoginInfoTable]
  val userLoginInfoTable = TableQuery[UserLoginInfoTable]
  val oauth2InfoTable = TableQuery[OAuth2InfoTable]
}