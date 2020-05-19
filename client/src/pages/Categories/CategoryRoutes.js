import React from 'react';
import { Switch } from 'react-router-dom';
import CategoriesPage from './CategoriesPage';
import CategoryPage from './CategoryPage';
import EditCategoryPage from './EditCategoryPage';
import CreateCategoryPage from './CreateCategoryPage';
import MainLayoutRoute from '../../routes/MainLayoutRoute';

const CategoryRoutes = () => {
    return <Switch>
        <MainLayoutRoute path='/categories' component={CategoriesPage} exact />
        <MainLayoutRoute path='/categories/create' component={CreateCategoryPage} exact />
        <MainLayoutRoute path='/categories/:id/edit' component={EditCategoryPage} />
        <MainLayoutRoute path='/categories/:id' component={CategoryPage} />
    </Switch>
};

export default CategoryRoutes;