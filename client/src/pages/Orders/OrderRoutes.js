import React from 'react';
import { Switch } from 'react-router-dom';
import OrdersPage from './OrdersPage';
import AuthorizedOnlyRoute from '../../routes/AuthorizedOnlyRoute';
import OrderPage from './OrderPage';
import ShippingInfoPage from './ShippingInfoPage';
import PaymentPage from './PaymentPage';

const OrderRoutes = () => {
    return <Switch>
        <AuthorizedOnlyRoute path='/orders' component={OrdersPage} exact />
        <AuthorizedOnlyRoute path='/orders/:id/shipping-info' component={ShippingInfoPage} />
        <AuthorizedOnlyRoute path='/orders/:id/payment' component={PaymentPage} />
        <AuthorizedOnlyRoute path='/orders/:id' component={OrderPage} />
    </Switch>
};

export default OrderRoutes;