import React from 'react';
import { Switch } from 'react-router-dom';
import MainLayoutRoute from '../../routes/MainLayoutRoute';
import CartPage from './CartPage';

const CartRoutes = () => {
    return <Switch>
        <MainLayoutRoute path='/cart' component={CartPage} exact />
    </Switch>
};

export default CartRoutes;