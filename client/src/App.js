import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import MainLayoutRoute from './MainLayoutRoute';
import CategoryRoutes from './pages/Categories/CategoryRoutes';

const App = () => {
  return (
      <Router>
        <Switch>
          <MainLayoutRoute path='/' component={MainPage} exact />
          <CategoryRoutes />

          <Route component={NotFoundPage} />
        </Switch>
      </Router>
  )
};

export default App;
