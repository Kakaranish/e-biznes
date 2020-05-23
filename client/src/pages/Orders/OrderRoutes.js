import React from 'react';
import { Switch, Route } from 'react-router-dom';
import OrdersPage from './OrdersPage';

const OrderRoutes = () => {
    return <Switch>
        <Route path='/orders' component={OrdersPage} exact />
    </Switch>
};

export default OrderRoutes;