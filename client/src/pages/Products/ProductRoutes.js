import React from 'react';
import { Switch, Route } from 'react-router-dom';
import MainLayoutRoute from '../../routes/MainLayoutRoute';
import ProductsPage from './ProductsPage';
import ProductPage from './ProductPage';
import ProductsByCategoryPage from './ProductsByCategoryPage';

const ProductRoutes = () => {
    return <Switch>
        <Route path='/products' component={ProductsPage} exact />
        <Route path='/products/category/:id' component={ProductsByCategoryPage} />
        <Route path='/products/:id' component={ProductPage} />
    </Switch>
};

export default ProductRoutes;