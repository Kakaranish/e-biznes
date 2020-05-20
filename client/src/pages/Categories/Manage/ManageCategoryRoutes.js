import React from 'react';
import { Switch } from 'react-router-dom';
import CategoriesPage from './CategoriesPage';
import CategoryPage from './CategoryPage';
import EditCategoryPage from './EditCategoryPage';
import CreateCategoryPage from './CreateCategoryPage';
import AuthorizedOnlyRoute from '../../../routes/AuthorizedOnlyRoute';

const ManageCategoryRoutes = () => {
    return <Switch>
        <AuthorizedOnlyRoute path='/manage/categories' component={CategoriesPage} roles={["ADMIN"]} exact />
        <AuthorizedOnlyRoute path='/manage/categories/create' component={CreateCategoryPage} roles={["ADMIN"]} exact />
        <AuthorizedOnlyRoute path='/manage/categories/:id/edit' component={EditCategoryPage} roles={["ADMIN"]} />
        <AuthorizedOnlyRoute path='/manage/categories/:id' component={CategoryPage} roles={["ADMIN"]} />
    </Switch>
};

export default ManageCategoryRoutes;