import React from 'react';
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import NotFoundPage from './pages/NotFoundPage';
import ManageCategoryRoutes from './pages/Categories/Manage/ManageCategoryRoutes';
import ManageProductRoutes from './pages/Products/Manage/ManageProductRoutes';
import UserRoutes from './pages/Users/UserRoutes';
import AuthRoutes from './pages/Auth/AuthRoutes';
import CartRoutes from './pages/Cart/CartRoutes';
import ProductRoutes from './pages/Products/ProductRoutes';
import WishlistPage from './pages/WishlistPage';
import CategoriesPage from './pages/Categories/CategoriesPage';
import MainLayout from './routes/MainLayout';
import AuthorizedOnlyRoute from './routes/AuthorizedOnlyRoute';
import OrderRoutes from './pages/Orders/OrderRoutes';
import NotificationFetcher from './components/NotificationFetcher';
import NotificationsPage from './pages/Notifications/NotificationsPage';

const App = () => {
  return <>
    <NotificationFetcher />
    <Router>
      <MainLayout>
        <Switch>
          <Redirect exact from='/' to='/products' />
          <Route path='/products' component={ProductRoutes} />
          <Route path='/manage/products' component={ManageProductRoutes} />
          <Route path='/categories' component={CategoriesPage} />
          <Route path='/manage/categories' component={ManageCategoryRoutes} />
          <Route path='/orders' component={OrderRoutes} />
          <Route path='/users' component={UserRoutes} />
          <Route path='/auth' component={AuthRoutes} />
          <AuthorizedOnlyRoute path='/cart' component={CartRoutes} />
          <AuthorizedOnlyRoute path='/notifications' component={NotificationsPage} />
          <AuthorizedOnlyRoute path='/wishlist' component={WishlistPage} />

          <Route component={NotFoundPage} />
        </Switch>
      </MainLayout>
    </Router>
  </>
};

export default App;
