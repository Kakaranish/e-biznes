import React from 'react';
import { Switch, Route } from 'react-router-dom';
import UserRoutes from './Users/UserRoutes';
import ManageProductRoutes from './Products/ManageProductRoutes';
import ManageCategoryRoutes from './Categories/ManageCategoryRoutes';
import AuthorizedOnlyRoute from '../../routes/AuthorizedOnlyRoute';
import CreateNotification from './Notifications/CreateNotification';
import ManageOrderRoutes from './Orders/ManageOrderRoutes';

const Routes = () => {
    return <Switch>
        <Route path='/manage/users' component={UserRoutes} />
        <Route path='/manage/products' component={ManageProductRoutes} />
        <Route path='/manage/categories' component={ManageCategoryRoutes} />
        <Route path='/manage/orders' component={ManageOrderRoutes} />
        <AuthorizedOnlyRoute path='/manage/notifications/create'
            component={CreateNotification} roles={["ADMIN"]} />
    </Switch>
};

export default Routes;