import React from 'react';
import MainLayoutRoute from '../../MainLayoutRoute';
import CategoriesPage from '../CategoriesPage';

const CategoryRoutes = () => {
    return (
        <>
            <MainLayoutRoute path='/categories' component={CategoriesPage} />
        </>
    );
};

export default CategoryRoutes;