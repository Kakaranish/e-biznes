import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import ManageCategoryRoutes from './pages/Categories/Manage/ManageCategoryRoutes';
import ManageProductRoutes from './pages/Products/Manage/ManageProductRoutes';
import UserRoutes from './pages/Users/UserRoutes';
import AuthRoutes from './pages/Auth/AuthRoutes';
import MainLayoutRoute from './routes/MainLayoutRoute';
import CartRoutes from './pages/Cart/CartRoutes';
import ProductRoutes from './pages/Products/ProductRoutes';
import WishlistPage from './pages/WishlistPage';
import CategoriesPage from './pages/Categories/CategoriesPage';
import MainLayout from './routes/MainLayout';
import AuthorizedOnlyRoute from './routes/AuthorizedOnlyRoute';

const App = () => {
  return <>
    <Router>
      <MainLayout>
        <Switch>
          <Route path='/' component={MainPage} exact />
          <Route path='/products' component={ProductRoutes} />
          <Route path='/manage/products' component={ManageProductRoutes} />
          <Route path='/categories' component={CategoriesPage} />
          <Route path='/manage/categories' component={ManageCategoryRoutes} />
          <Route path='/users' component={UserRoutes} />
          <Route path='/auth' component={AuthRoutes} />
          <Route path='/cart' component={CartRoutes} />
          <AuthorizedOnlyRoute path='/wishlist' component={WishlistPage} />

          <MainLayoutRoute component={NotFoundPage} />
        </Switch>
      </MainLayout>
    </Router>
  </>
};

export default App;
