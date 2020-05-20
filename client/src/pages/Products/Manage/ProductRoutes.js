import React from 'react';
import { Switch, Route } from 'react-router-dom';
import ProductPage from './ProductPage';
import CreateProductPage from './CreateProductPage';
import EditProductPage from './EditProductPage';
import ProductsPage from './ProductsPage';
import AuthorizedOnlyRoute from '../../../routes/AuthorizedOnlyRoute';

const ProductRoutes = () => {
    return <Switch>
        <AuthorizedOnlyRoute path='/manage/products' component={ProductsPage} roles={["ADMIN"]} exact />
        <AuthorizedOnlyRoute path='/manage/products/create' component={CreateProductPage} roles={["ADMIN"]} />
        <AuthorizedOnlyRoute path='/manage/products/:id/edit' component={EditProductPage} roles={["ADMIN"]} />
        <AuthorizedOnlyRoute path='/manage/products/:id' component={ProductPage} exact roles={["ADMIN"]} />
    </Switch>
};

export default ProductRoutes;