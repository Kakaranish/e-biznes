import React from 'react';
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import NotFoundPage from './pages/NotFoundPage';
import CartRoutes from './pages/Cart/CartRoutes';
import AuthRoutes from './pages/Auth/AuthRoutes';
import ProductRoutes from './pages/Products/ProductRoutes';
import WishlistPage from './pages/WishlistPage';
import CategoriesPage from './pages/Categories/CategoriesPage';
import MainLayout from './routes/MainLayout';
import AuthorizedOnlyRoute from './routes/AuthorizedOnlyRoute';
import OrderRoutes from './pages/Orders/OrderRoutes';
import NotificationFetcher from './skeleton/NotificationFetcher';
import NotificationsPage from './pages/Notifications/NotificationsPage';
import ManagementRoutes  from './pages/Management/Routes';

const App = () => {
  return <>
    <NotificationFetcher />
    <Router>
      <MainLayout>
        <Switch>
          <Redirect exact from='/' to='/products' />
          <Route path='/products' component={ProductRoutes} />
          <Route path='/categories' component={CategoriesPage} />
          <Route path='/orders' component={OrderRoutes} />
          <Route path='/auth' component={AuthRoutes} />
          <AuthorizedOnlyRoute path='/cart' component={CartRoutes} />
          <AuthorizedOnlyRoute path='/notifications' component={NotificationsPage} />
          <AuthorizedOnlyRoute path='/wishlist' component={WishlistPage} />

          <Route path='/manage' component={ManagementRoutes} />
          <Route component={NotFoundPage} />
        </Switch>
      </MainLayout>
    </Router>
  </>
};

export default App;
