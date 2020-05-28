import React from 'react';
import { Switch, Route } from 'react-router-dom';
import UserRoutes from './Users/UserRoutes';
import ManageProductRoutes from './Products/ManageProductRoutes';
import ManageCategoryRoutes from './Categories/ManageCategoryRoutes';

const Routes = () => {
    return <Switch>
        <Route path='/manage/users' component={UserRoutes}/>
        <Route path='/manage/products' component={ManageProductRoutes}/>
        <Route path='/manage/categories' component={ManageCategoryRoutes}/>
    </Switch>
};

export default Routes;