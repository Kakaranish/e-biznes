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

const App = () => {
  return (
    <Router>
      <Switch>
        <MainLayoutRoute path='/' component={MainPage} exact />
        <Route path='/products' component={ProductRoutes} />
        <Route path='/manage/products' component={ManageProductRoutes} />
        <Route path='/manage/categories' component={ManageCategoryRoutes} />
        <Route path='/users' component={UserRoutes} />
        <Route path='/auth' component={AuthRoutes} />
        <Route path='/cart' component={CartRoutes} />

        <MainLayoutRoute component={NotFoundPage} />
      </Switch>
    </Router>
  )
};

export default App;
