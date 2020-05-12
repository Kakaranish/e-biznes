import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle';
import MainPage from './pages/MainPage';
import CategoriesPage from './pages/CategoriesPage';
import NotFoundPage from './pages/NotFoundPage';
import MainLayoutRoute from './MainLayoutRoute';

const App = () => {
  return (
      <BrowserRouter>
        <Switch>
          <MainLayoutRoute path='/' component={MainPage} exact />
          <MainLayoutRoute path='/categories' component={CategoriesPage} />

          <Route component={NotFoundPage} />
        </Switch>
      </BrowserRouter>
  )
};

export default App;
