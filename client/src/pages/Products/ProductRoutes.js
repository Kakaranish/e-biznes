import React from 'react';
import { Switch } from 'react-router-dom';
import MainLayoutRoute from '../../routes/MainLayoutRoute';
import ProductsPage from './ProductsPage';
import ProductPage from './ProductPage';

const ProductRoutes = () => {
    return <Switch>
        <MainLayoutRoute path='/products' component={ProductsPage} exact />
        <MainLayoutRoute path='/products/:id' component={ProductPage} exact />
    </Switch>
};

export default ProductRoutes;