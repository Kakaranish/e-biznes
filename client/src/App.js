import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import './App.css';
import MainPage from './pages/MainPage';
import CategoriesPage from './pages/CategoriesPage';
import NotFoundPage from './pages/NotFoundPage';

const App = () => {
  return (
    <BrowserRouter>
      <Switch>
        <Route path='/' component={MainPage} exact />
        <Route path='/categories' component={CategoriesPage} />

        <Route component={NotFoundPage} />
      </Switch>
    </BrowserRouter>
  )
};

export default App;
