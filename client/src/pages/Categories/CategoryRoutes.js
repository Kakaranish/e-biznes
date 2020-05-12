import React from 'react';
import CategoriesPage from '../CategoriesPage';
import { Route } from 'react-router-dom';

const CategoryRoutes = () => {
    return (
        <>
            <Route path='/' component={CategoriesPage} />
        </>
    );
};

export default CategoryRoutes;