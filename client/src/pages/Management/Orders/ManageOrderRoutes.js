import React from 'react';
import { Switch, Route } from 'react-router-dom';
import OrdersPage from './OrdersPage';
import OrderPage from './OrderPage';

const ManageOrderRoutes = () => {
    return <Switch>
        <Route exact path='/manage/orders' component={OrdersPage} />
        <Route path='/manage/orders/:id' component={OrderPage} />
    </Switch>
};

export default ManageOrderRoutes;