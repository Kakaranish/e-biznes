import React from 'react';
import { Route, Switch } from 'react-router-dom';
import LoginPage from './LoginPage';
import RegisterPage from './RegisterPage';

const AuthRoutes = () => {
    return (
        <Switch>
            <Route path='/auth/login' component={LoginPage} />
            <Route path='/auth/register' component={RegisterPage} />
        </Switch>
    );
};

export default AuthRoutes;