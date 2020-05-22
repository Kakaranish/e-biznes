import React from 'react';
import { Switch, Route } from 'react-router-dom';
import CartPage from './CartPage';

const CartRoutes = () => {
    return <Switch>
        <Route path='/cart' component={CartPage} exact />
    </Switch>
};

export default CartRoutes;