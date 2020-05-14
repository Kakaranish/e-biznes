import React from 'react';
import { Switch, Route } from 'react-router-dom';
import ProductPage from './ProductPage';
import CreateProductPage from './CreateProductPage';
import EditProductPage from './EditProductPage';

const ProductRoutes = () => {
    return (
        <Switch>
            <Route path='/products/create' component={CreateProductPage} />
            <Route path='/products/:id/edit' component={EditProductPage} exact />
            <Route path='/products/:id' component={ProductPage} exact />
        </Switch>
    );
};

export default ProductRoutes;