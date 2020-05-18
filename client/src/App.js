import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import CategoryRoutes from './pages/Categories/CategoryRoutes';
import ProductRoutes from './pages/Products/ProductRoutes';
import UserRoutes from './pages/Users/UserRoutes';
import AuthRoutes from './pages/Auth/AuthRoutes';
import MainLayoutRoute from './routes/MainLayoutRoute';

const App = () => {
  return (
    <Router>
      <Switch>
        <MainLayoutRoute path='/' component={MainPage} exact />
        <Route path='/categories' component={CategoryRoutes} />
        <Route path='/products' component={ProductRoutes} />
        <Route path='/users' component={UserRoutes} />
        <Route path='/auth' component={AuthRoutes} />

        <MainLayoutRoute component={NotFoundPage} />
      </Switch>
    </Router>
  )
};

export default App;
