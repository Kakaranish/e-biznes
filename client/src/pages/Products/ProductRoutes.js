import React from 'react';
import { Switch, Route } from 'react-router-dom';
import ProductPage from './ProductPage';
import CreateProductPage from './CreateProductPage';
import EditProductPage from './EditProductPage';
import ProductsPage from './ProductsPage';
import MainLayoutRoute from '../../routes/MainLayoutRoute';

const ProductRoutes = () => {
    return <Switch>
        <MainLayoutRoute path='/products' component={ProductsPage} exact />
        <MainLayoutRoute path='/products/create' component={CreateProductPage} />
        <MainLayoutRoute path='/products/:id/edit' component={EditProductPage} />
        <MainLayoutRoute path='/products/:id' component={ProductPage} exact />
    </Switch>
};

export default ProductRoutes;