import React from 'react';
import { BrowserRouter as Router, Switch } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import MainLayoutRoute from './MainLayoutRoute';
import CategoryRoutes from './pages/Categories/CategoryRoutes';
import ProductRoutes from './pages/Products/ProductRoutes';

const App = () => {
  return (
      <Router>
        <Switch>
          <MainLayoutRoute path='/' component={MainPage} exact />
          <MainLayoutRoute path='/categories' component={CategoryRoutes} />
          <MainLayoutRoute path='/products' component={ProductRoutes} />
          
          <MainLayoutRoute component={NotFoundPage} />
        </Switch>
      </Router>
  )
};

export default App;
