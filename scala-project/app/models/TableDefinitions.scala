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
  val userTable = TableQuery[UserTable]
  val wishlistItemTable = TableQuery[WishlistItemTable]
  val appUserTable = TableQuery[AppUserTable]
  val passwordInfoTable = TableQuery[PasswordInfoTable]
  val loginInfoTable = TableQuery[LoginInfoTable]
  val userLoginInfoTable = TableQuery[UserLoginInfoTable]
}