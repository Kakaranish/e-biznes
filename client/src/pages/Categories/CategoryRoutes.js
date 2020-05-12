import React from 'react';
import { Route } from 'react-router-dom';
import CategoriesPage from './CategoriesPage';
import CategoryPage from './CategoryPage';

const CategoryRoutes = () => {
    return (
        <>
            <Route path='/categories' component={CategoriesPage} exact/>
            <Route path='/categories/:id' component={CategoryPage} />
        </>
    );
};

export default CategoryRoutes;