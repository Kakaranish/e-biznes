import React from 'react';
import { Switch, Route } from 'react-router-dom';
import ProductPage from './ProductPage';

const ProductRoutes = () => {
    return (
        <Switch>
            <Route path='/products/:id' component={ProductPage} exact />
        </Switch>
    );
};

export default ProductRoutes;