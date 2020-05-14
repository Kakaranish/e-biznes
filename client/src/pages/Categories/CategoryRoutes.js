import React from 'react';
import { Route, Switch } from 'react-router-dom';
import CategoriesPage from './CategoriesPage';
import CategoryPage from './CategoryPage';
import EditCategoryPage from './EditCategoryPage';
import CreateCategoryPage from './CreateCategoryPage';

const CategoryRoutes = () => {
    return (
        <>
            <Switch>
                <Route path='/categories' component={CategoriesPage} exact />
                <Route path='/categories/create' component={CreateCategoryPage} exact />
                <Route path='/categories/:id/edit' component={EditCategoryPage} />
                <Route path='/categories/:id' component={CategoryPage} />
            </Switch>
        </>
    );
};

export default CategoryRoutes;